package pago.pago.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pago.pago.domain.Pago;
import pago.pago.domain.VerificacionPago;
import pago.pago.service.PagoService;

public class ConsultasView {

    // TAB 1: Consultar Estado
    @FXML private TextField txtConsultaIdPago;
    @FXML private Button btnConsultarEstado;
    @FXML private TextArea txtResultadoConsulta;

    // TAB 2: Actualizar Estado
    @FXML private TextField txtActualizarIdPago;
    @FXML private ComboBox<String> cmbNuevoEstado;
    @FXML private Button btnActualizarEstado;
    @FXML private TextArea txtResultadoActualizar;

    // TAB 3: Validar Datos
    @FXML private TextField txtValidarId, txtValidarValor, txtValidarFecha, txtValidarReserva;
    @FXML private ComboBox<String> cmbValidarTipo;
    @FXML private Button btnValidarDatos;
    @FXML private TextArea txtResultadoValidacion;

    // TAB 4: Historial
    @FXML private TextField txtHistorialReserva;
    @FXML private Button btnMostrarHistorial;
    @FXML private TableView<Pago> tablaHistorial;
    @FXML private TableColumn<Pago, String> colHistorialId, colHistorialFecha, colHistorialTipo,
            colHistorialValor, colHistorialEstado;
    @FXML private TextArea txtResumenHistorial;

    // TAB 5: Filtrar Verificaciones
    @FXML private TextField txtFiltroFecha, txtFiltroEncargado;
    @FXML private ComboBox<String> cmbFiltroResultado;
    @FXML private Button btnFiltrar, btnLimpiarFiltros;
    @FXML private TableView<VerificacionPago> tablaVerificaciones;
    @FXML private TableColumn<VerificacionPago, String> colVerifId, colVerifIdPago, colVerifFecha,
            colVerifResultado, colVerifEncargado, colVerifObs;

    private PagoService pagoService;

    @FXML
    public void initialize() {
        pagoService = new PagoService();

        configurarTab1();
        configurarTab2();
        configurarTab3();
        configurarTab4();
        configurarTab5();
    }

    // ========================================
    // TAB 1: CONSULTAR ESTADO DEL PAGO
    // ========================================
    private void configurarTab1() {
        btnConsultarEstado.setOnAction(e -> consultarEstadoPago());
    }

    private void consultarEstadoPago() {
        String idPago = txtConsultaIdPago.getText().trim();

        if (idPago.isEmpty()) {
            txtResultadoConsulta.setText("ERROR: Debe ingresar un ID de pago");
            return;
        }

        txtResultadoConsulta.clear();
        txtResultadoConsulta.appendText("═══════════════════════════════════════\n");
        txtResultadoConsulta.appendText("  CONSULTANDO ESTADO DEL PAGO\n");
        txtResultadoConsulta.appendText("═══════════════════════════════════════\n\n");

        // Usar el servicio
        String info = pagoService.obtenerInformacionPago(idPago);
        txtResultadoConsulta.appendText(info);

        txtResultadoConsulta.appendText("\n═══════════════════════════════════════\n");
        txtResultadoConsulta.appendText("Consulta completada\n");
    }

    // ========================================
    // TAB 2: ACTUALIZAR ESTADO DEL PAGO
    // ========================================
    private void configurarTab2() {
        cmbNuevoEstado.getItems().addAll("PENDIENTE", "VERIFICADO", "RECHAZADO");
        btnActualizarEstado.setOnAction(e -> actualizarEstadoPago());
    }

    private void actualizarEstadoPago() {
        String idPago = txtActualizarIdPago.getText().trim();
        String nuevoEstado = cmbNuevoEstado.getValue();

        if (idPago.isEmpty() || nuevoEstado == null) {
            txtResultadoActualizar.setText("ERROR: Complete todos los campos");
            return;
        }

        txtResultadoActualizar.clear();
        txtResultadoActualizar.appendText("═══════════════════════════════════════\n");
        txtResultadoActualizar.appendText("  ACTUALIZANDO ESTADO DEL PAGO\n");
        txtResultadoActualizar.appendText("═══════════════════════════════════════\n\n");

        Pago pago = pagoService.buscarPago(idPago);

        if (pago == null) {
            txtResultadoActualizar.appendText("ERROR: Pago no encontrado\n");
            return;
        }

        String estadoAnterior = pago.getEstado();
        txtResultadoActualizar.appendText("Pago: " + idPago + "\n");
        txtResultadoActualizar.appendText("Estado anterior: " + estadoAnterior + "\n");
        txtResultadoActualizar.appendText("Nuevo estado: " + nuevoEstado + "\n\n");

        boolean exito = pagoService.actualizarEstadoPago(idPago, nuevoEstado);

        if (exito) {
            txtResultadoActualizar.appendText("✓ Estado actualizado correctamente\n");
            txtResultadoActualizar.appendText("\nCambio registrado:\n");
            txtResultadoActualizar.appendText(estadoAnterior + " → " + nuevoEstado + "\n");
        } else {
            txtResultadoActualizar.appendText("✗ Error al actualizar el estado\n");
        }

        txtResultadoActualizar.appendText("\n═══════════════════════════════════════\n");
    }

    // ========================================
    // TAB 3: VALIDAR DATOS DEL PAGO
    // ========================================
    private void configurarTab3() {
        cmbValidarTipo.getItems().addAll("Transferencia", "Efectivo", "Tarjeta");
        btnValidarDatos.setOnAction(e -> validarDatosPago());
    }

    private void validarDatosPago() {
        Pago pago = new Pago();
        pago.setId(txtValidarId.getText().trim());
        pago.setTipo(cmbValidarTipo.getValue());
        pago.setValor(txtValidarValor.getText().trim());
        pago.setFecha(txtValidarFecha.getText().trim());
        pago.setIdReserva(txtValidarReserva.getText().trim());
        pago.setEstado("PENDIENTE"); // No importa para validación

        txtResultadoValidacion.clear();
        txtResultadoValidacion.appendText("═══════════════════════════════════════\n");
        txtResultadoValidacion.appendText("  VALIDANDO DATOS DEL PAGO\n");
        txtResultadoValidacion.appendText("═══════════════════════════════════════\n\n");

        String resultado = pagoService.validarDatosPago(pago);
        txtResultadoValidacion.appendText(resultado);

        txtResultadoValidacion.appendText("\n═══════════════════════════════════════\n");
    }

    // ========================================
    // TAB 4: HISTORIAL DE PAGOS
    // ========================================
    private void configurarTab4() {
        // Configurar columnas de la tabla
        colHistorialId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colHistorialFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHistorialTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colHistorialValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colHistorialEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        btnMostrarHistorial.setOnAction(e -> mostrarHistorialPagos());
    }

    private void mostrarHistorialPagos() {
        String idReserva = txtHistorialReserva.getText().trim();

        if (idReserva.isEmpty()) {
            txtResumenHistorial.setText("ERROR: Debe ingresar un ID de reserva");
            return;
        }

        // Cargar historial en la tabla
        tablaHistorial.setItems(pagoService.obtenerHistorialPagos(idReserva));

        // Mostrar resumen
        String resumen = pagoService.obtenerResumenHistorial(idReserva);
        txtResumenHistorial.setText(resumen);
    }

    // ========================================
    // TAB 5: FILTRAR VERIFICACIONES
    // ========================================
    private void configurarTab5() {
        // Configurar columnas
        colVerifId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colVerifIdPago.setCellValueFactory(new PropertyValueFactory<>("idPago"));
        colVerifFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colVerifResultado.setCellValueFactory(new PropertyValueFactory<>("resultado"));
        colVerifEncargado.setCellValueFactory(new PropertyValueFactory<>("encargado"));
        colVerifObs.setCellValueFactory(new PropertyValueFactory<>("observaciones"));

        cmbFiltroResultado.getItems().addAll("Todos", "Aprobado", "Rechazado");
        cmbFiltroResultado.setValue("Todos");

        btnFiltrar.setOnAction(e -> filtrarVerificaciones());
        btnLimpiarFiltros.setOnAction(e -> limpiarFiltros());

        // Cargar todas las verificaciones al inicio
        tablaVerificaciones.setItems(pagoService.getLstVerificaciones());
    }

    private void filtrarVerificaciones() {
        String fecha = txtFiltroFecha.getText().trim();
        String encargado = txtFiltroEncargado.getText().trim();
        String resultado = cmbFiltroResultado.getValue();

        // Si resultado es "Todos", pasar null
        if ("Todos".equals(resultado)) {
            resultado = null;
        }

        // Aplicar filtros
        var resultados = pagoService.consultarVerificacionesConFiltros(
                fecha.isEmpty() ? null : fecha,
                encargado.isEmpty() ? null : encargado,
                resultado
        );

        tablaVerificaciones.setItems(resultados);
    }

    private void limpiarFiltros() {
        txtFiltroFecha.clear();
        txtFiltroEncargado.clear();
        cmbFiltroResultado.setValue("Todos");
        tablaVerificaciones.setItems(pagoService.getLstVerificaciones());
    }
}