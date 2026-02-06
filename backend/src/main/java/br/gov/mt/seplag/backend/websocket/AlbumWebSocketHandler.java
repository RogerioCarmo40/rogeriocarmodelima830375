package br.gov.mt.seplag.backend.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.mt.seplag.backend.domain.websocket.AlbumNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlbumWebSocketHandler extends TextWebSocketHandler {
    
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessions.put(sessionId, session);
        log.info("WebSocket conexão estabelecida: {}", sessionId);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        sessions.remove(sessionId);
        log.info("WebSocket conexão fechada: {}", sessionId);
    }
    
    public void notificarNovoAlbum(Long albumId, String titulo) {
        AlbumNotification notification = AlbumNotification.builder()
            .albumId(albumId)
            .titulo(titulo)
            .mensagem("Novo álbum cadastrado: " + titulo)
            .timestamp(System.currentTimeMillis())
            .build();
        
        try {
            String json = objectMapper.writeValueAsString(notification);
            TextMessage message = new TextMessage(json);
            
            sessions.values().forEach(session -> {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(message);
                    }
                } catch (IOException e) {
                    log.error("Erro ao enviar notificação WebSocket: {}", e.getMessage());
                }
            });
            
            log.info("Notificação enviada para {} sessões: {}", sessions.size(), titulo);
            
        } catch (Exception e) {
            log.error("Erro ao serializar notificação: {}", e.getMessage());
        }
    }
}
