package Controlador;

import Modelo.Cliente;
import Modelo.Equipo;

import java.util.ArrayList;

public class ControladorArriendoEquipos {
        private static Controlador.ControladorArriendoEquipos instance = null;
        private final ArrayList<Cliente> clientes;
        private final ArrayList<Equipo> equipos;
        private ControladorArriendoEquipos() {
            clientes = new ArrayList<>();
            equipos = new ArrayList<>();
        }
        public static Controlador.ControladorArriendoEquipos getInstance() {
            if (instance == null) {
                instance = new Controlador.ControladorArriendoEquipos();
            }
            return instance;
        }
        public void creaCliente(String rut, String nom, String dir, String tel) {
            clientes.add(new Cliente(rut,nom,dir,tel));
        }
        public void creaEquipo(long codigo, String descripcion, long precioArriendoDia) {
            equipos.add(new Equipo(codigo,descripcion,precioArriendoDia));
        }
        public String [][] listaClientes(){
            String [][] clientesArr = new String[clientes.size()][5];
            int i=0;
            for(Cliente cliente: clientes){
                clientesArr[i][0]= clientes.get(i).getRut();
                clientesArr[i][1]= clientes.get(i).getNombre();
                clientesArr[i][2]= clientes.get(i).getDireccion();
                clientesArr[i][3]= clientes.get(i).getTelefono();
                clientesArr[i][4]= String.valueOf(clientes.get(i).isActivo());
                i++;
            }
            return clientesArr;
        }
        public String [][] listaEquipos(){
            String [][] equiposArr=new String[equipos.size()][3];
            int i=0;
            for(Equipo equipo: equipos){
                equiposArr[i][0]=Long.toString(equipo.getCodigo());
                equiposArr[i][1]=equipo.getDescripcion();
                equiposArr[i][2]=Long.toString(equipo.getPrecioArriendoDia());
                equiposArr[i][3]=String.valueOf(equipo.getEstado());
                i++;
            }
            return equiposArr;
        }
        public long creaArriendo (String rutCliente){

        }
    }

