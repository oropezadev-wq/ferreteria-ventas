package pe.idat.ferreteria.configuracion;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Patrón Singleton
public class ConfiguracionGlobal {
    private static ConfiguracionGlobal instancia;

    private String razonSocial = "Ferretería El Constructor S.A.C.";
    private String ruc = "20999999991";
    private String direccion = "Av. Los Constructores 123, Lima";
    private String telefono = "(01) 555-0123";
    private BigDecimal igv = new BigDecimal("18");
    private BigDecimal tipoCambio = new BigDecimal("3.75");

    private ConfiguracionGlobal() {
    }

    public static synchronized ConfiguracionGlobal getInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracionGlobal();
        }
        return instancia;
    }

    public void actualizar(String razonSocial, String ruc, String direccion, String telefono, BigDecimal igv,
                           BigDecimal tipoCambio) {
        if (razonSocial == null || razonSocial.isBlank()) {
            throw new IllegalArgumentException("Ingrese la razón social");
        }
        if (ruc == null || !ruc.matches("\\d{11}")) {
            throw new IllegalArgumentException("El RUC debe tener 11 dígitos");
        }
        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("Ingrese la dirección");
        }
        if (igv == null || igv.compareTo(BigDecimal.ZERO) < 0 || igv.compareTo(new BigDecimal("30")) > 0) {
            throw new IllegalArgumentException("El IGV debe estar entre 0 y 30 %");
        }
        if (tipoCambio == null || tipoCambio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El tipo de cambio debe ser mayor a cero");
        }
        this.razonSocial = razonSocial.trim();
        this.ruc = ruc;
        this.direccion = direccion.trim();
        this.telefono = telefono == null ? "" : telefono.trim();
        this.igv = igv;
        this.tipoCambio = tipoCambio;
    }

    // convierte un monto en dólares a soles con el tipo de cambio de la tienda
    public BigDecimal convertirASoles(BigDecimal dolares) {
        return dolares.multiply(tipoCambio).setScale(2, RoundingMode.HALF_UP);
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public BigDecimal getTipoCambio() {
        return tipoCambio;
    }
}
