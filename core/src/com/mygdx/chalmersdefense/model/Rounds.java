package com.mygdx.chalmersdefense.model;

/**
 * @author Daniel Persson
 * A class for representing round mechanic with current round and winning round
 */
final class Rounds {
    private int currentRound = 0;   // Current round
    private final int winningRound;       // Winning round

    /**
     * Create an instance of Rounds
     *
     * @param winningRound the round where the player wins
     */
    Rounds(int winningRound) {
        this.winningRound = winningRound;
    }

    /**
     * Increments current round
     */
    void incrementToNextRound() {
        currentRound++;
    }

    /**
     * Checks if the current round is the winning round
     *
     * @return true if current and winning round is equal
     */
    boolean gameWon() {
        return currentRound == winningRound;
    }

    /**
     * Returns the greatest number of currentRound or 1.
     *
     * @return the greatest int
     */
    int getCurrentRound() {
        return Math.max(currentRound, 1);
    }

    /**
     * Returns which round is the winning round
     *
     * @return an int with winning round
     */
    int getWinningRound() {
        return winningRound;
    }
}
