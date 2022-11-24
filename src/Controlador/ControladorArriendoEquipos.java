package Controlador;

import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.*;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
            if (validarRut(rut)){
                clientes.add(new Cliente(rut, nom, dir, tel));

                   if(cliente.getRut().equals(rut)){
                    throw new ClienteException("El cliente ya se encuentra registrado, intentelo nuevamente");
                }
            }
        }
    }

    public void creaEquipo(long codigo, String descripcion, long precioArriendoDia) throws EquipoException {
        for (Equipo equipo : equipos) {
            if(validarCodigo(codigo)){
                equipos.add(new Equipo(codigo, descripcion, precioArriendoDia));
            }else{
                System.out.println("Codigo erroneo");
            }
            if (equipo.getCodigo() == codigo) {
                throw new EquipoException("El equipo ingresado ya existe");
            }
        }

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

    public String[][] listaArriendosPorDevolver(String rutCliente) throws ClienteException {
        Cliente cliente = buscaCliente(rutCliente);
        if (cliente == null) {
            throw new ClienteException("No existe un cliente con el rut dado");
        } else {
            Arriendo[] devolverCliente = cliente.getArriendosPorDevolver();
            String[][] listaArrDevolver = new String[devolverCliente.length][8];
            for (int i = 0; i < listaArrDevolver.length; i++) {
                String[] listaArriendosPorDevolverX = consultaArriendo(cliente.getArriendosPorDevolver()[i].getCodigo());
                for (int j = 0; j < listaArrDevolver[0].length; j++) {
                    listaArrDevolver[i][j] = listaArriendosPorDevolverX[j];
                }
            }
            return listaArrDevolver;
        }
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
    private static boolean validarRut(String rut) {
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

    public void devuelveEquipos(){
        String rut, code;
        System.out.println("Devolviendo equipos arrendados...");
        System.out.println("Rut Cliente: ");
        rut=teclado.next().trim();
        if(rut==null){
            System.out.println("Debe ingresar algun rut valido");
            return;
        }

        String [] datos=ControladorArriendoEquipos.getInstance().consultaCliente(rut);
        System.out.println("Nombre Cliente: "+ datos[0]);
        System.out.println();
        try{
            String [][] devuelta=ControladorArriendoEquipos.getInstance().listaArriendoPorDevolver(rut);
            System.out.println("Los arriendos por devolver son =>>");
            System.out.printf("%-14s%-20s%-20s%-14s%-20s%", "Codigo", "Fecha inicio", "Fecha devol.", "Estado", "Rut Cliente", "Monto Total");
            for(int i=0;1<devuelta.length;i++){
                if(devuelta[i][4].equals(rut)){
                    System.out.printf("%",devuelta[i][0]);
                    System.out.printf("-14s%", devuelta[i][1]);
                    System.out.printf("-20s%", devuelta[i][2]);
                    System.out.printf("-20s%", devuelta[i][3]);
                    System.out.printf("-14s%", devuelta[i][4]);
                    System.out.printf("-20s%", devuelta[i][6]);
                    System.out.println();
                }
            }
            System.out.print("Codigo arriendo a devolver: ");
            code=teclado.next().trim();
            if(code==null){
                System.out.println("Por favor ingrese un codigo valido");
                return;
            }
            System.out.println("Ingrese codigo y estado en el que se devuelve cada equipo que se indica >>>");
            String [][] detalle=ControladorArriendoEquipos.getInstance().listaDetallesArriendos(Long.parseLong(code));
            int acum=0, estado;
            for(String[] detalles:detalle){
                acum++;
                System.out.println(detalles[1]+"("+detalles[0]+") -> Estado (1: Operativo, 2: reparacion, 3: Dado de baja: ");
                estado=teclado.nextInt();

                if(estado==1){
                    ControladorArriendoEquipos.getInstance().devuelveEquipos(), new EstadoEquipo[] {EstadoEquipo.OPERATIVO};
                }
                if(estado==2){
                    ControladorArriendoEquipos.getInstance().devuelveEquipos(), new EstadoEquipo[] {EstadoEquipo.EN_REPARACION};
                }
                if(estado==3){
                    ControladorArriendoEquipos.getInstance().devuelveEquipos(), new EstadoEquipo[] {EstadoEquipo.DADO_DE_BAJA};
                }else{
                    throw new IllegalStateException("Unexpected vaule: "+ estado);
                }
                System.out.println();
            }
            System.out.println(acum+ "equipo(s) fue(ron) devuelto(s) exitosamente");
        }catch(ClienteException e){
            throw new RuntimeException(e);
        }catch(ArriendoException a){
            throw new RuntimeException(a);
        }
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


    public String [][] listaEquipos(){
        String [][] datos;
        for(int i=0;i<0;i++){

        }

    }

    public String[][] listaArriendo(LocalDate inicio, LocalDate fin) {
        if (arriendos.isEmpty()) {
            return new String[0][0];
        }

        ArrayList<String[]> datos = new ArrayList<>();
        for (Arriendo arriendo: arriendos) {
            LocalDate fecha = arriendo.getFechaInicio();
            if (fecha.isBefore(inicio) && fecha.isAfter(fin)) {
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


}

















