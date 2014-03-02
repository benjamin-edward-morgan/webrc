package webrc.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 2/25/14
 * Time: 10:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManyToManyMap<A,B> {

    Map<A,Set<B>> a2b = new HashMap<A,Set<B>>();
    Map<B,Set<A>> b2a = new HashMap<B,Set<A>>();

    public void put(A a, B b)
    {
        Set<B> mappedBs;
        if(a2b.containsKey(a))
        {
            mappedBs = a2b.get(a);
        }
        else
        {
            mappedBs = new HashSet<B>();
            a2b.put(a, mappedBs);
        }
        mappedBs.add(b);

        Set<A> mappedAs;
        if(b2a.containsKey(b))
        {
            mappedAs = b2a.get(b);
        }
        else
        {
            mappedAs = new HashSet<A>();
            b2a.put(b, mappedAs);
        }
        mappedAs.add(a);
    }

    public void put(A a, Set<B> bs)
    {
        Set<B> mappedBs;
        if(a2b.containsKey(a))
        {
            mappedBs = a2b.get(a);
        }
        else
        {
            mappedBs = new HashSet<B>();
            a2b.put(a, mappedBs);
        }
        mappedBs.addAll(bs);

        for(B b : bs)
        {
            Set<A> mappedAs;
            if(b2a.containsKey(b))
            {
                mappedAs = b2a.get(b);
            }
            else
            {
                mappedAs = new HashSet<A>();
                b2a.put(b, mappedAs);
            }
            mappedAs.add(a);
        }
    }

    public void put(Set<A> as, B b)
    {
        Set<A> mappedAs;
        if(b2a.containsKey(b))
        {
            mappedAs = b2a.get(b);
        }
        else
        {
            mappedAs = new HashSet<A>();
            b2a.put(b, mappedAs);
        }
        mappedAs.addAll(as);

        for(A a : as)
        {
            Set<B> mappedBs;
            if(a2b.containsKey(a))
            {
                mappedBs = a2b.get(a);
            }
            else
            {
                mappedBs = new HashSet<B>();
                a2b.put(a, mappedBs);
            }
            mappedBs.add(b);
        }
    }

    public Set<A> getAforB(B b)
    {
        return b2a.get(b);
    }

    public Set<A> getAforB(Set<B> bs)
    {
        Set<A> as = new HashSet<A>();
        for(B b : bs)
        {
            Set<A> as_0 = b2a.get(b);
            if(as_0 != null)
                as.addAll(as_0);
        }
        return as;
    }

    public Set<B> getBforA(A a)
    {
        return a2b.get(a);
    }

    public Set<B> getBforA(Set<A> as)
    {
        Set<B> bs = new HashSet<B>();
        for(A a : as)
        {
            Set<B> bs_0 = a2b.get(a);
            if(bs_0!=null)
                 bs.addAll(bs_0);
        }
        return bs;
    }

}
