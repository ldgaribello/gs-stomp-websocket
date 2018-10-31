package gs.websocket.controller;

import gs.websocket.model.Greeting;
import gs.websocket.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {

    private int counter = 0;
    private WebSocketStompClient stompClient;

    @Autowired
    private SimpMessagingTemplate template;

    public void connect() throws Exception {
        StompSessionHandler sessionHandler = new CustomSessionHandler();

        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        ListenableFuture<StompSession> future =
                stompClient.connect("ws://localhost:8080/gs-stomp-websocket", sessionHandler);

        StompSession session = future.get();
        session.subscribe("/topic/greetings", sessionHandler);
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Message message) throws Exception {
        if (stompClient == null){
            connect();
        }

        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getFrom()) + "!");
    }

    @Scheduled(fixedDelay = 10000)
    public void sendScheduledMessage() {
        if (counter <= 100) {
            this.template.convertAndSend("/topic/greetings", "{\"content\":\"self " + ++counter + "\"}");
        }
    }
}