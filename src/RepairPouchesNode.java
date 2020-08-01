import org.dreambot.api.methods.magic.Lunar;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.wrappers.items.Item;

import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

/**
 * -Switch to magic tab.
 * -Cast NPC Contact.
 * -Select Dark Mage.
 * -Wait for dialog.
 * -Continue through dialog until done.
 * -Switch to inventory tab.
 */
public class RepairPouchesNode extends Node {

    public RepairPouchesNode(LavaRunecrafter main) {
        super(main);
    }

    @Override
    public boolean validate() {
        final int BROKEN_LARGE_POUCH_ID = 5513;
        final int BROKEN_MEDIUM_POUCH_ID = 5511;
        return main.getInventory().contains(item ->
                (item.getID() == BROKEN_MEDIUM_POUCH_ID ||
                        item.getID() == BROKEN_LARGE_POUCH_ID));
    }

    @Override
    public int execute() {
        main.logExecution(getClass().getSimpleName());
        sleep(1000, 1500);
        main.getTabs().openWithFKey(Tab.MAGIC);
        sleepUntil(() -> main.getTabs().isOpen(Tab.MAGIC), random(1000, 1500));
        sleep(175, 250);
        main.getMagic().castSpell(Lunar.NPC_CONTACT);
        sleepUntil(() -> main.getWidgets().getWidgetChild(75, 14) != null, random(1000, 1500));
        main.getWidgets().getWidgetChild(75, 14).interact();
        sleep(1000, 1500);
        sleepUntil(() -> !main.getLocalPlayer().isAnimating(), random(6000, 7000));
        sleepUntil(() -> main.getDialogues().inDialogue(), random(1000, 1500));
        while (main.getDialogues().inDialogue()) {
            main.getDialogues().spaceToContinue();
            sleep(200, 400);
        }
        sleep(random(300, 400));
        main.getTabs().openWithFKey(Tab.INVENTORY);
        sleepUntil(() -> main.getTabs().isOpen(Tab.INVENTORY), random(1000, 1500));
        sleep(random(300, 400));
        rearrangePouches();
        return main.sleepTime();
    }

    private void rearrangePouches() {
        final int LARGE_POUCH_ID = 5512;
        final int MEDIUM_POUCH_ID = 5510;
        final int SMALL_POUCH_ID = 5509;
        final int LARGE_POUCH_TARGET_SLOT = 4;
        final int MEDIUM_POUCH_TARGET_SLOT = 8;
        final int SMALL_POUCH_TARGET_SLOT = 12;
        dragItem(LARGE_POUCH_ID, LARGE_POUCH_TARGET_SLOT);
        dragItem(MEDIUM_POUCH_ID, MEDIUM_POUCH_TARGET_SLOT);
        dragItem(SMALL_POUCH_ID, SMALL_POUCH_TARGET_SLOT);
    }

    private void dragItem(int itemId, int targetSlot) {
        Item pouch = main.getInventory().get(itemId);
        if (pouch != null && pouch.getSlot() != targetSlot) {
            main.getMouse().move(pouch.getDestination());
            sleep(50, 100);
            main.getMouse().drag(main.getInventory().slotBounds(targetSlot));
            sleepUntil(() -> pouch.getSlot() == targetSlot, random(1000, 1500));
            sleep(50, 100);
        }
    }
}
