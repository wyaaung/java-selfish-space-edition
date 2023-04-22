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

public class Astronaut implements Serializable{
    private GameEngine game;

    private List<Card> actions;
    private List<Oxygen> oxygens;
    private Collection<Card> track;

    private String name;

    private static final long serialVersionUID = 5016812401135889608L;

    public Astronaut(String name, GameEngine game) {
        this.game = game;
        this.name = name;

        this.actions = new ArrayList<Card>();
        this.oxygens = new ArrayList<Oxygen>();
        this.track = new ArrayList<Card>();
    }

    public void addToHand(Card card){
        if (card instanceof Oxygen){
            this.oxygens.add((Oxygen) card);
        }else{
            this.actions.add(card);
        }
    }

    public void addToTrack(Card card){
        this.track.add(card);
    }

    public int breathe(){
        if (this.oxygens.size() == 0){
            return 0;
        }

        this.oxygens.remove(this.oxygens.stream().filter(oxygen -> (oxygen.getValue() == 1))
                    .findFirst().orElse(null));

        return this.oxygens.stream().collect(Collectors.summingInt(oxygen -> oxygen.getValue()));
    }

    public int distanceFromShip(){
        return 6 - this.track.size();
    }

    public List<Card> getActions(){
        return this.actions.stream().sorted().collect(Collectors.toList());
    }

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

    public List<Card> getHand(){
        List<Card> hand = new ArrayList<Card>(this.actions);
        hand.addAll(this.oxygens);
        return hand.stream().sorted().collect(Collectors.toList());
    }

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

    public Collection<Card> getTrack(){
        return this.track;
    }

    public void hack(Card card){
        if (card == null){
            throw new IllegalArgumentException();
        }

        if (card instanceof Oxygen){
            this.oxygens.remove(card);
        }else{
            this.actions.remove(card);
        }

        if (this.oxygens.size() == 0){
            this.actions.removeAll(this.actions);
        }
    }

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

        if (this.oxygens.size() == 0){
            this.actions.removeAll(this.actions);
        }

        return removingCard;
    }

    public int hasCard(String card){
        ArrayList<Card> allCards = new ArrayList<Card>(this.actions);
        allCards.addAll(this.oxygens);
        return allCards.stream().filter(c -> c.toString().equals(card)).toArray().length;
    }

    public boolean hasMeltedEyeballs(){
        return this.track.stream().anyMatch(card -> card.toString().equals(SpaceDeck.SOLAR_FLARE));
    }

    public boolean hasWon(){
        if (this.distanceFromShip() == 0 && this.oxygenRemaining() >= 1){
            return true;
        }

        return false;
    }

    public boolean isAlive(){
        if (this.oxygenRemaining() > 0){
            return true;
        }
        return false;
    }

    public Card laserBlast(){
        if (this.track.size() == 0){
            throw new IllegalArgumentException();
        }

        LinkedList<Card> linkedList = new LinkedList<Card>(this.track);
        Card card = linkedList.removeLast();

        this.track.remove(card);
        return card;
    }

    public int oxygenRemaining(){
        // Get total number of oxygen the astronaut has
        int totalOxygenValue = 0;
        for (Oxygen oxygenCard: this.oxygens){
            totalOxygenValue += oxygenCard.getValue();
        }

        // Return total number of oxygen the astronaut has
        return totalOxygenValue;
    }

    public Card peekAtTrack(){
        if (this.distanceFromShip() < 0 || this.distanceFromShip() == 6){
            return null;
        }
        return new LinkedList<Card>(this.track).getLast();
    }

    public Oxygen siphon(){
        Oxygen oxygenCard = this.oxygens.stream().findFirst().orElse(null);

        if (oxygenCard.getValue() == 1){
            this.oxygens.remove(oxygenCard);
            return oxygenCard;
        }
        return null;
    }

    public Card steal(){
        ArrayList<Card> allCards = new ArrayList<Card>(this.actions);
        allCards.addAll(this.oxygens);

        Card stolenCard = allCards.stream().findAny().orElse(null);

        if (stolenCard instanceof Oxygen){
            this.oxygens.remove(stolenCard);
            return stolenCard;
        }

        this.actions.remove(stolenCard);
        return stolenCard;
    }

    public void swapTrack(Astronaut swapee){
        Collection<Card> swapeeTrack = swapee.getTrack();
        swapee.track = this.track;
        this.track = swapeeTrack;
    }

    public String toString() {
        if (this.isAlive()){
            return this.name;
        }
        return this.name + " (is dead)";
    }

}
