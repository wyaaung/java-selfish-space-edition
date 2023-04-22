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

/** GameEngine Class
 *  @author Wai Yan Aung
 *  @version 1
 */
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

    /**
     * GameEngine Constructor
     */
    private GameEngine(){
    }

    /**
     * GameEngine Constructor
     * @param seed The number to initiate Random object
     * @param gameDeck Text filepath for action cards
     * @param spaceDeck Text filepath for space cards
     * @throws GameException Type of Exception
     */
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

    /**
     * Create new Astronaut instances and returns the total number of players
     * @param player Player's name
     * @return The total number of players
     */
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

    /**
     * To update current player and active players
     * To return the number of players alive when the turn has ended
     * @return The number of players alive when the turn has ended
     */
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

    /**
     * Returns true if all the players are dead, or if someone has won.
     * @return true if all the players are dead, or if someone has won
     */
    public boolean gameOver(){
        if (this.corpses.size() == this.getFullPlayerCount()){
            return true;
        }

        if (this.getWinner() != null){
            return true;
        }

        return false;
    }

    /**
     * Returns all the players in the game including dead ones
     * @return all the players in the game including dead ones
     */
    public List<Astronaut> getAllPlayers(){
        List<Astronaut> allPlayers = new ArrayList<Astronaut>(this.activePlayers);
        allPlayers.addAll(this.corpses);
        if (currentPlayer != null){
            allPlayers.add(currentPlayer);
        }

        return allPlayers;
    }

    /**
     * Return the currentPlayer instance
     * @return the currentPlayer instance
     */
    public Astronaut getCurrentPlayer(){
        return this.currentPlayer;
    }

    /**
     * Returns number of all the players in the game including dead ones
     * @return number of all the players in the game including dead ones
     */
    public int getFullPlayerCount(){
        return this.getAllPlayers().size();
    }

    /**
     * Returns the gameDeck instance
     * @return the gameDeck instance
     */
    public GameDeck getGameDeck(){
        return this.gameDeck;
    }

    /**
     * Returns the gameDiscard instance
     * @return the gameDiscard instance
     */
    public GameDeck getGameDiscard(){
        return this.gameDiscard;
    }

    /**
     * Returns the spaceDeck instance
     * @return the spaceDeck instance
     */
    public SpaceDeck getSpaceDeck(){
        return this.spaceDeck;
    }

    /**
     * Returns the spaceDiscard instance
     * @return the spaceDiscard instance
     */
    public SpaceDeck getSpaceDiscard(){
        return this.spaceDiscard;
    }

    /**
     * Returns the winning player else null
     * @return the winning player else null
     */
    public Astronaut getWinner(){
        return this.activePlayers.stream()
                .filter(player -> player.hasWon()).findFirst().orElse(null);
    }

    /**
     * Helper function to update active players if someone ran out of Oxygen
     * @param corpse the specified player who ran out of oxygen
     */
    public void killPlayer(Astronaut corpse){
        this.corpses.add(corpse);
        this.activePlayers.remove(corpse);
        if (corpse.equals(this.getCurrentPlayer())){
            this.currentPlayer = null;
        }
    }

    /**
     * Reload the saved game
     * @param path The saved game filepath
     * @return The reloaded GameEngine
     * @throws GameException Type of Exception
     */
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

    /**
     * Helper function to shuffle the discard pile back into the main deck
     * @param deck1 the main deck
     * @param deck2 the discard deck
     */
    public void mergeDecks(Deck deck1, Deck deck2){
        int secondDeckSize = deck2.size();
        while (secondDeckSize > 0){
            deck1.add(deck2.draw());
            secondDeckSize--;
        }
        deck1.shuffle(this.random);
    }

    /**
     * Save the game
     * @param path The filepath to save
     * @throws GameException Type of Exception
     */
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

    /**
     * Helper function to exchange Oxygen(2) card with two Oxygen(1)
     * @param dbl Oxygen(2) card
     * @return list of two Oxygen(1)
     */
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

    /**
     * To deal out cards to each player
     * 1. Deal out Oxygen(2) cards
     * 2. Deal out Oxygen(1) cards
     * 3. Deal out action cards
     */
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

    /**
     * To update current player and active players
     */
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

    /**
     * reduce the specified player's Oxygen by 2,
     * draw a card
     * add the card to the specified player's track 
     * @param traveller A specified player
     * @return the drawn card from SpaceDeck
     */
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
