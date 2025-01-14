package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    //Общая сложность O(HEIGHT*WIDTH), т.к.основные операции имеют сложность O(HEIGHT*WIDTH)
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {


       // Инициализация массива расстояний O(HEIGHT*WIDTH)
        int[][] distances = initializeDistanceArray(attackUnit);

        // Массив для хранения посещенных клеток
        boolean[][] visited = new boolean[HEIGHT][WIDTH];

        // Массив для хранения предыдущих клеток
        Edge[][] previous = new Edge[HEIGHT][WIDTH];

        // Множество занятых клеток - o(n), где n < HEIGHT*WIDTH
        Set<String> occupiedCells = getOccupiedCells(existingUnitList, attackUnit, targetUnit);

        // Приоритетная очередь для хранения клеток
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingInt(e -> distances[e.getY()][e.getX()]));
        queue.add(new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate()));

        // Основной цикл алгоритма Дейкстры. В худшем случае O(HEIGHT*WIDTH)
        while (!queue.isEmpty()) {
            Edge current = queue.poll();
            int x = current.getX();
            int y = current.getY();

            // Пропускаем уже посещенные клетки
            if (visited[y][x]) {
                continue;
            }
            visited[y][x] = true;

            // Если достигли целевого юнита, завершаем поиск
            if (x == targetUnit.getxCoordinate() && y == targetUnit.getyCoordinate()) {
                break;
            }

            // Исследуем соседние клетки
            exploreNeighbors(current, occupiedCells, distances, previous, queue);
        }

        // Восстанавливаем путь
        return constructPath(previous, attackUnit, targetUnit);
    }

    // Инициализация массива расстояний. Cложность O(HEIGHT*WIDTH)
    private int[][] initializeDistanceArray(Unit attackUnit) {
        int[][] distances = new int[HEIGHT][WIDTH];
        for (int[] row : distances) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        distances[attackUnit.getyCoordinate()][attackUnit.getxCoordinate()] = 0;
        return distances;
    }

    // Получение занятых клеток. Сложность O(n), где n - количество юнитов в existingUnitList. При этом n очевидно меньше HEIGHT*WIDTH
    private Set<String> getOccupiedCells(List<Unit> existingUnitList, Unit attackUnit, Unit targetUnit) {
        Set<String> occupiedCells = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                occupiedCells.add(unit.getxCoordinate() + "," + unit.getyCoordinate());
            }
        }
        return occupiedCells;
    }

    // Исследование соседних клеток. В худшем случае сложность -  O(HEIGHT*WIDTH)
    private void exploreNeighbors(Edge current, Set<String> occupiedCells, int[][] distances, Edge[][] previous, PriorityQueue<Edge> queue) {
        for (int[] dir : DIRECTIONS) {
            int neighborX = current.getX() + dir[0];
            int neighborY = current.getY() + dir[1];
            if (isValid(neighborX, neighborY, occupiedCells)) {
                int newDistance = distances[current.getY()][current.getX()] + 1;
                if (newDistance < distances[neighborY][neighborX]) {
                    distances[neighborY][neighborX] = newDistance;
                    previous[neighborY][neighborX] = current;
                    queue.add(new Edge(neighborX, neighborY));
                }
            }
        }
    }

    // Проверка валидности координат. Сложность O(1)
    private boolean isValid(int x, int y, Set<String> occupiedCells) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && !occupiedCells.contains(x + "," + y);
    }

    // Конструирование пути. В худшем случае сложность - O(HEIGHT*WIDTH)
    private List<Edge> constructPath(Edge[][] previous, Unit attackUnit, Unit targetUnit) {
        List<Edge> path = new ArrayList<>();
        Edge current = new Edge(targetUnit.getxCoordinate(), targetUnit.getyCoordinate());
        while (current != null) {
            path.add(current);
            current = previous[current.getY()][current.getX()];
        }
        Collections.reverse(path);
        return path;
    }
}