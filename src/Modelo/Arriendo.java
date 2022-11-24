package Modelo;

import java.time.Period;
import java.util.ArrayList;
import java.util.Date;

public class Arriendo {
    private long codigo;
    private Date fechaInicio;
    private Date fechaDevolucion;
    private EstadoArriendo estado;
    private Cliente cliente;
    private ArrayList <DetalleArriendo> detallesArriendo= new ArrayList<>();

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

    public void addDetalleArriendo(Equipo equipo) {
        DetalleArriendo detalle = new DetalleArriendo(equipo.getPrecioArriendoDia(), equipo,this);
        detalleArriendo.add(detalle);
        equipo.addDetalleArriendo(detalle);
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

    public long getMontoTotal() {

        long total = 0;

        if (estado.equals(EstadoArriendo.DEVUELTO)) {

            for (DetalleArriendo detalleArriendo : detallesArriendo) {
                total += getNumeroDiasArriendo() * detalleArriendo.getEquipo().getPrecioArriendoDia();
            }
            return total;
        }

        if (estado.equals(EstadoArriendo.INICIADO)) {

            for (DetalleArriendo detalleArriendo : detallesArriendo) {
                if (detalleArriendo.getEquipo() == null) {
                    return 0;
                }
            }

            for (DetalleArriendo detalleArriendo : detallesArriendo) {
                total += detalleArriendo.getEquipo().getPrecioArriendoDia();
            }
            return total;

        } else {
            for (DetalleArriendo detalleArriendo : detallesArriendo) {
                total += detalleArriendo.getEquipo().getPrecioArriendoDia();
            }
            return total;
        }


    }


    public String[][] getDetallesToString() {

        String[][] arr = new String[detallesArriendo.size()][3];

        if (estado == EstadoArriendo.INICIADO && detallesArriendo == null) {
            return new String[0][0];
        }

        if (estado == EstadoArriendo.ENTREGADO || estado == EstadoArriendo.DEVUELTO) {

            int i = 0;
            int j = 0;
            for (DetalleArriendo detalleArriendo : detallesArriendo) {
                arr[i][j] = String.valueOf(detalleArriendo.getEquipo().getCodigo());
                j++;
                arr[i][j] = detalleArriendo.getEquipo().getDescripcion();
                j++;
                arr[i][j] = String.valueOf(detalleArriendo.getEquipo().getPrecioArriendoDia());
            }
        }

        return arr;
    }
    public Cliente getCliente(){
        return cliente;
    }
    public Equipo [] getEquipos(){
        return detallesArriendo.toArray(new Equipo[0]);

    }
}
