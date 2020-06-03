package com.jovi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import com.jovi.map.Island;
import com.jovi.map.MapGenerate;
import com.jovi.map.file.SaveResultMap;
import com.jovi.soldier.Viking;
import com.jovi.soldier.VikingGenerate;

public class App {
    private static final ByteArrayOutputStream outStream =
            new ByteArrayOutputStream();
    public static void main(String[] args) throws IOException {

        System.out.println("----------------------------------");
        System.out.println("   Welcome to the <Vikings War>");
        System.out.println("----------------------------------\n");

        try {
            System.out.println("             Init Map");
            System.out.println("            ----------");
            System.out.println("Type \"number\" of map between 1 and 5");
            System.out.println("Type \"exit\"   to leave the game");

            List<Island> islands = MapGenerate.factory();

            System.out.println("             Vikings");
            System.out.println("            ----------");
            System.out.println("Type \"number\" of Vikings. MaxValue is " + islands.size());
            System.out.println("Type \"exit\"   to leave the game");

            List<Viking> vikings = VikingGenerate.factory(islands);

            System.out.println();
            System.out.println("               RUN");
            System.out.println("              -----");

            PrintStream stdout = System.out;
            System.setOut(new PrintStream(outStream));
            War.start(vikings);
            System.setOut(stdout);
            System.out.print(outStream.toString());

            System.out.println("----------------------------------\n");
            System.out.println("            OUTPUT MAP");
            System.out.println("          --------------");

            String path = SaveResultMap.save(islands);
            System.out.println(path + "\n");


            System.out.println("----------------------------------");
            System.out.println("----------------------------------");
            System.out.println("            END GAME");


        } catch (EndGameException e) {
            System.err.println(e.getMessage());
        }
    }
}
