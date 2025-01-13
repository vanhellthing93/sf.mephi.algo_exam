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
        unsortedUnits.sort(Comparator.comparingDouble(
                unit -> -((double) unit.getBaseAttack() / unit.getCost() + (double) unit.getHealth() / unit.getCost())));
    }

}