import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class main {
    public static void main(String[] args) {
        String fisier = "comenzi.dat";

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fisier))) {
            for (int i = 1; i <= 15; i++) {
                Comanda c = new Comanda(i, "Client" + (i % 5), i * 1000, false);
                out.writeObject(c);
            }
        } catch (IOException e) {
            System.err.println("Eroare la scrierea comenzilor: " + e.getMessage());
        }

        try (RandomAccessFile raf = new RandomAccessFile(fisier, "rw")) {
            while (true) {
                long pos = raf.getFilePointer();
                Comanda comanda;
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fisier))) {
                    while (true) {
                        pos = raf.getFilePointer();
                        comanda = (Comanda) ois.readObject();
                        if (comanda.getValoare() > 5000 && !comanda.isFinalizata()) {
                            comanda.setFinalizata(true);

                            raf.seek(pos);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            ObjectOutputStream tempOut = new ObjectOutputStream(bos);
                            tempOut.writeObject(comanda);
                            tempOut.flush();

                            byte[] bytes = bos.toByteArray();
                            raf.write(bytes);
                        }
                    }
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    System.err.println("Clasa Comanda nu a fost gasita: " + e.getMessage());
                    break;
                }
            }
        } catch (IOException | SecurityException e) {
            System.err.println("Eroare acces random: " + e.getMessage());
        }

        List<Comanda> comenzi = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fisier))) {
            while (true) {
                try {
                    Comanda c = (Comanda) in.readObject();
                    comenzi.add(c);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Eroare la citirea comenzilor: " + e.getMessage());
        }

        List<Comanda> finalizate = comenzi.stream()
                .filter(Comanda::isFinalizata)
                .toList();

        double suma = finalizate.stream()
                .mapToDouble(Comanda::getValoare)
                .sum();

        Map<String, List<Comanda>> comenziPeClient = finalizate.stream()
                .collect(Collectors.groupingBy(Comanda::getNumeClient));

        System.out.println("Comenzi finalizate:");
        finalizate.forEach(System.out::println);

        System.out.println(suma);

        comenziPeClient.forEach((client, lista) -> {
            System.out.println("Client: " + client);
            lista.forEach(c -> System.out.println("  " + c));
        });
    }
}

class Comanda implements Serializable {
    private int id;
    private String numeClient;
    private double valoare;
    private boolean finalizata;

    public Comanda(int id, String numeClient, double valoare, boolean finalizata) {
        this.id = id;
        this.numeClient = numeClient;
        this.valoare = valoare;
        this.finalizata = finalizata;
    }

    public int getId() {
        return id;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public double getValoare() {
        return valoare;
    }

    public boolean isFinalizata() {
        return finalizata;
    }

    public void setFinalizata(boolean finalizata) {
        this.finalizata = finalizata;
    }

    @Override
    public String toString() {
        return String.format("Comanda:id=%d, client='%s', valoare=%.2f, finalizata=%b",
                id, numeClient, valoare, finalizata);
    }
}
