package selfish.deck;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import selfish.GameException;

/**
 * Deck Class
 *
 * @author Wai Yan Aung
 * @version 1
 */
public abstract class Deck implements Serializable {

    private static final long serialVersionUID = 5016812401135889608L;
    private final Collection<Card> cards = new ArrayList<>();

    /**
     * Deck Constructor
     */
    protected Deck() {
    }

    /**
     * Load cards from a text file
     *
     * @param path Text filepath for cards
     * @return List of cards
     * @throws GameException Type of Exception
     */
    protected static List<Card> loadCards(String path) throws GameException {

        BufferedReader fileStream = null;
        List<Card> returningCards = new ArrayList<>();
        try {
            fileStream = new BufferedReader(new FileReader(path));

            String temporaryString = fileStream.readLine();
            while (temporaryString != null) {
                if (!temporaryString.equals("NAME; DESCRIPTION; QUANTITY")) {
                    Card[] cards = stringToCards(temporaryString);

                    Collections.addAll(returningCards, cards);
                }
                temporaryString = fileStream.readLine();
            }

            fileStream.close();
        } catch (FileNotFoundException e) {
            throw new GameException("", e);
        } catch (IOException e) {
            throw new GameException("", e);
        }

        return returningCards;
    }

    /**
     * Convert each line of the file into the appropriate number of cards of particular type
     *
     * @param str each line of the file
     * @return Array of cards
     */
    protected static Card[] stringToCards(String str) {
        String[] splitted = str.split("; ");

        String name = splitted[0];
        String desciprtion = splitted[1];
        int numberOfCards = Integer.valueOf(splitted[2]);

        Card[] newCards = new Card[numberOfCards];

        for (int i = 0; i < numberOfCards; i++) {
            newCards[i] = new Card(name, desciprtion);
        }

        return newCards;
    }

    /**
     * Add card into collection of cards
     *
     * @param card Card
     * @return Total number of cards in the deck
     */
    public int add(Card card) {
        this.cards.add(card);
        return this.cards.size();
    }

    /**
     * Add list of cards into collection of cards
     *
     * @param cards list of cards
     * @return Total number of cards in the deck
     */
    protected int add(List<Card> cards) {
        this.cards.addAll(cards);
        return this.cards.size();
    }

    /**
     * Function that removes and returns a card from the deck
     *
     * @return Card
     */
    public Card draw() {
        if (this.cards.isEmpty()) {
            throw new IllegalStateException();
        }

        LinkedList<Card> linkedList = new LinkedList<Card>(this.cards);
        Card card = linkedList.removeLast();

        this.cards.remove(card);

        return card;
    }

    /**
     * Function to remove a card from the deck
     *
     * @param card Card
     */
    public void remove(Card card) {
        this.cards.remove(card);
    }

    /**
     * Function to shuffle the deck
     *
     * @param random Random instance of java.util.Random
     */
    public void shuffle(Random random) {
        Collections.shuffle((ArrayList<Card>) this.cards, random);
    }

    /**
     * Function to return the number of cards currently in this deck
     *
     * @return the number of cards currently in this deck
     */
    public int size() {
        return this.cards.size();
    }
}
