package com.jovi.map.file;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import com.jovi.map.Island;

public final class ReadInitialMap {
    static final ClassLoader loader = 
        ReadInitialMap.class.getClassLoader();
    Map<String, Island> islandsMap = new HashMap<String, Island>();
    List<Island> islands = new ArrayList<Island>();

    public List<Island> read(String map) throws IOException {
        InputStream inputStream = loader.getResourceAsStream("maps/" + map);
        InputStreamReader inputStreamReader = 
            new InputStreamReader(inputStream, "UTF-8");

        try (BufferedReader bufferedReader = 
                new BufferedReader(inputStreamReader)) {

            String row;
            while ((row = bufferedReader.readLine()) != null) {
                addIslandsToList(row);
            }
        }

        return islands;
    }

    private void addIslandsToList(String row) {
        String[] rowArr = row.split("\\s+");
        Island island = getExistOrNewIsland(rowArr[0]);
        islands.add(island);

        for (int i = 1 ; i < rowArr.length; i++) {
            String[] neighbourData = rowArr[i].split("=");
            Island neighbourIsland = getExistOrNewIsland(neighbourData[1]);
            island.addNeighbour(neighbourData[0], neighbourIsland);
        }
    }

    private Island getExistOrNewIsland(String islandName) {
        Island island = islandsMap.get(islandName);

        if (island == null) {
            island = new Island(islandName);
            islandsMap.put(islandName, island);
        }

        return island;
    }
}
