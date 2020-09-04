package org.anrdigital.rebootdeckbuilder.interfaces;

import org.anrdigital.rebootdeckbuilder.game.Format;

public interface OnDeckChangedListener {
    void onDeckCardsChanged();

    void onFormatChanged(Format format);
}
