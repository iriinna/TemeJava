import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    static final String DB_URL = "jdbc:postgresql://localhost:5432/temadb";
    static final String DB_USER = "postgres";
    static final String DB_PASSWORD = "15461";

    static final int POOL_SIZE = 5;
    static final int WORKER_COUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        SimpleConnectionPool pool = new SimpleConnectionPool(DB_URL, DB_USER, DB_PASSWORD, POOL_SIZE);
        ExecutorService executor = Executors.newFixedThreadPool(WORKER_COUNT);

        for (int i = 0; i < WORKER_COUNT; i++) {
            executor.execute(new Worker(pool, i + 1));
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Log")) {
            if (rs.next()) {
                System.out.println("Total inregistrari in Log: " + rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cs = conn.prepareCall("{call clean_old_logs()}")) {
            cs.execute();
            System.out.println("Procedura clean_old_logs() a fost apelata.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pool.closeAll();
    }
}

class SimpleConnectionPool {
    private final List<Connection> availableConnections = new ArrayList<>();

    public SimpleConnectionPool(String url, String user, String pass, int size) {
        for (int i = 0; i < size; i++) {
            try {
                Connection conn = DriverManager.getConnection(url, user, pass);
                availableConnections.add(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized Connection getConnection() throws InterruptedException {
        while (availableConnections.isEmpty()) {
            wait();
        }
        return availableConnections.remove(0);
    }

    public synchronized void releaseConnection(Connection conn) {
        availableConnections.add(conn);
        notifyAll();
    }

    public synchronized void closeAll() {
        for (Connection conn : availableConnections) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        availableConnections.clear();
    }
}

class Worker implements Runnable {
    private final SimpleConnectionPool pool;
    private final int id;

    public Worker(SimpleConnectionPool pool, int id) {
        this.pool = pool;
        this.id = id;
    }

    public void run() {
        try {
            Connection conn = pool.getConnection();
            try {
                for (int i = 0; i < 5; i++) {
                    insertLog(conn, "Mesaj de la thread-ul " + id);
                    Thread.sleep(new Random().nextInt(401) + 100); // 100–500 ms
                }
            } finally {
                pool.releaseConnection(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertLog(Connection conn, String msg) {
        String sql = "INSERT INTO Log (message, created_at) VALUES (?, now())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, msg);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
