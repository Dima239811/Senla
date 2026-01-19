public class ThreadTime implements Runnable{

    private int n;

    public ThreadTime(int n) {
        this.n = n;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(n);
                System.out.println("Time: " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }


}
