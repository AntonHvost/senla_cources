package t4;

public class ViewSystemTime {
    public static void main(String[] args) throws InterruptedException {
        DaemonSystemTimeThread daemonThread = new DaemonSystemTimeThread(10);

        daemonThread.setDaemon(true);
        daemonThread.start();

        Thread.sleep(50_000);
        System.out.println("Программа завершена");
    }
}
