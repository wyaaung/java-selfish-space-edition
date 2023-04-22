package selfish.deck;

import java.io.Serializable;

/** Card Class (Parent of Oxygen)
 *  @author Wai Yan Aung
 *  @version 1
 */
public class Card implements Comparable<Card>, Serializable{

    private String name;
    private String description;

    private static final long serialVersionUID = 5016812401135889608L;

    /**
     * Card Constructor
     * @param name Card's name
     * @param description Card's description
     */
    public Card (String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Return card's description
     * @return card's description
     */
    public String getDescription() {
        return this.description;
    }

    /** 
     * Return card's name
     * @return card's name
     */
    public String toString() {
        return this.name;
    }

    /**
     * To compare two card's names lexicographically
     * @param card Card
     * @return 0 if two card are equal
     */
    public int compareTo(Card card){
        return this.toString().compareTo(card.toString());
    }
}
