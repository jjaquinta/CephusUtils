package jo.clight.core.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jo.audio.util.model.data.AudioMessageBean;
import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipComponentInstanceBean;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.data.ShipReportBean;
import jo.clight.core.logic.text.TextLogic;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;

public class ShipTableLogic
{
    public static List<List<Object>> toDesignSheet(ShipDesignBean ship)
    {
        try
        {
            ParameterizedLogic.addContext(ship);
            List<List<Object>> table = new ArrayList<>();
            addLine(table, "{colspan|5}TL"+ShipDesignLogic.findMaxTech(ship)+" "+ShipDesignLogic.getHullDisplacement(ship)+" "+ship.getShipName()+" Design Sheet");
            addLine(table, "---");
            addLine(table, "Component","Notes","Count","Volume","Price");
            addLine(table, "---");
            double volume = 0;
            double price = 0;
            for (ShipComponentInstanceBean comp : ship.getComponents())
            {
                String desc = TextLogic.getString(comp.getComponent().getDescription());
                double vol = comp.getCount()*comp.getVolume();
                double pri = comp.getCount()*comp.getPrice();
                addLine(table, TextLogic.getString(comp.getComponent().getName()),
                        desc,
                        comp.getCount(),
                        vol,
                        pri);
                volume += vol;
                price += pri;
            }
            if (volume < 0)
            {
                int cargo = (int)(-volume);
                String desc = TextLogic.getString(new AudioMessageBean("CARGO_HOLD_DESC"));
                addLine(table, TextLogic.getString(new AudioMessageBean("CARGO_HOLD_NAME")),
                        desc,
                        cargo,
                        cargo,
                        0);
            }
            addLine(table, "---");
            String desc = TextLogic.getString(new AudioMessageBean("SPACE"));
            addLine(table, TextLogic.getString(new AudioMessageBean("TOTAL")),
                    desc,
                    "",
                    "",
                    price);
            return table;
        }
        finally
        {
            ParameterizedLogic.removeContext(ship);
        }
    }
    
    public static List<List<Object>> toShipSheet(ShipReportBean report)
    {
        ShipDesignBean ship = report.getShip();
        try
        {
            ParameterizedLogic.addContext(ship);
            List<List<Object>> table = new ArrayList<>();
            addLine(table, "{colspan|4}TL"+ShipDesignLogic.findMaxTech(ship)+" "+ShipDesignLogic.getHullDisplacement(ship)+" "+ship.getShipName());
            addLine(table, "{colspan|4|wrap}The "+ship.getShipName()+" "+ship.getShipFunction());
            addLine(table, "---");
            String tonnage = "Tonnage: "+report.getHullDisplacement();
            String armor = "Armor: ";
            if (report.getArmorRating() > 0)
                armor += String.valueOf(report.getArmorRating());
            else
                armor += "none";
            if (report.getArmor() != null)
                armor += " " + TextLogic.getString(report.getArmor().getComponent().getName());
            String config = "";
            if (report.getConfig() != null)
                config = TextLogic.getString(report.getConfig().getComponent().getName());
            addLine(table, ship.getShipName(),
                    tonnage,
                    armor,
                    config);
            addLine(table, "---");
            addLine(table,
                    "Maneuver: "+report.getManeuverCode(),
                    "Jump: "+report.getJumpCode(),
                    "P-Plant: "+report.getPowerCode(),
                    "Thrust: "+report.getThrustNumber()+" G");
            String fuel = report.getFuelTonnage()+" tons for :";
            if (report.getNumberOfJumps() > 0)
            {
                fuel += " "+report.getNumberOfJumps()+"x Jump";
                if (report.getNumberOfJumps() > 1)
                    fuel += "s";
            }
            if (report.getWeeksofPower() > 0)
            {
                if (!fuel.endsWith(":"))
                    fuel += ", ";
                if (report.getWeeksofPower() == 1)
                    fuel += report.getWeeksofPower()+" week endurance";
                else
                    fuel += report.getWeeksofPower()+" weeks endurance";
            }
            addLine(table,
                    "Jump Rating: "+report.getJumpNumber(),
                    "{colspan|3}Fuel : "+fuel);
            String computer = "no";
            if (report.getComputer() != null)
            {
                computer = TextLogic.getString(report.getComputer().getComponent().getName());
                if (report.getComputerBis() != null)
                    computer += TextLogic.getString(report.getComputerBis().getComponent().getName());
            }
            String armament = "";
            if (report.getTurrets().size() > 0)
            {
                if (armament.length() > 0)
                    armament += ", ";
                armament += TextLogic.getString(toCountedList(report.getTurrets()));
            }
            if (report.getBays().size() > 0)
            {
                if (armament.length() > 0)
                    armament += ", ";
                armament += TextLogic.getString(toCountedList(report.getBays()));
            }
            if (report.getWeapons().size() > 0)
            {
                if (armament.length() > 0)
                    armament += "; ";
                armament += TextLogic.getString(toCountedList(report.getWeapons()));
            }
            if (armament.length() == 0)
                armament = "none";
            addLine(table,
                    computer+" computer",
                    "{colspan|3|wrap}Armament : "+armament);
            List<ShipComponentInstanceBean> fittings = new ArrayList<>();
            fittings.addAll(ShipDesignLogic.getAllInstances(ship, ShipComponentBean.STATEROOM));
            fittings.addAll(ShipDesignLogic.getAllInstances(ship, ShipComponentBean.BERTH));
            fittings.addAll(ShipDesignLogic.getAllInstances(ship, ShipComponentBean.HANGER));
            fittings.addAll(ShipDesignLogic.getAllInstances(ship, ShipComponentBean.SCREENS));
            fittings.addAll(ShipDesignLogic.getAllInstances(ship, ShipComponentBean.ETC_ARMORY));
            fittings.addAll(ShipDesignLogic.getAllInstances(ship, ShipComponentBean.ETC_LAB));
            fittings.addAll(ShipDesignLogic.getAllInstances(ship, ShipComponentBean.ETC_FUEL_SCOOPS));
            if (report.getCargoTonnage() > 0)
                fittings.add(ShipComponentLogic.getInstance(ShipComponentBean.ETC_CARGO_HOLD, report.getCargoTonnage()));
            String fittingsText = TextLogic.getString(toCountedList(fittings));
            if (report.getRefineTonsPerDay() > 0)
                fittingsText += ", fuel processor ("+FormatUtils.sTons(report.getRefineTonsPerDay())+")";
            addLine(table,
                    "{colspan|4|wrap}Fittings : "+fittingsText);
            String crew = report.getCrewTotal()+" total -- "+TextLogic.getString(toCountedList(ShipDesignLogic.getAllInstances(ship, ShipComponentBean.CREW)));
            addLine(table,
                    "{colspan|4|wrap}Crew : "+crew);
            addLine(table,
                    "{colspan|4|wrap}Cost : "+FormatUtils.sCurrency(report.getCost())
                    +"; Construction Time: "+report.getConstructionTime()+" weeks");
            addLine(table, "---");
            return table;
        }
        finally
        {
            ParameterizedLogic.removeContext(ship);
        }
    }

    private static void addLine(List<List<Object>> table, Object... data)
    {
        List<Object> line = new ArrayList<>();
        for (int i = 0; i < data.length; i++)
            line.add(data[i]);
        table.add(line);
    }
    
    public static AudioMessageBean toCountedList(List<ShipComponentInstanceBean> comps)
    {
        List<AudioMessageBean> items = new ArrayList<>();
        for (ShipComponentInstanceBean comp : comps)
        {
            if (comp.getCount() == 1)
                items.add(comp.getComponent().getName());
            else
                items.add(new AudioMessageBean("BY", comp.getCount(), comp.getComponent().getName()));
        }
        return new AudioMessageBean(AudioMessageBean.LIST, items.toArray());
    }
    
    public static String formatTextTable(List<List<Object>> table, String tableHints)
    {
        StringTokenizer st = new StringTokenizer(tableHints, ",");
        ColumnHint[] hints = new ColumnHint[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++)
            hints[i] = new ColumnHint(st.nextToken());
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
                    String prefix = text.substring(0, o+1);
                    text = text.substring(o + 1);
                    for (int c = 0; c < hint.colspan; c++)
                    {
                        hint.width += hints[col+c].width();
                        if (c > 0)
                            hint.width++;
                    }
                    wrapLine = appendNormal(sb, wrapLine, col, text, hint, prefix);
                    col += hint.colspan;
                }
                else
                {
                    if (col < hints.length)
                    {
                        ColumnHint hint = hints[col];
                        if (hint.decimal)
                            appendDecimal(sb, text, hint);
                        else
                            wrapLine = appendNormal(sb, wrapLine, col, text, hint, "");
                        col++;
                    }
                }
                if ((wrapLine != null) && (wrapLine.size() < col))
                    wrapLine.add("");
            }
            sb.append("|\r\n");
        }
        return sb.toString();
    }
    
    public static String formatHTMLTable(List<List<Object>> table)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<table>\r\n");
        for (int row = 0; row < table.size(); row++)
        {
            List<Object> line = table.get(row);
            if (handleSpecialHTMLLine(sb, line))
                continue;
            sb.append("<tr>");
            for (int i = 0; i < line.size(); i++)
            {
                String text = line.get(i).toString();
                if (text.startsWith("{"))
                {
                    int o = text.indexOf('}');
                    ColumnHint hint = new ColumnHint(text.substring(1, o));
                    text = text.substring(o + 1);
                    sb.append("<td colspan=\""+hint.colspan+"\">");
                    sb.append(text);
                    sb.append("</td>");
                }
                else
                {
                    sb.append("<td>");
                    sb.append(text);
                    sb.append("</td>");
                }
            }
            sb.append("</tr>\r\n");
        }
        sb.append("</table>\r\n");
        return sb.toString();
    }

    public static List<Object> appendNormal(StringBuffer sb,
            List<Object> wrapLine, int col, String text, ColumnHint hint,
            String prefix)
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
                wrapLine.add(prefix+text.substring(o).trim());
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

    private static boolean handleSpecialHTMLLine(StringBuffer sb, List<Object> line)
    {
        if (line.size() != 1)
            return false;
        if ("---".equals(line.get(0)))
        {
            sb.append("<hr/>\r\n");
            return true;
        }
        if ("===".equals(line.get(0)))
        {
            sb.append("<hr/>\r\n");
            return true;
        }
        return false;
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