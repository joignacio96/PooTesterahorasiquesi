package Modelo;

import java.time.LocalDate;

public class Debito extends Pago{
    private String codTransaccion;
    private String numTarjeta;

    public Debito(long monto, LocalDate fecha, String codTrans, String numTarj) {
        super(monto, fecha);
        this.codTransaccion=codTrans;
        this.numTarjeta=numTarj;
    }

    public String getCodTransaccion() {
        return codTransaccion;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }
}
