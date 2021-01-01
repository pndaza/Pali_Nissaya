package mm.pndaza.palitawnissaya.model;

public class Nsy {
    private String id;
    private  String name;
    private  int pdfPageNumber;

    public Nsy(String id, String name, int pdfPageNumber){
        this.id = id;
        this.name = name;
        this.pdfPageNumber = pdfPageNumber;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPdfPageNumber() {
        return pdfPageNumber;
    }
}
