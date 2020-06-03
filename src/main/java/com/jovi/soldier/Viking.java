package com.jovi.soldier;

import com.jovi.map.Island;

import java.util.Objects;

public class Viking implements Soldier
{
    private String name;
    private Island island;
    private boolean dead = false;
    private boolean stopped = false;
    
    public Viking(String name) {
        this.name = name;
    }

    @Override
    public void attack() {
        Island nextIsland = island.getRandomNeighbour();
        if (nextIsland != null) {
            nextIsland.tryAddViking(this);
        } else {
            setStopped(true);
            StringBuilder stopMsg = new StringBuilder();
            stopMsg.append("АГР!!! ")
                    .append(getName())
                    .append(" застрял на ")
                    .append(island.getName())
                    .append(" и больше не участвует в войне");
            System.out.println(stopMsg);
        }
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public String getName() {
        return name;
    }
    public Island getIsland() {
        return island;
    }

    public void setIsland(Island island) {
        this.island = island;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Viking viking = (Viking) o;
        return dead == viking.dead &&
                stopped == viking.stopped &&
                name.equals(viking.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dead, stopped);
    }
}
