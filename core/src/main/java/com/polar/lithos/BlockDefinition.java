package com.polar.lithos;

public class BlockDefinition {
    int id;
    int health;
    boolean isSolid;

    public BlockDefinition(int id, int maxHealth, boolean isSolid) {
        this.id = id;
        this.health = maxHealth;
        this.isSolid = isSolid;
    }


}
