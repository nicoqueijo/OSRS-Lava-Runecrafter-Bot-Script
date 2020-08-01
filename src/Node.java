public abstract class Node {

    protected final LavaRunecrafter main;

    public Node(LavaRunecrafter main) {
        this.main = main;
    }

    public abstract boolean validate();

    public abstract int execute();
}
