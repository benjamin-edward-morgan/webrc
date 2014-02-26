package webrc.server;

//@ServerEndpoint("/echo")
public class RCWSEndpoint /*extends Endpoint*/{
//
//	@Override
//	public void onOpen(Session session, EndpointConfig endpointConfig) {
//
//		//TODO: use onClose(..) to remove message handlers from RC service
//
//		session.addMessageHandler(new RCWSMessageHandler(session));
//
//	}
//
//
//	public class RCWSMessageHandler implements MessageHandler.Whole<String>, RCMessageHandler
//	{
//		Session session;
//
//		public RCWSMessageHandler(Session session)
//		{
//			this.session=session;
//
//			//TODO: await serial number and register using serial number as topic
//
//			RCMessageService.addMessageHandler("up", this);
//		}
//
//		//messages received from the rc client go "down" to the rc robot
//		@Override
//		public void onMessage(String message) {
//			RCMessageService.sendMessage("down", message);
//		}
//
//		//messages coming "up" from the robot are relayed to the client
//		@Override
//		public void handleMessage(String message) {
//			try {
//				session.getBasicRemote().sendText(message);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
