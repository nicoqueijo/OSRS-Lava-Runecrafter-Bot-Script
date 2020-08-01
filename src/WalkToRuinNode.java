import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;

import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

/**
 * -Run to ruins.
 * -Switch to spellbook tab.
 * -Enter altar.
 */
public class WalkToRuinNode extends Node {

    public WalkToRuinNode(LavaRunecrafter main) {
        super(main);
    }

    @Override
    public boolean validate() {
        final Area DESERT_AREA = new Area(new Tile(3317, 3259, 0), new Tile(3304, 3230, 0));
        return DESERT_AREA.contains(main.getLocalPlayer().getTile());
    }

    @Override
    public int execute() {
        main.logExecution(getClass().getSimpleName());
        main.state = LavaRunecrafter.State.DESERT;
        final int RUINS_ID = 34817;
        final Tile TILE_OUTSIDE_RUINS = new Tile(3310, 3251, 0);
        main.getWalking().walk(TILE_OUTSIDE_RUINS);
        main.adjustCamera(LavaRunecrafter.State.DESERT);
        sleep(150, 200);
        main.getTabs().openWithFKey(Tab.MAGIC);
        sleepUntil(() -> main.getLocalPlayer().distance(TILE_OUTSIDE_RUINS) < 4, random(1500, 2000));
        boolean clickedToEnter = main.getGameObjects().closest(RUINS_ID).interact("Enter");
        sleep(150, 200);
        if (clickedToEnter) {
            main.getMouse().move(main.MINIMAP_RIGHT_SIDE_RECTANGLE);
        }
        sleep(1000, 1500);
        return main.sleepTime();
    }
}
