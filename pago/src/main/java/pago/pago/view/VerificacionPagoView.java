package pago.pago.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import pago.pago.domain.VerificacionPago;
import pago.pago.service.FactoryPago;
import pago.pago.viewModel.VerificacionPagoViewModel;

public class VerificacionPagoView {

    @FXML
    private TextField txtId, txtIdPago, txtFecha, txtResultado, txtEncargado;
    @FXML private TextArea txtObservaciones;
    @FXML private Button btnBuscar, btnNuevo, btnActualizar, btnEliminar, btnSeleccionar;
    @FXML private TableView<VerificacionPago> tabla;
    @FXML private ComboBox<VerificacionPago> comboVerificaciones;

    @FXML private TableColumn<VerificacionPago, String> colId, colIdPago, colFecha,
            colResultado, colEncargado;

    private VerificacionPagoViewModel vm;

    @FXML
    public void initialize() {
        vm = new VerificacionPagoViewModel(new FactoryPago());

        // ===================================
        // BINDINGS BIDIRECCIONALES
        // ===================================
        txtId.textProperty().bindBidirectional(vm.id);
        txtIdPago.textProperty().bindBidirectional(vm.idPago);
        txtFecha.textProperty().bindBidirectional(vm.fecha);
        txtResultado.textProperty().bindBidirectional(vm.resultado);
        txtEncargado.textProperty().bindBidirectional(vm.encargado);
        txtObservaciones.textProperty().bindBidirectional(vm.observaciones);

        // ===================================
        // CONFIGURACIÓN DE TABLA
        // ===================================
        colId.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getId()));
        colIdPago.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getIdPago()));
        colFecha.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getFecha()));
        colResultado.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getResultado()));
        colEncargado.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getEncargado()));

        tabla.setItems(vm.getLista());

        // ===================================
        // CONFIGURACIÓN DE COMBOBOX
        // ===================================
        comboVerificaciones.setItems(vm.getLista());

        // Convertidor para mostrar el ID en el ComboBox
        comboVerificaciones.setConverter(new StringConverter<VerificacionPago>() {
            @Override
            public String toString(VerificacionPago verificacion) {
                return verificacion != null ? verificacion.getId() : "";
            }

            @Override
            public VerificacionPago fromString(String string) {
                return comboVerificaciones.getItems().stream()
                        .filter(v -> v.getId().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // ===================================
        // EVENTOS DE BOTONES
        // ===================================
        btnBuscar.setOnAction(e -> vm.search());
        btnNuevo.setOnAction(e -> vm.limpiarPantalla());
        btnActualizar.setOnAction(e -> vm.createOrUpdate());
        btnEliminar.setOnAction(e -> vm.delete());

        // Botón Seleccionar (desde ComboBox)
        btnSeleccionar.setOnAction(e -> {
            VerificacionPago selected = comboVerificaciones.getSelectionModel().getSelectedItem();
            if (selected != null) {
                vm.updateViewModel(selected);
            }
        });

        // ===================================
        // EVENTOS DE SELECCIÓN
        // ===================================
        // Selección desde tabla
        tabla.setOnMouseClicked(e -> {
            VerificacionPago v = tabla.getSelectionModel().getSelectedItem();
            if (v != null) {
                vm.updateViewModel(v);
                comboVerificaciones.getSelectionModel().select(v);
            }
        });

        // Selección desde ComboBox
        comboVerificaciones.setOnAction(e -> {
            VerificacionPago v = comboVerificaciones.getSelectionModel().getSelectedItem();
            if (v != null) {
                vm.updateViewModel(v);
                tabla.getSelectionModel().select(v);
            }
        });
    }
}
