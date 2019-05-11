package jo.clight.core.logic;

import java.util.*;

import jo.clight.core.data.IParamEval;

public class ParameterizedLogic
{
    private static Map<Thread,List<Object>> mContextCache = new HashMap<>();
    private static Map<String,IParamEval> mEvaluators = new HashMap<>();

    public static void addContext(Object context)
    {
        synchronized (mContextCache)
        {
            List<Object> contexts = (List<Object>)mContextCache.get(Thread.currentThread());
            if(contexts == null)
            {
                contexts = new ArrayList<Object>();
                mContextCache.put(Thread.currentThread(), contexts);
            }
            contexts.add(0, context);
            debug(Thread.currentThread().hashCode()+" +context "+context.getClass().getSimpleName()+" ("+contexts.size()+")");
        }
    }

    public static void removeContext(Object context)
    {
        synchronized (mContextCache)
        {
            List<Object> contexts = (List<Object>)mContextCache.get(Thread.currentThread());
            if (contexts != null)
            {
                contexts.remove(context);
                if(contexts.size() == 0)
                    mContextCache.remove(Thread.currentThread());
                debug(Thread.currentThread().hashCode()+" -context "+context.getClass().getSimpleName()+" ("+contexts.size()+")");
            }
            else
                debug(Thread.currentThread().hashCode()+" -context "+context.getClass().getSimpleName()+" (-)");
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
        synchronized (mContextCache)
        {
            for(Iterator<Object> iterator = getContexts().iterator(); iterator.hasNext();)
            {
                Object context = iterator.next();
                if(clazz.isAssignableFrom(context.getClass()))
                    return context;
            }
            return null;
        }
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

    private static void debug(String msg)
    {
        //Throwable t = new Throwable();
        //System.out.println(msg+"   @ "+t.getStackTrace()[1].toString());
    }
}
