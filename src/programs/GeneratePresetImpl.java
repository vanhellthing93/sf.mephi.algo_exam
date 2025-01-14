package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    //Общая сложность O(n*log(n))
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        
        Army enemyArmy = new Army(); //Пустая армия
        List<Unit> listOfUnits = new ArrayList<>(); //Пустой список юнитов
        int totalPoints = 0; //Количество потраченных очков (начальное значение)
        int unitNumber = 0;

        sortUnits(unitList); //Сортировка юнитов по ценности O(n*log(n))

        for (Unit unit : unitList) { // Цикл по списку юнитов O(n)
            int maxUnitsForType = Math.min(11, maxPoints / unit.getCost()); 
            for (int i = 0; i < maxUnitsForType && totalPoints + unit.getCost() <= maxPoints; i++) { //Цикл по максиальному количеству юнитов. В худшем случае O(11), т.е. O(1)
                listOfUnits.add(createUnit(unit, i, unitNumber)); //добавление юнита в список
                unitNumber++;
            }
        }

        enemyArmy.setUnits(listOfUnits);
        enemyArmy.setPoints(totalPoints);
        return enemyArmy;
    }

    // Метод для создания юнита. Сложность O(1)
    private Unit createUnit (Unit unit, int unitTypeNumber, int unitNumber) {
        int yCoord = unitNumber;
        int xCoord = 2;
        if (yCoord > 41) {
            xCoord = 0;
            yCoord -= 42;
        }
        if (yCoord > 20) {
            xCoord = 1;
            yCoord -= 21;
        }

        Unit newUnit = new Unit(unit.getName(), unit.getUnitType(), unit.getHealth(),
                unit.getBaseAttack(), unit.getCost(), unit.getAttackType(),
                unit.getAttackBonuses(), unit.getDefenceBonuses(), unit.getxCoordinate(), unit.getyCoordinate());
        newUnit.setName(unit.getUnitType() + " " + unitTypeNumber);
        newUnit.setxCoordinate(xCoord);
        newUnit.setyCoordinate(yCoord);
        return newUnit;
    }

    // Соритровка юнитов в зависимсоти от соотношения атаки к стоимостии и соотношения здоровья к стоимости. Сложность Collections.sort() составляет O(n*log(n))
    private void sortUnits(List<Unit> unsortedUnits) {
        unsortedUnits.sort(new Comparator<Unit>() {
            @Override
            public int compare(Unit unit1, Unit unit2) {
                double value1 = -((double) unit1.getBaseAttack() / unit1.getCost() + (double) unit1.getHealth() / unit1.getCost());
                double value2 = -((double) unit2.getBaseAttack() / unit2.getCost() + (double) unit2.getHealth() / unit2.getCost());
                return Double.compare(value1, value2);
            }
        });

}
}