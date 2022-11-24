package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.Arriendo;
import Modelo.Cliente;
import Modelo.EstadoEquipo;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UIArriendoEquipos {
    private static UIArriendoEquipos instance = null;
    private Scanner teclado;

    private UIArriendoEquipos() {
        teclado = new Scanner(System.in);
        teclado.useDelimiter("[\r\n]+");
    }

    public static UIArriendoEquipos getInstance() {
        if (instance == null) {
            instance = new UIArriendoEquipos();
        }
        return instance;
    }

    public void menu() {
        int opcion=0;
        System.out.println("******* SISTEMA DE ARRIENDO DE EQUIPOS DE NIEVE ****** ");
        do {
            System.out.println("\n\n\n***MENU DE OPCIONES***");
            System.out.println("1. Crea un nuevo cliente");
            System.out.println("2. Crea un nuevo equipo");
            System.out.println("3. Arrienda equipos");
            System.out.println("4. Devuelve equipos");
            System.out.println("5. Cambia estado de un cliente");
            System.out.println("6. Lista de todos los clientes");
            System.out.println("7. Lista de todos los equipos");
            System.out.println("8. Lista todos los arriendos");
            System.out.println("9. Lista detalles de un arriendo");
            System.out.println("10. Salir");
            System.out.println("\n\n\nIngrese opcion: ");

            try{
                String opcionStr = teclado.next();
                opcion = Integer.parseInt(opcionStr);
                System.out.println("");
            }catch (InputMismatchException e){
                System.out.println("\nError: ");
            }


            switch (opcion) {
                case 1 -> creaCliente();            // Funciona
                case 2 -> creaEquipo();             // Funciona
                case 3 -> arriendaEquipos();        // Funciona
                case 4 -> devuelveEquipos();        //
                case 5 -> cambiaEstadoCliente();    // Funciona
                case 6 -> listaClientes();          // Funciona
                case 7 -> listaEquipos();           // Funciona
//                case 8 -> listaArriendos();       //
                case 9 -> listaDetallesArriendo();  //
                case 10 -> System.exit(2);    // Funciona
                default -> System.out.println("\nIngreso no valido");
            }
        } while (opcion != 10);
        teclado.close();
    }
    public void devuelveEquipos(){
        String rut;
        System.out.println("Devolviendo equipos arrendados...");
        System.out.println("Rut Cliente: ");
        rut=teclado.next().trim();
        if(rut.equals("")){
            System.out.println("Debe ingresar algun rut valido");
            return;
        }

        String [] datos=ControladorArriendoEquipos.getInstance().consultaCliente(rut);
        if (datos.length == 0) {
            System.out.println("No existe cliente");
            return;
        }
        System.out.println("Nombre Cliente: "+ datos[0]);
        System.out.println();
        try{
            String [][] devuelta=ControladorArriendoEquipos.getInstance().listaArriendoPorDevolver(rut);
            System.out.println("Los arriendos por devolver son =>>");
            System.out.printf("%-14s%-20s%-20s%-14s%-20s%s", "Codigo", "Fecha inicio", "Fecha devol.", "Estado", "Rut Cliente", "Monto Total");
            for(int i=0;1<devuelta.length;i++){
                if(devuelta[i][4].equals(rut)){
                    System.out.printf("%s",devuelta[i][0]);
                    System.out.printf("%-14s", devuelta[i][1]);
                    System.out.printf("%-20s", devuelta[i][2]);
                    System.out.printf("%-20s", devuelta[i][3]);
                    System.out.printf("%-14s", devuelta[i][4]);
                    System.out.printf("%-20s", devuelta[i][6]);
                    System.out.println();
                }
            }
            System.out.print("Codigo arriendo a devolver: ");
            boolean valido = true;
            long code;
            do {
                try {
                    String codStr = teclado.next();
                    code = Long.parseLong(codStr);
                    valido = false;
                } catch (NumberFormatException e) {
                    System.out.println("Numero no valido");
                    return;
                }
            } while (valido);
            System.out.println("Ingrese codigo y estado en el que se devuelve cada equipo que se indica >>>");
            String [][] detalle=ControladorArriendoEquipos.getInstance().listaDetallesArriendos(code);
            int estado;
            ArrayList<EstadoEquipo> estados = new ArrayList<>();
            for(String[] detalles:detalle){
                do {
                    System.out.println(detalles[1]+"("+detalles[0]+") -> Estado (1: Operativo, 2: reparacion, 3: Dado de baja: ");
                    try {
                        String estadoStr = teclado.next();
                        estado = Integer.parseInt(estadoStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Tiene que ser un valor numerico");
                        estado = -1;
                    }
                } while (estado==1 || estado==2 || estado==3);

                if(estado==1){
                    estados.add(EstadoEquipo.OPERATIVO);
                }
                if(estado==2){
                    estados.add(EstadoEquipo.EN_REPARACION);
                }
                if(estado==3){
                    estados.add(EstadoEquipo.DADO_DE_BAJA);
                }
                System.out.println();
            }
            ControladorArriendoEquipos.getInstance().devuelveEquipos(code, estados.toArray(new EstadoEquipo[0]));
            System.out.println(detalle.length+ "equipo(s) fue(ron) devuelto(s) exitosamente");
        }catch(ClienteException e){
            System.out.println(e.getMessage());
        }catch(ArriendoException e){
            System.out.println(e.getMessage());
        }
    }

    private void listaClientes() {
        String[][] datosClientes = ControladorArriendoEquipos.getInstance().listaClientes();
        int i = 0;
        if (datosClientes.length > 0) {
            System.out.println("\nLISTADO DE CLIENTES");
            System.out.println("------------");
            System.out.println();
            System.out.printf("%1$-15s%2$-30s%3$-20s%4$-15s%n", "RUT", "Nombre", "Direccion", "Telefono");
            for (String[] listado : datosClientes) {

                System.out.printf("%-25s%-12s%10s%10sn", listado[0], listado[1], listado[2], listado[3], listado[4]);
                i++;
            }
        } else {
            System.out.println("\n No existen clientes");
        }
    }

    private void listaEquipos() {
        String[][] datosEquipos = ControladorArriendoEquipos.getInstance().listaEquipos();
        if (datosEquipos.length > 0) {
            System.out.println("\nLISTADO DE EQUIPOS");
            System.out.println("------------");
            System.out.printf("%-15s%-30s%-20s%-15s%n", "Codigo", "Descripcion", "Precio", "fkdjsklf");
            for (String[] listado : datosEquipos) {
                System.out.printf("%-15s%-30s%-20s%-15s%n", listado[0], listado[1], listado[2], listado[3]);
            }
        } else {
            System.out.println("\nNo existen equipos");
        }
    }

    private void creaCliente() {
        String rut, nom, dir, tel;
        System.out.println("Creando un nuevo cliente...");
        System.out.print("\nRut: ");
        rut = teclado.next().trim();
        if (!validarRut(rut)) {
            System.out.println("No ha ingresado ningún dato, por favor inténtelo de nuevo");
            return;
        }
        System.out.print("Nombre: ");
        nom = teclado.next().trim();
        if (nom.isBlank() || nom.isEmpty()) {
            System.out.println("No ha ingresado ningún dato, por favor inténtelo de nuevo");
            return;
        }
        System.out.print("Domicilio: ");
        dir = teclado.next().trim();
        if (dir.isBlank() || dir.isEmpty()) {
            System.out.println("No ha ingresado ningún dato, por favor inténtelo de nuevo");
            return;
        }
        System.out.println("Telefono: ");
        tel = teclado.next().trim();
        if (tel.isBlank() || tel.isEmpty()) {
            System.out.println("No ha ingresado ningún dato, por favor inténtelo de nuevo");
            return;
        }
        try {
            ControladorArriendoEquipos.getInstance().creaCliente(rut, nom, dir, tel);
            System.out.println("Usted ha creado satisfactoriamente el cliente");
        } catch (ClienteException e) {
            System.out.println("Error creando el cliente, revise los datos nuevamente");
        }
    }

    private void creaEquipo() {
        String descripcion, code, precio;
        long codigo = 0, precioArriendoDia = 0;
        System.out.print("Creando un nuevo equipo... ");
        System.out.println("\n\n\nCodigo: ");
        code = teclado.next().trim();

        if (code.isBlank() || code.isEmpty()) {
            System.out.println("No ha ingresado ningún dato, por favor inténtelo de nuevo");
            return;
        }
        try {
            codigo = Long.parseLong(code);
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese solo numeros");
            return;
        }
        System.out.print("Descripcion: ");
        descripcion = teclado.next().trim();
        if (descripcion.isBlank() || descripcion.isEmpty()) {
            System.out.println("No ha ingresado ningún dato, por favor inténtelo de nuevo");
            return;
        }
        System.out.print("Precio arriendo por dia: ");
        precio = teclado.next().trim();
        if (precio.isBlank() || precio.isEmpty()) {
            System.out.println("No ha ingresado ningún dato, por favor inténtelo de nuevo");
            return;
        }
        try {
            precioArriendoDia = Long.parseLong(precio);
            if (precioArriendoDia < 0) {
                System.out.println("Por favor ingrese un precio válido");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese solo numeros");
            return;
        }

        try {
            ControladorArriendoEquipos.getInstance().creaEquipo(codigo, descripcion, precioArriendoDia);
        } catch (EquipoException e) {
            System.out.println("Error creando el equipo, intentelo de nuevo");
        }
    }

    public void arriendaEquipos(){
        System.out.println("Arrendando equipos...");
        ControladorArriendoEquipos controller = ControladorArriendoEquipos.getInstance();

        System.out.println("Rut cliente");
        String rut = teclado.next();
        if (!validarRut(rut)) {
            System.out.println("Rut no valido");
            return;
        }

        String eleccion;
        long codeArriendo;

        try {
            String[] cliente = controller.consultaCliente(rut);
            codeArriendo = controller.creaArriendo(rut);
            System.out.println("Nombre cliente: " + cliente[1]);
        } catch (ClienteException e) {
            System.out.println(e.getMessage());
            return;
        }

        String opcion = "s";
        do {
            System.out.println("Ingresa codigo del equipo");
            int codigoEquipo;
            try {
                String codeSrt = teclado.next();
                codigoEquipo = Integer.parseInt(codeSrt);
            }catch (NumberFormatException e) {
                System.out.println("Codigo debe ser numerico");
                return;
            }
            try {
                controller.agregaEquipoToArriendo(codeArriendo, codigoEquipo);
            } catch (ArriendoException | EquipoException e) {
                System.out.println(e.getMessage());
            }
            do {
                System.out.println("Desea ingresar otro equipo (s/n)");
                opcion = teclado.next();
            } while (!opcion.equals("s") && !opcion.equals("n"));
        } while (opcion.equals("s"));

        try {
            controller.cierraArriendo(codeArriendo);
        } catch (ArriendoException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cambiaEstadoCliente() {
        System.out.println("Cambiando el estado del cliente...");
        System.out.print("Rut cliente: ");
        String rut = teclado.next();
        if (!validarRut(rut)) {
            System.out.println("Rut no es valido");
            return;
        }
        String[] datosCliente;
        try {
            ControladorArriendoEquipos.getInstance().cambiaEstadoCliente(rut);
            datosCliente = ControladorArriendoEquipos.getInstance().consultaCliente(rut);
            System.out.printf("Se ha cambiado exitosamente el estado del cliente \"%s\" a \"%s\"%n", datosCliente[1], datosCliente[4]);
        } catch (ClienteException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listaDetallesArriendo() {
    }

    private boolean validarRut(String rut) {
        boolean validacion = false;
        rut = rut.toUpperCase();
        rut = rut.replace(".", "");
        rut = rut.replace("-", "");
        int rutNumerico;
        try {
            rutNumerico = Integer.parseInt(rut.substring(0, rut.length() - 1));
        } catch (NumberFormatException e) {
            return false;
        }

        char verificador = rut.charAt(rut.length() - 1);

        int m = 0, s = 1;
        for (; rutNumerico != 0; rutNumerico /= 10) {
            s = (s + rutNumerico % 10 * (9 - m++ % 6)) % 11;
        }
        if (verificador == (char) (s != 0 ? s + 47 : 75)) {
            validacion = true;
        }

        return validacion;
    }
}
