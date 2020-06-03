package com.jovi.map.file;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import com.jovi.map.Island;

public final class SaveResultMap {

    public static final String OUTPUT_FILE_NAME = "OutputMap.txt";

    public static String save(List<Island> islands) throws IOException {
        File file = new File(OUTPUT_FILE_NAME);
        file.createNewFile();
        try(PrintWriter pw = new PrintWriter(file)) {
            for (Island island : islands) {
                if (!island.isDestroyed()) {
                    pw.println(island.getIslandInfo());
                }
            }
        }
        
        return file.getAbsolutePath();
    }
}
