package t3;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerTaskWithConcurrency {
    public static void main(String[] args) {
        BlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(5);

        Thread producer = new Thread(() -> {
            Random random = new Random();

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int num = random.nextInt(10);
                    System.out.println("Производитель сделал число: " + num);
                    buffer.put(num);
                    System.out.println("Буфер: " + buffer);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Производитель приостановлен.");
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Integer buf_num = buffer.take();
                    System.out.println("Потребитель получил число: " + buf_num);
                    Thread.sleep(550);
                }
            }  catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Потребитель остановлен.");
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        producer.interrupt();
        consumer.interrupt();
    }
}
