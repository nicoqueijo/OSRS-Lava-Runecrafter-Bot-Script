import static org.dreambot.api.methods.MethodProvider.sleep;

/**
 * -Fill each pouch
 */
public class FillPouchesNode extends Node {

    public FillPouchesNode(LavaRunecrafter main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return !main.isPouchFull && main.state == LavaRunecrafter.State.BANK;
    }

    @Override
    public int execute() {
        main.logExecution(getClass().getSimpleName());
        final int GIANT_POUCH_ID = 5514;
        final int LARGE_POUCH_ID = 5512;
        main.getInventory().interact(GIANT_POUCH_ID, "Fill");
        sleep(100, 150);
        main.getInventory().interact(LARGE_POUCH_ID, "Fill");
        sleep(100, 150);
        main.isPouchFull = true;
        return main.sleepTime();
    }
}
