package selfish.deck;

/**
 * Class representing Oxygen Card. A Child Class of Card
 * @author Wai Yan Aung
 * @version 1
 */
public class Oxygen extends Card{
    private int value;

    private static final long serialVersionUID = 1L;

    /**
     * Public Constructor of Oxygen (Specialized Card)
     * @param value
     */
    public Oxygen(int value) {
        super("Oxygen", "Description Oxygen");
        this.value = value;
    }

    /**
     * Returns the number of Oxygen
     * @return The number of Oxygen
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Returns a string representation of this Tile object.
     * @return A string representation of this Tile object.
     */
    public String toString() {
        if (this.value == 1){
            return GameDeck.OXYGEN_1;
        }
        return GameDeck.OXYGEN_2;
    }

    /**
     * 
     * @param oxygen
     * @return
     */
    public int compareTo(Oxygen oxygen){
        return this.value - oxygen.value;
    }
}
