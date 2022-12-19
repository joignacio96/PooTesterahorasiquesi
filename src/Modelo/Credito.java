package Modelo;

import java.time.LocalDate;

public class Credito extends Pago {
    private String codTransaccion;
    private String numTarjeta;
    private int nroCuotas;
    public Credito(long monto, LocalDate fecha, String codTrans, String numTarj, int nroCtas) {
        super(monto, fecha);
        this.codTransaccion=codTrans;
        this.numTarjeta=numTarj;
        this.nroCuotas=nroCtas;
    }

    public String getCodTransaccion() {
        return codTransaccion;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public int getNroCuotas() {
        return nroCuotas;
    }
}
