package org.example.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.example.botMove.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameMap {
    private int width;
    private int height;
    private int numberOfFood;
    @JsonProperty("map")
    private Integer[][] fields;
    private List<Position> foodPosition;
    private boolean[][] visited;
    private Random random;

    public GameMap(int width, int height, int numberOfFood) {
        this.width = width;
        this.height = height;
        this.numberOfFood = numberOfFood;
        this.fields = new Integer[height][width];
        this.visited = new boolean[height][width];
        this.random = new Random();
    }

    public GameMap() {
    }


    public int getNumberOfFood() {
        return numberOfFood;
    }

    public void setNumberOfFood(int numberOfFood) {
        this.numberOfFood = numberOfFood;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Position> getFoodPosition() {
        int y1 = -1;
        foodPosition = new ArrayList<>();
        for (Integer[] y : fields){
            y1++;
            int x2 = -1;
            for (Integer x: y){
                x2++;
                if(x.equals(5)){
                    foodPosition.add(new Position(x2 , y1));
                }
            }
        }
        return foodPosition;
    }

    public void setFoodPosition(List<Position> foodPosition) {
        this.foodPosition = foodPosition;
    }

    public Integer[][] getFields() {
        return fields;
    }

    public void setFields(Integer[][] fields) {
        this.fields = fields;
    }

    public Integer[][] generateMaze() {
        generatePath(1, 1); // Начинаем с координат (0, 0)
        return fields;
    }

    private void generatePath(int x, int y) {
        visited[y][x] = true;
        fields[y][x] = 0; // Открываем клетку

        // Список направлений (восток, запад, юг, север)
        List<Integer[]> directions = new ArrayList<>();
        directions.add(new Integer[]{1, 0}); // Восток
        directions.add(new Integer[]{-1, 0}); // Запад
        directions.add(new Integer[]{0, 1}); // Южный
        directions.add(new Integer[]{0, -1}); // Северный

        // Перемешиваем направления случайным образом
        Collections.shuffle(directions, random);

        for (Integer[] direction : directions) {
            int newX = x + direction[0] * 2; // Пропускаем одну клетку
            int newY = y + direction[1] * 2; // Пропускаем одну клетку

            if (isInBounds(newX, newY) && !visited[newY][newX]) {
                // Удаляем стену между текущей клеткой и новой
                fields[y + direction[1]][x + direction[0]] = 0;
                // Рекурсивно продолжаем генерацию
                generatePath(newX, newY);
            }
        }
        addFood();
    }

    private boolean isInBounds(int x, int y) {
        return x > 0 && x < width && y > 0 && y < height;
    }

    public void addFood() {
        int foodPlaced = 0;
        while (numberOfFood != 0) {
            int x = random.nextInt(width); // Генерируем координаты по ширине
            int y = random.nextInt(height); // Генерируем координаты по высоте
            if (fields[y][x] == 0) { // Проверяем, что клетка проходимая
                fields[y][x] = 5; // Размещаем еду
                foodPosition.add(new Position(y, x));
                numberOfFood--;
            }
        }
    }

    public void printMap() {
        for (Integer[] row : fields) {
            for (Integer cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public boolean isValidPosition(Position position) {
        return position.getX() >= 0 && position.getX() < width
                && position.getY() >= 0 && position.getY() < height;
    }

    public boolean isObstacleAt(int x, int y) {
        if(fields[y][x] == 1){
            return true;
        }
        return false;
    }
}
