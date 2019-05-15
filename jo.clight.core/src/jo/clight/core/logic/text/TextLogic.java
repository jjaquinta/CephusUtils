package jo.clight.core.logic.text;

import java.util.*;
import jo.audio.util.model.data.AudioMessageBean;

public class TextLogic
{

    public TextLogic()
    {
    }

    public static String getString(AudioMessageBean msg)
    {
        if(msg == null)
            return "";
        if(msg.getIdent().equals(AudioMessageBean.GROUP))
            return getList(msg, "SPACE", "SPACE");
        if(msg.getIdent().equals(AudioMessageBean.LIST))
            return getList(msg, "COMMA", "COMMA");
        if(msg.getIdent().equals(AudioMessageBean.AND))
            return getList(msg, "COMMA", "AND");
        if(msg.getIdent().equals(AudioMessageBean.OR))
            return getList(msg, "COMMA", "OR");
        String ident = msg.getIdent();
        String text = RESOURCES.getString(ident);
        if(msg.getArgs() != null && msg.getArgs().length > 0)
        {
            Object args[] = new Object[msg.getArgs().length];
            System.arraycopy(((Object) (msg.getArgs())), 0, ((Object) (args)), 0, args.length);
            for(int i = 0; i < args.length; i++)
                if(args[i] instanceof AudioMessageBean)
                    args[i] = getString((AudioMessageBean)args[i]);
            text = String.format(text, args);
        }
        return text;
    }

    public static String getList(AudioMessageBean msg, String tween, String end)
    {
        StringBuffer text = new StringBuffer();
        for(int i = 0; i < msg.getArgs().length; i++)
        {
            Object o = msg.getArgs()[i];
            if(i > 0)
                if(i + 1 < msg.getArgs().length)
                    text.append(getString(new AudioMessageBean(tween)));
                else
                    text.append(getString(new AudioMessageBean(end)));
            if(o instanceof AudioMessageBean)
                text.append(getString((AudioMessageBean)o));
            else
                text.append(String.valueOf(o));
        }

        return text.toString();
    }

    private static boolean equals(AudioMessageBean msg1, AudioMessageBean msg2)
    {
        if(msg1 == msg2)
            return true;
        if(msg1 == null)
            return msg2 == null;
        if(msg2 == null)
            return false;
        if(!msg1.getIdent().equals(msg2.getIdent()))
            return false;
        if(msg1.getArgs().length != msg2.getArgs().length)
            return false;
        for(int i = 0; i < msg1.getArgs().length; i++)
        {
            Object arg1 = msg1.getArgs()[i];
            Object arg2 = msg2.getArgs()[i];
            if(arg1 instanceof AudioMessageBean)
                if(arg2 instanceof AudioMessageBean)
                {
                    if(!equals((AudioMessageBean)arg1, (AudioMessageBean)arg2))
                        return false;
                } else
                {
                    return false;
                }
            if(!arg1.equals(arg2))
                return false;
        }

        return true;
    }

    public static AudioMessageBean compress(List<AudioMessageBean> msgsIn)
    {
        List<AudioMessageBean> msgsOut = new ArrayList<>();
        for(int i = 0; i < msgsIn.size(); i++)
        {
            AudioMessageBean msg = (AudioMessageBean)msgsIn.get(i);
            int end;
            for(end = i + 1; end < msgsIn.size(); end++)
            {
                AudioMessageBean msg2 = (AudioMessageBean)msgsIn.get(end);
                if(!equals(msg, msg2))
                    break;
            }

            if(end - i == 1)
            {
                msgsOut.add(msg);
            } else
            {
                msgsOut.add(new AudioMessageBean("BY", new Object[] {
                    msg, Integer.valueOf(end - i)
                }));
                i = end - 1;
            }
        }

        if(msgsOut.size() == 1)
            return (AudioMessageBean)msgsOut.get(0);
        else
            return AudioMessageBean.LIST(msgsOut);
    }

    private static final Locale LOCAL;
    private static ResourceBundle RESOURCES;

    static 
    {
        LOCAL = Locale.getDefault();
        RESOURCES = ResourceBundle.getBundle("jo/clight/core/logic/text/Text", LOCAL);
    }
}
