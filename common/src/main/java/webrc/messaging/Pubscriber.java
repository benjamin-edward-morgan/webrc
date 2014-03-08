package webrc.messaging;

import org.springframework.beans.factory.annotation.Autowired;

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

    protected void publish(Map<String, Object> values)
    {
        messageService.publish(values);
    }

    protected void subscribe(Set<String> keys)
    {
        messageService.subscribe(keys, this);
    }

    protected abstract void notify(Map<String, Object> values);


}
