import java.io.*;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class main {
    public static void main(String[] args) {
        String fisierProduse = "produse.dat";
        String fisierErori = "erori.log";
        String fisierEpuizate = "epuizate.txt";

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fisierProduse));
             PrintWriter logger = new PrintWriter(new FileWriter(fisierErori, true))) {

            IntStream.rangeClosed(1, 10)
                    .mapToObj(i -> {
                        try {
                            return new Produs("Produs" + i, i * 10, i % 3 == 0 ? 0 : i * 5);
                        } catch (InvalidDataException e) {
                            logger.println("Eroare la produsul " + i + ": " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .forEach(p -> {
                        try {
                            out.writeObject(p);
                        } catch (IOException e) {
                            logger.println("Eroare la scriere produs: " + p.getNume());
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Produs> produse = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fisierProduse))) {
            Stream.generate(() -> {
                        try {
                            return (Produs) in.readObject();
                        } catch (EOFException e) {
                            return null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .takeWhile(Objects::nonNull)
                    .forEach(produse::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fisierEpuizate))) {
            produse.stream()
                    .filter(p -> p.getStoc() == 0)
                    .forEach(writer::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UnaryOperator<Produs> reducere = p -> {
            p.reduceStocCuProcent(0.10);
            return p;
        };

        List<Produs> produseReducere = produse.stream()
                .map(reducere)
                .collect(Collectors.toList());

        produseReducere.stream()
                .max(Comparator.comparingDouble(Produs::getPret))
                .ifPresent(p -> System.out.println("Produsul cu cel mai mare pret:\n" + p));
    }
}

class Produs implements Serializable {
    private final String nume;
    private final double pret;
    private int stoc;

    public Produs(String nume, double pret, int stoc) throws InvalidDataException {
        if (pret < 0 || stoc < 0) {
            throw new InvalidDataException("Pretul sau stocul nu pot fi negative.");
        }
        this.nume = nume;
        this.pret = pret;
        this.stoc = stoc;
    }

    public String getNume() {return nume;}

    public double getPret() {return pret;}

    public int getStoc() {return stoc;}

    public void reduceStocCuProcent(double procent) {
        this.stoc = (int) (this.stoc * (1 - procent));
    }

    @Override
    public String toString() {
        return String.format("%s - Pret: %.2f, Stoc: %d", nume, pret, stoc);
    }
}

class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}
