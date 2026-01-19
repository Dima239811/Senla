import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Task3 {
    private BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(5);
    private final AtomicBoolean running = new AtomicBoolean(true);

    private Runnable producer = () -> {
        Random r = new Random();

        while (running.get()) {
            try {
                int item = r.nextInt(1000);
                System.out.println("producer " + item);
                blockingQueue.put(item);
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

        }
    };

    private Runnable consumer = () -> {
        while(running.get() || !blockingQueue.isEmpty()) {
            try {
                Integer consumed = blockingQueue.take();
                System.out.println("Consumed: " + consumed);
                Thread.sleep(400);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    };


    void process() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(producer);
        executor.submit(consumer);

        try {
            TimeUnit.SECONDS.sleep(3);
            running.set(false);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }
    }

    public static void main(String[] args) {
        Task3 task3 = new Task3();
        task3.process();
    }
}
