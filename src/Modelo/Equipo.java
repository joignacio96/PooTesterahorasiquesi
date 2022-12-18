package Modelo;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Equipo {
    private long codigo;
    private String descripcion;
    private EstadoEquipo estado;
    private ArrayList <DetalleArriendo> detalles;
    private ArrayList<Conjunto>conjuntos;

    public Equipo(long codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.estado = EstadoEquipo.OPERATIVO;
        this.detalles = new ArrayList<>();
    }

    public long getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public abstract long getPrecioArriendoDia();

    public EstadoEquipo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }
    public void addDetalleArriendo (DetalleArriendo detalle){
        detalles.add(detalle);
    }

    public boolean isArrendado(){
        if(detalles.isEmpty()){
            return false;
        }
        int i=(detalles.size()-1);
        return detalles.get(i).getArriendo().getEstado() == (EstadoArriendo.ENTREGADO);
    }
}

