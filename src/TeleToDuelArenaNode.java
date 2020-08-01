import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.tabs.Tab;

import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

/**
 * -Switch to equipment tab.
 * -Tele duel arena.
 */
public class TeleToDuelArenaNode extends Node {

    public TeleToDuelArenaNode(LavaRunecrafter main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return main.BANK_TILE.equals(main.getLocalPlayer().getTile()) &&
                main.getInventory().isFull() &&
                main.isPouchFull &&
                !main.getBank().isOpen();

    }

    @Override
    public int execute() {
        main.logExecution(getClass().getSimpleName());
        main.getTabs().openWithFKey(Tab.EQUIPMENT);
        sleepUntil(() -> main.getTabs().isOpen(Tab.EQUIPMENT), random(1000, 1500));
        sleep(175, 250);
        main.getEquipment().interact(EquipmentSlot.RING, "Duel Arena");
        sleep(175, 250);
        main.getMouse().move(main.MINIMAP_LEFT_SIDE_RECTANGLE);
        sleepUntil(() -> !main.getLocalPlayer().getTile().equals(main.BANK_TILE), random(3000, 3500));
        return main.sleepTime();
    }
}
