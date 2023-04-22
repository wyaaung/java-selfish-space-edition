package selfish;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
// import java.util.Random;
import java.util.stream.Collectors;

import selfish.deck.Card;
import selfish.deck.GameDeck;
import selfish.deck.Oxygen;
import selfish.deck.SpaceDeck;

/** Astronaut Class
 *  @author Wai Yan Aung
 *  @version 1
 */
public class Astronaut implements Serializable{
    private GameEngine game;

    private List<Card> actions;
    private List<Oxygen> oxygens;
    private Collection<Card> track;

    private String name;

    private static final long serialVersionUID = 5016812401135889608L;

    /**
     * Astronaut constructor
     * @param name Name of the astronaut
     * @param game GameEngine insatnce this Astronaut belongs to
     */
    public Astronaut(String name, GameEngine game) {
        this.game = game;
        this.name = name;

        this.actions = new ArrayList<Card>();
        this.oxygens = new ArrayList<Oxygen>();
        this.track = new ArrayList<Card>();
    }

    /**
     * Adding action card or oxygen card to the Astronaut
     * @param card Action Card or Oxygen Card
     */
    public void addToHand(Card card){
        if (card instanceof Oxygen){
            this.oxygens.add((Oxygen) card);
        }else{
            this.actions.add(card);
        }
    }

    /**
     * Adding SpaceCard to track
     * @param card SpaceCard
     */
    public void addToTrack(Card card){
        this.track.add(card);
    }

    /**
     * Removes a single-value Oxygen from the Astronaut
     * and adds the card to the Game Engine's gameDiscard pile
     * @return the number of Oxygens remaining (Total value of all Oxygen Cards)
     */
    public int breathe(){
        if (this.oxygens.size() == 0){
            return 0;
        }

        Oxygen oxygenOneCard = this.oxygens.stream()
                    .filter(oxygen -> (oxygen.getValue() == 1))
                    .findFirst().orElse(null);

        if (oxygenOneCard == null){
            Oxygen oxygenTwoCard = this.oxygens.stream()
            .filter(oxygen -> (oxygen.getValue() == 2))
            .findFirst().orElse(null);

            Oxygen[] oxygenCards = this.game.splitOxygen(oxygenTwoCard);
            this.oxygens.remove(oxygenTwoCard);
            this.oxygens.add(oxygenCards[1]);
        } else{
            this.oxygens.remove(oxygenOneCard);
            this.game.getGameDiscard().add(oxygenOneCard);
        }

        if (this.oxygens.size() == 0){
            this.game.killPlayer(this);
        }

        return this.oxygens.stream().collect(Collectors.summingInt(oxygen -> oxygen.getValue()));
    }

    /**
     * Returns the number of spaces between an astronaut and the ship
     * @return The number of spaces between an astronaut and the ship
     */
    public int distanceFromShip(){
        return 6 - this.track.size();
    }

    /**
     * Returns the action cards
     * @return the action cards
     */
    public List<Card> getActions(){
        return this.actions.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Returns the action cards in string format
     * @param enumerated Enumeration applied or not
     * @param excludeShields boolean value to exclude shield cards
     * @return the action cards in string format
     */
    public String getActionsStr(boolean enumerated, boolean excludeShields){
        if (this.isAlive()){
            Map<String, Integer> processedActionCards = this.actions
            .stream()
            .filter(card -> (!excludeShields || !card.toString().equals(GameDeck.SHIELD)))
            .collect(Collectors.groupingBy(Card::toString, Collectors.summingInt(e -> 1)));

            List<String> cardStrings = new ArrayList<>();
            int index = 0;
            for (String cardName: processedActionCards.keySet()){
                if (enumerated){
                    char prefix = (char) ('A' + index);
                    cardStrings.add("["+ prefix + "] " + cardName);
                    index++;
                }else{
                    int count = processedActionCards.get(cardName);
                    if (count > 1) {
                        cardStrings.add(count + "x " + cardName);
                    } else {
                        cardStrings.add(cardName);
                    }
                }
            }
            return cardStrings.stream().sorted().collect(Collectors.joining(", "));
        }
        return "";
    }

    /**
     * Returns all cards the astronaut has
     * @return All cards the astronaut has
     */
    public List<Card> getHand(){
        List<Card> hand = new ArrayList<Card>(this.actions);
        hand.addAll(this.oxygens);
        return hand.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Returns all cards the astronaut has (action cards plus oxygen cards) in string format
     * @return All cards the astronaut has (action cards plus oxygen cards) in string format
     */
    public String getHandStr(){
        Map<String, Integer> processedActionCards = this.actions.stream()
        .collect(Collectors.groupingBy(Card::toString, Collectors.summingInt(e -> 1)));

        Map<String, Integer> processedOxygenCards = this.oxygens.stream()
        .collect(Collectors.groupingBy(Card::toString, Collectors.summingInt(e -> 1)));

        List<String> oxygenStrings = new ArrayList<>();
        List<String> actionStrings = new ArrayList<>();

        for (String cardName: processedOxygenCards.keySet()){
            int count = processedOxygenCards.get(cardName);
            if (count > 1) {
                oxygenStrings.add(count + "x " + cardName);
            } else {
                oxygenStrings.add(cardName);
            }
        }

        Collections.reverse(oxygenStrings);

        for (String cardName: processedActionCards.keySet()){
            int count = processedActionCards.get(cardName);
            if (count > 1) {
                actionStrings.add(count + "x " + cardName);
            } else {
                actionStrings.add(cardName);
            }
        }

        String concatenator = "";
        if (actionStrings.size() > 0){
            concatenator = "; ";
        }

        return (String.join(", ", oxygenStrings) + concatenator
         + actionStrings.stream().sorted().collect(Collectors.joining(", ")));
    }

    /**
     * Returns the list of track cards
     * @return the list of track cards
     */
    public Collection<Card> getTrack(){
        return this.track;
    }

    /**
     * Removes the specified card from an astronaut's hand
     * @param card the specified card
     */
    public void hack(Card card){
        if (card == null){
            throw new IllegalArgumentException();
        }

        ArrayList<Card> allCards = new ArrayList<Card>(this.actions);
        allCards.addAll(this.oxygens);

        Card removingCard = allCards.stream()
            .filter(c -> c.equals(card))
            .findFirst().orElse(null);

        if (removingCard == null){
            throw new IllegalArgumentException();
        }

        if (card instanceof Oxygen){
            this.oxygens.remove(removingCard);
        }else{
            this.actions.remove(removingCard);
        }

        if (this.oxygenRemaining() == 0){
            this.actions.removeAll(this.actions);
            this.game.killPlayer(this);
        }
    }

    /**
     * Removes the specified card from an astronaut's hand
     * @param card the specified card's name
     * @return the specified card from an astronaut's hand
     */
    public Card hack(String card){
        if (card == null){
            throw new IllegalArgumentException();
        }

        ArrayList<Card> allCards = new ArrayList<Card>(this.actions);
        allCards.addAll(this.oxygens);

        Card removingCard = allCards.stream()
            .filter(c -> c.toString().equals(card))
            .findFirst().orElse(null);

        if (removingCard == null){
            throw new IllegalArgumentException();
        }

        if (removingCard instanceof Oxygen){
            this.oxygens.remove(removingCard);
        }else{
            this.actions.remove(removingCard);
        }

        if (this.oxygenRemaining() == 0){
            this.actions.removeAll(this.actions);
            this.game.killPlayer(this);
        }

        return removingCard;
    }

    /**
     * Returns the number of instances of a particular card in an astronaut's hand
     * @param card Specified card's name
     * @return the number of instances of a particular card in an astronaut's hand
     */
    public int hasCard(String card){
        ArrayList<Card> allCards = new ArrayList<Card>(this.actions);
        allCards.addAll(this.oxygens);
        return allCards.stream().filter(c -> c.toString().equals(card)).toArray().length;
    }

    /**
     * Return a boolean value if the astronaut has Solar Flare card
     * @return a boolean value if the astronaut has Solar Flare card
     */
    public boolean hasMeltedEyeballs(){
        return this.track.stream()
        .anyMatch(card -> card.toString().equals(SpaceDeck.SOLAR_FLARE));
    }

    /**
     * Returns true if the astronaut reaches the ship and is still alive
     * and reached to the ship
     * @return Boolean value
     */
    public boolean hasWon(){
        if (this.distanceFromShip() == 0 && this.oxygenRemaining() >= 1){
            return true;
        }

        return false;
    }

    /**
     * Return false if the Astronaut ran out of Oxygen
     * @return Boolean value
     */
    public boolean isAlive(){
        if (this.oxygenRemaining() > 0){
            return true;
        }
        return false;
    }

    /**
     * Removes and returns the topmost card from the astronaut's track
     * @return The topmost card from an astronaut's track
     */
    public Card laserBlast(){
        if (this.track.size() == 0){
            throw new IllegalArgumentException();
        }

        LinkedList<Card> linkedList = new LinkedList<Card>(this.track);
        Card card = linkedList.removeLast();

        this.track.remove(card);
        return card;
    }

    /**
     * Returns the total value of all Oxygen cards the astronaut has
     * @return the total value of all Oxygen cards the astronaut has
     */
    public int oxygenRemaining(){
        // Get total number of oxygen the astronaut has
        int totalOxygenValue = 0;
        for (Oxygen oxygenCard: this.oxygens){
            totalOxygenValue += oxygenCard.getValue();
        }

        // Return total number of oxygen the astronaut has
        return totalOxygenValue;
    }

    /**
     * Return null if the astronaut is still in the starting space 
     * else the topmost card from the track of Astronaut
     * @return The topmost card from the track of Astronaut
     */
    public Card peekAtTrack(){
        if (this.distanceFromShip() < 0 || this.distanceFromShip() == 6){
            return null;
        }
        return new LinkedList<Card>(this.track).getLast();
    }

    /**
     * Removes and return an Oxygen(1) card from the astronaut's hand
     * @return an Oxygen(1) card from the astronaut's hand
     */
    public Oxygen siphon(){
        Oxygen oxygenCard = this.oxygens.stream().findFirst().orElse(null);

        if (oxygenCard == null){
            throw new IllegalStateException();
        }

        if (oxygenCard.getValue() == 1){
            this.oxygens.remove(oxygenCard);
            if (this.oxygenRemaining() == 0){
                this.game.killPlayer(this);
            }
            return oxygenCard;
        }

        Oxygen[] oxygenCards = this.game.splitOxygen(oxygenCard);
        this.oxygens.remove(oxygenCard);
        this.oxygens.add(oxygenCards[1]);
        return oxygenCards[1];
    }

    /**
     * Removes and returns a random Card from an astronaut's hand
     * @return a random Card from an astronaut's hand
     */
    public Card steal(){
        ArrayList<Card> allCards = new ArrayList<Card>(this.actions);
        allCards.addAll(this.oxygens);

        Card stolenCard = allCards.stream().findAny().orElse(null);

        if (stolenCard instanceof Oxygen){
            this.oxygens.remove(stolenCard);
            if (this.oxygenRemaining() == 0){
                this.game.killPlayer(this);
            }
            return stolenCard;
        }

        this.actions.remove(stolenCard);
        return stolenCard;
    }

    /**
     * Swaps an astronaut's track with the track of the specified Astronaut.
     * @param swapee other astronaut
     */
    public void swapTrack(Astronaut swapee){
        Collection<Card> swapeeTrack = swapee.getTrack();
        swapee.track = this.track;
        this.track = swapeeTrack;
    }

    /**
     * Expression of Astronaut
     * @return Expression of Astronaut
     */
    public String toString() {
        if (this.isAlive()){
            return this.name;
        }
        return this.name + " (is dead)";
    }

}
