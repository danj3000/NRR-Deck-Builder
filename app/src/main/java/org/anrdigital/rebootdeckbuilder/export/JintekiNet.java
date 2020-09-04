package org.anrdigital.rebootdeckbuilder.export;

import org.anrdigital.rebootdeckbuilder.game.Card;
import org.anrdigital.rebootdeckbuilder.game.Deck;

public class JintekiNet implements DeckFormatter {

    @Override
    public String fromDeck(Deck deck) {
        StringBuilder sb = new StringBuilder();
        for (Card card : deck.getCards()) {
            sb.append(String.format("%s %s\n", deck.getCardCount(card), card.getTitle()));
        }

        return sb.toString();
    }
}
