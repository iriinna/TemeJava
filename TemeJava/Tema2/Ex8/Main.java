enum NivelAcces {
    ADMIN(1, "Acces complet"),
    EDITOR(2, "Acces editare"),
    USER(3, "Acces citire"),
    GUEST(4, "Acces limitat");

    private final int cod;
    private final String descriere;

    NivelAcces(int cod, String descriere) {
        this.cod = cod;
        this.descriere = descriere;
    }

    public String getDescriere() {
        return descriere;
    }

    public int getCod() {
        return cod;
    }

    public static NivelAcces dinCod(int cod) {
        for (NivelAcces nivel : NivelAcces.values()) {
            if (nivel.getCod() == cod) {
                return nivel;
            }
        }
        throw new IllegalArgumentException("Codul nu corespunde niciunui nivel de acces.");
    }
}

class ContUtilizator {
    private String nume;
    private NivelAcces nivel;

    public ContUtilizator(String nume, NivelAcces nivelAcces) {
        this.nume = nume;
        this.nivel = nivelAcces;
    }

    public NivelAcces getNivelAcces() {
        return nivel;
    }

    @Override
    public String toString() {
        return "ContUtilizator\n"+"nume='"+nume + '\n'+", nivelAcces="+nivel.getDescriere()+'\n';
    }
}

// Clasa Main pentru demonstrație
public class Main {
    public static void main(String[] args) {
        // Creăm mai multe conturi cu diferite nivele de acces
        ContUtilizator cont1 = new ContUtilizator("Ion", NivelAcces.ADMIN);
        ContUtilizator cont2 = new ContUtilizator("Maria", NivelAcces.EDITOR);
        ContUtilizator cont3 = new ContUtilizator("George", NivelAcces.USER);
        ContUtilizator cont4 = new ContUtilizator("Alex", NivelAcces.GUEST);

        ContUtilizator[] conturi = {cont1, cont2, cont3, cont4};

        NivelAcces nivelMinim = NivelAcces.EDITOR; //min niv

        System.out.println("Conturi cu acces superior nivelului " + nivelMinim.getDescriere() + ":");
        for (ContUtilizator cont : conturi) {
            if (cont.getNivelAcces().ordinal() < nivelMinim.ordinal()) {
                System.out.println(cont);
            }
        }

        try {
            NivelAcces nivel = NivelAcces.dinCod(2);
            System.out.println("\nNivelul cu codul 2 este: " + nivel.getDescriere());
        } catch (IllegalArgumentException e) {
            System.out.println("\nEroare: " + e.getMessage());
        }
    }
}
