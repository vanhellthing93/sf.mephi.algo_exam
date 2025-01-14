package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    //В худшем случае, каждый юнит может атаковать каждого другого юнита, что дает сложность О(n^2 * logn)
    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {

        //Получение армий
        List<Unit> playerUnits = new ArrayList<>(playerArmy.getUnits());
        List<Unit> computerUnits = new ArrayList<>(computerArmy.getUnits());

        // Пока в обеих армиях есть живые юниты, они атакуют друг друга по очереди.
        while (hasAliveUnits(playerUnits) && hasAliveUnits(computerUnits)) {

          //Сортировка по убыванию значения атаки перед началом каждого раунда, чтобы первыми ходили самые сильные
            sortUnitsByAttack(playerUnits);
            sortUnitsByAttack(computerUnits);

            // Симуляция раунда
            battle(playerUnits, computerUnits);
            battle(computerUnits, playerUnits);
        }
    }

    //Проверка, что есть живые юниты
    private boolean hasAliveUnits(List<Unit> units) {
        return units.stream().anyMatch(Unit::isAlive);
    }

    //Сортировка юнитов по силе атаки. Сложность O(n*logn)
    private void sortUnitsByAttack(List<Unit> units) {
        units.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());
    }

    private void battle(List<Unit> attackArmy, List<Unit> defenseArmy) throws InterruptedException {
        Iterator<Unit> iterator = attackArmy.iterator();

        while (iterator.hasNext()) {
            Unit attackUnit = iterator.next();

            if (!attackUnit.isAlive()) {
                iterator.remove();
                continue;
            }

            Unit defenseUnit = attackUnit.getProgram().attack();

            if (defenseUnit != null) {
                printBattleLog.printBattleLog(attackUnit, defenseUnit);
                if (!defenseUnit.isAlive()) {
                    defenseArmy.remove(defenseUnit);
                }
            }

            if (!attackUnit.isAlive()) {
                iterator.remove();
            }
        }
    }
}