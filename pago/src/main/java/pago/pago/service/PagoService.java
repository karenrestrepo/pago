package pago.pago.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pago.pago.domain.Pago;
import pago.pago.domain.VerificacionPago;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión completa de pagos y sus verificaciones
 */
public class PagoService {

    private ObservableList<Pago> lstPagos = FXCollections.observableArrayList();
    private ObservableList<VerificacionPago> lstVerificaciones = FXCollections.observableArrayList();

    // Datos de ejemplo para pruebas
    public PagoService() {
        cargarDatosPrueba();
    }

    // ============================================
    // FUNCIONALIDAD 1: Consultar Estado del Pago
    // ============================================

    /**
     * Consulta el estado actual de un pago por su ID
     * @param idPago ID del pago a consultar
     * @return String con el estado o mensaje de error
     */
    public String consultarEstadoPago(String idPago) {
        Pago pago = buscarPago(idPago);
        if (pago == null) {
            return "ERROR: Pago no encontrado";
        }
        return pago.getEstado();
    }

    /**
     * Obtiene información detallada del estado del pago
     * @param idPago ID del pago
     * @return String con información completa
     */
    public String obtenerInformacionPago(String idPago) {
        Pago pago = buscarPago(idPago);
        if (pago == null) {
            return "Pago no encontrado en el sistema";
        }

        StringBuilder info = new StringBuilder();
        info.append("=== INFORMACIÓN DEL PAGO ===\n");
        info.append("ID: ").append(pago.getId()).append("\n");
        info.append("Tipo: ").append(pago.getTipo()).append("\n");
        info.append("Fecha: ").append(pago.getFecha()).append("\n");
        info.append("Estado: ").append(pago.getEstado()).append("\n");
        info.append("Valor: $").append(pago.getValor()).append("\n");
        info.append("ID Reserva: ").append(pago.getIdReserva()).append("\n");

        // Verificar si tiene verificaciones asociadas
        long numVerificaciones = lstVerificaciones.stream()
                .filter(v -> v.getIdPago().equals(idPago))
                .count();
        info.append("Verificaciones realizadas: ").append(numVerificaciones).append("\n");

        return info.toString();
    }

    // ============================================
    // FUNCIONALIDAD 2: Actualizar Estado del Pago
    // ============================================

    /**
     * Actualiza el estado de un pago existente
     * @param idPago ID del pago
     * @param nuevoEstado Nuevo estado (PENDIENTE, VERIFICADO, RECHAZADO)
     * @return true si se actualizó correctamente
     */
    public boolean actualizarEstadoPago(String idPago, String nuevoEstado) {
        Pago pago = buscarPago(idPago);
        if (pago == null) {
            return false;
        }

        // Validar que el nuevo estado sea válido
        if (!esEstadoValido(nuevoEstado)) {
            return false;
        }

        pago.setEstado(nuevoEstado);

        // Registrar el cambio en el log (simulado)
        registrarCambioEstado(idPago, nuevoEstado);

        return true;
    }

    private boolean esEstadoValido(String estado) {
        return estado.equals("PENDIENTE") ||
                estado.equals("VERIFICADO") ||
                estado.equals("RECHAZADO");
    }

    private void registrarCambioEstado(String idPago, String nuevoEstado) {
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        );
        System.out.println("[LOG] " + timestamp + " - Pago " + idPago +
                " cambió a estado: " + nuevoEstado);
    }

    // ============================================
    // FUNCIONALIDAD 3: Validar Datos del Pago
    // ============================================

    /**
     * Valida que los datos del pago cumplan con las reglas de negocio
     * @param pago Objeto Pago a validar
     * @return String con el resultado de la validación
     */
    public String validarDatosPago(Pago pago) {
        StringBuilder errores = new StringBuilder();

        // Validar ID
        if (pago.getId() == null || pago.getId().trim().isEmpty()) {
            errores.append("- El ID del pago es obligatorio\n");
        }

        // Validar tipo
        if (pago.getTipo() == null || pago.getTipo().trim().isEmpty()) {
            errores.append("- El tipo de pago es obligatorio\n");
        } else if (!esTipoPagoValido(pago.getTipo())) {
            errores.append("- El tipo de pago no es válido. Use: Transferencia, Efectivo, Tarjeta\n");
        }

        // Validar valor
        if (pago.getValor() == null || pago.getValor().trim().isEmpty()) {
            errores.append("- El valor del pago es obligatorio\n");
        } else {
            try {
                double valor = Double.parseDouble(pago.getValor());
                if (valor <= 0) {
                    errores.append("- El valor del pago debe ser mayor a cero\n");
                }
            } catch (NumberFormatException e) {
                errores.append("- El valor del pago debe ser un número válido\n");
            }
        }

        // Validar fecha
        if (pago.getFecha() == null || pago.getFecha().trim().isEmpty()) {
            errores.append("- La fecha del pago es obligatoria\n");
        }

        // Validar ID Reserva
        if (pago.getIdReserva() == null || pago.getIdReserva().trim().isEmpty()) {
            errores.append("- El ID de reserva asociado es obligatorio\n");
        }

        if (errores.length() == 0) {
            return "VALIDACIÓN EXITOSA: Todos los datos son correctos";
        } else {
            return "ERRORES DE VALIDACIÓN:\n" + errores.toString();
        }
    }

    private boolean esTipoPagoValido(String tipo) {
        return tipo.equals("Transferencia") ||
                tipo.equals("Efectivo") ||
                tipo.equals("Tarjeta");
    }

    // ============================================
    // FUNCIONALIDAD 4: Historial de Pagos
    // ============================================

    /**
     * Obtiene el historial de todos los pagos de una reserva
     * @param idReserva ID de la reserva
     * @return Lista observable de pagos
     */
    public ObservableList<Pago> obtenerHistorialPagos(String idReserva) {
        return lstPagos.stream()
                .filter(p -> p.getIdReserva().equals(idReserva))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Obtiene un resumen del historial de pagos
     * @param idReserva ID de la reserva
     * @return String con el resumen
     */
    public String obtenerResumenHistorial(String idReserva) {
        ObservableList<Pago> pagos = obtenerHistorialPagos(idReserva);

        if (pagos.isEmpty()) {
            return "No hay pagos registrados para la reserva " + idReserva;
        }

        StringBuilder resumen = new StringBuilder();
        resumen.append("=== HISTORIAL DE PAGOS - Reserva ").append(idReserva).append(" ===\n\n");

        for (Pago p : pagos) {
            resumen.append("Pago: ").append(p.getId()).append("\n");
            resumen.append("  Fecha: ").append(p.getFecha()).append("\n");
            resumen.append("  Tipo: ").append(p.getTipo()).append("\n");
            resumen.append("  Valor: $").append(p.getValor()).append("\n");
            resumen.append("  Estado: ").append(p.getEstado()).append("\n");
            resumen.append("  ---\n");
        }

        return resumen.toString();
    }

    // ============================================
    // FUNCIONALIDAD 5: Consultar Verificaciones con Filtros
    // ============================================

    /**
     * Consulta verificaciones filtradas por fecha
     * @param fecha Fecha a filtrar (formato: dd/MM/yyyy)
     * @return Lista de verificaciones
     */
    public ObservableList<VerificacionPago> consultarVerificacionesPorFecha(String fecha) {
        return lstVerificaciones.stream()
                .filter(v -> v.getFecha().contains(fecha))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Consulta verificaciones filtradas por encargado
     * @param encargado Nombre del encargado
     * @return Lista de verificaciones
     */
    public ObservableList<VerificacionPago> consultarVerificacionesPorEncargado(String encargado) {
        return lstVerificaciones.stream()
                .filter(v -> v.getEncargado().toLowerCase().contains(encargado.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Consulta verificaciones filtradas por resultado
     * @param resultado Estado del resultado (Aprobado/Rechazado)
     * @return Lista de verificaciones
     */
    public ObservableList<VerificacionPago> consultarVerificacionesPorResultado(String resultado) {
        return lstVerificaciones.stream()
                .filter(v -> v.getResultado().equalsIgnoreCase(resultado))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Consulta verificaciones con múltiples filtros
     * @param fecha Fecha (puede ser null)
     * @param encargado Encargado (puede ser null)
     * @param resultado Resultado (puede ser null)
     * @return Lista filtrada
     */
    public ObservableList<VerificacionPago> consultarVerificacionesConFiltros(
            String fecha, String encargado, String resultado) {

        return lstVerificaciones.stream()
                .filter(v -> (fecha == null || fecha.isEmpty() || v.getFecha().contains(fecha)))
                .filter(v -> (encargado == null || encargado.isEmpty() ||
                        v.getEncargado().toLowerCase().contains(encargado.toLowerCase())))
                .filter(v -> (resultado == null || resultado.isEmpty() ||
                        v.getResultado().equalsIgnoreCase(resultado)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    // ============================================
    // MÉTODOS CRUD BÁSICOS
    // ============================================

    public ObservableList<Pago> getLstPagos() {
        return lstPagos;
    }

    public void agregarPago(Pago pago) {
        lstPagos.add(pago);
    }

    public void actualizarPago(Pago pago) {
        for (int i = 0; i < lstPagos.size(); i++) {
            if (lstPagos.get(i).getId().equals(pago.getId())) {
                lstPagos.set(i, pago);
                return;
            }
        }
    }

    public void eliminarPago(String id) {
        lstPagos.removeIf(p -> p.getId().equals(id));
    }

    public Pago buscarPago(String id) {
        return lstPagos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // ============================================
    // GESTIÓN DE VERIFICACIONES
    // ============================================

    public void agregarVerificacion(VerificacionPago verificacion) {
        lstVerificaciones.add(verificacion);
    }

    public ObservableList<VerificacionPago> getLstVerificaciones() {
        return lstVerificaciones;
    }

    // ============================================
    // DATOS DE PRUEBA
    // ============================================

    private void cargarDatosPrueba() {
        // Pagos de ejemplo
        lstPagos.add(new Pago("PAG-001", "Transferencia", "18/11/2025",
                "PENDIENTE", "450000", "RSV-001"));
        lstPagos.add(new Pago("PAG-002", "Efectivo", "17/11/2025",
                "VERIFICADO", "230000", "RSV-002"));
        lstPagos.add(new Pago("PAG-003", "Tarjeta", "19/11/2025",
                "RECHAZADO", "680000", "RSV-003"));

        // Verificaciones de ejemplo
        lstVerificaciones.add(new VerificacionPago("VER-001", "PAG-002",
                "17/11/2025 14:30", "Aprobado", "María González",
                "Pago verificado correctamente"));
        lstVerificaciones.add(new VerificacionPago("VER-002", "PAG-003",
                "19/11/2025 10:15", "Rechazado", "Juan Pérez",
                "Monto no coincide con reserva"));
    }
}