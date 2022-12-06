package Controlador;

import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ControladorArriendoEquipos {
    private static Controlador.ControladorArriendoEquipos instance = null;
    private final ArrayList<Cliente> clientes;
    private final ArrayList<Equipo> equipos;
    private final ArrayList<Arriendo> arriendos;

    private ControladorArriendoEquipos() {
        clientes = new ArrayList<>();
        equipos = new ArrayList<>();
        arriendos = new ArrayList<>();
    }

    public static Controlador.ControladorArriendoEquipos getInstance() {
        if (instance == null) {
            instance = new Controlador.ControladorArriendoEquipos();
        }
        return instance;
    }

    public void creaCliente(String rut, String nom, String dir, String tel) throws ClienteException {
        for (Cliente cliente : clientes) {
            if(cliente.getRut().equals(rut)){
                throw new ClienteException("Ya existe un cliente con el rut dado");
            }
        }
        Cliente cliente = new Cliente(rut, nom, dir, tel);
        clientes.add(cliente);
    }

    public void creaEquipo(long codigo, String descripcion, long precioArriendoDia) throws EquipoException {
        for (Equipo equipo : equipos) {
            if (equipo.getCodigo() == codigo) {
                throw new EquipoException("Ya existe el equipo indicado");
            }
        }
        Equipo equipo = new Equipo(codigo, descripcion, precioArriendoDia);
        equipos.add(equipo);
    }

    public long creaArriendo(String rutCliente) throws ClienteException {
        Cliente cliente = buscaCliente(rutCliente);
        if (cliente == null) {
            throw new ClienteException("No existe un cliente para arrendar equipos");
        }
        if (!cliente.isActivo()) {
            throw new ClienteException("El cliente no esta activo para arrendar equipos");
        }
        int codArriendo = arriendos.size();
        Arriendo arriendo = new Arriendo(codArriendo, LocalDate.now(), cliente);
        arriendos.add(arriendo);
        return codArriendo;
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
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            throw new ArriendoException("Error: ");
        }
        if (arriendo.getEstado() != EstadoArriendo.INICIADO) {
            throw new ArriendoException("El estado del arriendo no esta iniciado");
        }

        Equipo equipo = buscaEquipo(codEquipo);
        if (equipo == null) {
            throw new EquipoException("No existe el equipo solicitado");
        }
        if (equipo.getEstado() != EstadoEquipo.OPERATIVO) {
            throw new EquipoException("El equipo no esta operativo");
        } else if (equipo.isArrendado()) {
            throw new EquipoException("El equipo esta arrendado");
        }

        arriendo.addDetalleArriendo(equipo);
        return equipo.getDescripcion();
    }

    //aiuda
    public long cierraArriendo(long codArriendo) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            throw new ArriendoException("No existe el equipo arrendado");
        }
        if (arriendo.getEquipos().length == 0) {
            throw new ArriendoException("No quedan equipos");
        }

        arriendo.setEstado(EstadoArriendo.ENTREGADO);
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
                return arreglo;
            }
        }
        return arreglo = new String[0];
    }

    public String[] consultaCliente(String rut){
        String[] arr;
        Cliente cliente = buscaCliente(rut);

        if(cliente==null){
            arr = new String[0];
            return arr;
        }else{
            arr = new String[6];
            arr[0] = cliente.getRut();
            arr[1] = cliente.getNombre();
            arr[2] = cliente.getDireccion();
            arr[3] = cliente.getTelefono();
            if(cliente.isActivo()) {
                arr[4] = "Activo";
            }else {
                arr[4] = "Inactivo";
            }
            arr[5] = String.valueOf(cliente.getArriendosPorDevolver().length);
            return arr;
        }
    }

    public String[] consultaArriendo(long codigo) {
        Arriendo arriendo = buscaArriendo(codigo);
        if (arriendo == null) {
            return new String[0];
        }
        String [] datos = new String[7];

        Cliente cliente = arriendo.getCliente();
        DateTimeFormatter formate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        datos[0] = arriendo.getCodigo() + "";
        datos[1] = arriendo.getFechaInicio().format(formate);

        LocalDate fecha = arriendo.getFechaDevolucion();
        if (fecha == null) {
            datos[2] = "No devuelto";
        } else {
            datos[2] = fecha.format(formate);
        }
        datos[3] = String.valueOf(arriendo.getEstado());

        datos[4] = cliente.getRut();
        datos[5] = cliente.getNombre();
        datos[6] = arriendo.getMontoTotal() + "";

        return datos;
    }

    //aiuda
    private boolean validarRut(String rut) {
        boolean validacion = false;
        try {
            rut =  rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {

        }
        return validacion;
    }
    private boolean validarCodigo(long codigo) {
        int longitud=String.valueOf(codigo).length();
        try {
            if (longitud != 15) {
                System.out.println("Numero incorrecto, el número debe ser de 9 digitos\n");
                return false;
            } else {
                System.out.println("procesando...\n");
                return true;
            }
        } catch (UnsupportedOperationException e) {
            System.out.println("Error!:" + e);

        }
        return false;
    }
    public String[][] listaClientes() {
        if (clientes.size() == 0) {
            return new String[0][0];
        }

        String[][] arrClientes = new String[clientes.size()][6];

        int i = 0;
        for (Cliente cliente : clientes) {

            arrClientes[i][0] = cliente.getRut();
            arrClientes[i][1] = cliente.getNombre();
            arrClientes[i][2] = cliente.getDireccion();
            arrClientes[i][3] = cliente.getTelefono();

            if (cliente.isActivo()) {
                arrClientes[i][4] = "Activo";
            } else {
                arrClientes[i][4] = "Inactivo";
            }

            arrClientes[i][5] = String.valueOf(cliente.getArriendosPorDevolver().length);

            i++;
        }
        return arrClientes;

    }

    public String[][] listaEquipos() {
        if (equipos.isEmpty()) {
            return new String[0][0];
        }
        String[][] arrEquipos = new String[equipos.size()][5];

        int i = 0;

        for (Equipo equipo : equipos) {
            String estado = "";
            switch (equipo.getEstado()) {
                case DADO_DE_BAJA -> {
                    estado = "Dado de baja";
                }
                case EN_REPARACION -> {
                    estado = "En reparacion";
                }
                case OPERATIVO -> {
                    estado = "Operativo";
                }
            }

            String situacion;
            if (equipo.isArrendado()) {
                situacion = "Arrendado";
            } else {
                situacion = "Disponible";
            }

            arrEquipos[i][0] = String.valueOf(equipo.getCodigo());
            arrEquipos[i][1] = equipo.getDescripcion();
            arrEquipos[i][2] = String.valueOf(equipo.getPrecioArriendoDia());
            arrEquipos[i][3] = estado;
            arrEquipos[i][4] = situacion;
            i++;
        }
        return arrEquipos;
    }

    public String[][] listaDetallesArriendos(long codArriendo) {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            return new String[0][0];
        }

        return arriendo.getDetallesToString();

    }

    public String[][] listaArriendo(LocalDate inicio, LocalDate fin) {
        if (arriendos.isEmpty()) {
            return new String[0][0];
        }

        ArrayList<String[]> datos = new ArrayList<>();
        for (Arriendo arriendo: arriendos) {
            LocalDate fechaInicio = arriendo.getFechaInicio();
            if (!fechaInicio.isBefore(inicio) && !fechaInicio.isAfter(fin)) {
                String[] texto = new String[6];
                texto[0] = String.valueOf(arriendo.getCodigo());
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                texto[1] = arriendo.getFechaInicio().format(formato);
                LocalDate fecha1 = arriendo.getFechaDevolucion();
                String texto1;
                if (fecha1 == null) {
                    texto1 = "No devuelto";
                } else {
                    texto1 = fecha1.format(formato);
                }
                texto[2] = texto1;
                texto[3] = arriendo.getEstado() + "";
                texto[4] = arriendo.getCliente().getRut();
                texto[5] = arriendo.getMontoTotal() + "";
                datos.add(texto);

            }
        }
        return datos.toArray(new String[0][0]);
    }

    public String[][] listaArriendoPorDevolver(String rut) throws ClienteException{
        Cliente cliente = buscaCliente(rut);
        if (cliente == null) {
            throw new ClienteException("No existe el cliente indicado");
        }

        Arriendo[] arriendosPorDevolver = cliente.getArriendosPorDevolver();
        String[][] resultadod = new String[arriendosPorDevolver.length][6];
        int i = 0;
        for (Arriendo arriendo: arriendosPorDevolver) {
            resultadod[i][0] = String.valueOf(arriendo.getCodigo());
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            resultadod[i][1] = arriendo.getFechaInicio().format(formato);
            LocalDate fecha = arriendo.getFechaDevolucion();
            String texto;
            if (fecha == null) {
                texto = "No devuelto";
            } else {
                texto = fecha.format(formato);
            }
            resultadod[i][2] = texto;
            resultadod[i][3] = arriendo.getEstado() + "";
            resultadod[i][4] = cliente.getRut();
            resultadod[i][5] = arriendo.getMontoTotal() + "";
            i++;
        }

        return resultadod;
    }

    public void devuelveEquipos(long codArriendo, EstadoEquipo[] estadoEquipos) throws ArriendoException {

        Arriendo arriendo = buscaArriendo(codArriendo);

        if (arriendo == null) {
            throw new ArriendoException("No existe arriendo");
        } else if (arriendo.getEstado() != EstadoArriendo.ENTREGADO) {
            throw new ArriendoException("El arriendo no es pendiente");
        }

        Equipo[] equiposArriendo = arriendo.getEquipos();
        for (int i=0; i<equiposArriendo.length; i++) {
            equiposArriendo[i].setEstado(estadoEquipos[i]);
        }

        arriendo.setEstado(EstadoArriendo.DEVUELTO);
        arriendo.setFechaDevolucion(LocalDate.now());
    }


}

















