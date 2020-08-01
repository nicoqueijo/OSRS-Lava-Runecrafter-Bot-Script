import org.dreambot.api.methods.container.impl.bank.BankType;

import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.logInfo;
import static org.dreambot.api.methods.MethodProvider.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

/**
 * -Open bank
 * -Deposit all lava/fire runes.
 */
public class DepositRunesNode extends Node {

    private final int LAVA_RUNE_ID = 4699;

    public DepositRunesNode(LavaRunecrafter main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return main.getInventory().contains(item -> item.getID() == LAVA_RUNE_ID) &&
                main.BANK_TILE.distance(main.getLocalPlayer().getTile()) < 20;
    }

    @Override
    public int execute() {
        maybeTakeABreak();
        main.logExecution(getClass().getSimpleName());
        main.state = LavaRunecrafter.State.BANK;
        main.adjustCamera(LavaRunecrafter.State.BANK);
        if (!main.getBank().isOpen()) {
            main.getMouse().click(main.getBank().getClosestBank(BankType.CHEST));
            sleep(random(450, 500));
        }
        sleepUntil(() -> main.getBank().isOpen(), random(1000, 1500));
        main.getBank().depositAll(LAVA_RUNE_ID);
        return main.sleepTime();
    }

    private void maybeTakeABreak() {
        if (main.getWalking().getRunEnergy() < 15) {
            int breakLength = random(170, 175) * 1000;
            logInfo("Restoring energy at " + main.timer.formatTime());
            sleep(breakLength);
        } else {
            int random = random(125);
            if (random == 0) {
                logInfo("Taking break #" + (++main.breaksTaken) + " at " + main.timer.formatTime());
                sleep(6000, 10000);
                main.getMouse().moveMouseOutsideScreen();
                int oneMinute = 1000 * 60;
                int threeMinutes = 1000 * 60 * 3;
                sleep(oneMinute, threeMinutes);
            }
        }
    }
}
