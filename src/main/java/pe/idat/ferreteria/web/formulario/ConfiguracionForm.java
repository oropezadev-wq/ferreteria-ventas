package pe.idat.ferreteria.web.formulario;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pe.idat.ferreteria.configuracion.ParametrosTienda;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Datos editables de la pantalla /configuracion. Es un DTO separado de {@link ParametrosTienda}: tiene setters
 * para el enlace de datos de Spring y la tasa de IGV se escribe en porcentaje (18 = 0.18).
 */
public class ConfiguracionForm {

    private static final BigDecimal CIEN = new BigDecimal("100");

    @NotBlank(message = "Ingrese la razón social")
    @Size(max = ParametrosTienda.LARGO_MAXIMO_RAZON_SOCIAL, message = "Máximo {max} caracteres")
    private String razonSocial;

    @NotBlank(message = "Ingrese el RUC")
    @Pattern(regexp = "^(10|15|17|20)\\d{9}$", message = "11 dígitos que empiecen con 10, 15, 17 o 20")
    private String ruc;

    @NotBlank(message = "Ingrese la dirección")
    @Size(max = ParametrosTienda.LARGO_MAXIMO_DIRECCION, message = "Máximo {max} caracteres")
    private String direccion;

    @NotBlank(message = "Ingrese el teléfono")
    @Pattern(regexp = "^[0-9 +()-]{6,20}$", message = "Entre 6 y 20 caracteres: dígitos, espacios, +, - o paréntesis")
    private String telefono;

    @NotNull(message = "Ingrese la tasa de IGV")
    @DecimalMin(value = "0.00", message = "No puede ser negativa")
    @DecimalMax(value = "30.00", message = "Máximo 30 %")
    @Digits(integer = 2, fraction = 2, message = "Hasta 2 decimales")
    private BigDecimal tasaIgvPorcentaje;

    @NotNull(message = "Ingrese el umbral de stock bajo")
    @Min(value = 0, message = "No puede ser negativo")
    @Max(value = ParametrosTienda.UMBRAL_STOCK_MAXIMO, message = "Máximo {value}")
    private Integer umbralStockBajo;

    public static ConfiguracionForm desde(ParametrosTienda parametros) {
        ConfiguracionForm formulario = new ConfiguracionForm();
        formulario.razonSocial = parametros.razonSocial();
        formulario.ruc = parametros.ruc();
        formulario.direccion = parametros.direccion();
        formulario.telefono = parametros.telefono();
        formulario.tasaIgvPorcentaje = parametros.tasaIgv().multiply(CIEN).setScale(2, RoundingMode.HALF_UP);
        formulario.umbralStockBajo = parametros.umbralStockBajo();
        return formulario;
    }

    /** Nuevos parámetros a partir de los actuales; lanza IllegalArgumentException si no son válidos. */
    public ParametrosTienda aParametros(ParametrosTienda actuales) {
        BigDecimal tasa = tasaIgvPorcentaje.divide(CIEN, 4, RoundingMode.HALF_UP);
        return actuales.conDatosEditables(razonSocial, ruc, direccion, telefono, tasa, umbralStockBajo);
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public BigDecimal getTasaIgvPorcentaje() {
        return tasaIgvPorcentaje;
    }

    public void setTasaIgvPorcentaje(BigDecimal tasaIgvPorcentaje) {
        this.tasaIgvPorcentaje = tasaIgvPorcentaje;
    }

    public Integer getUmbralStockBajo() {
        return umbralStockBajo;
    }

    public void setUmbralStockBajo(Integer umbralStockBajo) {
        this.umbralStockBajo = umbralStockBajo;
    }
}
