package t1;

public class DemoRunnable implements Runnable {
    @Override
    public void run() {
        getResource();
    }

    public static synchronized void getResource() {
        while (true) {

        }
    }
}
