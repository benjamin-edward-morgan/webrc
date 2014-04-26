package webrc.messaging;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 2/25/14
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Pubscriber {

    @Autowired
    protected
    MessageService messageService;

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    protected void publish(Map<String, Object> values) {
        messageService.publish(values);
    }

    protected void publishOne(String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        publish(map);
    }

    protected void subscribe(Set<String> keys) {
        messageService.subscribe(keys, this);
    }

    protected void subscribe(String key) {
        Set<String> set = new HashSet<String>();
        set.add(key);
        subscribe(set);
    }

    protected abstract void notify(Map<String, Object> values);


}
