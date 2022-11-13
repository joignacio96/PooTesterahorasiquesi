package Modelo;

import java.util.Date;

public class Arriendo {
    private long codigo;
    private Date fechaInicio;
    private Date fechaDevolucion;
    private EstadoArriendo estado;
    private Cliente cliente;

    public Arriendo(long codigo, Date fechaInicio, Cliente cliente) {
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.cliente=cliente;
        estado= EstadoArriendo.INICIADO;

    }

    public long getCodigo() {
        return codigo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public EstadoArriendo getEstado() {
        return estado;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setEstado(EstadoArriendo estado) {
        this.estado = estado;
    }
    //De aqui para abajo hay que trabajar en los metodos
    public void addDetalleArriendo(Equipo equipo){
        Arriendo DetalleArriendo=new Arriendo();
    }
    public int getNumeroDiasArriendo(){

    }
    public long getMontoTotal(){

    }
    public String[][] getDetallesToString(){

    }
    public Cliente getCliente(){
        return cliente;
    }
    public Equipo [] getEquipos(){

    }
}
