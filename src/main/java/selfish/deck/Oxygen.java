package selfish.deck;

/**
 * Class representing Oxygen Card. A Child Class of Card
 *
 * @author Wai Yan Aung
 * @version 1
 */
public class Oxygen extends Card {
  private static final long serialVersionUID = 5016812401135889608L;
  private final int value;

  /**
   * Public Constructor of Oxygen (Specialized Card)
   *
   * @param value Oxygen value
   */
  public Oxygen(int value) {
    super("Oxygen", "Description Oxygen");
    this.value = value;
  }

  /**
   * Returns the number of Oxygen
   *
   * @return The number of Oxygen
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Returns a string representation of this Tile object.
   *
   * @return A string representation of this Tile object.
   */
  public String toString() {
    if (this.value == 1) {
      return GameDeck.OXYGEN_1;
    }
    return GameDeck.OXYGEN_2;
  }

  /**
   * To compare two oxygen card's values
   *
   * @param oxygen Oxygen card
   * @return 0 if two oxygen cards are equal
   */
  public int compareTo(Oxygen oxygen) {
    return this.value - oxygen.value;
  }
}
