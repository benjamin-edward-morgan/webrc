package webrc.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import webrc.messaging.Pubscriber;

import java.lang.System;
import java.util.Map;

/**
 * ?action=set&driveX=-10&driveY=8&camX=4&camY=9
 * <p/>
 * if action is "set" we set the state variables. If the state has changed, we
 * wake up any sleeping threads and immediately return. If the state did not
 * change, we will sleep and return the same variables
 * <p/>
 * if the action is "get" and there are no other parameters, we immediately
 * return the state. If there are parameters and they match the current state,
 * we sleep for the timeout or until they are changed.
 *
 * @author benjaminmorgan
 */
@Controller
@RequestMapping("/webrc")
public class RCCarServlet extends Pubscriber {

    public RCCarServlet() {
        System.out.println("init rc car servlet");
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    String post(@RequestBody Map<String, Object> values) {

        //todo: map to car key in session
        this.publishOne("", (Object)values);
        //System.out.println(values);


        return values.toString() ;
    }


    @Override
    protected void notify(Map<String, Object> values) {
        //TODO: notify long-polling clients
    }
}
