package Modelo;

import java.io.Serializable;
import java.time.Period;
import java.util.ArrayList;
import java.time.LocalDate;

public class Arriendo implements Serializable {
    private long codigo;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucion;
    private EstadoArriendo estado;
    private Cliente cliente;
    private ArrayList<DetalleArriendo> detallesArriendo;

    private ArrayList<Pago> pagos;
    private ArrayList<Pago> contado;
    private ArrayList<Pago> debito;
    private ArrayList<Pago> credito;

    public Arriendo(long codigo, LocalDate fechaInicio, Cliente cliente) {
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.cliente = cliente;
        this.estado = EstadoArriendo.INICIADO;
        this.detallesArriendo = new ArrayList<>();
        this.pagos = new ArrayList<>();
        this.contado = new ArrayList<>();
        this.debito = new ArrayList<>();
        this.credito = new ArrayList<>();
        cliente.addArriendo(this);
    }

    public long getCodigo() {
        return codigo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public EstadoArriendo getEstado() {
        return estado;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setEstado(EstadoArriendo estado) {
        this.estado = estado;
    }

    public void addDetalleArriendo(Equipo equipo) {
        DetalleArriendo detalle = new DetalleArriendo(equipo.getPrecioArriendoDia(), equipo, this);
        detallesArriendo.add(detalle);
    }

    public void addPagoContado(Contado pago) {
        if (!contado.contains(pago)) {
            contado.add(pago);
        }
    }

    public void addPagoDebito(Debito pago) {
        if (!debito.contains(pago)) {
            debito.add(pago);
        }
    }

    public void addPagoCredito(Credito pago) {
        if (!credito.contains(pago)) {
            credito.add(pago);
        }
    }

    public String[][] getPagosToString() {
        int i = 0;
        String[][] pagosTotal;
        pagos.addAll(contado);
        pagos.addAll(debito);
        pagos.addAll(credito);
        if (pagos.size() != 0) {
            pagosTotal = new String[pagos.size()][3];
            for (Pago pago : pagos) {
                pagosTotal[i][0] = pago.getMonto() + "";
                pagosTotal[i][1] = pago.getFecha() + "";
                pagosTotal[i][2] = pago.getClass().getName();
            }
            return pagosTotal;
        } else {
            return new String[0][0];
        }

    }

    public long getSaldoAdeudado() {
        return getMontoTotal() - getMontoPagado();
    }

    public long getMontoPagado() {
        int pago = 0;
        for (Pago monto : pagos) {
            pago += monto.getMonto();
        }
        return pago;
    }

    public int getNumeroDiasArriendo() {
        //trabajar con metodos LocalDate.
        if (estado == EstadoArriendo.DEVUELTO) {
            if (fechaInicio.equals(fechaDevolucion)) {
                return 1;
            } else {
                Period period = Period.between(fechaInicio, getFechaDevolucion());
                return period.getDays();
            }
        } else {
            return 0;
        }
    }

    public long getMontoTotal() {

        long total = 0;

        if (estado.equals(EstadoArriendo.DEVUELTO)) {

            for (DetalleArriendo detalleArriendo : detallesArriendo) {
                total += getNumeroDiasArriendo() * detalleArriendo.getEquipo().getPrecioArriendoDia();
            }
            return total;
        }
        if (estado.equals(EstadoArriendo.ENTREGADO)) {

            for (DetalleArriendo detalleArriendo : detallesArriendo) {
                total += getNumeroDiasArriendo() * detalleArriendo.getEquipo().getPrecioArriendoDia();
            }
            return total;
        }
        return 0;

    }

    public String[][] getDetallesToString() {

        String[][] arr = new String[detallesArriendo.size()][3];

        if (estado == EstadoArriendo.INICIADO) {
            return new String[0][0];
        }

        if (estado == EstadoArriendo.ENTREGADO || estado == EstadoArriendo.DEVUELTO) {

            int i = 0;
            int j = 0;
            for (DetalleArriendo detalleArriendo : detallesArriendo) {
                arr[i][j] = String.valueOf(detalleArriendo.getEquipo().getCodigo());
                j++;
                arr[i][j] = detalleArriendo.getEquipo().getDescripcion();
                j++;
                arr[i][j] = String.valueOf(detalleArriendo.getEquipo().getPrecioArriendoDia());
            }
        }

        return arr;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Equipo[] getEquipos() {
        ArrayList<Equipo> equipos = new ArrayList<>();
        for (DetalleArriendo detalleArriendo : detallesArriendo) {
            equipos.add(detalleArriendo.getEquipo());
        }
        return equipos.toArray(new Equipo[0]);
    }
}
