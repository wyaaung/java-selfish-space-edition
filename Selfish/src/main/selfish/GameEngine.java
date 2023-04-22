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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public int addPlayer(String player){
        if (this.getFullPlayerCount() == 5){
            throw new IllegalStateException();
        }

        if (this.hasStarted){
            throw new IllegalStateException();
        }
        Astronaut newPlayer = new Astronaut(player, this);
        this.activePlayers.add(newPlayer);
        return this.activePlayers.size();
    }

    public int endTurn(){
        if (!this.hasStarted){
            throw new IllegalStateException();
        }

        if (this.currentPlayer != null){
            if (this.currentPlayer.oxygenRemaining() == 0){
                this.killPlayer(currentPlayer);
            }else {
                this.activePlayers.add(currentPlayer);
            }
            
            this.currentPlayer = null;
        }

        return 0;
    }

    public boolean gameOver(){
        if (this.corpses.size() == this.getFullPlayerCount()){
            return true;
        }

        if (this.getWinner() != null){
            return true;
        }

        return false;
    }

    public List<Astronaut> getAllPlayers(){
        List<Astronaut> allPlayers = new ArrayList<Astronaut>(this.activePlayers);
        allPlayers.addAll(this.corpses);
        if (currentPlayer != null){
            allPlayers.add(currentPlayer);
        }

        return allPlayers;
    }

    public Astronaut getCurrentPlayer(){
        return this.currentPlayer;
    }

    public int getFullPlayerCount(){
        return this.getAllPlayers().size();
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
        return this.activePlayers.stream()
                .filter(player -> player.hasWon()).findFirst().orElse(null);
    }

    public void killPlayer(Astronaut corpse){
        this.corpses.add(corpse);
        this.activePlayers.remove(corpse);
        if (corpse.equals(this.getCurrentPlayer())){
            this.currentPlayer = null;
        }
    }

    public static GameEngine loadState(String path) throws GameException{
        GameEngine data = null;

        try (FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn)) {

            data = (GameEngine) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new GameException("", e);
        }

        return data;
    }

    public void mergeDecks(Deck deck1, Deck deck2){
        int secondDeckSize = deck2.size();
        while (secondDeckSize > 0){
            deck1.add(deck2.draw());
            secondDeckSize--;
        }
        deck1.shuffle(this.random);
    }

    public void saveState(String path) throws GameException{
        try {
            FileOutputStream fileOutput = new FileOutputStream(path);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(this);
            objectOutput.close();
            fileOutput.close();
        } catch (IOException e) {
            throw new GameException("", e);
        }
    }

    public Oxygen[] splitOxygen(Oxygen dbl){
        Oxygen deckOxygenOne = null;
        Oxygen discardOxygenOne = null;

        try {
            deckOxygenOne = this.gameDeck.drawOxygen(1);
        } catch (IllegalStateException e) {
            Oxygen[] oxygenCards = this.gameDiscard.splitOxygen(dbl);
            return oxygenCards;
        }
        
        try {
            discardOxygenOne = this.gameDiscard.drawOxygen(1);
        } catch (IllegalStateException e) {
            if (deckOxygenOne != null){
                discardOxygenOne = this.gameDeck.drawOxygen(1);
                this.gameDeck.add(dbl);

                return new Oxygen[]{deckOxygenOne, discardOxygenOne};
            }

            Oxygen[] oxygenCards = this.gameDeck.splitOxygen(dbl);
            return oxygenCards;
        }

        this.gameDeck.add(dbl);
        return new Oxygen[]{deckOxygenOne, discardOxygenOne};
    }

    public void startGame(){
        int allPlayersSize = this.getAllPlayers().size();
        if (allPlayersSize > 5 || allPlayersSize == 1){
            throw new IllegalStateException();
        }

        if (this.hasStarted){
            throw new IllegalStateException();
        }

        for (Astronaut player: this.activePlayers){
            Oxygen oxygenTwoCard = this.gameDeck.drawOxygen(2);
            player.addToHand(oxygenTwoCard);
            for (int i = 0; i < 4; i++){
                Oxygen oxygenOneCard = this.gameDeck.drawOxygen(1);
                player.addToHand(oxygenOneCard);
            }
        }

        for (int i = 0; i < 4; i++){
            for (Astronaut player: this.activePlayers){
                Card card = this.gameDeck.draw();
                player.addToHand(card);
            }
        }

        this.hasStarted = true;
    }

    public void startTurn(){
        if (!this.hasStarted){
            throw new IllegalStateException("HAS NOT STARTED");
        }

        if (this.gameOver()){
            throw new IllegalStateException("GAME OVER");
        }

        if (this.currentPlayer != null){
            throw new IllegalStateException("ADSADASDS");
        }

        currentPlayer = activePlayers.iterator().next();
        currentPlayer.addToHand(gameDeck.draw());
        activePlayers.remove(currentPlayer);
    }

    public Card travel(Astronaut traveller){
        traveller.siphon();
        traveller.siphon();

        Card card = spaceDeck.draw();
        if (!card.toString().equals(SpaceDeck.GRAVITATIONAL_ANOMALY)){
            traveller.addToTrack(card);
            if (traveller.hasWon()){
                activePlayers.remove(traveller);
            }
        }

        return card;
    }
}
