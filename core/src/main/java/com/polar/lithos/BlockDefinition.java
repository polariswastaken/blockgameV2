package com.polar.lithos;

// Holds static information about what a block is.
public class BlockDefinition {
    int id;
    int maxHealth;
    boolean isSolid;

    public BlockDefinition(int id, int maxHealth, boolean isSolid) {
        this.id = id;
        this.maxHealth = maxHealth;
        this.isSolid = isSolid;
    }


}
