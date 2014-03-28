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
//	private static final long serialVersionUID = 1L;
//
//	ServerSocketNotifier ssn = ServerSocketNotifier.getSSN();
//
//	public RCCarServlet() {
//		super();
//	}
//
//	@Override
//	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		doGet(request, response);
//	}
//
//	@Override
//	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		doGet(request, response);
//	}
//
//	@Override
//	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		//super.doGet(request, response);
//
//		Integer driveX = null;
//		Integer driveY = null;
//		Integer camX = null;
//		Integer camY = null;
//		String key = null;
//
//
//
//		try {
//			driveX = Integer.parseInt(request.getParameter("driveX"));
//			driveY = Integer.parseInt(request.getParameter("driveY"));
//			camX = Integer.parseInt(request.getParameter("camX"));
//			camY = Integer.parseInt(request.getParameter("camY"));
//			key = request.getParameter("key");
//		} catch (Exception e) {
//
//		}
//
//		System.out.println(driveX + "," + driveY + "," + camX + "," + camY + "," + key);
//
//		Map<String, Object> m = new HashMap<String, Object>();
//
//		m.put("driveX", driveX);
//		m.put("driveY", driveY);
//		m.put("camX", camX);
//		m.put("camY", camY);
//
//		ssn.update(m);
//
//		response.flushBuffer();

//		boolean clientStateInSync = false;
//		if (driveX != null && driveY != null && camX != null && camY != null && key!=null) {
//			// the "getting" client knows the values. check if they are correct
//			// if not, we return the correct values immediately
//			if (driveX.equals(driveX_SN.getState()) && driveY.equals(driveY_SN.getState()) && camX.equals(camX_SN.getState()) && camY.equals(camY_SN.getState()))
//				clientStateInSync = true;
//		}
//		
//		if(!driveX_SN.isKnown() && !driveY_SN.isKnown() && !camX_SN.isKnown() && !camY_SN.isKnown()
//				&& driveX==null && driveY == null && camX==null && camY==null)
//			clientStateInSync = true;
//		
//		
//		if("get".equals(action))
//		{
//			if(clientStateInSync)
//				sleep();
//		}
//		else if("set".equals(action))
//		{
//			if(clientStateInSync)
//				sleep();
//			else
//			{
//				driveX_SN.setVal(driveX);
//				driveY_SN.setVal(driveY);
//				camX_SN.setVal(camX);
//				camY_SN.setVal(camY);
//				
//				interruptSleepingThreads();
//			}
//		}

//
//
// boolean clientIsCorrect = false;
// if (knownState != null) {
// if (knownState.equals("unknown") && !BooleanState.isKnown())
// clientIsCorrect = true;
// else if (knownState.equals("on") && BooleanState.isOn() &&
// BooleanState.isKnown())
// clientIsCorrect = true;
// else if (knownState.equals("off") && !BooleanState.isOn() &&
// BooleanState.isKnown())
// clientIsCorrect = true;
// }
//
// if (clientIsCorrect) {
// sleep();
// }
//
// if (!BooleanState.isKnown())
// response.getWriter().write("unknown");
// else if (BooleanState.isOn())
// response.getWriter().write("on");
// else
// response.getWriter().write("off");

//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("driveX", driveX_SN.getState());
//		map.put("driveY", driveY_SN.getState());
//		map.put("camX", camX_SN.getState());
//		map.put("camY", camY_SN.getState());

//response.getWriter().append(toJson(map));


//		System.out.println("buffer flushed");
//	}
//
//	public String toJson(Map<String, Object> map) {
//		StringBuilder sb = new StringBuilder("{");
//		Iterator<String> keyIter = map.keySet().iterator();
//		while (keyIter.hasNext()) {
//			String key = keyIter.next();
//			sb.append(key + "=" + map.get(key) + (keyIter.hasNext() ? "," : "}"));
//		}
//		return sb.toString();
//	}
//
//
//}
