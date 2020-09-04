package org.anrdigital.rebootdeckbuilder.export;

import org.anrdigital.rebootdeckbuilder.game.Deck;

public interface DeckFormatter {
    String fromDeck(Deck deck);
}
