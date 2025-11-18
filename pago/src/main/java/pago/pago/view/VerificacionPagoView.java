package pago.pago.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import pago.pago.domain.VerificacionPago;
import pago.pago.service.FactoryPago;
import pago.pago.viewModel.VerificacionPagoViewModel;

public class VerificacionPagoView {

    @FXML
    private TextField txtId, txtIdPago, txtFecha, txtResultado, txtEncargado;
    @FXML private TextArea txtObservaciones;
    @FXML private Button btnBuscar, btnNuevo, btnActualizar, btnEliminar;
    @FXML private TableView<VerificacionPago> tabla;

    @FXML private TableColumn<VerificacionPago, String> colId, colIdPago, colFecha, colResultado, colEncargado;

    private VerificacionPagoViewModel vm;

    @FXML
    public void initialize() {
        vm = new VerificacionPagoViewModel(new FactoryPago());

        // Bindings
        txtId.textProperty().bindBidirectional(vm.id);
        txtIdPago.textProperty().bindBidirectional(vm.idPago);
        txtFecha.textProperty().bindBidirectional(vm.fecha);
        txtResultado.textProperty().bindBidirectional(vm.resultado);
        txtEncargado.textProperty().bindBidirectional(vm.encargado);
        txtObservaciones.textProperty().bindBidirectional(vm.observaciones);

        // Tabla
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getId()));
        colIdPago.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIdPago()));
        colFecha.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFecha()));
        colResultado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getResultado()));
        colEncargado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEncargado()));

        tabla.setItems(vm.getLista());

        btnBuscar.setOnAction(e -> vm.search());
        btnNuevo.setOnAction(e -> vm.limpiarPantalla());
        btnActualizar.setOnAction(e -> vm.createOrUpdate());
        btnEliminar.setOnAction(e -> vm.delete());

        tabla.setOnMouseClicked(e -> {
            VerificacionPago v = tabla.getSelectionModel().getSelectedItem();
            if (v != null)
                vm.updateViewModel(v);
        });
    }
}
