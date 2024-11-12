package org.example.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Map {
    private final int width;
    private final int height;
    private int numberOfFood;
    private Integer[][] map;
    private final boolean[][] visited;
    private final Random random;

    public Map(int width, int height, int numberOfFood) {
        this.width = width;
        this.height = height;
        this.numberOfFood = numberOfFood;
        this.map = new Integer[height][width];
        this.visited = new boolean[height][width];
        this.random = new Random();

        // Инициализация карты
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = 1; // Заполняем стенами
                visited[y][x] = false; // Все клетки изначально непосещены
            }
        }
    }

    public int getNumberOfFood() {
        return numberOfFood;
    }

    public void setNumberOfFood(int numberOfFood) {
        this.numberOfFood = numberOfFood;
    }

    public Integer[][] getMap() {
        return map;
    }

    public void setMap(Integer[][] map) {
        this.map = map;
    }

    public Integer[][] generateMaze() {
        generatePath(1, 1); // Начинаем с координат (0, 0)
        return map;
    }

    private void generatePath(int x, int y) {
        visited[y][x] = true;
        map[y][x] = 0; // Открываем клетку

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
                map[y + direction[1]][x + direction[0]] = 0;
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
            if (map[y][x] == 0) { // Проверяем, что клетка проходимая
                map[y][x] = 5; // Размещаем еду
                numberOfFood--;
            }
        }
    }

    public void printMap() {
        for (Integer[] row : map) {
            for (Integer cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
