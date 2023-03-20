package selfish.deck;

public class Oxygen extends Card{
    private int value;

    private static final long serialVersionUID = 5016812401135889608L;

    public Oxygen(int value) {
        super("Oxygen", "Description Oxygen");
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return "";
    }
}
