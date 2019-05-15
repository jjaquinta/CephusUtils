package jo.clight.core.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jo.audio.util.model.data.AudioMessageBean;
import jo.clight.core.data.ShipComponentInstanceBean;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.logic.text.TextLogic;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;

public class ShipTableLogic
{
    public static List<List<Object>> toTable(ShipDesignBean ship)
    {
        try
        {
            ParameterizedLogic.addContext(ship);
            List<List<Object>> table = new ArrayList<>();
            List<Object> line = new ArrayList<>();
            line.add("{colspan|5}TL"+ShipDesignLogic.findMaxTech(ship)+" "+ShipDesignLogic.getHullDisplacement(ship)+" "+ship.getShipName()+" Design Sheet");
            table.add(line);
            line = new ArrayList<>();
            line.add("---");
            table.add(line);
            line = new ArrayList<>();
            line.add("Component");
            line.add("Notes");
            line.add("Count");
            line.add("Volume");
            line.add("Price");
            table.add(line);
            line = new ArrayList<>();
            line.add("---");
            table.add(line);
            double volume = 0;
            double price = 0;
            for (ShipComponentInstanceBean comp : ship.getComponents())
            {
                line = new ArrayList<>();
                line.add(TextLogic.getString(comp.getComponent().getName()));
                String desc = TextLogic.getString(comp.getComponent().getDescription());
                line.add(desc);
                line.add(comp.getCount());
                double vol = comp.getCount()*comp.getVolume();
                line.add(vol);
                double pri = comp.getCount()*comp.getPrice();
                line.add(pri);
                table.add(line);
                volume += vol;
                price += pri;
            }
            if (volume < 0)
            {
                int cargo = (int)(-volume);
                line = new ArrayList<>();
                line.add(TextLogic.getString(new AudioMessageBean("CARGO_HOLD_NAME")));
                String desc = TextLogic.getString(new AudioMessageBean("CARGO_HOLD_DESC"));
                line.add(desc);
                line.add(cargo);
                line.add(cargo);
                line.add(0);
                table.add(line);
            }
            line = new ArrayList<>();
            line.add("---");
            table.add(line);            line = new ArrayList<>();
            line.add(TextLogic.getString(new AudioMessageBean("TOTAL")));
            String desc = TextLogic.getString(new AudioMessageBean("SPACE"));
            line.add(desc);
            line.add("");
            line.add("");
            line.add(price);
            table.add(line);
            return table;
        }
        finally
        {
            ParameterizedLogic.removeContext(ship);
        }
    }
    
    public static String toTableText(ShipDesignBean ship, String tableHints)
    {
        StringTokenizer st = new StringTokenizer(tableHints, ",");
        ColumnHint[] hints = new ColumnHint[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++)
            hints[i] = new ColumnHint(st.nextToken());
        List<List<Object>> table = toTable(ship);
        for (List<Object> line : table)
        {
            int col = 0;
            for (int i = 0; i < line.size(); i++)
                col = computeHint(line, i, col, hints);
        }
        int fullWidth = 1;
        for (ColumnHint hint : hints)
            fullWidth += hint.width() + 1;
        StringBuffer sb = new StringBuffer();
        List<Object> wrapLine = null;
        for (int row = 0; (row < table.size()) || (wrapLine != null); row++)
        {
            List<Object> line;
            if (wrapLine != null)
            {
                line = wrapLine;
                wrapLine = null;
                row--;
            }
            else
                line = table.get(row);
            if (handleSpecialLine(sb, line, fullWidth))
                continue;
            int col = 0;
            for (int i = 0; i < line.size(); i++)
            {
                String text = line.get(i).toString();
                if (text.startsWith("{"))
                {
                    int o = text.indexOf('}');
                    ColumnHint hint = new ColumnHint(text.substring(1, o));
                    text = text.substring(o + 1);
                    for (int c = 0; c < hint.colspan; c++)
                    {
                        hint.width += hints[col+c].width();
                        if (c > 0)
                            hint.width++;
                    }
                    wrapLine = appendNormal(sb, wrapLine, col, text, hint);
                    col += hint.colspan;
                }
                else
                {
                    ColumnHint hint = hints[col];
                    if (hint.decimal)
                        appendDecimal(sb, text, hint);
                    else
                        wrapLine = appendNormal(sb, wrapLine, col, text, hint);
                    col++;
                }
                if ((wrapLine != null) && (wrapLine.size() < col))
                    wrapLine.add("");
            }
            sb.append("|\r\n");
        }
        return sb.toString();
    }

    public static List<Object> appendNormal(StringBuffer sb,
            List<Object> wrapLine, int col, String text, ColumnHint hint)
    {
        if (text.length() > hint.width)
            if (hint.wrap)
            {
                if (wrapLine == null)
                {
                    wrapLine = new ArrayList<>();
                    for (int c = 0; c < col; c++)
                        wrapLine.add("");
                }
                int o = text.lastIndexOf(" ", hint.width);
                wrapLine.add(text.substring(o).trim());
                text = text.substring(0, o).trim();
            }
            else
                text = text.substring(0, hint.width - 3) + "...";
        if (hint.rightAdjust)
            sb.append("|"+StringUtils.spacePrefix(text, hint.width));
        else
            sb.append("|"+StringUtils.spaceSuffix(text, hint.width));
        return wrapLine;
    }

    public static void appendDecimal(StringBuffer sb, String text,
            ColumnHint hint)
    {
        if (!isNumber(text))
            sb.append("|"+StringUtils.spacePrefix(text, hint.width()));
        else
        {
            int o = text.indexOf('.');
            if (o < 0)
            {
                o = text.length();
                text += ".";
            }
            String left = text.substring(0, o);
            left = StringUtils.spacePrefix(left, hint.widthDecimalLeft);
            String right = text.substring(o + 1);
            if (right.length() > hint.widthDecimalRight)
                right = right.substring(0, hint.widthDecimalRight);
            else
                right = StringUtils.spaceSuffix(right, hint.widthDecimalRight);
            sb.append("|"+left+"."+right);
        }
    }

    private static boolean handleSpecialLine(StringBuffer sb, List<Object> line, int fullWidth)
    {
        if (line.size() != 1)
            return false;
        if ("---".equals(line.get(0)))
        {
            sb.append(StringUtils.suffix("", '-', fullWidth));
            sb.append("\r\n");
            return true;
        }
        if ("===".equals(line.get(0)))
        {
            sb.append(StringUtils.suffix("", '=', fullWidth));
            sb.append("\r\n");
            return true;
        }
        return false;
    }
    
    private static boolean isNumber(String text)
    {
        if (text.length() == 0)
            return false;
        for (char c : text.toCharArray())
            if (!Character.isDigit(c) && (c != '-') && (c != '.'))
                return false;
        return true;
    }

    private static int computeHint(List<Object> line, int i, int col,
            ColumnHint[] hints)
    {
        String text = line.get(i).toString();
        if (text.startsWith("{"))
        {
            int o = text.indexOf('}');
            ColumnHint hint = new ColumnHint(text.substring(1, o));
            col += hint.colspan;
            return col;
        }
        ColumnHint hint = hints[col];
        if ("---".equals(text) || "===".equals(text))
            return line.size();
        if (hint.calc)
            hint.width = Math.max(hint.width, text.length());
        else if (hint.calcDecimalLeft || hint.calcDecimalRight)
        {
            int o = text.indexOf('.');
            if (o < 0)
            {
                o = text.length();
                text += ".";
            }
            if (hint.calcDecimalLeft)
                hint.widthDecimalLeft = Math.max(hint.widthDecimalLeft, text.substring(0, o).length());
            if (hint.calcDecimalRight)
                hint.widthDecimalRight = Math.max(hint.widthDecimalRight, text.substring(o+1).length());
        }
        col++;
        return col;
    }
}

class ColumnHint
{
    boolean calc;
    int     width;
    boolean wrap;
    boolean rightAdjust;
    boolean decimal;
    boolean calcDecimalLeft;
    boolean calcDecimalRight;
    int     widthDecimalLeft;
    int     widthDecimalRight;
    int     colspan;
    
    public ColumnHint(String hints)
    {
        for (StringTokenizer st = new StringTokenizer(hints, "|"); st.hasMoreTokens(); )
        {
            String hint = st.nextToken();
            if ("0".equals(hint))
                calc = true;
            else if ("wrap".equals(hint))
                wrap = true;
            else if ("right".equals(hint))
                rightAdjust = true;
            else if ("colspan".equals(hint))
                colspan = IntegerUtils.parseInt(st.nextToken());
            else if (hint.indexOf('.') >= 0)
            {
                decimal = true;
                int o = hint.indexOf('.');
                String left = hint.substring(0, o);
                if ("0".equals(left))
                {
                    calcDecimalLeft = true;
                    widthDecimalLeft = 0;
                }
                else
                    widthDecimalLeft = IntegerUtils.parseInt(left);
                String right = hint.substring(o + 1);
                if ("0".equals(right))
                {
                    calcDecimalRight = true;
                    widthDecimalRight = 0;
                }
                else
                    widthDecimalRight = IntegerUtils.parseInt(right);
            }
            else
                width = IntegerUtils.parseInt(hint);
        }
    }
    
    int width()
    {
        if (decimal)
            return widthDecimalLeft + 1 + widthDecimalRight;
        else
            return width;
    }
}