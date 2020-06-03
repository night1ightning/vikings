package com.jovi;

import java.util.ArrayList;
import java.util.List;
import com.jovi.soldier.Viking;
import java.lang.Runtime;
import java.util.concurrent.Semaphore;
import com.jovi.soldier.Soldier;

public class War {
    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    public static void start(List<Viking> vikings) {
        Semaphore table = new Semaphore(PROCESSORS);
        List<Thread> threads = new ArrayList<Thread>(vikings.size());
        Thread thread;

        for (Viking viking : vikings) {
            thread = new Thread(new SoldierInWar(viking, table));
            thread.start();
            threads.add(thread);
        }

        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class SoldierInWar implements Runnable {
        private static final int DAYS_BEFORE_STARVATION = 10_000;

        private Semaphore table;
        private int daysInWar = 0;
        private Soldier soldier;

        SoldierInWar(Soldier soldier, Semaphore table) {
           this.soldier = soldier;
           this.table = table;
        }

        @Override
        public void run() {
            try {
                while (daysInWar < DAYS_BEFORE_STARVATION) {
                    table.acquire();
                    soldier.attack();

                    if (soldier.isDead()) {
                        break;
                    }
                    if (soldier.isStopped()) {
                        break;
                    }

                    daysInWar++;
                    table.release();
                }
                table.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
