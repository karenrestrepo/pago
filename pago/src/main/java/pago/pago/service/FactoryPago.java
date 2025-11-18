package pago.pago.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pago.pago.domain.VerificacionPago;

public class FactoryPago {
    private ObservableList<VerificacionPago> lstVerificaciones =
            FXCollections.observableArrayList();

    public ObservableList<VerificacionPago> getLstVerificaciones() {
        return lstVerificaciones;
    }

    // NEW
    public void newVerificacion(VerificacionPago v) {
        lstVerificaciones.add(v);
    }

    // UPDATE
    public void actualizarVerificacion(VerificacionPago v) {
        for (int i = 0; i < lstVerificaciones.size(); i++) {
            if (lstVerificaciones.get(i).getId().equals(v.getId())) {
                lstVerificaciones.set(i, v);
                return;
            }
        }
    }

    // DELETE
    public void eliminarVerificacion(String id) {
        lstVerificaciones.removeIf(v -> v.getId().equals(id));
    }

    // SEARCH
    public VerificacionPago buscarVerificacion(String id) {
        return lstVerificaciones.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
