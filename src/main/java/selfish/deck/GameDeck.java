package selfish.deck;

import java.util.ArrayList;
import java.util.Collections;
import selfish.GameException;

/**
 * GameDeck Class (Extended from Deck Class)
 *
 * @author Wai Yan Aung
 * @version 1
 */
public class GameDeck extends Deck {
  // HACK_SUIT's name
  public static final String HACK_SUIT = "Hack suit";

  // HOLE_IN_SUIT's name
  public static final String HOLE_IN_SUIT = "Hole in suit";

  // LASER_BLAST's name
  public static final String LASER_BLAST = "Laser blast";

  // OXYGEN's name
  public static final String OXYGEN = "Oxygen";

  // OXYGEN_1's name
  public static final String OXYGEN_1 = "Oxygen(1)";

  // OXYGEN_2's name
  public static final String OXYGEN_2 = "Oxygen(2)";

  // OXYGEN_SIPHON's name
  public static final String OXYGEN_SIPHON = "Oxygen siphon";

  // ROCKET_BOOSTER's name
  public static final String ROCKET_BOOSTER = "Rocket booster";

  // SHIELD's name
  public static final String SHIELD = "Shield";

  // TETHER's name
  public static final String TETHER = "Tether";

  // TRACTOR_BEAM's name
  public static final String TRACTOR_BEAM = "Tractor beam";

  private static final long serialVersionUID = 5016812401135889608L;

  /**
   * GameDeck Constructor
   */
  public GameDeck() {
  }

  /**
   * GameDeck Constructor
   *
   * @param path The text filepath for action cards
   * @throws GameException Type of Exception
   */
  public GameDeck(String path) throws GameException {
    // Load action cards first
    this.add(Deck.loadCards(path));

    // Load Oxygen(2) cards
    for (int i = 0; i < 10; i++) {
      this.add(new Oxygen(2));
    }

    // Load Oxygen(1) cards
    for (int i = 0; i < 38; i++) {
      this.add(new Oxygen(1));
    }
  }

  /**
   * Function to draw Oxygen Cards from collection of cards
   *
   * @param value The value of oxygen card
   * @return Oxygen Card
   */
  public Oxygen drawOxygen(int value) {
    int deckSize = this.size();

    if (deckSize == 0) {
      throw new IllegalStateException();
    }

    ArrayList<Card> cardsList = new ArrayList<>();
    Oxygen oxygenCard = null;
    for (int i = 0; i < deckSize; i++) {
      Card tempCard = this.draw();

      if (tempCard instanceof Oxygen && ((Oxygen) tempCard).getValue() == value) {
        oxygenCard = (Oxygen) tempCard;
        break;
      } else {
        cardsList.add(tempCard);
      }
    }

    Collections.reverse(cardsList);
    this.add(cardsList);

    if (oxygenCard == null) {
      throw new IllegalStateException();
    }

    return oxygenCard;
  }

  /**
   * Function to split Oxygen(2) card into two Oxygen(1) cards
   *
   * @param unit Oxygen(2) card
   * @return List of two Oxygen(1) cards
   */
  public Oxygen[] splitOxygen(Oxygen unit) {
    if (unit.getValue() != 2) {
      throw new IllegalArgumentException();
    }

    // Draw Oxygen(1) twice
    Oxygen oxygenCardOne = this.drawOxygen(1);
    Oxygen oxygenCardTwo = this.drawOxygen(1);

    // Adding the Oxygen(2) Card
    this.add(unit);

    Oxygen[] oxygenLists = {oxygenCardTwo, oxygenCardOne};
    return oxygenLists;
  }

}