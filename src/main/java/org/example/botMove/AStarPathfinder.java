package org.example.botMove;

import org.example.components.GameMap;

import java.util.*;

public class AStarPathfinder {


    private GameMap gameMap; // Ссылка на структуру данных карты
    private final int[] dirX = {0, 1, 0, -1}; // Направления по оси X (право, вниз, лево, вверх)
    private final int[] dirY = {1, 0, -1, 0}; // Направления по оси Y (право, вниз, лево, вверх)

    public AStarPathfinder(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public List<Position> findPath(Position start, Position target) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(node -> node.fScore));
        Map<Position, Double> gScore = new HashMap<>();
        Map<Position, Double> fScore = new HashMap<>();
        Map<Position, Position> cameFrom = new HashMap<>();

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, target));
        openSet.add(new Node(start, fScore.get(start)));

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            // Если текущая позиция является целевой, восстанавливаем путь
            if (current.position.equals(target)) {
                return reconstructPath(cameFrom, current.position);
            }

            for (int i = 0; i < dirX.length; i++) {
                int newX = current.position.getX() + dirX[i];
                int newY = current.position.getY() + dirY[i];
                Position neighbor = new Position(newX, newY);

                // Проверяем, является ли соседняя позиция допустимой
                if (gameMap.isValidPosition(neighbor) && !gameMap.isObstacleAt(newX, newY)) {
                    double tentativeGScore = gScore.get(current.position) + 1; // Предполагаем, что все перемещения равны по стоимости

                    if (tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                        cameFrom.put(neighbor, current.position);
                        gScore.put(neighbor, tentativeGScore);
                        fScore.put(neighbor, tentativeGScore + heuristic(neighbor, target));
                        openSet.add(new Node(neighbor, fScore.get(neighbor)));
                    }
                }
            }
        }

        // Путь не найден
        return Collections.emptyList();
    }
    private List<Position> reconstructPath(Map<Position, Position> cameFrom, Position current) {
        List<Position> totalPath = new ArrayList<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }
        Collections.reverse(totalPath);
        return totalPath;
    }

    private double heuristic(Position a, Position b) {
        // Используем манхэттенское расстояние как эвристическую функцию
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    private static class Node {
        Position position;
        double fScore;

        Node(Position position, double fScore) {
            this.position = position;
            this.fScore = fScore;
        }
    }
}
