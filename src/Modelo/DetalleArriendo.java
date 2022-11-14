package Modelo;

import java.util.ArrayList;

public class DetalleArriendo {
    private long precioAplicado;
    private Equipo equipo;
    private ArrayList <Arriendo> arriendo =new ArrayList<>();

    public DetalleArriendo(long precioAplicado, Equipo equipo, ArrayList<Arriendo> arriendo) {
        this.precioAplicado = precioAplicado;
        this.equipo = equipo;
        this.arriendo = arriendo;
    }

    public long getPrecioAplicado() {
        return precioAplicado;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public ArrayList<Arriendo> getArriendo() {
        return arriendo;
    }
}
