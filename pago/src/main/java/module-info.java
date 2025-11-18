module pago.pago {
    requires javafx.controls;
    requires javafx.fxml;


    opens pago.pago to javafx.fxml;
    exports pago.pago;
    opens pago.pago.domain;
    opens pago.pago.service;
    opens pago.pago.view;
    opens pago.pago.viewModel;
}