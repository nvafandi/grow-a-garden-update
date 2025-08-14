package grow.a.garden.service;

import grow.a.garden.constant.Constant;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@ClientEndpoint
@Slf4j
public class WebSocketService {

    private Session session;

    private final StringBuilder messageBuffer = new StringBuilder();

    private final AtomicBoolean connected = new AtomicBoolean(false);

//    @PostConstruct
    public void connect() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();

            // Set buffer sizes to 64KB - adjust if necessary
            container.setDefaultMaxTextMessageBufferSize(64 * 1024);
            container.setDefaultMaxBinaryMessageBufferSize(64 * 1024);

            session = container.connectToServer(this, new URI(Constant.URL.WS_URL));

            // Also set buffer sizes on session explicitly
            session.setMaxTextMessageBufferSize(64 * 1024);
            session.setMaxBinaryMessageBufferSize(64 * 1024);

            log.info("WebSocket connected to {}", Constant.URL.WS_URL);
            connected.set(true);

        } catch (Exception e) {
            log.error("Failed to connect WebSocket: ", e);
            connected.set(false);
            // Optionally add reconnect logic here
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("WebSocket opened: Session id: {}", session.getId());
    }

    /**
     * Handle partial text messages. Append parts to buffer until 'last' is true.
     */
    @OnMessage
    public void onMessage(String messagePart, boolean last) {
        messageBuffer.append(messagePart);
        if (last) {
            String completeMessage = messageBuffer.toString();
            messageBuffer.setLength(0); // Reset buffer
            log.info("Complete message received: {}", completeMessage);
            // Process the complete message here, e.g., parse JSON, etc.
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.info("WebSocket closed. Session id: {}, Reason: {}", session.getId(), closeReason);
        connected.set(false);
        // Implement reconnect logic if needed
        // reconnect();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("WebSocket error occurred for session id: " + (session != null ? session.getId() : "null"), throwable);
        connected.set(false);
        // Optionally close and attempt reconnect
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (Exception e) {
                log.error("Error closing session after error", e);
            }
        }
        // reconnect();
    }

    public boolean isConnected() {
        return connected.get();
    }

    public void close() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
                log.info("WebSocket session closed manually.");
            } catch (Exception e) {
                log.error("Error closing WebSocket session", e);
            }
        }
        connected.set(false);
    }

    /**
     * Example reconnect method (basic).
     * In production, add retry limits and backoff strategy.
     */
    private void reconnect() {
        log.info("Attempting to reconnect WebSocket...");
        close();
        try {
            Thread.sleep(3000); // wait before reconnecting
        } catch (InterruptedException ignored) {
        }
        connect();
    }

}
