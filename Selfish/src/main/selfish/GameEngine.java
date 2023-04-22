package selfish;

import java.util.Random;

import selfish.deck.GameDeck;
import selfish.deck.SpaceDeck;
import selfish.deck.Deck;
import selfish.deck.Oxygen;
import selfish.deck.Card;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.io.Serializable;


public class GameEngine implements Serializable{

    private Collection<Astronaut> activePlayers;

    private List<Astronaut> corpses;

    private Astronaut currentPlayer;

    private boolean hasStarted;

    private Random random;

    private GameDeck gameDeck;

    private GameDeck gameDiscard;

    private SpaceDeck spaceDeck;

    private SpaceDeck spaceDiscard;

    private static final long serialVersionUID = 5016812401135889608L;

    private GameEngine(){
    }

    public GameEngine(long seed, String gameDeck, String spaceDeck) throws GameException{
        this.random = new Random(seed);

        this.gameDeck = new GameDeck(gameDeck);
        this.gameDiscard = new GameDeck();
        this.gameDeck.shuffle(random);

        this.spaceDeck = new SpaceDeck(spaceDeck);
        this.spaceDiscard = new SpaceDeck();
        this.spaceDeck.shuffle(random);

        this.activePlayers = new ArrayList<Astronaut>();
        this.corpses = new ArrayList<Astronaut>();
        this.currentPlayer = null;
        this.hasStarted = false;
    }

    public int addPlayer(String player){return 0;}

    public int endTurn(){return 0;}

    public boolean gameOver(){return false;}

    public List<Astronaut> getAllPlayers(){
        List<Astronaut> allPlayers = new ArrayList<Astronaut>(this.activePlayers);
        allPlayers.addAll(this.corpses);
        if (currentPlayer != null){
            allPlayers.add(currentPlayer);
        }

        return allPlayers;
    }

    public Astronaut getCurrentPlayer(){
        return currentPlayer;
    }

    public int getFullPlayerCount(){
        return 0;
    }

    public GameDeck getGameDeck(){
        return this.gameDeck;
    }

    public GameDeck getGameDiscard(){
        return this.gameDiscard;
    }

    public SpaceDeck getSpaceDeck(){
        return this.spaceDeck;
    }

    public SpaceDeck getSpaceDiscard(){
        return this.spaceDiscard;
    }

    public Astronaut getWinner(){
        return null;
    }

    public void killPlayer(Astronaut corpse){
    }

    public static GameEngine loadState(String path){
        return null;
    }

    public void mergeDecks(Deck deck1, Deck deck2){}

    public void saveState(String path){}

    public Oxygen[] splitOxygen(Oxygen dbl){
        return null;
    }

    public void startGame(){}

    public void startTurn(){}

    public Card travel(Astronaut traveller){return null;}
}
