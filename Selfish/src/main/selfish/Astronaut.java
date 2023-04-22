package selfish;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import selfish.deck.Card;
import selfish.deck.Oxygen;

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
        return 0;
    }

    public int distanceFromShip(){
        return 0;
    }

    public List<Card> getActions(){
        return actions;
    }

    public String getActionsStr(boolean enumerated, boolean excludeShields){
        return null;
    }

    public List<Card> getHand(){
        return null;
    }

    public String getHandStr(){
        return null;
    }

    public Collection<Card> getTrack(){
        return this.track;
    }

    public void hack(Card card){
    }

    public Card hack(String card){
        return null;
    }

    public int hasCard(String card){
        return 0;
    }

    public boolean hasMeltedEyeballs(){
        return false;
    }

    public boolean hasWon(){
        return false;
    }

    public boolean isAlive(){
        if (this.oxygenRemaining() > 0){
            return true;
        }
        return false;
    }

    public Card laserBlast(){
        return null;
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
        return null;
    }

    public Oxygen siphon(){
        return null;
    }

    public Card steal(){
        return null;
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
