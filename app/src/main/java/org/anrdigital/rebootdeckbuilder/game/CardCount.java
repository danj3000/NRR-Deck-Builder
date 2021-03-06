package org.anrdigital.rebootdeckbuilder.game;

public class CardCount {
    private Card mCard;
    private int mCount;
    private boolean mDone;

    public CardCount(Card card, int count) {
        mCard = card;
        mCount = count;
        mDone = false;
    }

    public CardCount(Card card, int count, boolean done) {
        this(card, count);
        mDone = done;
    }

    public Card getCard() {
        return mCard;
    }

    public void setCard(Card mCard) {
        this.mCard = mCard;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }

    public void add(int quantity) {
        this.mCount += quantity;
    }
}
