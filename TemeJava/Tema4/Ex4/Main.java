import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    private static final int PORT = 8888;
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private static volatile boolean running = true;

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equalsIgnoreCase("server")) {
            runServer();
        } else {
            runClient();
        }
    }

    private static void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (running) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.out.println("Serverul a fost oprit");
        }
    }

    private static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    private static void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    private static void shutdownServer() {
        running = false;
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage("Serverul se inchide");
                client.close();
            }
            clients.clear();
        }
        System.exit(0);
    }

    static class ClientHandler implements Runnable {
        private static final ThreadLocal<Socket> clientSocket = new ThreadLocal<>();
        private BufferedReader in;
        private PrintWriter out;
        private String name;
        private boolean isAdmin = false;

        public ClientHandler(Socket socket) {
            clientSocket.set(socket);
        }

        public void run() {
            try {
                Socket socket = clientSocket.get();
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                name = in.readLine();
                if ("admin".equalsIgnoreCase(name)) {
                    isAdmin = true;
                }

                broadcast(name + " s-a conectat.", this);

                String msg;
                while ((msg = in.readLine()) != null) {
                    if ("/quit".equalsIgnoreCase(msg)) {
                        out.println("Deconectat");
                        break;
                    } else if ("/shutdown".equalsIgnoreCase(msg)) {
                        if (isAdmin) {
                            shutdownServer();
                            break;
                        } else {
                            out.println("Nu poti opri serverul");
                        }
                    } else {
                        broadcast(name + ": " + msg, this);
                    }
                }
            } catch (IOException e) {
                System.out.println("Eroare la client");
            } finally {
                removeClient(this);
                broadcast(name + " s-a deconectat.", this);
                close();
            }
        }

        public void sendMessage(String msg) {
            out.println(msg);
        }

        public void close() {
            try {
                clientSocket.get().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void runClient() {
        try (Socket socket = new Socket("localhost", PORT)) {
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);

            Thread readerThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = serverIn.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("Conexiunea cu serverul a fost intrerupta");
                }
            });

            Thread writerThread = new Thread(() -> {
                try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
                    String input;
                    while ((input = userInput.readLine()) != null) {
                        serverOut.println(input);
                        if ("/quit".equalsIgnoreCase(input)) break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            readerThread.start();
            writerThread.start();

            readerThread.join();
            writerThread.join();

        } catch (IOException | InterruptedException e) {
            System.out.println("Nu se poate conecta la server: " + e.getMessage());
        }
    }
}
