module InvoiceGenerator {
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    requires java.logging;
    requires commons.math3;
    requires org.apache.commons.lang3;
    requires java.desktop;

    exports com.invoicegenerator;
    exports com.invoicegenerator.utils.backend; // Ajouté pour FileUtil
    exports com.invoicegenerator.utils.ihm; // Ajouté pour MenuBuilder

    opens com.invoicegenerator.modeles to com.google.gson, javafx.base;
    opens com.invoicegenerator.viewmodels to com.google.gson, javafx.base;
    opens com.invoicegenerator.modeles.navettedtos to com.google.gson, javafx.base;
}
