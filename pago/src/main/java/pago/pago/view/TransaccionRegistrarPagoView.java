package pago.pago.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pago.pago.domain.Cliente;
import pago.pago.domain.Pago;
import pago.pago.domain.Reserva;
import pago.pago.service.PagoService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransaccionRegistrarPagoView {

    // PASO 1: Cliente
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private Label lblCedula, lblEmail, lblTelefono;

    // PASO 2: Reserva
    @FXML private ComboBox<Reserva> cmbReserva;
    @FXML private Label lblCodigoReserva, lblFechaReserva, lblTotalReserva, lblEstadoReserva;

    // PASO 3: Pago
    @FXML private TextField txtIdPago, txtFechaPago, txtValorPago, txtReferencia;
    @FXML private ComboBox<String> cmbTipoPago;
    @FXML private TextArea txtObservaciones;
    @FXML private Button btnFechaHoy, btnRegistrarPago, btnLimpiar;

    // Columna derecha
    @FXML private Label lblResumenCliente, lblResumenReserva, lblResumenTotal;
    @FXML private Label lblResumenIdPago, lblResumenTipo, lblResumenValor, lblResumenFecha, lblResumenEstado;
    @FXML private TextArea txtLog;
    @FXML private Button btnVerVerificaciones, btnVerConsultas;

    private PagoService pagoService;
    private Cliente clienteSeleccionado;
    private Reserva reservaSeleccionada;

    @FXML
    public void initialize() {
        pagoService = new PagoService();

        cargarDatosQuemados();
        configurarCombos();
        configurarEventos();

        agregarLog("Sistema inicializado");
        agregarLog("Esperando datos del cliente y reserva...");
    }

    private void cargarDatosQuemados() {
        // Clientes de ejemplo
        Cliente cliente1 = new Cliente("CLI-001", "1234567890", "Juan", "Pérez",
                "juan.perez@email.com", "3001234567");
        Cliente cliente2 = new Cliente("CLI-002", "0987654321", "María", "González",
                "maria.gonzalez@email.com", "3107654321");
        Cliente cliente3 = new Cliente("CLI-003", "1122334455", "Carlos", "Rodríguez",
                "carlos.rodriguez@email.com", "3209876543");

        cmbCliente.getItems().addAll(cliente1, cliente2, cliente3);

        // Reservas de ejemplo (asociadas a clientes)
        Reserva reserva1 = new Reserva("RSV-001", "RES-2025-001", "18/11/2025 14:30",
                "PENDIENTE DE PAGO", "450000", "CLI-001");
        Reserva reserva2 = new Reserva("RSV-004", "RES-2025-004", "20/11/2025 10:00",
                "PENDIENTE DE PAGO", "320000", "CLI-002");
        Reserva reserva3 = new Reserva("RSV-005", "RES-2025-005", "20/11/2025 15:30",
                "PENDIENTE DE PAGO", "580000", "CLI-003");

        cmbReserva.getItems().addAll(reserva1, reserva2, reserva3);

        agregarLog("Datos quemados cargados: 3 clientes, 3 reservas pendientes");
    }

    private void configurarCombos() {
        // Configurar ComboBox de Cliente
        cmbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente c) {
                return c != null ? c.getNombreCompleto() + " (" + c.getCedula() + ")" : "";
            }

            @Override
            public Cliente fromString(String string) {
                return null;
            }
        });

        // Configurar ComboBox de Reserva
        cmbReserva.setConverter(new StringConverter<Reserva>() {
            @Override
            public String toString(Reserva r) {
                return r != null ? r.getId() + " - " + r.getCodigo() + " ($" + r.getTotal() + ")" : "";
            }

            @Override
            public Reserva fromString(String string) {
                return null;
            }
        });

        // Configurar ComboBox de Tipo de Pago
        cmbTipoPago.getItems().addAll("Transferencia", "Efectivo", "Tarjeta");
    }

    private void configurarEventos() {
        // Evento al seleccionar cliente
        cmbCliente.setOnAction(e -> {
            clienteSeleccionado = cmbCliente.getValue();
            if (clienteSeleccionado != null) {
                lblCedula.setText(clienteSeleccionado.getCedula());
                lblEmail.setText(clienteSeleccionado.getEmail());
                lblTelefono.setText(clienteSeleccionado.getTelefono());
                lblResumenCliente.setText(clienteSeleccionado.getNombreCompleto());

                // Filtrar reservas del cliente seleccionado
                cmbReserva.getItems().clear();
                cargarReservasDelCliente(clienteSeleccionado.getId());

                agregarLog("Cliente seleccionado: " + clienteSeleccionado.getNombreCompleto());
            }
        });

        // Evento al seleccionar reserva
        cmbReserva.setOnAction(e -> {
            reservaSeleccionada = cmbReserva.getValue();
            if (reservaSeleccionada != null) {
                lblCodigoReserva.setText(reservaSeleccionada.getCodigo());
                lblFechaReserva.setText(reservaSeleccionada.getFecha());
                lblTotalReserva.setText("$" + reservaSeleccionada.getTotal());
                lblEstadoReserva.setText(reservaSeleccionada.getEstado());

                lblResumenReserva.setText(reservaSeleccionada.getId());
                lblResumenTotal.setText("$" + reservaSeleccionada.getTotal());

                // Auto-completar valor del pago
                txtValorPago.setText(reservaSeleccionada.getTotal());
                lblResumenValor.setText("$" + reservaSeleccionada.getTotal());

                agregarLog("Reserva seleccionada: " + reservaSeleccionada.getId() +
                        " (Total: $" + reservaSeleccionada.getTotal() + ")");
            }
        });

        // Eventos de actualización en tiempo real
        txtIdPago.textProperty().addListener((obs, old, newVal) ->
                lblResumenIdPago.setText(newVal.isEmpty() ? "-" : newVal));

        cmbTipoPago.valueProperty().addListener((obs, old, newVal) ->
                lblResumenTipo.setText(newVal == null ? "-" : newVal));

        txtValorPago.textProperty().addListener((obs, old, newVal) ->
                lblResumenValor.setText(newVal.isEmpty() ? "$0" : "$" + newVal));

        txtFechaPago.textProperty().addListener((obs, old, newVal) ->
                lblResumenFecha.setText(newVal.isEmpty() ? "-" : newVal));

        // Botón fecha hoy
        btnFechaHoy.setOnAction(e -> {
            String hoy = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            txtFechaPago.setText(hoy);
        });

        // Botón principal
        btnRegistrarPago.setOnAction(e -> registrarPago());
        btnLimpiar.setOnAction(e -> limpiarFormulario());

        // Botones secundarios
        btnVerVerificaciones.setOnAction(e -> abrirCrud());
        btnVerConsultas.setOnAction(e -> abrirConsultas());
    }

    private void cargarReservasDelCliente(String idCliente) {
        // Buscar reservas del cliente (simulado)
        Reserva reserva1 = new Reserva("RSV-001", "RES-2025-001", "18/11/2025 14:30",
                "PENDIENTE DE PAGO", "450000", idCliente);
        Reserva reserva2 = new Reserva("RSV-006", "RES-2025-006", "19/11/2025 09:00",
                "PENDIENTE DE PAGO", "275000", idCliente);

        cmbReserva.getItems().addAll(reserva1, reserva2);
        agregarLog("Reservas encontradas para el cliente: 2");
    }

    private void registrarPago() {
        agregarLog("═══════════════════════════════════════");
        agregarLog("INICIANDO REGISTRO DE PAGO");
        agregarLog("═══════════════════════════════════════");

        // Validaciones
        if (clienteSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un cliente", Alert.AlertType.ERROR);
            agregarLog("✗ ERROR: Cliente no seleccionado");
            return;
        }

        if (reservaSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar una reserva", Alert.AlertType.ERROR);
            agregarLog("✗ ERROR: Reserva no seleccionada");
            return;
        }

        if (txtIdPago.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar el ID del pago", Alert.AlertType.ERROR);
            agregarLog("✗ ERROR: ID de pago vacío");
            return;
        }

        if (cmbTipoPago.getValue() == null) {
            mostrarAlerta("Error", "Debe seleccionar el tipo de pago", Alert.AlertType.ERROR);
            agregarLog("✗ ERROR: Tipo de pago no seleccionado");
            return;
        }

        if (txtFechaPago.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar la fecha del pago", Alert.AlertType.ERROR);
            agregarLog("✗ ERROR: Fecha de pago vacía");
            return;
        }

        if (txtValorPago.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar el valor del pago", Alert.AlertType.ERROR);
            agregarLog("✗ ERROR: Valor del pago vacío");
            return;
        }

        // Validar que el valor sea numérico
        try {
            Double.parseDouble(txtValorPago.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El valor del pago debe ser un número válido", Alert.AlertType.ERROR);
            agregarLog("✗ ERROR: Valor del pago inválido");
            return;
        }

        agregarLog("✓ Validaciones completadas");

        // Obtener datos del formulario
        String idPago = txtIdPago.getText().trim();
        String tipo = cmbTipoPago.getValue();
        String fecha = txtFechaPago.getText().trim();
        String valor = txtValorPago.getText().trim();
        String idReserva = reservaSeleccionada.getId();

        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        );

        agregarLog("[" + timestamp + "] Registrando pago...");
        agregarLog("Cliente: " + clienteSeleccionado.getNombreCompleto());
        agregarLog("Reserva: " + reservaSeleccionada.getId() + " ($" + reservaSeleccionada.getTotal() + ")");

        try {
            // PASO 1: Crear el objeto Pago
            Pago nuevoPago = new Pago(idPago, tipo, fecha, "PENDIENTE", valor, idReserva);
            agregarLog("✓ Objeto Pago creado");

            // PASO 2: Validar datos del pago con el servicio
            String resultadoValidacion = pagoService.validarDatosPago(nuevoPago);
            if (resultadoValidacion.startsWith("ERRORES")) {
                mostrarAlerta("Error de Validación", resultadoValidacion, Alert.AlertType.ERROR);
                agregarLog("✗ Validación de datos falló");
                return;
            }
            agregarLog("✓ Datos del pago validados correctamente");

            // PASO 3: Verificar que el monto coincida con la reserva
            if (!valor.equals(reservaSeleccionada.getTotal())) {
                agregarLog("⚠ ADVERTENCIA: El monto del pago ($" + valor +
                        ") no coincide con el total de la reserva ($" +
                        reservaSeleccionada.getTotal() + ")");

                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Advertencia");
                confirmacion.setHeaderText("Los montos no coinciden");
                confirmacion.setContentText("¿Desea continuar de todos modos?");

                if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                    agregarLog("✗ Registro cancelado por el usuario");
                    return;
                }
            } else {
                agregarLog("✓ Monto verificado: coincide con la reserva");
            }

            // PASO 4: Agregar el pago al servicio
            pagoService.agregarPago(nuevoPago);
            agregarLog("✓ Pago agregado al sistema: " + idPago);

            // PASO 5: Actualizar estado de la reserva (simulado)
            String estadoAnterior = reservaSeleccionada.getEstado();
            reservaSeleccionada.setEstado("PENDIENTE DE PAGO");
            agregarLog("✓ Estado de reserva actualizado: " + estadoAnterior +
                    " → PENDIENTE DE PAGO");

            // PASO 6: Asociar pago con reserva
            reservaSeleccionada.setIdPago(idPago);
            agregarLog("✓ Pago asociado a la reserva");

            // PASO 7: Registrar en log de auditoría
            agregarLog("✓ Registro de auditoría completado");

            // PASO 8: Simular notificación al cliente
            agregarLog("✓ Notificación enviada a: " + clienteSeleccionado.getEmail());
            agregarLog("   Asunto: 'Pago registrado - Pendiente de verificación'");

            agregarLog("═══════════════════════════════════════");
            agregarLog("REGISTRO DE PAGO COMPLETADO EXITOSAMENTE");
            agregarLog("═══════════════════════════════════════");
            agregarLog("ID Pago: " + idPago);
            agregarLog("Estado: PENDIENTE (Requiere verificación)");
            agregarLog("Reserva: " + idReserva);
            agregarLog("═══════════════════════════════════════");

            // Mostrar resumen
            mostrarAlerta("Registro Exitoso",
                    "El pago ha sido registrado correctamente.\n\n" +
                            "ID Pago: " + idPago + "\n" +
                            "Estado: PENDIENTE\n" +
                            "Valor: $" + valor + "\n" +
                            "Reserva: " + idReserva + "\n\n" +
                            "⚠️ IMPORTANTE:\n" +
                            "El pago quedó en estado PENDIENTE.\n" +
                            "Requiere verificación posterior para ser aprobado.\n" +
                            "Use el módulo 'Verificaciones' (CRUD) para verificar y aprobar el pago.",
                    Alert.AlertType.INFORMATION);

            // Limpiar formulario
            limpiarFormulario();

        } catch (Exception e) {
            agregarLog("✗ ERROR en el registro: " + e.getMessage());
            mostrarAlerta("Error", "Ocurrió un error al registrar el pago: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private void limpiarFormulario() {
        // Limpiar selecciones
        cmbCliente.getSelectionModel().clearSelection();
        cmbReserva.getSelectionModel().clearSelection();
        cmbTipoPago.getSelectionModel().clearSelection();

        // Limpiar labels de cliente
        lblCedula.setText("-");
        lblEmail.setText("-");
        lblTelefono.setText("-");

        // Limpiar labels de reserva
        lblCodigoReserva.setText("-");
        lblFechaReserva.setText("-");
        lblTotalReserva.setText("-");
        lblEstadoReserva.setText("-");

        // Limpiar campos de pago
        txtIdPago.clear();
        txtFechaPago.clear();
        txtValorPago.clear();
        txtReferencia.clear();
        txtObservaciones.clear();

        // Limpiar resumen
        lblResumenCliente.setText("No seleccionado");
        lblResumenReserva.setText("No seleccionada");
        lblResumenTotal.setText("$0");
        lblResumenIdPago.setText("-");
        lblResumenTipo.setText("-");
        lblResumenValor.setText("$0");
        lblResumenFecha.setText("-");
        lblResumenEstado.setText("PENDIENTE");

        // Limpiar referencias
        clienteSeleccionado = null;
        reservaSeleccionada = null;

        agregarLog("Formulario limpiado - Sistema listo para nuevo registro");
    }

    private void abrirCrud() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pago/pago/VerificacionPagoView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("CRUD - Verificaciones de Pago");
            stage.show();

            agregarLog("Abriendo módulo de Verificaciones (CRUD)...");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir el CRUD: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void abrirConsultas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pago/pago/ConsultasView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Consultas y Funcionalidades Avanzadas");
            stage.show();

            agregarLog("Abriendo módulo de Consultas...");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir Consultas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void agregarLog(String mensaje) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        txtLog.appendText("[" + timestamp + "] " + mensaje + "\n");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}