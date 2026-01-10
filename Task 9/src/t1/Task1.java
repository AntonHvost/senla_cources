package t1;

public class Task1 {

    private static final Object lock = new Object();
    private static volatile boolean flag = false;
    private static volatile boolean blocked = false;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new DemoRunnable());

        System.out.println(thread.getState());

        thread.start();

        System.out.println(thread.getState());

        Thread.sleep(200);
        System.out.println(thread.getState());

        synchronized (lock) {
            flag = true;
            lock.notify();
        }
        Thread.sleep(50);

        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(1000);
                }  catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        thread2.start();
        Thread.sleep(200);

        synchronized (DemoRunnable.class) {
            blocked = true;
        }

        Thread.sleep(200);
        System.out.println(thread.getState());
        thread2.join();
        Thread.sleep(10);
        System.out.println(thread.getState());
        thread.join();
        System.out.println(thread.getState());
        System.exit(0);
    }

    static class DemoRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    while (!flag) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            while (!blocked) {
                Thread.yield();
            }

            synchronized (lock) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
