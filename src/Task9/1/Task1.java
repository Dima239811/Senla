public class Task1 {
    public static void main(String[] args) throws InterruptedException {
        // NEW
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("NEW: " + thread1.getState());

        // RUNNABLE
        thread1.start();
        System.out.println("RUNNABLE: " + thread1.getState());

        Object lock = new Object();

        // WAITING
        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("thread2 захватил монитор и ждёт...");
                    Thread.sleep(500);
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.start();
        Thread.sleep(100);
        System.out.println("WAITING: " + thread2.getState());

        // BLOCKED
        Thread blockedThread = new Thread(() -> {
            System.out.println("blockedThread пытается захватить монитор...");
            synchronized (lock) {
                System.out.println("blockedThread получил монитор!");
            }
        });
        blockedThread.start();

        Thread.sleep(300);
        System.out.println("Состояние blockedThread: " + blockedThread.getState());

        // Разблокируем thread2
        synchronized (lock) {
            lock.notify();
        }

        thread1.join();
        thread2.join();
        blockedThread.join();

        System.out.println("TERMINATED (thread1): " + thread1.getState());
        System.out.println("TERMINATED (thread2): " + thread2.getState());
        System.out.println("TERMINATED (blockedThread): " + blockedThread.getState());
    }
}
