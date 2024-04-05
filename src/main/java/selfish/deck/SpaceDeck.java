package selfish.deck;

import java.util.List;
import selfish.GameException;

/**
 * SpaceDeck Class (Extended from Deck Class)
 *
 * @author Wai Yan Aung
 * @version 1
 */
public class SpaceDeck extends Deck {
  // ASTEROID_FIELD's name
  public static final String ASTEROID_FIELD = "Asteroid field";

  // BLANK_SPACE's name
  public static final String BLANK_SPACE = "Blank space";

  // COSMIC_RADIATION's name
  public static final String COSMIC_RADIATION = "Cosmic radiation";

  // GRAVITATIONAL_ANOMALY's name
  public static final String GRAVITATIONAL_ANOMALY = "Gravitational anomaly";

  // HYPERSPACE's name
  public static final String HYPERSPACE = "Hyperspace";

  // METEOROID's name
  public static final String METEOROID = "Meteoroid";

  // MYSTERIOUS_NEBULA's name
  public static final String MYSTERIOUS_NEBULA = "Mysterious nebula";

  // SOLAR_FLARE's name
  public static final String SOLAR_FLARE = "Solar flare";

  // USEFUL_JUNK's name
  public static final String USEFUL_JUNK = "Useful junk";

  // WORMHOLE's name
  public static final String WORMHOLE = "Wormhole";

  private static final long serialVersionUID = 5016812401135889608L;

  /**
   * SpaceDeck Constructor
   */
  public SpaceDeck() {
  }

  /**
   * SpaceDeck Constructor
   *
   * @param path The text filepath for space cards
   * @throws GameException Type of Exception
   */
  public SpaceDeck(String path) throws GameException {
    List<Card> loadedCards = Deck.loadCards(path);
    this.add(loadedCards);
  }

}