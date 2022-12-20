package Modelo;

import java.util.ArrayList;

public class Conjunto extends Equipo{
    private ArrayList<Equipo>equipos;
    public Conjunto(long codigo, String descripcion) {
        super(codigo,descripcion);
        equipos=new ArrayList<>();
    }

    @Override
    public long getPrecioArriendoDia() {
        long suma=0;
        for (Equipo equipo:equipos) {
            long valor= equipo.getPrecioArriendoDia();
            suma=suma+valor;

        }
        return suma;
    }
    public void addEquipo(Equipo equipo){
        if (equipo!=null && !equipos.contains(equipo)){
            equipos.add(equipo);
        }else{
            System.out.println("El equipo ya se encuentra en la lista");
        }
    }
public int getNroEquipos(){
        return equipos.size();
}
}

