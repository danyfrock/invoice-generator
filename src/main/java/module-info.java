module InvoiceGenerator {
    requires java.desktop;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    requires java.logging;
    requires commons.math3;

    exports com.invoicegenerator;
    opens com.invoicegenerator.modeles to com.google.gson, javafx.base;
    opens com.invoicegenerator.viewmodels to com.google.gson, javafx.base;
    opens com.invoicegenerator.modeles.navettedtos to com.google.gson, javafx.base;
}