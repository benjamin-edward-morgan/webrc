package webrc.robot.controller;

import org.springframework.beans.factory.BeanNameAware;
import webrc.robot.notifier.Subscriber;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 2/25/14
 * Time: 11:22 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Controller extends Subscriber implements BeanNameAware {

    String beanName;

    protected void publish(Map<String, Object> values)
    {
        messageService.publish(values);
    }

    @Override
    protected abstract void notify(Map<String, Object> values);

    @Override
    public void setBeanName(String s) {
        beanName = s;
    }
}
