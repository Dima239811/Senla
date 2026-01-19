public class task2 {
    private static final Object lock = new Object();

    private static boolean isFirstThreadTurn = true;

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            String threadName = "Поток 1";
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    while (!isFirstThreadTurn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(threadName);
                    isFirstThreadTurn = false;
                    lock.notifyAll();
                }
            }
        });

        // Второй поток
        Thread thread2 = new Thread(() -> {
            String threadName = "Поток 2";
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    while (isFirstThreadTurn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(threadName);
                    isFirstThreadTurn = true;
                    lock.notifyAll();
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
