import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Objects;

class Produs {
    private String cod;
    private String nume;
    private int pret;

    public Produs(String cod, String nume, int pret) {
        this.cod = cod;
        this.nume = nume;
        this.pret = pret;
    }

    public String getCod() {
        return cod;
    }

    public String getNume() {
        return nume;
    }

    public double getPret() {
        return pret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produs)) return false;
        Produs produs = (Produs) o;
        return Objects.equals(cod, produs.cod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cod);
    }

    @Override
    public String toString() {
        return "Produs{" +
                "cod='" + cod + '\'' +
                ", nume='" + nume + '\'' +
                ", pret=" + pret +
                '}';
    }
}

public class Main {
    public static void main(String[] args) {
        Set<Produs> produse = new HashSet<>();

        Produs p1 = new Produs("1", "Laptop", 3500);
        Produs p2 = new Produs("2", "Telefon", 2200);
        Produs p3 = new Produs("1", "Laptop Gaming", 4500);

        produse.add(p1);
        produse.add(p2);
        produse.add(p3); // Nu se adauga

        System.out.println("produse in hashset:");
        produse.forEach(System.out::println);
        Map<Produs, Integer> stocuri = new HashMap<>();

        stocuri.put(p1, 10);
        stocuri.put(p2, 5);
        stocuri.put(p3, 7);

        System.out.println("Stoc produse:");
        for (Map.Entry<Produs, Integer> entry : stocuri.entrySet()) {
            System.out.println(entry.getKey() + " Stoc: " + entry.getValue());
        }

        stocuri.forEach((produs, stoc) -> System.out.println(produs + "Stoc: " + stoc));
    }
}
