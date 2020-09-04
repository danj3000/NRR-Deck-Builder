package org.anrdigital.rebootdeckbuilder.export;

import android.content.Context;

import org.anrdigital.rebootdeckbuilder.R;
import org.anrdigital.rebootdeckbuilder.game.Card;
import org.anrdigital.rebootdeckbuilder.game.Deck;
import org.anrdigital.rebootdeckbuilder.helper.Sorter;

import java.util.Collections;
import java.util.List;

public class PlainText implements DeckFormatter {

    private final Context context;

    public PlainText(Context context) {
        this.context = context;
    }

    @Override
    public String fromDeck(Deck deck) {
        List<Card> cards = deck.getCards();
        Collections.sort(cards, new Sorter.CardSorterByCardType());

        StringBuilder sb = new StringBuilder();
        // Title
        sb.append(String.format("%s (%s %s)\n", deck.getName(), deck.getDeckSize(), context.getString(R.string.cards)));
        // Identity
        sb.append(String.format("%s\n", deck.getIdentity().getTitle()));
        // Cards
        String lastType = "";
        for (Card card : cards) {
            if (!card.getTypeCode().equals(lastType)) {
                lastType = card.getTypeCode();
                sb.append(String.format("-- %s (%s %s)\n", lastType, deck.getCardCountByType(card.getTypeCode()), context.getString(R.string.cards)));
            }
            sb.append(String.format("%s %s\n", deck.getCardCount(card), card.getTitle()));
        }
        return sb.toString();
    }
}
