package org.example.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.botMove.AStarPathfinder;
import org.example.botMove.Position;
import org.example.components.interfaces.BotAction;
import org.example.dto.GameResponseDTO;
import org.example.dto.MoveDTO;
import org.example.dto.RegistrationDTO;
import org.example.enums.BodyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Component
public class Bot implements ApplicationRunner, BotAction {
    private static int MAX_RETRIES = 3;

    Logger logger = LoggerFactory.getLogger(Bot.class);
    @Value("${bot.url}")
    private String botURL;
    @Value("${bot.nickName}")
    private String botNickname;
    private GameMap gameMap;
    private Position startPosition;
    private Position position;
    private final ObjectMapper mapper = new ObjectMapper();
    public RestTemplate restTemplate = new RestTemplate();
    /* TODO ЧТ21 - Оставить в run лишь один цикл, а уже он будет вызывать методы(на данный момент циклы, которые вызывает нужные нам методы) при разных условиях
                   в зависимости от которых он будет выходить из цикла(завершать работу бота) или переходить на новый этап.
                 - Поправить логику бота чтобы он вызывал findPathTo один раз(он возвращает путь, состоящий из Positions) и уже этот путь будет разбирать новый метод.
    */
    public void run(ApplicationArguments args) throws Exception {
        if(validateRegistration()){
            if(validateStartGame()){
                while (true){
                    if(!validateMove()){
                        logger.info("Игра окончена");
                        return;
                    }
                }
            }else return;
        }else return;
    }

    private List<Position> findPathTo(Position target) {
        AStarPathfinder pathfinder = new AStarPathfinder(gameMap);
        List<Position> path = pathfinder.findPath(position, target);
        return path; // Указываем, что путь невозможен
    }

    @Override
    public ResponseEntity<GameResponseDTO> move(int x, int y){
        UriComponentsBuilder uriRegistration = UriComponentsBuilder.fromHttpUrl(botURL)
                .path("/game/move");
        MoveDTO moveDTO = new MoveDTO(botNickname, x, y);
        RequestEntity<MoveDTO> request = RequestEntity.post(uriRegistration.build().toUri()).body(moveDTO);
        ResponseEntity<GameResponseDTO> responseDTO = restTemplate.exchange(request, GameResponseDTO.class);
        return responseDTO;
    }

    public void getGameInfo() throws JsonProcessingException {
        UriComponentsBuilder uriRegistration = UriComponentsBuilder.fromHttpUrl(botURL)
                .path("/game/getGameInfo");
        RegistrationDTO registrationDTO = new RegistrationDTO(botNickname);
        RequestEntity<RegistrationDTO> request = RequestEntity.post(uriRegistration.build().toUri()).body(registrationDTO);
        ResponseEntity<GameResponseDTO> responseDTO = restTemplate.exchange(request, GameResponseDTO.class);
        logger.info(responseDTO.getBody() == null ? "body is null" : responseDTO.getBody().getMessage() == null ? "body.getMessage is null" : responseDTO.getBody().getMessage());
        if(responseDTO.getBody().getBodyType().equals(BodyType.GAME)){
            Game game = mapper.readValue(responseDTO.getBody().getStringJSON(), Game.class);
            this.gameMap = game.getMap();
            int positionX = game.getPlayer(botNickname).getPositionX();
            int positionY = game.getPlayer(botNickname).getPositionY();
            this.startPosition = new Position(positionX, positionY);
            this.position = startPosition;
        }
    }
    @Override
    public ResponseEntity<GameResponseDTO> registration() throws HttpClientErrorException{
        UriComponentsBuilder uriRegistration = UriComponentsBuilder.fromHttpUrl(botURL)
                .path("/game/registration");
        logger.info(uriRegistration.toUriString());
        RegistrationDTO registrationDTO = new RegistrationDTO(botNickname);
        RequestEntity<RegistrationDTO> request = RequestEntity.post(uriRegistration.build().toUri()).body(registrationDTO);
        return restTemplate.exchange(request, GameResponseDTO.class);
    }

    @Override
    public ResponseEntity<GameResponseDTO> startGame() {
        UriComponentsBuilder uriRegistration = UriComponentsBuilder.fromHttpUrl(botURL)
                .path("/game/validateStart");
        RegistrationDTO registrationDTO = new RegistrationDTO(botNickname);
        RequestEntity<RegistrationDTO> request = RequestEntity.post(uriRegistration.build().toUri()).body(registrationDTO);
        return restTemplate.exchange(request, GameResponseDTO.class);
    }

    @Override
    public void eat() {

    }

    private boolean validateRegistration() throws InterruptedException {
        for (MAX_RETRIES = 0; MAX_RETRIES < 3; MAX_RETRIES++) {
            try {
                // Бот пытается зарегистрироваться
                ResponseEntity<GameResponseDTO> registration = registration();
                if(registration.getStatusCode().is2xxSuccessful()){
                    logger.info(registration.getBody() == null ? "body is null" : registration.getBody().getMessage() == null ? "body.getMessage is null" : "Сервер пишет - " + registration.getBody().getMessage());
                    return true;
                }
            }catch (HttpClientErrorException e){
                if(e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(400))){
                    logger.info("Сервер пишет - " + Objects.requireNonNull(e.getResponseBodyAs(GameResponseDTO.class)).getMessage());
                }
                Thread.sleep(2000);
            }catch (ResourceAccessException e){
                logger.error("Server not found");
                Thread.sleep(2000);
            }
        }
        if(MAX_RETRIES == 3){
            logger.info("Бот не смог зарегистрироваться");
            return false;
        }
        return false;
    }
    private boolean validateStartGame() throws JsonProcessingException, InterruptedException {
        while (true){
            // Бот пытается присоединиться к игре
            try {
                if (startGame().getStatusCode().is2xxSuccessful()) {
                    // Бот получает информацию от сервера
                    logger.info("Подключился к игре");
                    getGameInfo();
                    logger.info("Получил информацию об игре");
                    return true;
                }
            }catch (HttpClientErrorException e){
                if (e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(400))){
                    logger.info("Сервер пишет - " + Objects.requireNonNull(e.getResponseBodyAs(GameResponseDTO.class)).getMessage());
                }
                logger.info("Пытаюсь подключиться к игре");
                Thread.sleep(2000);
            }
        }
    }
    private boolean validateMove() throws InterruptedException {
        // Бот начинает двигаться
        for (Position foodPosition: gameMap.getFoodPosition()){
            List<Position> pathTo = findPathTo(foodPosition);
            if(pathTo == null){
                logger.info("Я не могу найти путь");
                Thread.sleep(5000);
            }else {
                pathTo.remove(0);
                for (Position positionToMove: pathTo) {
                    try {
                        ResponseEntity<GameResponseDTO> move = move(positionToMove.getX(), positionToMove.getY());
                        logger.info("Сервер пишет - " + Objects.requireNonNull(move.getBody()).getMessage());
                        Thread.sleep(2000);
                    }catch (HttpClientErrorException e){
                        if(e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(400))){
                            logger.info("Сервер пишет - " + Objects.requireNonNull(e.getResponseBodyAs(GameResponseDTO.class)).getMessage());
                            Thread.sleep(2000);
                        }
                    }
                }
            }
        }
        return false;
    }
}
