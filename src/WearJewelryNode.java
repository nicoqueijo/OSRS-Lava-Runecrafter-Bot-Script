import static org.dreambot.api.methods.MethodProvider.sleep;

/**
 * -Wear ring/necklace
 */
public class WearJewelryNode extends Node {

    private final int BINDING_NECKLACE_ID = 5521;
    private final int RING_OF_DUELING_8_ID = 2552;

    public WearJewelryNode(LavaRunecrafter main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return (main.getInventory().contains(
                item -> item.getID() == BINDING_NECKLACE_ID ||
                        item.getID() == RING_OF_DUELING_8_ID));
    }

    @Override
    public int execute() {
        main.logExecution(getClass().getSimpleName());
        if (main.getInventory().contains(BINDING_NECKLACE_ID)) {
            sleep(100, 200);
            main.getInventory().interact(BINDING_NECKLACE_ID, "Wear");
        }
        if (main.getInventory().contains(RING_OF_DUELING_8_ID)) {
            sleep(100, 200);
            main.getInventory().interact(RING_OF_DUELING_8_ID, "Wear");
        }
        return main.sleepTime();
    }
}
