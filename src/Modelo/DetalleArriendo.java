package Modelo;

import java.util.ArrayList;

public class DetalleArriendo {
    private long precioAplicado;
    private Equipo equipo;
    private Arriendo arriendo;

    public DetalleArriendo(long precioAplicado, Equipo equipo, Arriendo arriendo) {
        this.precioAplicado = precioAplicado;
        this.equipo = equipo;
        this.arriendo = arriendo;
        equipo.addDetalleArriendo(this);
    }

    public long getPrecioAplicado() {
        return precioAplicado;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public Arriendo getArriendo() {
        return arriendo;
    }
}
