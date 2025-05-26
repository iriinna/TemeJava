import java.util.*;

public class Main {

    static final List<String> bufferA = Collections.synchronizedList(new ArrayList<>());
    static final List<String> bufferB = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws InterruptedException {
        simulateFixedVersion();
    }

    static void simulateDeadlock() throws InterruptedException {
        Thread p1 = new Thread(() -> {
            synchronized (bufferA) {
                System.out.println("P1 a blocat BufferA");
                sleep(100);
                synchronized (bufferB) {
                    System.out.println("P1 a blocat BufferB");
                    bufferA.add("P1-data");
                    bufferB.add("P1-data");
                }
            }
        });

        Thread p2 = new Thread(() -> {
            synchronized (bufferB) {
                System.out.println("P2 a blocat BufferB");
                sleep(100);
                synchronized (bufferA) {
                    System.out.println("P2 a blocat BufferA");
                    bufferA.add("P2-data");
                    bufferB.add("P2-data");
                }
            }
        });

        Thread c1 = new Thread(() -> {
            synchronized (bufferA) {
                System.out.println("C1 a blocat BufferA");
                sleep(100);
                synchronized (bufferB) {
                    System.out.println("C1 a blocat BufferB");
                    consume("C1");
                }
            }
        });

        Thread c2 = new Thread(() -> {
            synchronized (bufferB) {
                System.out.println("C2 a blocat BufferB");
                sleep(100);
                synchronized (bufferA) {
                    System.out.println("C2 a blocat BufferA");
                    consume("C2");
                }
            }
        });

        p1.start(); p2.start(); c1.start(); c2.start();
        Thread.sleep(10000);
    }

    static void simulateFixedVersion() throws InterruptedException {
        Object lock1 = bufferA;
        Object lock2 = bufferB;

        Thread p1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("P1 a blocat BufferA");
                sleep(100);
                synchronized (lock2) {
                    System.out.println("P1 a blocat BufferB");
                    bufferA.add("P1-data");
                    bufferB.add("P1-data");
                }
            }
        });

        Thread p2 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("P2 a blocat BufferA");
                sleep(100);
                synchronized (lock2) {
                    System.out.println("P2 a blocat BufferB");
                    bufferA.add("P2-data");
                    bufferB.add("P2-data");
                }
            }
        });

        Thread c1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("C1 a blocat BufferA");
                sleep(100);
                synchronized (lock2) {
                    System.out.println("C1 a blocat BufferB");
                    consume("C1");
                }
            }
        });

        Thread c2 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("C2 a blocat BufferA");
                sleep(100);
                synchronized (lock2) {
                    System.out.println("C2 a blocat BufferB");
                    consume("C2");
                }
            }
        });

        p1.start(); p2.start(); c1.start(); c2.start();
        p1.join(); p2.join(); c1.join(); c2.join();

    }

    static void consume(String who) {
        if (!bufferA.isEmpty() && !bufferB.isEmpty()) {
            String a = bufferA.remove(0);
            String b = bufferB.remove(0);
            System.out.println(who + " a consumat " + a + " + " + b);
        } else {
            System.out.println(who + "buffere goale");
        }
    }

    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
