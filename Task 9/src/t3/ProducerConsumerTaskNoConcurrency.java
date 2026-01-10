package t3;

import java.util.Arrays;
import java.util.Random;

public class ProducerConsumerTaskNoConcurrency {
    private static final int MAX_SIZE = 5;
    private static final Integer[] buffer = new Integer[MAX_SIZE];
    private static int in = 0;
    private static int out = 0;
    private static int count = 0;

    public static void main(String[] args) throws InterruptedException{
        Thread producer = new Thread(() -> {
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                int num = rand.nextInt(100);
                synchronized (buffer) {
                    while (count == MAX_SIZE) {
                        try {
                            buffer.wait();
                        }  catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    buffer[in] = num;
                    in = (in + 1) % MAX_SIZE;
                    count++;

                    System.out.println("Производитель сделал число: " + num + ". Буфер: " + Arrays.stream(buffer).toList());
                    buffer.notifyAll();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (buffer) {
                    while (count == 0) {
                        try {
                            buffer.wait();
                        }  catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    int num = buffer[out];
                    buffer[out] = null;
                    out = (out + 1) % MAX_SIZE;
                    count--;

                    System.out.println("Потребитель обработал число: " + num + ". Буффер: " + Arrays.stream(buffer).toList());
                    buffer.notifyAll();

                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }
}
