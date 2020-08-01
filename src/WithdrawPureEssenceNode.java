import org.dreambot.api.methods.container.impl.bank.BankLocation;

import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

/**
 * -Withdraw all pure ess.
 * -Close bank.
 */
public class WithdrawPureEssenceNode extends Node {

    public WithdrawPureEssenceNode(LavaRunecrafter main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return main.getInventory().getEmptySlots() > 2 && main.state == LavaRunecrafter.State.BANK;
    }

    @Override
    public int execute() {
        main.logExecution(getClass().getSimpleName());
        final int PURE_ESSENCE_ID = 7936;
        main.getBank().open(BankLocation.CASTLE_WARS);
        sleepUntil(() -> main.getBank().isOpen(), random(1000, 1500));
        main.getBank().withdrawAll(PURE_ESSENCE_ID);
        sleepUntil(() -> main.getInventory().isFull(), random(1000, 1500));
        main.getBank().close();
        return main.sleepTime();
    }
}
