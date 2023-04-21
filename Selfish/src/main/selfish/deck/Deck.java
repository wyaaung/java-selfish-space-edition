package selfish.deck;

import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

public abstract class Deck implements Serializable{

    private Collection<Card> cards = new ArrayList<Card>();

    private static final long serialVersionUID = 5016812401135889608L;

    protected Deck(){
    }

    protected static List<Card> loadCards(String path) throws IOException{
        BufferedReader fileStream = new BufferedReader(new FileReader(path));

        List<Card> returningCards = new ArrayList<Card>();

        String temporaryString = fileStream.readLine();
        while (temporaryString != null) {
            if (!temporaryString.equals("NAME; DESCRIPTION; QUANTITY")){
                Card[] cards = stringToCards(temporaryString);

                Collections.addAll(returningCards, cards);
            }
            temporaryString = fileStream.readLine();
        }

        fileStream.close();

        return returningCards;
    }

    protected static Card[] stringToCards(String str){
        String[] splitted = str.split("; ");

        String name = splitted[0];
        String desciprtion = splitted[1];
        int numberOfCards = Integer.valueOf(splitted[2]);

        Card[] newCards = new Card[numberOfCards];

        for (int i = 0; i < numberOfCards; i++){
            newCards[i] = new Card(name, desciprtion);
        }

        return newCards;
    }

    public int add(Card card){
        this.cards.add(card);
        return this.cards.size();
    }

    protected int add(List<Card> cards){
        this.cards.addAll(cards);
        return this.cards.size();
    }

    public Card draw(){
        if (this.cards.isEmpty()){
            throw new IllegalStateException();
        }

        LinkedList<Card> linkedList = new LinkedList<Card>(cards);
        Card card = linkedList.removeLast();

        this.cards.remove(card);

        return card;
    }

    public void remove(Card card){
        this.cards.remove(card);
    }

    public void shuffle(Random random){
        Collections.shuffle((ArrayList<Card>) this.cards, random);
    }

    public int size(){
        return this.cards.size();
    }
}
