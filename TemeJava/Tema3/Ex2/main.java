import java.io.*;
import java.util.*;
import java.util.stream.*;

public class main {
    public static void main(String[] args) {
        String fisier = "comenzi.dat";

        //scriere
        List<Comanda> comenziInitiale = IntStream.rangeClosed(1, 15)
                .mapToObj(i -> new Comanda(i, "Client" + (i % 5), i * 1000.0, false))
                .toList();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fisier))) {
            comenziInitiale.forEach(c -> {
                try {
                    out.writeObject(c);
                } catch (IOException e) {
                    System.err.println("Eroare la scriere: " + c);
                }
            });
        } catch (IOException e) {
            System.err.println("Eroare generala la scriere: " + e.getMessage());
        }

        //citire
        List<Comanda> comenzi = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fisier))) {
            Stream.generate(() -> {
                        try {
                            return (Comanda) in.readObject();
                        } catch (EOFException e) {
                            return null;
                        } catch (IOException | ClassNotFoundException e) {
                            System.err.println("Eroare la citire: " + e.getMessage());
                            return null;
                        }
                    })
                    .takeWhile(Objects::nonNull)
                    .forEach(comenzi::add);
        } catch (IOException e) {
            System.err.println("Eroare la deschiderea fisierului: " + e.getMessage());
        }

        List<Comanda> comenziModificate = comenzi.stream()
                .map(c -> c.getValoare() > 5000 && !c.isFinalizata()
                        ? new Comanda(c.getId(), c.getNumeClient(), c.getValoare(), true)
                        : c)
                .toList();

        //rescriere fisier
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fisier))) {
            comenziModificate.forEach(c -> {
                try {
                    out.writeObject(c);
                } catch (IOException e) {
                    System.err.println("Eroare la scriere actualizata: " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Eroare la rescriere: " + e.getMessage());
        }

        List<Comanda> finalizate = comenziModificate.stream()
                .filter(Comanda::isFinalizata)
                .toList();

        double sumaFinalizate = finalizate.stream()
                .mapToDouble(Comanda::getValoare)
                .sum();

        Map<String, List<Comanda>> grupatePeClient = finalizate.stream()
                .collect(Collectors.groupingBy(Comanda::getNumeClient));

        System.out.println("Comenzi finalizate:");
        finalizate.forEach(System.out::println);

        System.out.printf("Suma: %.2f RON%n", sumaFinalizate);

        System.out.println("\nComenzi grupate pe client:");
        grupatePeClient.forEach((client, lista) -> {
            System.out.println("Client: " + client);
            lista.forEach(c -> System.out.println("  " + c));
        });
    }
}
