package org.anrdigital.rebootdeckbuilder.helper;

import org.anrdigital.rebootdeckbuilder.game.Card;
import org.anrdigital.rebootdeckbuilder.game.CardBuilder;
import org.anrdigital.rebootdeckbuilder.game.Deck;
import org.anrdigital.rebootdeckbuilder.game.Format;
import org.anrdigital.rebootdeckbuilder.game.FormatBuilder;
import org.anrdigital.rebootdeckbuilder.game.MostWantedList;
import org.anrdigital.rebootdeckbuilder.game.Pack;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DeckValidatorTest {
    private ArrayList<Pack> packs;
    private String legalSetCode = "sc19";

    @Before
    public void Setup(){
        packs = new ArrayList<>();

        Pack p = new Pack();
        p.setCode(legalSetCode);
        packs.add(p);
    }

    @Test
    public void NoMWLNoRotation_OnlylegalCards_PassesValidation(){
        MostWantedList mwl = new MostWantedList();
        DeckValidator validator = new DeckValidator(mwl);
        Card idCard = new CardBuilder("").withCode("idCard").withSetCode(legalSetCode).Build();
        Format format = new FormatBuilder().asCoreExperience().Build();
        Deck deck = new Deck(idCard, format);
        Card legalCard = new CardBuilder("").withSetCode(legalSetCode).Build();
        assertTrue(format.getPacks().contains(legalSetCode));
        deck.updateCardCount(legalCard, 2);
        assertEquals(1, deck.getCards().size());

        boolean valid = validator.validate(deck, packs);
        assertTrue(valid);
    }

    @Test
    public void NoMWLNoRotation_IllegalCards_FailValidation(){
        MostWantedList mwl = new MostWantedList();
        DeckValidator validator = new DeckValidator(mwl);
        Card idCard = new CardBuilder("").withCode("idCard").Build();
        Format format = new FormatBuilder().asCoreExperience().Build();
        Deck deck = new Deck(idCard, format);
        String illegalSetCode = "illegal-notSC19";
        Card illegalCard = new CardBuilder("").withSetCode(illegalSetCode).Build();
        assertFalse(format.getPacks().contains(illegalSetCode));
        deck.updateCardCount(illegalCard,2);
        assertEquals(1, deck.getCards().size());

        boolean valid = validator.validate(deck, packs);
        assertFalse(valid);

    }

}