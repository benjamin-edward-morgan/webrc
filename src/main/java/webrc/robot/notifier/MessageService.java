package webrc.robot.notifier;

import webrc.robot.util.ManyToManyMap;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 2/25/14
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageService {

    ManyToManyMap<String, Subscriber> keySubscribers = new ManyToManyMap<String, Subscriber>();


    public void publish(Map<String, Object> values)
    {
        Set<Subscriber> subscribers = keySubscribers.getBforA(values.keySet());
        for(Subscriber subscriber : subscribers)
        {
            subscriber.notify(values);
        }
    }

    public void subscribe(Set<String> keys, Subscriber subscriber)
    {
        keySubscribers.put(keys, subscriber);
    }

    //TODO: subscribeAll
    //public void subscribeAll(Subscriber subscriber) {}

}
