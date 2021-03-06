package org.anrdigital.rebootdeckbuilder.helper;

import org.anrdigital.rebootdeckbuilder.game.Card;
import org.anrdigital.rebootdeckbuilder.game.CardCount;
import org.anrdigital.rebootdeckbuilder.game.Deck;

import java.util.Comparator;

public final class Sorter {

    public static final class IdentitySorter implements Comparator<Card> {

        @Override
        public int compare(Card lhs, Card rhs) {
            if (lhs.getFactionCode().equals(rhs.getFactionCode())) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            } else {
                return lhs.getFactionCode().compareTo(rhs.getFactionCode());
            }
        }

    }

    public static final class DeckSorter implements Comparator<Deck> {

        @Override
        public int compare(Deck lhs, Deck rhs) {
            // One deck is null
            if (lhs == null && rhs == null) {
                return 0;
            } else if (lhs == null) {
                return -1;
            } else if (rhs == null) {
                return 1;
            }

            // Identity is null
            if (lhs.getIdentity() == null && rhs.getIdentity() == null) {
                return 0;
            } else if (lhs.getIdentity() == null) {
                return -1;
            } else if (rhs.getIdentity() == null) {
                return 1;
            }

            // All is OK
            if (lhs.isStarred() != rhs.isStarred()) {
                return ((Boolean) !lhs.isStarred()).compareTo(!rhs.isStarred());
            } else if (lhs.getFactionCode().equals(rhs.getFactionCode())) {
                return lhs.getName().compareTo(rhs.getName());
            } else {
                return lhs.getFactionCode().compareTo(rhs.getFactionCode());
            }
        }

    }

    public static final class CardSorterByName implements Comparator<Card> {

        @Override
        public int compare(Card lhs, Card rhs) {
            return lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());
        }

    }

    public static final class CardCountSorterByName implements Comparator<CardCount> {

        @Override
        public int compare(CardCount lhs, CardCount rhs) {
            return lhs.getCard().getTitle().toLowerCase().compareTo(rhs.getCard().getTitle().toLowerCase());
        }

    }

    public static final class CardCountSorterByTypeThenName implements Comparator<CardCount> {

        @Override
        public int compare(CardCount lhs, CardCount rhs) {
            if (lhs.getCard().getTypeCode().equals(rhs.getCard().getTypeCode())) {
                return lhs.getCard().getTitle().toLowerCase().compareTo(rhs.getCard().getTitle().toLowerCase());
            } else {
                return lhs.getCard().getTypeCode().compareTo(rhs.getCard().getTypeCode());
            }
        }
    }

    public static final class CardSorterByFaction implements Comparator<Card> {

        @Override
        public int compare(Card lhs, Card rhs) {
            if (lhs.getFactionCode().equals(rhs.getFactionCode())) {
                return lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());
            } else {
                return lhs.getFactionCode().compareTo(rhs.getFactionCode());
            }
        }
    }

    /**
     * @author sebas_000
     * <p/>
     * returns the cards in this order:
     * 1. My Faction (ordered by name)
     * 2. Neutral faction (ordered by name)
     * 3. Other faction (ordered by name)
     */
    public static final class CardSorterByFactionWithMineFirst implements Comparator<Card> {
        private final String factionCode;

        public CardSorterByFactionWithMineFirst(String factionCode) {
            this.factionCode = factionCode;
        }

        @Override
        public int compare(Card lhs, Card rhs) {
            // Faction is my faction
            if (lhs.getFactionCode().equals(factionCode) && rhs.getFactionCode().equals(factionCode)) {
                return lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());
            } else {
                if (lhs.getFactionCode().equals(factionCode)) {
                    return -1;
                } else if (rhs.getFactionCode().equals(factionCode)) {
                    return 1;
                } else {
                    // Faction is neutral
                    if (lhs.getFactionCode().startsWith(Card.Faction.FACTION_NEUTRAL) && rhs.getFactionCode().startsWith(Card.Faction.FACTION_NEUTRAL)) {
                        return lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());
                    } else {
                        if (lhs.getFactionCode().startsWith(Card.Faction.FACTION_NEUTRAL)) {
                            return -1;
                        } else if (rhs.getFactionCode().startsWith(Card.Faction.FACTION_NEUTRAL)) {
                            return 1;
                        } else {
                            // NOT my faction and NOT neutral
                            if (lhs.getFactionCode().equals(rhs.getFactionCode())) {
                                return lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());
                            } else {
                                return lhs.getFactionCode().compareTo(rhs.getFactionCode());
                            }


                        }

                    }

                }

            }

        }
    }

    public static final class CardSorterByCardType implements Comparator<Card> {

        @Override
        public int compare(Card lhs, Card rhs) {
            if (lhs.getTypeCode().equals(rhs.getTypeCode())) {
                return lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());
            } else {
                return lhs.getTypeCode().compareTo(rhs.getTypeCode());
            }
        }
    }

    public static final class CardSorterByCardNumber implements Comparator<Card> {

        @Override
        public int compare(Card lhs, Card rhs) {
            return Integer.valueOf(lhs.getNumber()).compareTo(rhs.getNumber());
        }
    }
}
