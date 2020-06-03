package com.jovi.soldier;

import java.util.ArrayList;
import java.util.List;
import java.lang.Integer;
import java.util.Collections;
import java.io.IOException;
import java.io.InputStreamReader; 
import java.io.BufferedReader;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import com.jovi.map.Island;
import com.jovi.EndGameException;

public final class VikingGenerate
{
    public static List<Viking> factory(List<Island> islands) 
            throws IOException,
                   EndGameException {
        int countVikings = getCountVikings(islands.size());
        return populateIslands(islands, countVikings);
    }

    private static int getCountVikings(int maxCount) 
            throws IOException, 
                   EndGameException {

        BufferedReader reader =  
                   new BufferedReader(new InputStreamReader(System.in)); 
        String str;
        int number = 0;
        boolean waitUserInput = true;

        while(waitUserInput) {
            System.out.print("     - ");
            str = reader.readLine().trim();
            if (str.equals("exit")) {
                throw new EndGameException("You didn 't select a map. Game is stopped");
            }

            try {
                number = Integer.parseInt(str);
            } catch( NumberFormatException nfe) {
                System.err.println("You type is bad number");
            }
            waitUserInput = number > maxCount;
        }
        System.out.println();
        return number;
    }

    private static List<Viking> populateIslands(List<Island> islands, int countVikings) {
        List<Integer> islandIds = IntStream.rangeClosed(0, islands.size() - 1)
            .boxed().collect(Collectors.toList());
        Collections.shuffle(islandIds);

        List<Viking> vikings = new ArrayList<Viking>();
        Viking viking;
        Island island;

        for (int i = 0; i < countVikings; i++) {
            island = islands.get(islandIds.get(i));
            viking = new Viking("Viking" + i);
            island.populateViking(viking);
            System.out.println(viking.getName() + ": Cтартовал с " + island);
            vikings.add(viking);
        }

        return vikings;
    }
}
