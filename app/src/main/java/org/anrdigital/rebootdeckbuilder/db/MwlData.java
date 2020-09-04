package org.anrdigital.rebootdeckbuilder.db;

import org.anrdigital.rebootdeckbuilder.game.MostWantedList;

import java.util.ArrayList;

public class MwlData {
    public ArrayList<MostWantedList> allLists = new ArrayList<>();

    public ArrayList<MostWantedList> getMWLs() {
        return this.allLists;
    }
}
