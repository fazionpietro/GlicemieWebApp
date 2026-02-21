package it.univr.glicemiewebapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import it.univr.glicemiewebapp.controller.ComunicazioneWebSocketHandler;
import it.univr.glicemiewebapp.controller.LogsWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final LogsWebSocketHandler webSocketHandler;
  private final ComunicazioneWebSocketHandler comunicazioneWebSocketHandler;
  private final WebSocketAuthInterceptor authInterceptor;

  public WebSocketConfig(LogsWebSocketHandler webSocketHandler,
      ComunicazioneWebSocketHandler comunicazioneWebSocketHandler,
      WebSocketAuthInterceptor authInterceptor) {
    this.webSocketHandler = webSocketHandler;
    this.comunicazioneWebSocketHandler = comunicazioneWebSocketHandler;
    this.authInterceptor = authInterceptor;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(webSocketHandler, "/ws/logs")
        .addInterceptors(authInterceptor)
        .setAllowedOrigins("*");

    registry.addHandler(comunicazioneWebSocketHandler, "/ws/comunicazioni")
        .addInterceptors(authInterceptor)
        .setAllowedOrigins("*");
  }

}
