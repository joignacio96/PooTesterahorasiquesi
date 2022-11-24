package Controlador;

import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.*;

import java.util.ArrayList;
import java.util.Date;

public class ControladorArriendoEquipos {
    private static Controlador.ControladorArriendoEquipos instance = null;
    private final ArrayList<Cliente> clientes;
    private final ArrayList<Equipo> equipos;
    private static ArrayList<Arriendo> arriendos;

    private ControladorArriendoEquipos(ArrayList<Arriendo> arriendos) {
        ControladorArriendoEquipos.arriendos = new ArrayList<>();
        clientes = new ArrayList<>();
        equipos = new ArrayList<>();
    }

    public static Controlador.ControladorArriendoEquipos getInstance() {
        if (instance == null) {
            instance = new Controlador.ControladorArriendoEquipos(arriendos);
        }
        return instance;
    }

    public void creaCliente(String rut, String nom, String dir, String tel) throws ClienteException {

        for (Cliente cliente : clientes) {
            if (cliente.getRut().equals(rut)) {
                throw new ClienteException("El cliente ya se encuentra registrado, intentelo nuevamente");
            }
        }
        clientes.add(new Cliente(rut, nom, dir, tel));
    }

    public void creaEquipo(long codigo, String descripcion, long precioArriendoDia) throws EquipoException {
        for (Equipo equipo : equipos) {
            if (equipo.getCodigo() == codigo) {
                throw new EquipoException("El equipo ingresado ya existe");
            }
        }
        equipos.add(new Equipo(codigo, descripcion, precioArriendoDia));
    }

    public String[][] listaClientes() {
        String[][] clientesArr = new String[clientes.size()][5];
        int i = 0;
        for (Cliente cliente : clientes) {
            clientesArr[i][0] = clientes.get(i).getRut();
            clientesArr[i][1] = clientes.get(i).getNombre();
            clientesArr[i][2] = clientes.get(i).getDireccion();
            clientesArr[i][3] = clientes.get(i).getTelefono();
            clientesArr[i][4] = String.valueOf(clientes.get(i).isActivo());
            i++;
        }
        return clientesArr;
    }

    public String[][] listaEquipos() {
        String[][] equiposArr = new String[equipos.size()][3];
        int i = 0;
        for (Equipo equipo : equipos) {
            equiposArr[i][0] = Long.toString(equipo.getCodigo());
            equiposArr[i][1] = equipo.getDescripcion();
            equiposArr[i][2] = Long.toString(equipo.getPrecioArriendoDia());
            equiposArr[i][3] = String.valueOf(equipo.getEstado());
            i++;
        }
        return equiposArr;
    }

    public long creaArriendo(String rutCliente) throws ClienteException {
        int i;
        for (Cliente cliente : clientes) {
            if (cliente.getRut().equalsIgnoreCase(rutCliente) && cliente.isActivo()) {
                long miliseconds = System.currentTimeMillis();
                Date date = new Date(miliseconds);
                new Arriendo(clientes.indexOf(cliente), date, clientes.get(clientes.indexOf(cliente)));

                return clientes.indexOf(cliente);
            }
        }
        if (!clientes.contains(rutCliente)) {
            throw new ClienteException("No existe un cliente con el rut dado");
        } else {
            throw new ClienteException("El cliente está inactivo");
        }

    }

    private Equipo buscaEquipo(long codigo) {
        for (Equipo equipo : equipos) {
            if ((String.valueOf(equipo.getCodigo()).equals(String.valueOf(codigo)))) {
                return equipo;
            }
        }
        return null;
    }

    private Arriendo buscaArriendo(long codigo) {
        for (Arriendo arriendo : arriendos) {
            if (arriendo.getCodigo() == codigo) {
                return arriendo;
            }
        }
        return null;
    }

    private Cliente buscaCliente(String rut) {
        for (Cliente cliente : clientes) {
            if (cliente.getRut().equals(rut)) {
                return cliente;
            }
        }
        return null;
    }

    public String agregaEquipoToArriendo(long codArriendo, long codEquipo) throws ArriendoException, EquipoException {
        if (buscaArriendo(codArriendo) != null) {
            if (buscaArriendo(codArriendo).getEstado().equals(EstadoArriendo.INICIADO)) {
                if (buscaEquipo(codEquipo) != null) {
                    if (!buscaEquipo(codEquipo).isArrendado()) {
                        if (buscaEquipo(codEquipo).getEstado().equals(EstadoEquipo.OPERATIVO)) {
                            buscaArriendo(codArriendo).addDetalleArriendo(buscaEquipo(codEquipo));
                        } else {
                            throw new EquipoException("El equipo no esta operativo");
                        }
                    } else {
                        throw new EquipoException("El equipo se encuentra arrendado");
                    }
                } else {
                    throw new EquipoException("No existe un equipo con el codigo dado");
                }
            } else {
                throw new ArriendoException("El arriendo no está iniciado");
            }

        } else {
            throw new ArriendoException("No existe un Arriendo con el codigo dado");
        }

        return buscaEquipo(codEquipo).getDescripcion();


    }

    //aiuda
    public long cierraArriendo(long codArriendo) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo != null) {
            if (arriendo.getEquipos() != null) {
                arriendo.setEstado(EstadoArriendo.ENTREGADO);
            } else {
                throw new ArriendoException("No existen equipos asociados al arriendo");
            }
        } else {
            throw new ArriendoException("No existe un arriendo con el codigo dado");
        }
        return arriendo.getMontoTotal();
    }

    public void cambiaEstadoCliente(String rutCliente) throws ClienteException {
        if (buscaCliente(rutCliente) != null) {
            Cliente cliente = buscaCliente(rutCliente);
            if (cliente.isActivo()) {
                cliente.setInactivo();
            } else {
                cliente.setActivo();
            }
        } else {
            throw new ClienteException("No existe cliente con el rut dado");
        }

    }

    public String[] consultaEquipo(long codigo) {
        String[] arreglo;
        Equipo equipo = buscaEquipo(codigo);
        String estado;
        if (equipo.getEstado().equals(EstadoEquipo.DADO_DE_BAJA)) {
            estado = "Dado de baja";
        } else {
            if (equipo.getEstado().equals(EstadoEquipo.EN_REPARACION)) {
                estado = "En reparacion";
            } else {
                estado = "Operativo";
            }
            String estadoArriendo;
            if (equipo.isArrendado()) {
                estadoArriendo = "Arrendado";
            } else {
                estadoArriendo = "Disponible";
            }
            if (equipo != null) {
                arreglo = new String[]{String.valueOf(equipo.getCodigo()), equipo.getDescripcion(),
                        String.valueOf(equipo.getPrecioArriendoDia()), estado, estadoArriendo};
            } else {
                return arreglo = new String[0];
            }
            return arreglo;
        }

        public String[] consultaCliente(String rut){
            String[] arr;
            String aux = null;
            Cliente cliente = buscaCliente(rut);
            if (!cliente.isActivo()) {
                aux = "Inactivo";
            }
            if (cliente.isActivo()) {
                aux = "Activo";
            }
            if (cliente != null) {
                arr = new String[]{cliente.getRut(), cliente.getNombre(), cliente.getDireccion(), cliente.getTelefono(), aux, String.valueOf(cliente.getArriendosPorDevolver().length)};
                return arr;
            }
            return new String[0];

        }
//aiuda
        public String[][] listaArriendosPorDevoler (String rut) throws ClienteException {
            if (buscaCliente(rut) != null) {
                Cliente cliente = buscaCliente(rut);
                Arriendo[] arreglo = cliente.getArriendosPorDevolver();
                for (int i = 0; i < arreglo.length; i++) {
                    (arreglo[i]) =;
                }
            } else {
                throw new ClienteException("No existe un cliente con el rut dado");
            }
        }


    }
}














