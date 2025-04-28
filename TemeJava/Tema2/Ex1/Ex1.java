interface PoateEdita {}
interface PoateSterge {}
interface PoateVizualiza {}

abstract class Utilizator {
    private String nume;

    public Utilizator(String nume) {
        this.nume = nume;
    }

    public String getNume() {
        return nume;
    }
}

class Administrator extends Utilizator implements PoateEdita, PoateSterge, PoateVizualiza {
    public Administrator(String nume) {
        super(nume);
    }
}

class Editor extends Utilizator implements PoateEdita, PoateVizualiza {
    public Editor(String nume) {
        super(nume);
    }
}

class Vizitator extends Utilizator implements PoateVizualiza {
    public Vizitator(String nume) {
        super(nume);
    }
}

class ActiuneService {
    public void afisare(Utilizator utilizator) {
        System.out.println("Actiuni permise pentru " + utilizator.getNume() + ":");

        if (utilizator instanceof PoateVizualiza) {
            System.out.println("-vizualizare");
        }
        if (utilizator instanceof PoateEdita) {
            System.out.println("-editare");
        }
        if (utilizator instanceof PoateSterge) {
            System.out.println("-stergere");
        }
        System.out.println();
    }
}

public class Ex1 {
    public static void main(String[] args) {
        Utilizator admin = new Administrator("admin");
        Utilizator editor = new Editor("edit");
        Utilizator vizitator = new Vizitator("vizit");

        ActiuneService service = new ActiuneService();

        service.afisare(admin);
        service.afisare(editor);
        service.afisare(vizitator);
    }
}
