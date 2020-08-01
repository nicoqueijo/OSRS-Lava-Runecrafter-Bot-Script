import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.dreambot.api.methods.Calculations.random;

@ScriptManifest(
        author = "Nico",
        description = "Runecrafts lava runes",
        category = Category.RUNECRAFTING,
        version = 1.0,
        name = "Lava Runecrafter"
)
public class LavaRunecrafter extends AbstractScript {
    /**
     * Equipment: Fire tiara, Graceful cape, Binding necklace, Mist battlestaff, Gracefultop,
     * Graceful legs, Tome of fire, Graceful gloves, Graceful boots, Ring of dueling
     * <p>
     * Inventory: Earth runes, Large pouch, medium pouch, Small pouch,
     * Rune pouch (Cosmic runes, Astral runes)
     * <p>
     * Spellbook: Ancient
     */

    public enum State {
        BANK, DESERT, RUINS
    }

    public State state = State.BANK;
    public final Tile BANK_TILE = new Tile(2443, 3083, 0);
    public final Rectangle MINIMAP_LEFT_SIDE_RECTANGLE = new Rectangle(600, 90, 30, 40);
    public final Rectangle MINIMAP_RIGHT_SIDE_RECTANGLE = new Rectangle(645, 90, 30, 40);
    public final Rectangle BANK_RECTANGLE = new Rectangle(310, 10, 200, 90);

    public Timer timer = new Timer();
    private final long timeToStop = (long) 60 * 1000 * 118;
    private List<Node> nodes = new ArrayList<>();
    public boolean isPouchFull = false;
    public int breaksTaken = 0;

    private Node lastNodeExecuted = null;
    private int timesExecutedSameNode = 0;

    private final int XP_LEVEL_82 = 2421087;
    private int beginningXp;

    @Override
    public void onStart() {
        // Assure run on
        // Assure bank is set on quantity all
        super.onStart();
        logInfo("Started at " + timer.formatTime());
        beginningXp = getSkills().getExperience(Skill.RUNECRAFTING);
        nodes.add(new RepairPouchesNode(this));
        nodes.add(new DepositRunesNode(this));
        nodes.add(new WithdrawJewelryNode(this));
        nodes.add(new WithdrawPureEssenceNode(this));
        nodes.add(new WearJewelryNode(this));
        nodes.add(new FillPouchesNode(this));
        nodes.add(new WithdrawPureEssenceNode(this));
        nodes.add(new TeleToDuelArenaNode(this));
        nodes.add(new WalkToRuinNode(this));
        nodes.add(new RunecraftNode(this));
    }



    @Override
    public void onPaint(Graphics graphics) {
        super.onPaint(graphics);
        int currentXp = getSkills().getExperience(Skill.RUNECRAFTING);
        int xpGrained = currentXp - beginningXp;
        graphics.setColor(Color.BLACK);
        graphics.drawString("Lava Runecrafter", 330, 365);
        graphics.drawString("Time running: " + timer.formatTime(), 330, 383);
        graphics.drawString("Total XP gained: " + formatInt(xpGrained), 330, 401);
        graphics.drawString("Hourly XP rate: " + formatInt(timer.getHourlyRate(xpGrained)), 330, 419);
        graphics.drawString("XP until 82: " + formatInt(XP_LEVEL_82 - currentXp), 330, 437);
        graphics.drawString("Hours until 82: " + String.format("%.2f",
                (double) (XP_LEVEL_82 - currentXp) / timer.getHourlyRate(xpGrained)), 330, 455);
    }

    @Override
    public int onLoop() {
        log("Looping...");
        for (Node node : nodes) {
            if (node.validate()) {
                checkInfiniteLoop(node);
                return node.execute();
            }
            if (timer.elapsed() > timeToStop) {
                logInfo("Stopped after " + timer.formatTime() + ". Reached time limit.");
                stop();
            }
        }
        return sleepTime();
    }

    private String formatInt(int num) {
        return NumberFormat.getNumberInstance(Locale.US).format(num);
    }

    private void checkInfiniteLoop(Node node) {
        if (node == lastNodeExecuted) {
            timesExecutedSameNode++;
        } else {
            timesExecutedSameNode = 0;
        }
        if (timesExecutedSameNode > 5) {
            logInfo("Stopped after " + timer.formatTime() + ". " + node.getClass().getSimpleName() + " in infinite loop.");
            stop();
        }
        lastNodeExecuted = node;
    }

    public int sleepTime() {
        return random(150, 200);
    }

    public void adjustCamera(State state) {
        int yaw = getCamera().getYaw();
        int pitch = getCamera().getPitch();
        final int BANK_RUINS_MIN_YAW = 1300;
        final int BANK_RUINS_MAX_YAW = 1450;
        final int DESERT_MIN_YAW = 1850;
        final int DESERT_MAX_YAW = 2000;
        final int MIN_PITCH = 128;
        final int MAX_PITCH = 383;
        switch (state) {
            case BANK: {
                yaw = random(BANK_RUINS_MIN_YAW, BANK_RUINS_MAX_YAW);
                pitch = MIN_PITCH;
                break;
            }
            case DESERT: {
                yaw = random(DESERT_MIN_YAW, DESERT_MAX_YAW);
                pitch = MIN_PITCH;
                break;
            }
            case RUINS: {
                yaw = random(BANK_RUINS_MIN_YAW, BANK_RUINS_MAX_YAW);
                pitch = MAX_PITCH;
                break;
            }
        }
        getCamera().rotateTo(yaw, pitch);
    }

    public void logValidation(String node) {
        log("Validating: " + node);
    }

    public void logExecution(String node) {
        log("Executing: " + node);
    }
}
