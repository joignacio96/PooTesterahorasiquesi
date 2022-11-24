package Modelo;

import java.time.Period;
import java.util.ArrayList;
import java.time.LocalDate;

public class Arriendo {
    private long codigo;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucion;
    private EstadoArriendo estado;
    private Cliente cliente;
    private ArrayList<DetalleArriendo> detallesArriendo = new ArrayList<>();

    public Arriendo(long codigo, LocalDate fechaInicio, Cliente cliente) {
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.cliente = cliente;
        estado = EstadoArriendo.INICIADO;

    }

    public long getCodigo() {
        return codigo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public EstadoArriendo getEstado() {
        return estado;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setEstado(EstadoArriendo estado) {
        this.estado = estado;
    }

    //De aqui para abajo hay que trabajar en los metodos
    public void addDetalleArriendo(Equipo equipo) {
        detallesArriendo.add(equipo);
    }

    public int getNumeroDiasArriendo() {
        //trabajar con metodos LocalDate.
        if (estado == EstadoArriendo.DEVUELTO) {
            if (fechaInicio.equals(fechaDevolucion)) {
                return 1;
            } else {
                Period period = Period.between(fechaInicio, getFechaDevolucion());
                return period.getDays();
            }
        } else {
            return 0;
        }
    }

    public long getMontoTotal() {

        if (estado == EstadoArriendo.DEVUELTO) {
            int totPagar;
            totPagar = (getNumeroDiasArriendo() * (int) detallesArriendo.getPrecioAplicado());
            return totPagar;
        }
        if (estado == EstadoArriendo.ENTREGADO) {
            return detallesArriendo.getPrecioAplicado();
        } else {
            return 0;
        }
    }

    public String[][] getDetallesToString() {
        if (estado == EstadoArriendo.INICIADO) {
            String[][] arregloVacio = {};
            return arregloVacio;
        } else {
            String[][] arregloDetalles = new String[][ 3];
            return String [][]  //matriz con codigo, descripcion y precio de los equipos incluidos en el arriendo.

        }

    }

    public Cliente getCliente() {
        return cliente;
    }

    public Equipo[] getEquipos() {
        return detallesArriendo.toArray(new Equipo[0]);

    }
}
