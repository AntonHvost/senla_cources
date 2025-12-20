package t1;

public class Task1 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new DemoRunnable());
        Thread thread2 = new Thread(new DemoRunnable());

        System.out.println(thread1.getState());

        thread1.start();
        thread2.start();

        System.out.println(thread1.getState());

        Thread.sleep(200);

        System.out.println(thread2.getState());

        System.exit(0);
    }
}
