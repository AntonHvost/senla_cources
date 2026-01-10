package t2;

public class MessageThread implements Runnable {
    private final static Object lock = new Object();
    private final String message;

    public MessageThread(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        for(int i = 0; i < 5; i++) {
            synchronized (lock) {
                System.out.println("Имя потока " + message + ". Имя потока в системе " + Thread.currentThread().getName());
                lock.notify();
                try {
                    lock.wait();
                }  catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
