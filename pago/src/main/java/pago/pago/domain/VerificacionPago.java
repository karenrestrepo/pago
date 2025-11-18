package pago.pago.domain;

public class VerificacionPago {

    private String id;
    private String idPago;
    private String fecha;
    private String resultado;
    private String encargado;
    private String observaciones;

    public VerificacionPago() {}

    public VerificacionPago(String id, String idPago, String fecha,
                            String resultado, String encargado, String observaciones) {
        this.id = id;
        this.idPago = idPago;
        this.fecha = fecha;
        this.resultado = resultado;
        this.encargado = encargado;
        this.observaciones = observaciones;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdPago() { return idPago; }
    public void setIdPago(String idPago) { this.idPago = idPago; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public String getEncargado() { return encargado; }
    public void setEncargado(String encargado) { this.encargado = encargado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
