package jo.cephus.core.logic;

import java.util.*;
import jo.cephus.core.data.IParamEval;

public class ParameterizedLogic
{

    public ParameterizedLogic()
    {
    }

    public static void addContext(Object context)
    {
        List<Object> contexts = (List<Object>)mContextCache.get(Thread.currentThread());
        if(contexts == null)
        {
            contexts = new ArrayList<Object>();
            mContextCache.put(Thread.currentThread(), contexts);
        }
        contexts.add(context);
    }

    public static void removeContext(Object context)
    {
        List<Object> contexts = (List<Object>)mContextCache.get(Thread.currentThread());
        if(contexts != null)
        {
            contexts.remove(context);
            if(contexts.size() == 0)
                mContextCache.remove(Thread.currentThread());
        }
    }

    public static List<Object> getContexts()
    {
        if(mContextCache.containsKey(Thread.currentThread()))
            return (List<Object>)mContextCache.get(Thread.currentThread());
        else
            return new ArrayList<Object>();
    }

    public static Object getContext(Class<?> clazz)
    {
        for(Iterator<Object> iterator = getContexts().iterator(); iterator.hasNext();)
        {
            Object context = iterator.next();
            if(clazz.isAssignableFrom(context.getClass()))
                return context;
        }

        return null;
    }

    public static void addEval(String id, IParamEval eval)
    {
        mEvaluators.put(id, eval);
    }

    public static Object getParameterizedValue(Object base, String id, Object hardCoded)
    {
        IParamEval eval = (IParamEval)mEvaluators.get(id);
        if(eval != null)
            return eval.getParameterizedValue(base, hardCoded);
        else
            return hardCoded;
    }

    private static Map<Thread,List<Object>> mContextCache = new HashMap<>();
    private static Map<String,IParamEval> mEvaluators = new HashMap<>();

}
