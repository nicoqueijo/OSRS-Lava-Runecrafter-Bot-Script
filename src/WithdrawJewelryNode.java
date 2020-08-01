import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;

import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

/**
 * -If ring/necklace missing, withdraw one of each.
 */
public class WithdrawJewelryNode extends Node {

    private final int BINDING_NECKLACE_ID = 5521;
    private final int RING_OF_DUELING_8_ID = 2552;
    private boolean isBindingNecklaceMissing = true;
    private boolean isRingOfDuelingMissing = true;

    public WithdrawJewelryNode(LavaRunecrafter main) {
        super(main);
    }

    @Override
    public boolean validate() {
        isBindingNecklaceMissing = !main.getInventory().contains(BINDING_NECKLACE_ID) &&
                !main.getEquipment().contains(BINDING_NECKLACE_ID);
        isRingOfDuelingMissing = !main.getInventory().contains(RING_OF_DUELING_8_ID) &&
                !main.getEquipment().getNameForSlot(EquipmentSlot.RING.getSlot())
                        .startsWith("Ring of dueling");
        return (isBindingNecklaceMissing || isRingOfDuelingMissing) && !main.getInventory().isFull();
    }

    @Override
    public int execute() {
        main.logExecution(getClass().getSimpleName());
        boolean areItemsMissing = isBindingNecklaceMissing || isRingOfDuelingMissing;
        if (areItemsMissing) {
            main.getBank().open(BankLocation.CASTLE_WARS);
            if (isBindingNecklaceMissing) {
                main.getBank().withdraw(BINDING_NECKLACE_ID);
                sleepUntil(() -> main.getInventory().contains(
                        item -> item.getID() == BINDING_NECKLACE_ID),
                        random(1000, 1500));
            }
            sleep(100, 200);
            if (isRingOfDuelingMissing) {
                main.getBank().withdraw(RING_OF_DUELING_8_ID);
                sleepUntil(() -> main.getInventory().contains(
                        item -> item.getID() == RING_OF_DUELING_8_ID),
                        random(1000, 1500));
            }
        }
        return main.sleepTime();
    }
}
