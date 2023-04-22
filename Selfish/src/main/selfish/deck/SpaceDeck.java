package selfish.deck;

import java.util.List;

import selfish.GameException;

public class SpaceDeck extends Deck{
    public static final String ASTEROID_FIELD = "Asteroid field";
    public static final String BLANK_SPACE = "Blank space";
    public static final String COSMIC_RADIATION = "Cosmic radiation";
    public static final String GRAVITATIONAL_ANOMALY = "Gravitational anomaly";
    public static final String HYPERSPACE = "Hyperspace";
    public static final String METEOROID = "Meteoroid";
    public static final String MYSTERIOUS_NEBULA = "Mysterious nebula";
    public static final String SOLAR_FLARE = "Solar flare";
    public static final String USEFUL_JUNK = "Useful junk";
    public static final String WORMHOLE = "Wormhole";

    private static final long serialVersionUID = 5016812401135889608L;

    /**
     * SpaceDeck Constructor
     */
    public SpaceDeck() {
    }

    /**
     * SpaceDeck Constructor
     * @param path The text filepath for space cards
     * @throws GameException Type of Exception
     */
    public SpaceDeck(String path) throws GameException{
        List<Card> loadedCards = Deck.loadCards(path);
        this.add(loadedCards);
    }

}