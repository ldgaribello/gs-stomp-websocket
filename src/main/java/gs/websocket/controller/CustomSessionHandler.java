package gs.websocket.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class CustomSessionHandler extends StompSessionHandlerAdapter {

    private Logger logger = LogManager.getLogger(CustomSessionHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("Connected");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        logger.info("Received payload: {}", payload);
    }
}
