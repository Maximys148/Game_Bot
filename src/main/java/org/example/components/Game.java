package org.example.components;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@Component
public class Game {
    private List<Player> players;
    private Map map;
    private Integer[][] fields;
    private Integer indexMove;

    @Autowired
    public Game(Map map) {
        this.players = new ArrayList<Player>();
        this.map = map;
        this.fields = map.generateMaze();
        this.indexMove = 1;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Integer[][] getFields() {
        return fields;
    }

    public void setFields(Integer[][] fields) {
        this.fields = fields;
    }

    public Integer getIndexMove() {
        return indexMove;
    }

    public void setIndexMove(Integer indexMove) {
        this.indexMove = indexMove;
    }

    public Player getPlayer(String nickName) {
        Player player = null;
        for (Player randomplayer : players) {
            if (randomplayer.getNickName().equals(nickName)) {
                player = randomplayer;
                break;
            }
        }
        return player;
    }
    public Integer getCountPlayers(){
        return players.size();
    }
    public Integer[][] printMap() {
        for(Integer[] str : fields){
            System.out.println();
            for (Integer symbol : str)
                System.out.printf("%4d", symbol);
        }
        return fields;
    }
    public boolean addPlayer(String nickname){
        if(players != null) {
            for (Player player : players) {
                if (player.getNickName().equals(nickname))
                    return false;
            }
        }
        Player player = new Player(nickname);
        if (players.isEmpty()){
            player.setIndexMove(1);
        }else {
            player.setIndexMove(players.size() + 1);
        }
        Random random = new Random();
        //выдаю игроку стартовую позицию
        while (true) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            if(fields[y][x] == 0){
                player.setPositionX(x);
                player.setPositionY(y);
                fields[y][x] = player.getIndexMove() * 11;
                player.setId(String.valueOf(player.getIndexMove() * 11));
                break;
            }
        }
        players.add(player);
        return true;
    }
    public String move(String nickName, int newX, int newY) {
        Player player = null;
        for (Player randomplayer : players) {
            if (randomplayer.getNickName().equals(nickName)) {
                player = randomplayer;
                break;
            }
        }
        if (player == null)
            return "Такого игрока нет в игре";

        if (map.getNumberOfFood() == 1){
            return "Игра окончена, вся еда съедена";
        }
        if (!player.getIndexMove().equals(indexMove)) {
            return "Не ваша очередь";
        }if (indexMove.equals(players.size())) {
            indexMove = 1;
        }else {
            indexMove++;
        }

        int x = player.getPositionX(); // Текущая позиция игрока по X
        int y = player.getPositionY(); // Текущая позиция игрока по Y

        // Проверка на выход за границы карты
        if (newX < 0 || newX >= fields[0].length || newY < 0 || newY >= fields.length) {
            return "Выход за границы карты";
        }

        // Проверяем, что движение разрешено (т.е. только на одну клетку вверх, вниз, влево или вправо)
        int deltaX = Math.abs(x - newX);
        int deltaY = Math.abs(y - newY);
        if ((deltaX + deltaY) != 1) {
            return "Недопустимое движение. Вы можете двигаться только на одну клетку.";
        }

        // Проверка на наличие еды
        if (fields[newY][newX] == 5) { // Изменено с map[newX][newY] на map[newY][newX]
            Integer countFood = player.getCountFood();
            player.setCountFood(countFood + 1);
            fields[y][x] = 0; // Сброс старой позиции
            player.setPositionX(newX);
            player.setPositionY(newY);
            fields[newY][newX] = player.getIndexMove() * 11; // Изменено с map[newX][newY] на map[newY][newX]
            return "Вы успешно сделали шаг и покушали)";
        }
        if (fields[newY][newX] != 0) { // Изменено с map[newX][newY] на map[newY][newX]
            return "На этом месте есть препятствие или игрок";
        }

        // Проверка на наличие препятствий

        // Обновляем карту и позицию игрока
        fields[y][x] = 0; // Сброс старой позиции
        player.setPositionX(newX);
        player.setPositionY(newY);
        fields[newY][newX] = player.getIndexMove() * 11; // Изменено с map[newX][newY] на map[newY][newX]
        return "Вы успешно сделали шаг";
    }
}
