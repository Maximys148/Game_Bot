package org.example.thread;

import org.example.components.Player;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class BotThread extends Thread{
    private Player player;
    @Value("${bot.url-move}")
    private String botURL;
    //private Map map;
    public RestTemplate restTemplate = new RestTemplate();
    public BotThread(String nickName) {
        this.player = new Player(nickName);
    }

    @Override
    public void run() {
        while (true){
            UriComponentsBuilder uriAuthenticate = UriComponentsBuilder.fromHttpUrl(botURL)
                    .path("/game/move")
                    .queryParam("nickname", player.getNickName())
                    .queryParam("x", 10)
                    .queryParam("y", 10);
            HttpEntity<String> httpEntity = new HttpEntity<>("");
            ResponseEntity<String> response = restTemplate.exchange(uriAuthenticate.toUriString(), HttpMethod.POST, httpEntity, String.class);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
