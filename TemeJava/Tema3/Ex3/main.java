import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class main {
    public static void main(String[] args) {

        List<Client> clienti = List.of(
                new Client("Ana", 30, 8000, Optional.of("VIP")),
                new Client("Mihai", 22, 2000, Optional.of("Standard")),
                new Client("Ioana", 45, 15000, Optional.of("VIP")),
                new Client("Irina", 28, 5000, Optional.empty()),
                new Client("Elena", 24, 7000, Optional.of("Nou")),
                new Client("Cristi", 19, 1500, Optional.of("Standard")),
                new Client("Roberta", 26, 1000, Optional.empty()),
                new Client("Anastasia", 23, 6500, Optional.of("VIP")),
                new Client("Denisa", 21, 3000, Optional.of("Nou")),
                new Client("Maria", 35, 12000, Optional.of("VIP")),
                new Client("Horia", 27, 4000, Optional.empty()),
                new Client("Bianca", 20, 2200, Optional.of("Standard"))
        );

        Predicate<Client> esteVIP = c -> c.getTipClient().orElse("Necunoscut").equals("VIP");
        Supplier<Double> mediaSume = () -> clienti.stream()
                .mapToDouble(Client::getSumaCont)
                .average()
                .orElse(0.0);

        Predicate<Client> pesteMedia = c -> c.getSumaCont() > mediaSume.get();
        List<Client> clientiFiltrati = clienti.stream()
                .filter(esteVIP.and(pesteMedia))
                .collect(Collectors.toList());

        clientiFiltrati.forEach(System.out::println);

        Function<Client, String> formatNumeVarsta = c -> c.getNume() + " - " + c.getVarsta() + " ani";
        List<String> clientiFormatati = clienti.stream()
                .map(formatNumeVarsta)
                .collect(Collectors.toList());

        clientiFormatati.forEach(System.out::println);

        BiFunction<Double, Client, Double> acumulare = (acc, c) -> acc + c.getSumaCont();
        Double total = clienti.stream()
                .reduce(0.0, acumulare, Double::sum);

        System.out.println("\nSuma totala: " + total);

        Map<String, Long> grupareTip = clienti.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getTipClient().orElse("Necunoscut"),
                        Collectors.counting()
                ));

        grupareTip.forEach((tip, nr) -> System.out.println(tip + ": " + nr));

        Predicate<Client> sub25 = c -> c.getVarsta() < 25;
        Function<Client, String> doarNume = Client::getNume;

        String numeTineri = clienti.stream()
                .filter(sub25)
                .map(doarNume)
                .collect(Collectors.joining(", "));

        System.out.println("\nClienti sub 25 ani: " + numeTineri);
    }
}

class Client {
    private final String nume;
    private final int varsta;
    private final double sumaCont;
    private final Optional<String> tipClient;

    public Client(String nume, int varsta, double sumaCont, Optional<String> tipClient) {
        this.nume = nume;
        this.varsta = varsta;
        this.sumaCont = sumaCont;
        this.tipClient = tipClient;
    }

    public String getNume() {return nume;}

    public int getVarsta() {return varsta;}

    public double getSumaCont() {return sumaCont;}

    public Optional<String> getTipClient() {return tipClient;}

    @Override
    public String toString() {
        return String.format("%s, %d ani - %.2f[%s]",
                nume, varsta, sumaCont, tipClient.orElse("Necunoscut"));
    }
}
