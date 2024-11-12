package org.example.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.components.interfaces.BotAction;
import org.example.dto.GameResponseDTO;
import org.example.dto.RegistrationDTO;
import org.example.enums.BodyType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Component
public class Bot implements ApplicationRunner, BotAction {
    @Value("${bot.url}")
    private String botURL;
    @Value("${bot.nickName}")
    private String botNickname;
    private Integer[][] fields;
    int positionX;
    int positionY;
    private ObjectMapper mapper;
    public RestTemplate restTemplate = new RestTemplate();
    public void run(ApplicationArguments args) throws Exception {
        int i;
        for (i = 0; i < 3; i++) {
            ResponseEntity<GameResponseDTO> registration = registration();
            if(registration.getStatusCode().is2xxSuccessful()){
                System.out.println(registration.getBody() == null ? "body is null" : registration.getBody().getMessage() == null ? "body.getMessage is null" : registration.getBody().getMessage());
                ResponseEntity<GameResponseDTO> gameInfo = getGameInfo();
                System.out.println(gameInfo.getBody() == null ? "body is null" : gameInfo.getBody().getMessage() == null ? "body.getMessage is null" : gameInfo.getBody().getMessage());
                if(gameInfo.getBody().getBodyType().equals(BodyType.GAME)){
                    Game game = mapper.readValue(gameInfo.getBody().getStringJSON().toString(), Game.class);
                    this.fields = game.getFields();
                    this.positionX = game.getPlayer(botNickname).getPositionX();
                    this.positionY = game.getPlayer(botNickname).getPositionY();
                }
                break;
            }
        }
        if(i == 3){
            System.out.println("Бот не смог зарегистрироваться");
            return;
        }
    }

    @Override
    public boolean move(int x, int y) {

        return false;
    }

    public ResponseEntity<GameResponseDTO> getGameInfo(){
        UriComponentsBuilder uriRegistration = UriComponentsBuilder.fromHttpUrl(botURL)
                .path("/game/getGameInfo");
        RegistrationDTO registrationDTO = new RegistrationDTO(botNickname);
        RequestEntity<RegistrationDTO> request = RequestEntity.post(uriRegistration.build().toUri()).body(registrationDTO);
        ResponseEntity<GameResponseDTO> responseDTO = restTemplate.exchange(request, GameResponseDTO.class);
        return responseDTO;
    }

    @Override
    public ResponseEntity<GameResponseDTO> registration() {
        UriComponentsBuilder uriRegistration = UriComponentsBuilder.fromHttpUrl(botURL)
                .path("/game/registration");
        RegistrationDTO registrationDTO = new RegistrationDTO(botNickname);
        RequestEntity<RegistrationDTO> request = RequestEntity.post(uriRegistration.build().toUri()).body(registrationDTO);
        ResponseEntity<GameResponseDTO> responseDTO = restTemplate.exchange(request, GameResponseDTO.class);
        return responseDTO;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void eat() {

    }
}
