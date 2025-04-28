import java.util.ArrayList;
import java.util.List;

abstract class OrganismViu {
    public abstract void respira();
    public abstract void seHraneste();
}

abstract class Animal extends OrganismViu {
    @Override
    public final void respira() {
        System.out.println(this.getClass().getSimpleName() + " respira aer.");
    }
}

abstract class Mamifer extends Animal {
    public abstract void arePar();
}

class Urs extends Mamifer {
    @Override
    public void seHraneste() {
        System.out.println("Ursul se hraneste cu miere.");
    }

    @Override
    public void arePar() {
        System.out.println("Ursul are par.");
    }
}

class Delfin extends Mamifer {
    @Override
    public void seHraneste() {
        System.out.println("Delfinul se hransete cu pesti.");
    }

    @Override
    public void arePar() {
        System.out.println("Delfinul nu are par");
    }
}

public class Main {
    public static void main(String[] args) {
        List<OrganismViu> organisme = new ArrayList<>();
        organisme.add(new Urs());
        organisme.add(new Delfin());

        for (OrganismViu organism : organisme) {
            organism.respira();
            organism.seHraneste();

            if (organism instanceof Mamifer mamifer) {
                mamifer.arePar();
            }

            System.out.println();
        }
    }
}
