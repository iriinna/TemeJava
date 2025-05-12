import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class main {
    public static void main(String[] args) {
        Path inputPath = Paths.get("date.txt");
        Path outputPath = Paths.get("rezultat.txt");

        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter writer = Files.newBufferedWriter(outputPath)) {

            List<Persoana> persoane = reader.lines()
                    .map(main::parseLinie)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            List<Persoana> filtrate = persoane.stream()
                    .filter(p -> p.getVarsta() > 30)
                    .filter(p -> p.getOras().startsWith("B"))
                    .sorted(Comparator.comparing(Persoana::getNume).thenComparing(Persoana::getVarsta))
                    .collect(Collectors.toList());

            Map<String, List<Persoana>> grupate = persoane.stream()
                    .collect(Collectors.groupingBy(Persoana::getOras));

            Map<String, Double> mediiVarsta = persoane.stream()
                    .collect(Collectors.groupingBy(
                            Persoana::getOras,
                            Collectors.averagingInt(Persoana::getVarsta)
                    ));

            Optional<Persoana> maxVarsta = persoane.stream()
                    .max(Comparator.comparingInt(Persoana::getVarsta));

            filtrate.forEach(p -> scrie(writer, p.toString()));

            grupate.forEach((oras, lista) -> {
                scrie(writer, "Oras: " + oras);
                lista.forEach(p -> scrie(writer, "  " + p));
            });

            mediiVarsta.forEach((oras, media) ->
                    scrie(writer, oras + ": " + String.format("%.2f", media)));

            maxVarsta.ifPresent(p -> scrie(writer, p.toString()));

        } catch (IOException e) {
            System.err.println("Eroare la citire/scriere fisier: " + e.getMessage());
        }
    }

    private static Persoana parseLinie(String linie) {
        try {
            String[] parts = linie.split(";");
            String nume = parts[0].trim();
            int varsta = Integer.parseInt(parts[1].trim());
            String oras = parts[2].trim();
            return new Persoana(nume, varsta, oras);
        } catch (Exception e) {
            System.err.println("Eroare la parsarea liniei: " + linie);
            return null;
        }
    }

    private static void scrie(BufferedWriter writer, String linie) {
        try {
            writer.write(linie);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Eroare la scriere: " + e.getMessage());
        }
    }
}

class Persoana {
    private final String nume;
    private final int varsta;
    private final String oras;

    public Persoana(String nume, int varsta, String oras) {
        this.nume = nume;
        this.varsta = varsta;
        this.oras = oras;
    }

    public String getNume() {return nume;}

    public int getVarsta() {return varsta;}

    public String getOras() {return oras;}

    @Override
    public String toString() {
        return nume + " (" + varsta + " ani) " + oras;
    }
}
