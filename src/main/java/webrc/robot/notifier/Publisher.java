package webrc.robot.notifier;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 2/25/14
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Publisher implements BeanNameAware {

    @Autowired
    MessageService messageService;

    String beanName;

    @Override
    public void setBeanName(String s) {
        beanName=s;
    }

    protected void publish(Map<String, Object> values)
    {
        messageService.publish(values);
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
