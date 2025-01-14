package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army enemyArmy = new Army();
        List<Unit> listOfUnits = new ArrayList<>();
        int totalPoints = 0;

        sortUnits(unitList);

        for (Unit unit : unitList) {
            int maxUnitsForType = Math.min(11, maxPoints / unit.getCost());
            for (int i = 0; i < maxUnitsForType && totalPoints + unit.getCost() <= maxPoints; i++) {
                listOfUnits.add(createUnit(unit));
            }
        }

        enemyArmy.setUnits(listOfUnits);
        enemyArmy.setPoints(totalPoints);
        return enemyArmy;
    }

    private Unit createUnit (Unit unit) {
        Unit newUnit = new Unit(unit.getName(), unit.getUnitType(), unit.getHealth(),
                unit.getBaseAttack(), unit.getCost(), unit.getAttackType(),
                unit.getAttackBonuses(), unit.getDefenceBonuses(), unit.getxCoordinate(), unit.getyCoordinate());
        newUnit.setName(unit.getUnitType());
        return newUnit;
    }

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