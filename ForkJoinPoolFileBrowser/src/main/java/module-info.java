module ForkJoinPoolFileBrowser {
    requires javafx.controls;
    requires javafx.fxml;


    opens gov.iti.jets.lab2 to javafx.fxml;
    exports gov.iti.jets.lab2;
}