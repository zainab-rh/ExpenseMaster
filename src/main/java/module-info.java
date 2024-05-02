module com.l22e11 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.l22e11 to javafx.fxml;
    exports com.l22e11;
}
