package t4;

import java.time.LocalTime;

public class DaemonSystemTimeThread extends Thread {
    private long n = 0;

    DaemonSystemTimeThread(long n) {
        this.n = n;
    }
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            System.out.println("Текущее время в системе: " + LocalTime.now());
            try {
                Thread.sleep(n * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
