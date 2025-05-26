import java.util.*;
import java.util.concurrent.*;

public class Main {

    enum TaskStatus {
        RUNNING,
        COMPLETED,
        TIMED_OUT,
        INTERRUPTED
    }

    static class Task implements Runnable {
        private final int id;
        private final long durata;
        private final Object monitor;
        private final Map<Integer, TaskStatus> statusMap;
        private volatile boolean running = true;

        public Task(int id, long durata, Object monitor, Map<Integer, TaskStatus> statusMap) {
            this.id = id;
            this.durata = durata;
            this.monitor = monitor;
            this.statusMap = statusMap;
        }

        @Override
        public void run() {
            statusMap.put(id, TaskStatus.RUNNING);
            System.out.printf("Task %d\nDurata: %d ms\n", id, durata);
            try {
                synchronized (monitor) {
                    long start = System.currentTimeMillis();
                    while (running && (System.currentTimeMillis() - start) < durata) {
                        Thread.sleep(50);
                    }
                }

                if (running) {
                    synchronized (monitor) {
                        statusMap.put(id, TaskStatus.COMPLETED);
                        System.out.printf("%d s-a oprit\n", id);
                    }
                }

            } catch (InterruptedException e) {
                statusMap.put(id, TaskStatus.INTERRUPTED);
                System.out.printf("%d a fost intrerupt.\n", id);
            }
        }

        public void stop() {
            running = false;
        }
    }

    static class Watchdog extends Thread {
        private final Map<Integer, TaskStatus> statusMap;

        public Watchdog(Map<Integer, TaskStatus> statusMap) {
            this.statusMap = statusMap;
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                System.out.printf("%-10s | %-12s\n", "Task ID", "Status");
                for (Map.Entry<Integer, TaskStatus> entry : statusMap.entrySet()) {
                    System.out.printf("%-10d | %-12s\n", entry.getKey(), entry.getValue());
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int nr_threads = 4;
        final long timeout = 3000;
        final Object monitor = new Object();

        ConcurrentHashMap<Integer, TaskStatus> statusMap = new ConcurrentHashMap<>();
        List<Thread> threads = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();

        Watchdog watchdog = new Watchdog(statusMap);
        watchdog.start();

        for (int i = 0; i < nr_threads; i++) {
            long durata = 1000 + new Random().nextInt(3000);
            Task task = new Task(i, durata, monitor, statusMap);
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);
            tasks.add(task);

            final int taskId = i;
            new Thread(() -> {
                try {
                    thread.join(timeout);
                    if (thread.isAlive()) {
                        task.stop();
                        thread.interrupt();
                        statusMap.put(taskId, TaskStatus.TIMED_OUT);
                        System.out.printf("%d timeout\n", taskId);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\ns-au oprit toate taskurile. Se opreste watchdog");
        watchdog.interrupt();
    }
}
