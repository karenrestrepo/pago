package pago.pago.viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import pago.pago.domain.VerificacionPago;
import pago.pago.service.FactoryPago;

public class VerificacionPagoViewModel {
    public StringProperty id = new SimpleStringProperty("");
    public StringProperty idPago = new SimpleStringProperty("");
    public StringProperty fecha = new SimpleStringProperty("");
    public StringProperty resultado = new SimpleStringProperty("");
    public StringProperty encargado = new SimpleStringProperty("");
    public StringProperty observaciones = new SimpleStringProperty("");

    private final FactoryPago factory;

    public VerificacionPagoViewModel(FactoryPago factory) {
        this.factory = factory;
    }

    public ObservableList<VerificacionPago> getLista() {
        return factory.getLstVerificaciones();
    }

    // --------------------------
    // UPDATE VIEW MODEL
    // --------------------------
    public void updateViewModel(VerificacionPago v) {
        if (v == null) {
            mostrarAlerta("Advertencia", "No se encontró la verificación", Alert.AlertType.WARNING);
            return;
        }
        id.set(v.getId() != null ? v.getId() : "");
        idPago.set(v.getIdPago() != null ? v.getIdPago() : "");
        fecha.set(v.getFecha() != null ? v.getFecha() : "");
        resultado.set(v.getResultado() != null ? v.getResultado() : "");
        encargado.set(v.getEncargado() != null ? v.getEncargado() : "");
        observaciones.set(v.getObservaciones() != null ? v.getObservaciones() : "");
    }

    // --------------------------
    // CREATE OR UPDATE (Actualizar)
    // --------------------------
    public void createOrUpdate() {
        // Validación
        if (!validarCampos()) {
            return;
        }

        VerificacionPago v = new VerificacionPago(
                id.get(), idPago.get(), fecha.get(),
                resultado.get(), encargado.get(), observaciones.get()
        );

        VerificacionPago old = factory.buscarVerificacion(id.get());
        if (old == null) {
            // Crear nueva verificación
            factory.newVerificacion(v);
            mostrarAlerta("Éxito", "Verificación creada correctamente", Alert.AlertType.INFORMATION);
        } else {
            // Actualizar verificación existente
            factory.actualizarVerificacion(v);
            mostrarAlerta("Éxito", "Verificación actualizada correctamente", Alert.AlertType.INFORMATION);
        }
    }

    // --------------------------
    // DELETE (Eliminar)
    // --------------------------
    public void delete() {
        if (id.get() == null || id.get().isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar un ID para eliminar", Alert.AlertType.ERROR);
            return;
        }

        VerificacionPago v = factory.buscarVerificacion(id.get());
        if (v == null) {
            mostrarAlerta("Advertencia", "No existe verificación con ese ID", Alert.AlertType.WARNING);
            return;
        }

        factory.eliminarVerificacion(id.get());
        mostrarAlerta("Éxito", "Verificación eliminada correctamente", Alert.AlertType.INFORMATION);
        limpiarPantalla();
    }

    // --------------------------
    // SEARCH (Buscar)
    // --------------------------
    public void search() {
        if (id.get() == null || id.get().isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar un ID para buscar", Alert.AlertType.ERROR);
            return;
        }

        VerificacionPago v = factory.buscarVerificacion(id.get());
        if (v == null) {
            mostrarAlerta("Información", "No se encontró verificación con ID: " + id.get(),
                    Alert.AlertType.INFORMATION);
            return;
        }
        updateViewModel(v);
    }

    // --------------------------
    // CLEAR FORM (Limpiar Pantalla)
    // --------------------------
    public void limpiarPantalla() {
        id.set("");
        idPago.set("");
        fecha.set("");
        resultado.set("");
        encargado.set("");
        observaciones.set("");
    }

    // --------------------------
    // VALIDACIÓN
    // --------------------------
    private boolean validarCampos() {
        if (id.get() == null || id.get().trim().isEmpty()) {
            mostrarAlerta("Error de Validación", "El ID es obligatorio", Alert.AlertType.ERROR);
            return false;
        }
        if (idPago.get() == null || idPago.get().trim().isEmpty()) {
            mostrarAlerta("Error de Validación", "El ID Pago es obligatorio", Alert.AlertType.ERROR);
            return false;
        }
        if (fecha.get() == null || fecha.get().trim().isEmpty()) {
            mostrarAlerta("Error de Validación", "La fecha es obligatoria", Alert.AlertType.ERROR);
            return false;
        }
        if (resultado.get() == null || resultado.get().trim().isEmpty()) {
            mostrarAlerta("Error de Validación", "El resultado es obligatorio", Alert.AlertType.ERROR);
            return false;
        }
        if (encargado.get() == null || encargado.get().trim().isEmpty()) {
            mostrarAlerta("Error de Validación", "El encargado es obligatorio", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    // --------------------------
    // ALERTAS
    // --------------------------
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
