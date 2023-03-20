package selfish.deck;

import java.io.Serializable;

public class Card implements Comparable<Card>, Serializable{
    private String name;

    private String description;

    private static final long serialVersionUID = 5016812401135889608L;

    public Card (String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return "";
    }

    public int compareTo(Card card){
        return 0;
    }
}
