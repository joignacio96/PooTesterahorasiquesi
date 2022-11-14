package Modelo;

import java.time.Period;
import java.util.Date;

public class Arriendo {
    private long codigo;
    private Date fechaInicio;
    private Date fechaDevolucion;
    private EstadoArriendo estado;
    private Cliente cliente;
    private DetalleArriendo detalleArriendo;

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
        //trabajar con metodos Date.
if (estado ==EstadoArriendo.DEVUELTO) {
    if (fechaInicio.equals(fechaDevolucion)){
        return 1;
    }else{
        int milisegundos = 86400000;
        int totDias = (int) (fechaInicio.getTime() - fechaDevolucion.getTime()) / milisegundos;
        return totDias;
    }
}else {
    return 0;
}
}
    public long getMontoTotal(){

        if (estado==EstadoArriendo.DEVUELTO){
            int totPagar;
            totPagar=(getNumeroDiasArriendo()* (int) detalleArriendo.getPrecioAplicado());
            return totPagar;
        }
if (estado==EstadoArriendo.ENTREGADO){
    return detalleArriendo.getPrecioAplicado();
}
else{
    return 0;
}
    }
    public String[][] getDetallesToString(){
        //Trabajar con el size del arreglo
        String [][] detalles= new String[3][];
        if (estado==EstadoArriendo.INICIADO) {

            return //matriz vacía
        }else{
           return //matriz con codigo, descripcion y precio de los equipos incluidos en el arriendo.

        }

    }
    public Cliente getCliente(){
        return cliente;
    }
    public Equipo [] getEquipos(){

    }
}
