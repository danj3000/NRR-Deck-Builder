package org.anrdigital.rebootdeckbuilder.importer;

import org.anrdigital.rebootdeckbuilder.db.CardRepository;
import org.anrdigital.rebootdeckbuilder.game.Deck;

import java.util.ArrayList;

/**
 * Created by sebast on 22/06/16.
 */

public class UniversalImporter implements IDeckImporter {

    private ArrayList<Deck> mDecks;

    public UniversalImporter(String text, CardRepository repo, String fileName) throws DeckFormatNotSupportedException {
        try {
            mDecks = (new JsonImporter(text)).toDecks();
        } catch (Exception ignored) {
            try {
                mDecks = (new XmlImporter(text, repo, fileName)).toDecks();
            } catch (Exception ignored2) {
            }
        }
    }

    @Override
    public ArrayList<Deck> toDecks() {
        return mDecks;
    }


}
