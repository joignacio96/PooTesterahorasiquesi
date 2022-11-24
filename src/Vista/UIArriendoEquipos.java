package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.Cliente;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UIArriendoEquipos {
    private static UIArriendoEquipos instance = null;
    private Scanner teclado;

    private UIArriendoEquipos() {
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
                opcion = teclado.nextInt();
                System.out.println("");
            }catch (InputMismatchException e){
                System.out.println("\nError: ");
            }


            switch (opcion) {
                case 1 -> creaCliente();
                case 2 -> creaEquipo();
                case 3 -> arriendaEquipos();
                case 4 -> devuelveEquipos();
                case 5 -> cambiaEstadoCliente();
                case 6 -> listaClientes();
                case 7 -> listaEquipos();
                case 8 -> listaArriendos();
                case 9 -> listaDetallesArriendo();
                case 10 -> System.exit(2);
                default -> System.out.println("\nIngreso no valido");
            }
        } while (opcion != 10);
        teclado.close();
    }
    public void devuelveEquipos(){
        String rut, code;
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
            if(code.isEmpty()){
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
            System.out.printf("%1$-15s%2$-30s%3$-20s%4$-15s%n", "Codigo", "Descripcion", "Precio");
            for (String[] listado : datosEquipos) {
                System.out.printf("%1$-15s%2$-30s%3$-20s%4$-15s%n", listado[0], listado[1], listado[2], listado[3]);
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
        if (rut.isBlank() || rut.isEmpty()) {
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
        }

        try {
            ControladorArriendoEquipos.getInstance().creaEquipo(codigo, descripcion, precioArriendoDia);
        } catch (EquipoException e) {
            System.out.println("Error creando el equipo, intentelo de nuevo");
        }
    }

    public void arriendaEquipos() throws ClienteException, EquipoException, ArriendoException {
        System.out.println("Ingrese rut");
        String rut = teclado.next();
        //validacion rut
        boolean validacion = false;
        try {
            rut = rut.toUpperCase();
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

        } catch (NumberFormatException e) {
            System.out.println("Error!: rut inválido" + e);
        } catch (Exception e) {
            System.out.println("Error!: rut inválido" + e);
        }
        //fin validacion
        if (validacion == true) {
            //validar si existe cliente
            if (ControladorArriendoEquipos.getInstance().consultaCliente(rut).length > 0) {

            }
            String nombreCliente;
            nombreCliente = datos[2];
            long codigo = ControladorArriendoEquipos.getInstance().creaArriendo(rut);
            do {
                System.out.println("Ingrese el codigo del equipo, si desea terminar, ingrese 0");
                long codigoEquipo = teclado.nextLong();
                if ()
                    //validacion long

                    //fin validacion
                    System.out.println("Agregando equipo al arriendo...");
                ControladorArriendoEquipos.getInstance().agregaEquipoToArriendo(codigo, codigoEquipo);
                ControladorArriendoEquipos.getInstance().consultaEquipo();

            } while (codigoEquipo != 0);
        }


    }


    public void cambiaEstadoCliente() {
        System.out.println("Ingrese rut");
        String rut = teclado.next();
        try {
            if (ControladorArriendoEquipos.getInstance().consultaCliente(rut).length > 0) {
                ControladorArriendoEquipos.getInstance().cambiaEstadoCliente(rut);
            }

        } catch (ClienteException ex) {
            throw new RuntimeException(ex);
        }
    } catch(
    NumberFormatException e)

    {
        System.out.println("Error!: rut inválido" + e);
    } catch(
    Exception e)

    {
        System.out.println("Error!: rut inválido" + e);
    }
        if(validacion=true)

    {
        //validar si existe cliente
        String[] datos;
        datos = ControladorArriendoEquipos.getInstance().consultaCliente(rut);

    }


}


    }

            }

