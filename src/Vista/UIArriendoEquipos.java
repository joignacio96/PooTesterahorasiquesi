package Vista;

import Controlador.ControladorArriendoEquipos;
import java.util.Scanner;

public class UIArriendoEquipos {
    private static UIArriendoEquipos instance=null;
    private Scanner teclado;
    private UIArriendoEquipos(){}
    public static UIArriendoEquipos getInstance() {
        if (instance == null) {
            instance = new UIArriendoEquipos();
        }
        return instance;
    }
    public void menu() {
        int opcion;
        System.out.println("******* SISTEMA DE ARRIENDO DE EQUIPOS DE NIEVE ****** ");
        do {
            System.out.println("\n\n\n***MENU DE OPCIONES***");
            System.out.println("1. Crea un nuevo cliente");
            System.out.println("2. Crea un nuevo equipo");
            System.out.println("3. Lista todos los clientes");
            System.out.println("4. Lista todos los equipos");
            System.out.println("5. Salir");
            System.out.println("\n\n\nIngrese opcion: ");
            opcion = teclado.nextInt();

            switch (opcion) {
                case 1 -> creaCliente();
                case 2 -> creaEquipo();
                case 3 -> listaClientes();
                case 4 -> listaEquipos();
                case 5 -> {}
                default -> System.out.println("\nIngreso no valido");
            }
        } while (opcion!=5);
        teclado.close();
    }

    private void listaClientes() {
        String [][] datosClientes= ControladorArriendoEquipos.getInstance().listaClientes();
        int i=0;
        if(datosClientes.length>0) {
            System.out.println("\nLISTADO DE CLIENTES");
            System.out.println("------------");
            System.out.println();
            System.out.printf("%1$-15s%2$-30s%3$-20s%4$-15s%n", "RUT", "Nombre", "Direccion", "Telefono");
            for (String[] listado : datosClientes) {

                System.out.printf("%-25s%-12s%10s%10sn", listado[0], listado[1], listado[2], listado[3], listado[4]);
                i++;
            }
        }else{
            System.out.println("\n No existen clientes");
        }
    }

    private void listaEquipos() {
        String [][] datosEquipos= ControladorArriendoEquipos.getInstance().listaEquipos();
        if(datosEquipos.length>0){
            System.out.println("\nLISTADO DE EQUIPOS");
            System.out.println("------------");
            System.out.printf("%1$-15s%2$-30s%3$-20s%4$-15s%n", "Codigo", "Descripcion", "Precio");
            for(String[] listado: datosEquipos){
                System.out.printf("%1$-15s%2$-30s%3$-20s%4$-15s%n", listado[0], listado[1], listado[2], listado[3]);
            }
        }else{
            System.out.println("\nNo existen equipos");
        }
    }

    private void creaCliente() {
        String rut,nom, dir, tel;
        System.out.println("Creando un nuevo cliente...");
        System.out.print("\nRut: ");
        rut = teclado.next();
        System.out.print("Nombre: ");
        nom = teclado.next();
        System.out.print("Domicilio: ");
        dir = teclado.next();
        System.out.println("Telefono: ");
        tel=teclado.next();
        ControladorArriendoEquipos.getInstance().creaCliente(rut,nom,dir,tel);
    }
    private void creaEquipo() {
        String descripcion;
        long codigo, precioArriendoDia;
        System.out.print("Creando un nuevo equipo... ");
        System.out.println("\n\n\nCodigo: ");
        codigo= teclado.nextLong();
        System.out.print("Descripcion: ");
        descripcion = teclado.next();
        System.out.print("Precio arriendo por dia: ");
        precioArriendoDia = teclado.nextLong();
        ControladorArriendoEquipos.getInstance().creaEquipo(codigo,descripcion,precioArriendoDia);
    }
}

