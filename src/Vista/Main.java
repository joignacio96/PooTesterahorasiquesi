package Vista;

import Excepciones.EquipoException;

public class Main {
    public static void main(String[]args) throws EquipoException {
        UIArriendoEquipos.getInstance().menu();
    }
}