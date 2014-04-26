package webrc.messaging;

import webrc.util.ManyToManyMap;

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

    ManyToManyMap<String, Pubscriber> keySubscribers = new ManyToManyMap<String, Pubscriber>();

    public MessageService() {
    }

    public void publish(Map<String, Object> values) {
        Set<Pubscriber> subscribers = keySubscribers.getBforA(values.keySet());
        for (Pubscriber subscriber : subscribers) {
            //TODO: only notify the subscriber about what it has subscribed to
            subscriber.notify(values);
        }
    }

    public void subscribe(Set<String> keys, Pubscriber subscriber) {
        keySubscribers.put(keys, subscriber);
    }

    //TODO: subscribeAll
    //public void subscribeAll(Pubscriber subscriber) {}

}
