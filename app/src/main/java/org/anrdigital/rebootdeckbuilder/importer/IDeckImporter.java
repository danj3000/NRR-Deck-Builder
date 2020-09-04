package org.anrdigital.rebootdeckbuilder.importer;

import org.anrdigital.rebootdeckbuilder.game.Deck;

import java.util.ArrayList;

public interface IDeckImporter {
    ArrayList<Deck> toDecks();
}