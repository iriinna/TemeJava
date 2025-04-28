interface Dispozitiv {
    void porneste();
    void opreste();

    default void status() {
        System.out.println("Status: " + verificare());
    }

    static void descriereGenerala() {
        System.out.println("Dispozitivele funtioneaza cu ajutorul electricitatii.");
    }

    private String verificare() {
        return "Functioneaza bine";
    }
}

interface Smart extends Dispozitiv {
    void actualizareSoftware();
}

interface Conectabil extends Dispozitiv {
    void conectareInternet();
}

class Telefon implements Smart, Conectabil {
    @Override
    public void porneste() {
        System.out.println("Telefonul se porneste.");
    }

    @Override
    public void opreste() {
        System.out.println("Telefonul se opreste.");
    }

    @Override
    public void actualizareSoftware() {
        System.out.println("Telefonul actualizeaza software ul.");
    }

    @Override
    public void conectareInternet() {
        System.out.println("Telefonul se conecteaza la internet.");
    }
}

class Smartwatch implements Smart {
    @Override
    public void porneste() {
        System.out.println("Smartwatch-ul se porneste.");
    }

    @Override
    public void opreste() {
        System.out.println("Smartwatch-ul se opreste.");
    }

    @Override
    public void actualizareSoftware() {
        System.out.println("Smartwatch-ul actualizeaza software ul.");
    }
}

class Televizor implements Conectabil {
    @Override
    public void porneste() {
        System.out.println("Televizorul se porneste.");
    }

    @Override
    public void opreste() {
        System.out.println("Televizorul se opreste.");
    }

    @Override
    public void conectareInternet() {
        System.out.println("Televizorul se conecteaza la internet.");
    }
}

public class Main {
    public static void main(String[] args) {
        Dispozitiv.descriereGenerala();
        System.out.println();

        Telefon telefon = new Telefon();
        Smartwatch ceas = new Smartwatch();
        Televizor tv = new Televizor();

        telefon.porneste();
        telefon.status();
        telefon.actualizareSoftware();
        telefon.conectareInternet();
        telefon.opreste();
        System.out.println();

        ceas.porneste();
        ceas.status();
        ceas.actualizareSoftware();
        ceas.opreste();
        System.out.println();

        tv.porneste();
        tv.status();
        tv.conectareInternet();
        tv.opreste();
    }
}
