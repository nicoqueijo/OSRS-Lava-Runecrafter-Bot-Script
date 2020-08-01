import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.magic.Lunar;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;

import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

public class RunecraftNode extends Node {

    /**
     * -Run to altar.
     * -Cast magic imbue.
     * -Switch to inventory tab.
     * -Use earth runes on altar.
     * -Empty all pouches.
     * -Use earth runes on altar.
     * -Switch to equipment tab.
     * -Tele to castle wars.
     *
     * @param main
     */
    public RunecraftNode(LavaRunecrafter main) {
        super(main);
    }

    @Override
    public boolean validate() {
        final Area RUINS_AREA = new Area(new Tile(2597, 4855, 0), new Tile(2570, 4826, 0));
        return RUINS_AREA.contains(main.getLocalPlayer().getTile()) && main.getInventory().isFull();
    }

    @Override
    public int execute() {
        main.logExecution(getClass().getSimpleName());
        main.state = LavaRunecrafter.State.RUINS;
        final int LARGE_POUCH_ID = 5512;
        final int MEDIUM_POUCH_ID = 5510;
        final int SMALL_POUCH_ID = 5509;
        final int EARTH_RUNE_ID = 557;
        final int ALTAR_ID = 34764;
        final Tile TILE_NEXT_TO_ALTAR = new Tile(2582, 4840, 0);
        main.getWalking().walk(TILE_NEXT_TO_ALTAR);
        main.adjustCamera(LavaRunecrafter.State.RUINS);
        sleep(150, 200);
        boolean spellCasted = main.getMagic().castSpell(Lunar.MAGIC_IMBUE);
        sleepUntil(() -> spellCasted, random(1000, 1500));
        main.getTabs().openWithFKey(Tab.INVENTORY);
        sleep(25, 50);
        main.getMouse().move(main.getInventory().slotBounds(0));
        sleepUntil(() -> main.getTabs().isOpen(Tab.INVENTORY), random(1000, 1500));
        sleep(1000, 1250);
        boolean runesCrafter = main.getInventory().get(EARTH_RUNE_ID).useOn(main.getGameObjects().closest(ALTAR_ID));
        if (!runesCrafter) {
            main.getInventory().deselect();
            main.getInventory().get(EARTH_RUNE_ID).useOn(main.getGameObjects().closest(ALTAR_ID));
        }
        main.getMouse().move(main.getInventory().slotBounds(4));
        sleepUntil(() -> !main.getInventory().isFull(), random(1000, 1500));
        sleep(300, 500);
        main.getKeyboard().pressShift();
        main.getInventory().get(LARGE_POUCH_ID).interact();
        sleep(150, 200);
        main.getInventory().get(MEDIUM_POUCH_ID).interact();
        sleep(150, 200);
        main.getInventory().get(SMALL_POUCH_ID).interact();
        sleep(150, 200);
        main.getKeyboard().releaseShift();
        sleep(150, 200);
        main.isPouchFull = false;
        runesCrafter = main.getInventory().get(EARTH_RUNE_ID).useOn(main.getGameObjects().closest(ALTAR_ID));
        if (!runesCrafter) {
            main.getInventory().deselect();
            main.getInventory().get(EARTH_RUNE_ID).useOn(main.getGameObjects().closest(ALTAR_ID));
        }
        sleep(175, 250);
        main.getTabs().openWithFKey(Tab.EQUIPMENT);
        sleepUntil(() -> main.getTabs().isOpen(Tab.EQUIPMENT), random(1000, 1500));
        sleep(175, 250);
        main.getEquipment().interact(EquipmentSlot.RING, "Castle Wars");
        sleep(150, 200);
        main.getMouse().move(main.BANK_RECTANGLE);
        sleep(300, 400);
        main.getTabs().openWithFKey(Tab.INVENTORY);
        return main.sleepTime();
    }
}
