package Modelo;

import java.util.ArrayList;
import java.util.Objects;

public class Cliente {
    private final String rut;
    private final String nombre;
    private final String direccion;
    private final String telefono;
    private boolean activo;
    //Relacion con arriendos
    public ArrayList<Arriendo> arriendos=new ArrayList<>();

    public Cliente(String rut, String nom, String dir, String tel) {
        this.direccion=dir;
        this.telefono=tel;
        this.nombre=nom;
        this.rut = rut;
        activo=true;
    }

    public String getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }
    public boolean isActivo(){
if (activo=true){
    return true;
}else{
    return false;
}
    }
    public void setActivo(){
        activo=true;
    }
    public void setInactivo(){
        activo=false;
    }

    @Override
    public String toString() {
        return  rut + '\'' + nombre + '\'' + direccion + '\'' +telefono + '\'' + activo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return activo == cliente.activo && rut.equals(cliente.rut) && Objects.equals(nombre, cliente.nombre) && Objects.equals(direccion, cliente.direccion) && Objects.equals(telefono, cliente.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rut, nombre, direccion, telefono, activo);
    }


}


