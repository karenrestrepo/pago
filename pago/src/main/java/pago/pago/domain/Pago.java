package pago.pago.domain;

public class Pago {
    private String id;
    private String tipo;
    private String fecha;
    private String estado;
    private String valor;
    private String idReserva;

    public Pago() {}

    public Pago(String id, String tipo, String fecha, String estado, String valor, String idReserva) {
        this.id = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.estado = estado;
        this.valor = valor;
        this.idReserva = idReserva;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public String getIdReserva() { return idReserva; }
    public void setIdReserva(String idReserva) { this.idReserva = idReserva; }
}
