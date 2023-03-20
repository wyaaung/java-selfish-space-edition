package selfish;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import selfish.deck.Card;
import selfish.deck.Oxygen;

public class Astronaut implements Serializable{
    private GameEngine game;

    private List<Card> actions;
    
    private List<Oxygen> oxygens;

    private String name;

    private Collection<Card> track;

    private static final long serialVersionUID = 5016812401135889608L;

    public Astronaut(String name, GameEngine game) {
        this.game = game;
        this.name = name;
    }

    public void addToHand(Card card){}

    public void addToTrack(Card card){}

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
        return "";
    }

    public List<Card> getHand(){
        return actions;
    }

    public String getHandStr(){
        return "";
    }

    public Collection<Card> getTrack(){
        return track;
    }

    public void hack(Card card){}

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
        return true;
    }

    public Card laserBlast(){
        return null;
    }

    public int oxygenRemaining(){
        return oxygens.size();
    }

    public Card peekAtTrack(){
        return null;
    }

    public Oxygen siphon(){
        return new Oxygen(7);
    }

    public Card steal(){
        return new Card("SAMPLE", "SAMPLE");
    }

    public void swapTrack(Astronaut swapee){}

    public String toString() {
        return "";
    }

}
