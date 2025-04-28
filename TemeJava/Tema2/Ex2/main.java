 sealed abstract class MetodaPlata
        permits Card, Cash, TransferBancar {
    public abstract void valideaza();
}

 final class Card extends MetodaPlata {
    private String cvv;
    private String dataExpirare; // Format MM/YY

    public Card(String cvv, String dataExpirare) {
        this.cvv = cvv;
        this.dataExpirare = dataExpirare;
    }

    @Override
    public void valideaza() {
        if (cvv.length() == 3 && dataExpirare.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            System.out.println("Card valid");
        } else {
            System.out.println("card invalid");
        }
    }
}

 final class Cash extends MetodaPlata {
    @Override
    public void valideaza() {
        System.out.println("Tranzactia e instantanee.");
    }
}

 final class TransferBancar extends MetodaPlata {
    private String iban;

    public TransferBancar(String iban) {
        this.iban = iban;
    }

    @Override
    public void valideaza() {
        if (iban.length() == 24) {
            System.out.println("IBAN valid.");
        } else {
            System.out.println("IBAN invalid");
        }
    }
}

 class ProcesatorPlati {
     void proceseazaPlata(MetodaPlata metoda) {
        if (metoda instanceof Card card) {
            card.valideaza();
        } else if (metoda instanceof Cash cash) {
            cash.valideaza();
        } else if (metoda instanceof TransferBancar transfer) {
            transfer.valideaza();
        } else {
            System.out.println("Incercati alta metoda");
        }
    }
}

public class main {
    public static void main(String[] args) {
        ProcesatorPlati procesator = new ProcesatorPlati();

        MetodaPlata plata1 = new Card("123", "12/25");
        MetodaPlata plata2 = new Cash();
        MetodaPlata plata3 = new TransferBancar("123435644823345111237260");

        procesator.proceseazaPlata(plata1);
        System.out.println();
        procesator.proceseazaPlata(plata2);
        System.out.println();
        procesator.proceseazaPlata(plata3);
    }
}
