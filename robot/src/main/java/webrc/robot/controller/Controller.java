package webrc.robot.controller;

import webrc.messaging.Pubscriber;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 2/25/14
 * Time: 11:22 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Controller extends Pubscriber {


    protected void publish(Map<String, Object> values) {
        messageService.publish(values);
    }

    @Override
    protected abstract void notify(Map<String, Object> values);

}
