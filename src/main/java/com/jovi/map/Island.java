package com.jovi.map;

import java.util.*;
import java.lang.StringBuilder;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.ThreadLocalRandom;
import com.jovi.soldier.Viking;

public class Island {
    private Lock lock = new ReentrantLock();
    private List<Map.Entry<String, Island>> neighbours=
            new ArrayList<Map.Entry<String, Island>>();
    private List<Viking> vikings =
        new ArrayList<Viking>(3);
    private String name;
    private volatile boolean destroyed = false;

    public Island(String name) {
        setName(name);
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addNeighbour(String direction, Island island) {
        neighbours.add(
                new AbstractMap.SimpleEntry<String, Island>(direction, island)
        );
    }

    @Override 
    public String toString() {
        return getName();
    }

    public String getIslandInfo() {
        return this.name + " " + getNeighbours();
    }

    private String getNeighbours() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Island> entry : neighbours) {
            stringBuilder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue().getName())
                    .append(" ");
        }
        return stringBuilder.toString();
    }

    public Island getRandomNeighbour() {
        removeDestroedIslands();
        if (neighbours.size() == 0) {
            return null;
        }
        int numberNextIsland =
                ThreadLocalRandom.current().nextInt(0, neighbours.size());
        Map.Entry<String, Island> entry = neighbours.get(numberNextIsland);
        return entry.getValue();
    }

    private void removeDestroedIslands() {
        for (int i = 0; i < neighbours.size() ; i++) {
            Map.Entry<String, Island> entry = neighbours.get(i);
            if (entry.getValue().isDestroyed()) {
                neighbours.remove(i);
            }
        }
    }

    public void populateViking(Viking viking) {
        viking.setIsland(this);

        vikings.add(viking);
        if (vikings.size() > 1) {
            destroyIsland();
        }
    }

    public void tryAddViking(Viking viking) {
        while (true) {

            if (!lock.tryLock()) {
                continue;
            }

            Island island = viking.getIsland();
            if (island != this) {
                island.tryAddViking(viking, this);
            }
            
            lock.unlock();
            break;
        }
    }

    private void tryAddViking(Viking viking, Island newIsland) {
        for (int i = 0; i < 20; i++) {
            if (!lock.tryLock()) {
                continue;
            }
            if (newIsland.isDestroyed() || 
                    viking.isDead()) {
                lock.unlock();
                break;
            }
            System.out.println(viking.getName() + ": Приплыл на остров " + newIsland);
            vikings.remove(viking);
            newIsland.populateViking(viking);
            lock.unlock();
            break;
        }
    }

    private void destroyIsland() {
        StringBuilder deadMsg = new StringBuilder("АГР!!! На ");
        deadMsg.append(getName())
                .append(" уничтожен маяк, благодаря ");
        destroyed = true;
        for (Viking viking : vikings) {
            deadMsg.append(viking.getName());
            deadMsg.append(" и ");
            viking.setDead(true);
        }
        deadMsg.setLength(deadMsg.length() - 3);
        System.out.println(deadMsg);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Island island = (Island) o;
        return destroyed == island.destroyed &&
                name.equals(island.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, destroyed);
    }
}
