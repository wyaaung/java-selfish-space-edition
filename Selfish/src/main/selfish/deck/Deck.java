package selfish.deck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.Random;

public abstract class Deck implements Serializable{
    private Collection<Card> cards;

    private static final long serialVersionUID = 5016812401135889608L;

    protected Deck(){}

    protected static List<Card> loadCards(String path){return new ArrayList<Card>();}

    protected static Card[] stringToCards(String str){
        Card[] cs = {new Card("Sample", "Sample")};
        return cs;
    }

    public int add(Card card){
        return 0;
    }

    protected int add(List<Card> cards){
        return 0;
    }

    public Card draw(){
        return new Card("Sample", "Sample");
    }

    public void remove(Card card){
    }

    public void shuffle(Random random){
    }

    public int size(){
        return 0;
    }
}
