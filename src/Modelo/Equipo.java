package Modelo;

import java.util.ArrayList;

public class Equipo {
    private long codigo;
    private String descripcion;
    private long precioArriendoDia;
    private EstadoEquipo estado;
    private ArrayList <DetalleArriendo> detalle;

    public Equipo(long codigo, String descripcion, long precioArriendoDia) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioArriendoDia = precioArriendoDia;
        this.estado = EstadoEquipo.OPERATIVO;
    }

    public long getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public long getPrecioArriendoDia() {
        return precioArriendoDia;
    }

    public EstadoEquipo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }
    public void addDetalleArriendo (DetalleArriendo detalle){
ArrayList<DetalleArriendo>arriendo=new ArrayList<>();
arriendo.add(detalle);
    }
    public boolean isArrendado(){
        if (//ver si el equipo se encuentra arrendado){


        }
    }
}

