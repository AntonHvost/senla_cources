package t2;

public class MainThread {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new MessageThread("Поток 1"));
        Thread t2 = new Thread(new MessageThread("Поток 2"));

        Thread.sleep(200);
        t1.start();
        t2.start();
        Thread.sleep(200);
        System.exit(0);
    }
}
