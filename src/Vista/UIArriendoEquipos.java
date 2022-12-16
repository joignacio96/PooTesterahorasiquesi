package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

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
        int opcion = 0;
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

            try {
                String opcionStr = teclado.next();
                opcion = Integer.parseInt(opcionStr);
                System.out.println("");
            } catch (InputMismatchException e) {
                System.out.println("\nError: ");
            }

            switch (opcion) {
                case 1 -> creaCliente();            // Funciona
                case 2 -> creaEquipo();             // Funciona
                case 3 -> arriendaEquipos();        // Funciona
                case 4 -> devuelveEquipos();        // Funciona
                case 5 -> cambiaEstadoCliente();    // Funciona
                case 6 -> listaClientes();          // Funciona
                case 7 -> listaEquipos();           // Funciona
                case 8 -> listaArriendos();         // Funciona
                case 9 -> listaDetallesArriendo();  // Funciona
                case 10 -> System.exit(2);    // Funciona
                default -> System.out.println("\nIngreso no valido");
            }
        } while (opcion != 10);
        teclado.close();
    }

    public void devuelveEquipos() {
        String rut;
        System.out.println("Devolviendo equipos arrendados...");
        System.out.println("Rut Cliente: ");
        rut = teclado.next().trim();
        if (rut.equals("")) {
            System.out.println("Debe ingresar algun rut valido");
            return;
        }

        String[] datos = ControladorArriendoEquipos.getInstance().consultaCliente(rut);
        if (datos.length == 0) {
            System.out.println("No existe cliente");
            return;
        }
        System.out.println("Nombre Cliente: " + datos[1]);
        System.out.println();
        try {
            String[][] devuelta = ControladorArriendoEquipos.getInstance().listaArriendoPorDevolver(rut);
            if (devuelta.length == 0) {
                System.out.println("No hay arriendo por devolver");
                return;
            }
            System.out.println("Los arriendos por devolver son =>>");
            //%1$-15s%2$-30s%3$-20s%4$-15s%n
            System.out.printf("%1$-14s%2$-20s%3$-20s%4$-14s%5$-20s%6$-10s%n", "Codigo", "Fecha inicio", "Fecha devol.", "Estado", "Rut Cliente", "Monto Total");
            System.out.println("");
            for (int i = 0; i < devuelta.length; i++) {
                if (devuelta[i][4].equals(rut)) {
                    System.out.printf("%1$-14s%2$-20s%3$-20s%4$-14s%5$-20s%6$-10s%n", devuelta[i][0]
                            , devuelta[i][1]
                            , devuelta[i][2]
                            , devuelta[i][3]
                            , devuelta[i][4]
                            , devuelta[i][5]);
                    System.out.println();
                }
            }
            System.out.println("");
            System.out.println("Codigo arriendo a devolver: ");
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

            String[][] detalle = ControladorArriendoEquipos.getInstance().listaDetallesArriendos(code);

            int estado;
            ArrayList<EstadoEquipo> estados = new ArrayList<>();
            System.out.println(detalle.length);
            for (String[] detalles : detalle) {
                do {
                    System.out.println(detalles[1] + "(" + detalles[0] + ") -> Estado (1: Operativo, 2: reparacion, 3: Dado de baja): ");
                    try {
                        String estadoStr = teclado.next();
                        estado = Integer.parseInt(estadoStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Tiene que ser un valor numerico");
                        estado = -1;
                    }
                } while (estado != 1 && estado != 2 && estado != 3);

                if (estado == 1) {
                    estados.add(EstadoEquipo.OPERATIVO);
                }
                if (estado == 2) {
                    estados.add(EstadoEquipo.EN_REPARACION);
                }
                if (estado == 3) {
                    estados.add(EstadoEquipo.DADO_DE_BAJA);
                }
                System.out.println();
            }
            ControladorArriendoEquipos.getInstance().devuelveEquipos(code, estados.toArray(new EstadoEquipo[0]));
            System.out.println(detalle.length + "equipo(s) fue(ron) devuelto(s) exitosamente");
        } catch (ClienteException e) {
            System.out.println(e.getMessage());
        } catch (ArriendoException e) {
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

                System.out.printf("%1$-15s%2$-30s%3$-20s%4$-15s%n", listado[0], listado[1], listado[2], listado[3], listado[4]);
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
            System.out.printf("%-15s%-30s%-20s%-15s%n", "Codigo", "Descripcion", "Precio", "Estado");
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
        if (!tel.matches("[0-9]+")) {
            System.out.println("Por favor, ingrese solo numeros");
            return;
        }
        try {
            ControladorArriendoEquipos.getInstance().creaCliente(rut, nom, dir, tel);
            System.out.println("Usted ha creado satisfactoriamente el cliente");
        } catch (ClienteException e) {
            System.out.println("Ya existe un cliente con los datos obtenidos, intentelo nuevamente");
        }
    }


    private void creaEquipo() throws EquipoException {
        String descripcion, code, precio, tipoEquipo;
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
        System.out.println("Tipo equipo (1: Implemento, 2: Conjunto):");
        tipoEquipo = teclado.next().trim();
        if (tipoEquipo.isBlank() || tipoEquipo.isEmpty()) {
            System.out.println("No ha ingresado ningún dato, por favor inténtelo de nuevo");
            return;
        }
        if (Integer.parseInt(tipoEquipo) < 0 || Integer.parseInt(tipoEquipo) > 2) {
            System.out.println("Por favor, ingrese una opción valida");
            return;
        }
        switch (tipoEquipo) {
            case "1":
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
                Implemento implemento = new Implemento(codigo, descripcion, precioArriendoDia);
                System.out.println("Se ha creado exitosamente un nuevo implemento");

            case "2":
                System.out.println("Numero de equipos componentes:");
                int numComponentes = teclado.nextInt();
                long[] codEquipos = new long[numComponentes];
                for (int i = 0; i < numComponentes; i++) {
                    System.out.println("Codigo equipo " + i + " de " + numComponentes);
                    long codEquipo = teclado.nextLong();


        }
    }






    public void listaArriendos() {
        LocalDate inicio, fin;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            System.out.print("Fecha inicio periodo (dd/mm/aaaa):");
            inicio = LocalDate.parse(teclado.next(), formatter);
            System.out.print("Fecha fin (dd/mm/aaaa):");
            fin = LocalDate.parse(teclado.next(), formatter);
        } catch (DateTimeParseException e) {
            System.out.println("La fecha no tiene un formato valido");
            return;
        }
        ControladorArriendoEquipos controlador = ControladorArriendoEquipos.getInstance();
        String[][] listaArr = controlador.listaArriendo(inicio, fin);
        if (listaArr.length == 0) {
            System.out.println("No existen datos de arriendos");
            return;
        }
        System.out.println("LISTADO ARRIENDOS");
        System.out.println("-----------------------------");
        System.out.println("");
        System.out.printf("%-8s %-14s %-14s %-12s %-15s %-13s%n", "Codigo", "Fecha Inicio", "Fecha devol.", "Estado", "Rut Cliente", "Monto Total");
        for (String[] lista : listaArr) {
            System.out.printf("%-8s %-14s %-14s %-12s %-15s %-13s%n", lista[0], lista[1], lista[2], lista[3], lista[4], lista[5]);
        }
    }

    public void arriendaEquipos() {
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
            } catch (NumberFormatException e) {
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
        System.out.print("Codigo arriendo: ");
        long codigo;
        try {
            String codString = teclado.next();
            codigo = Long.parseLong(codString);
        } catch (NumberFormatException e) {
            System.out.println("Codigo debe ser un numero");
            return;
        }
        ControladorArriendoEquipos controlado = ControladorArriendoEquipos.getInstance();
        String[] datosArriendo = controlado.consultaArriendo(codigo);
        if (datosArriendo.length == 0) {
            System.out.println("No existe el arriendo");
            return;
        }
        System.out.println("-------------------------------");
        System.out.printf("Codigo: %s\n", datosArriendo[0]);
        System.out.printf("Fecha Inicio: %s\n", datosArriendo[1]);
        System.out.printf("Fecha Devolucion: %s\n", datosArriendo[2]);
        System.out.printf("Estado: %s\n", datosArriendo[3]);
        System.out.printf("Rut cliente: %s\n", datosArriendo[4]);
        System.out.printf("Nombre cliente: %s\n", datosArriendo[5]);
        System.out.printf("Monto total: $%s\n", datosArriendo[6]);
        System.out.println("----------------------------------");
        System.out.println("\t\t\tDETALLE DEL ARRIENDO");
        System.out.println("----------------------------------");
        String[][] datosDetalles = controlado.listaDetallesArriendos(codigo);
        // No es necesario revisar que el arriendo exista ya que en cosultaArriendo se reviso
        System.out.printf("%-13s %-25s %-23s\n", "Codigo Equipo", "Descripcion equipo", "Precio arriendo por dia");
        for (String[] equipo : datosDetalles) {
            System.out.printf("%-13s %-25s %-23s\n", equipo[0], equipo[1], equipo[2]);
        }

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
