package com.jovi.map;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.io.BufferedReader; 
import com.jovi.EndGameException;
import com.jovi.map.file.ReadInitialMap;

public final class MapGenerate
{
    public static List<Island> factory() 
            throws EndGameException, 
                   IOException {
            String map = choiseAvailableMap();
            return (new ReadInitialMap()).read(map);
    }

    private static String choiseAvailableMap() 
            throws IOException, 
                   EndGameException {
        BufferedReader reader =  
                   new BufferedReader(new InputStreamReader(System.in)); 
        String str = "";
        int number = 0;
        boolean waitUserInput = true;
        
        while(waitUserInput) {
            System.out.print("     - ");
            str = reader.readLine().trim(); 
            if (str.equals("exit")) {
                throw new EndGameException("You didn't select a map. Game is stopped");
            }

            try {
                number = Integer.parseInt(str);
            } catch( NumberFormatException nfe) {
                System.err.println("You type is bad number");
            }
            waitUserInput = number < 1 || number > 5;
        }
        System.out.println();
        return "map" + str + ".txt";
    }
}
