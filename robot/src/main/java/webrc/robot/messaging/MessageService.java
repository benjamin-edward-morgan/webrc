package webrc.robot.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger log = LoggerFactory.getLogger(this.getClass());

    ManyToManyMap<String, Pubscriber> keySubscribers = new ManyToManyMap<String, Pubscriber>();

    public MessageService() {
    }

    public void publish(Map<String, Object> values) {

        Set<Pubscriber> subscribers = keySubscribers.getBforA(values.keySet());
        for (Pubscriber subscriber : subscribers) {
            //TODO: only notify the subscriber about what it has subscribed to

            log.debug("notifying: " + subscriber + " : " + values);

            subscriber.notify(values);
        }
    }

    public void subscribe(Set<String> keys, Pubscriber subscriber) {
        keySubscribers.put(keys, subscriber);
    }

    //TODO: subscribeAll
    //public void subscribeAll(Pubscriber subscriber) {}

}
