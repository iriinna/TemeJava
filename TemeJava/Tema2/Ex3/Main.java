class RezervareException extends Exception {
    public RezervareException(String mesaj) {
        super(mesaj);
    }
}

class LocIndisponibilException extends RezervareException {
    public LocIndisponibilException(String mesaj) {
        super(mesaj);
    }
}

class DateInvalideException extends RezervareException {
    public DateInvalideException(String mesaj) {
        super(mesaj);
    }
}

class Rezervari {
    private boolean[] locuriDisponibile = new boolean[7];

    public Rezervari() {
    }

    public void rezervaLoc(String data, int loc) throws LocIndisponibilException, DateInvalideException {
        if (data == null || data.length() < 5) {
            throw new DateInvalideException("Data invalida\n");
        }

        if (loc < 0 || loc >= locuriDisponibile.length) {
            throw new DateInvalideException("Loc invalid\n");
        }

        if (locuriDisponibile[loc]) {
            throw new LocIndisponibilException("Locul " + loc + " este deja rezervat\n");
        }

        locuriDisponibile[loc] = true;
        System.out.println("Rezervarea a fost facuta\n");
    }
}

public class Main {
    public static void main(String[] args) {
        Rezervari sistem = new Rezervari();


        try {
            sistem.rezervaLoc("10/05/2025", 2);
        } catch (LocIndisponibilException e) {
            System.out.println("Loc indisponibil - " + e.getMessage());
        } catch (DateInvalideException e) {
            System.out.println("Data invalida - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        } finally {
            System.out.println("terminat proces");
        }

        try {
            sistem.rezervaLoc("", 3);
        } catch (LocIndisponibilException e) {
            System.out.println("Loc indisponibil - " + e.getMessage());
        } catch (DateInvalideException e) {
            System.out.println("Data invalida - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        } finally {
            System.out.println("terminat proces");
        }

        try {
            sistem.rezervaLoc("15/06/2025", 2);
        } catch (LocIndisponibilException e) {
            System.out.println("Loc indisponibil - " + e.getMessage());
        } catch (DateInvalideException e) {
            System.out.println("Data invalida - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        } finally {
            System.out.println("terminat proces");
        }

    }
}
