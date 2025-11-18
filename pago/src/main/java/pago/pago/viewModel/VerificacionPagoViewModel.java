package pago.pago.viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import pago.pago.domain.VerificacionPago;
import pago.pago.service.FactoryPago;

public class VerificacionPagoViewModel {
    public StringProperty id = new SimpleStringProperty();
    public StringProperty idPago = new SimpleStringProperty();
    public StringProperty fecha = new SimpleStringProperty();
    public StringProperty resultado = new SimpleStringProperty();
    public StringProperty encargado = new SimpleStringProperty();
    public StringProperty observaciones = new SimpleStringProperty();

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
        if (v == null) return;
        id.set(v.getId());
        idPago.set(v.getIdPago());
        fecha.set(v.getFecha());
        resultado.set(v.getResultado());
        encargado.set(v.getEncargado());
        observaciones.set(v.getObservaciones());
    }

    // --------------------------
    // NEW / UPDATE
    // --------------------------
    public void createOrUpdate() {
        VerificacionPago v = new VerificacionPago(
                id.get(), idPago.get(), fecha.get(),
                resultado.get(), encargado.get(), observaciones.get()
        );

        VerificacionPago old = factory.buscarVerificacion(id.get());
        if (old == null)
            factory.newVerificacion(v);
        else
            factory.actualizarVerificacion(v);
    }

    // --------------------------
    // DELETE
    // --------------------------
    public void delete() {
        factory.eliminarVerificacion(id.get());
    }

    // --------------------------
    // SEARCH
    // --------------------------
    public void search() {
        VerificacionPago v = factory.buscarVerificacion(id.get());
        updateViewModel(v);
    }

    // --------------------------
    // CLEAR FORM
    // --------------------------
    public void limpiarPantalla() {
        id.set("");
        idPago.set("");
        fecha.set("");
        resultado.set("");
        encargado.set("");
        observaciones.set("");
    }
}
