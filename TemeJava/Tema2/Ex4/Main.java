// este util atunci cand integram sisteme noi cu sisteme vechi, fara sa rescriem un cod existent


class SistemExistent {
    public void afiseazaXML(String xml) {
        System.out.println("Afisare XML:");
        System.out.println(xml);
    }
}

class SistemNou {
    public String genereazaJSON() {
        return "{ \"nume\": \"Andrei\", \"varsta\": 25 }";
    }
}

class AdaptorJsonToXml {
    private SistemExistent sistemExistent;

    public AdaptorJsonToXml(SistemExistent sistemExistent) {
        this.sistemExistent = sistemExistent;
    }

    public void trimiteDate(String json) {
        String xml = convertesteJsonInXml(json);
        sistemExistent.afiseazaXML(xml);
    }

    private String convertesteJsonInXml(String json) {
        json = json.replace("{", "<date>")
                .replace("}", "</date>")
                .replace("\"", "")
                .replace(":", ">")
                .replace(",", "</camp><camp>");
        return "<camp>" + json + "</camp>";
    }
}

public class Main {
    public static void main(String[] args) {
        SistemNou nou = new SistemNou();
        SistemExistent existent = new SistemExistent();
        AdaptorJsonToXml adaptor = new AdaptorJsonToXml(existent);
        String dateJson = nou.genereazaJSON();
        adaptor.trimiteDate(dateJson);
    }
}
