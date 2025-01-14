package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            for (Unit unit : row) {
                if (unit != null && unit.isAlive()) {
                    if (isEdgeUnit(unit, row, isLeftArmyTarget)) {
                        suitableUnits.add(unit);
                    }
                }
            }
        }

        return suitableUnits;
    }

    private boolean isEdgeUnit(Unit unit, List<Unit> row, boolean isLeftArmyTarget) {
        int unitIndex = row.indexOf(unit);
        if (isLeftArmyTarget) {
            for (int i = unitIndex + 1; i < row.size(); i++) {
                if (row.get(i) != null) {
                    return false;
                }
            }
        } else {
            for (int i = unitIndex - 1; i >= 0; i--) {
                if (row.get(i) != null) {
                    return false;
                }
            }
        }
        return true;
    }