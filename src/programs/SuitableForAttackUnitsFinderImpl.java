package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    //алгоритмическая сложность O(n*m), мы один раз перебираем всех юнитов (m), во всех рядах (n)
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        //создаём список доступных юнитов
        List<Unit> suitableUnits = new ArrayList<>();

        //перебираем все ряды
        for (List<Unit> row : unitsByRow) {

            // Проверяем юниты справа налево
            if (isLeftArmyTarget) {
                Unit unitFromRight = null; //переменная, в которой хранится юнит справа от текущего. Если она null, то он доступен для атаки
                for (int i = row.size() - 1; i >= 0; i--) {
                    Unit unit = row.get(i);
                    if (unit != null && unit.isAlive()) {
                        if (unitFromRight == null) {
                            suitableUnits.add(unit);
                        }
                        unitFromRight = unit; // сохарняем текущий юнит в переменную, в которой хранится юнит справа
                    }
                }

           // Проверяем юниты слева направо
            } else {
     
                Unit unitFromLeft = null; //переменная, в которой хранится юнит слева от текущего. Если она null, то он доступен для атаки
                for (int i = 0; i < row.size(); i++) {
                    Unit unit = row.get(i);
                    if (unit != null && unit.isAlive()) {
                        if (unitFromLeft == null) {
                            suitableUnits.add(unit);
                        }
                        unitFromLeft = unit; // сохарняем текущий юнит в переменную, в которой хранится юнит слева
                    }
                }
            }
        }

        return suitableUnits;
    }
}