package com.polar.lithos;

// Tile entity. Exists only for blocks currently taking damage.
public class DamagedBlock {
    public int currentHealth;

    public DamagedBlock(int startingHealth) {
        this.currentHealth = startingHealth;
    }
}
