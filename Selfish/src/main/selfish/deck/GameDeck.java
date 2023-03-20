package selfish.deck;

public class GameDeck extends Deck{
    public static final String HACK_SUIT = "Hack suit";
    public static final String HOLE_IN_SUIT = "Hole in suit";
    public static final String LASER_BLAST = "Laser blast";
    public static final String OXYGEN = "Oxygen";
    public static final String OXYGEN_1 = "Oxygen(1)";
    public static final String OXYGEN_2 = "Oxygen(2)";
    public static final String OXYGEN_SIPHON = "Oxygen siphon";
    public static final String ROCKET_BOOSTER = "Rocket booster";
    public static final String SHIELD = "Shield";
    public static final String TETHER = "Tether";
    public static final String TRACTOR_BEAM = "Tractor beam";

    private static final long serialVersionUID = 5016812401135889608L;

    public GameDeck(){}

    public GameDeck(String path){}

    public Oxygen drawOxygen(int value){
        return new Oxygen(5);
    }

    public Oxygen[] splitOxygen(Oxygen unit){
        return null;
    }

}