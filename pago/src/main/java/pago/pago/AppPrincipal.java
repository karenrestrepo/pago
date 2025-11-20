package pago.pago;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class AppPrincipal extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear menú principal
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #2196F3, #1976D2);");

        // Título
        Label titulo = new Label("SISTEMA DE VERIFICACIÓN DE PAGOS");
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitulo = new Label("Proceso: Reservar Producto | Actividad: Verificar el Pago");
        subtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #E3F2FD;");

        // Botón para Transacción
        Button btnTransaccion = new Button("🔄 TRANSACCIÓN: Registrar Pago");
        btnTransaccion.setPrefSize(400, 80);
        btnTransaccion.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );
        btnTransaccion.setOnMouseEntered(e ->
                btnTransaccion.setStyle(
                        "-fx-font-size: 18px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-color: #45a049; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand; " +
                                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);"
                )
        );
        btnTransaccion.setOnMouseExited(e ->
                btnTransaccion.setStyle(
                        "-fx-font-size: 18px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                )
        );

        // Botón para CRUD
        Button btnCrud = new Button("📋 CRUD: Gestión de Verificaciones");
        btnCrud.setPrefSize(400, 80);
        btnCrud.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );
        btnCrud.setOnMouseEntered(e ->
                btnCrud.setStyle(
                        "-fx-font-size: 18px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-color: #1976D2; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand; " +
                                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);"
                )
        );
        btnCrud.setOnMouseExited(e ->
                btnCrud.setStyle(
                        "-fx-font-size: 18px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-color: #2196F3; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                )
        );

        // Botón para Consultas
        Button btnConsultas = new Button("📊 CONSULTAS: Funcionalidades Avanzadas");
        btnConsultas.setPrefSize(400, 80);
        btnConsultas.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #FF9800; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );
        btnConsultas.setOnMouseEntered(e ->
                btnConsultas.setStyle(
                        "-fx-font-size: 18px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-color: #F57C00; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand; " +
                                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);"
                )
        );
        btnConsultas.setOnMouseExited(e ->
                btnConsultas.setStyle(
                        "-fx-font-size: 18px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-color: #FF9800; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                )
        );

        // Footer
        Label footer = new Label("Karen Vanessa Restrepo Morales - Ingeniería de Software 2025");
        footer.setStyle("-fx-font-size: 12px; -fx-text-fill: #E3F2FD; -fx-padding: 20 0 0 0;");

        root.getChildren().addAll(titulo, subtitulo, btnTransaccion, btnCrud, btnConsultas, footer);

        // Eventos
        btnTransaccion.setOnAction(e -> abrirTransaccion());
        btnCrud.setOnAction(e -> abrirCrud());
        btnConsultas.setOnAction(e -> abrirConsultas());

        Scene scene = new Scene(root, 600, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sistema de Verificación de Pagos - Menú Principal");
        primaryStage.show();
    }

    private void abrirTransaccion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TransaccionRegistrarPagoView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Transacción: Registrar Pago");
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error al abrir transacción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirCrud() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VerificacionPagoView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("CRUD: Gestión de Verificaciones");
            stage.show();
        } catch (Exception e) {
            System.err.println("Error al abrir CRUD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirConsultas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConsultasView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Consultas y Funcionalidades Avanzadas");
            stage.show();
        } catch (Exception e) {
            System.err.println("Error al abrir consultas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}