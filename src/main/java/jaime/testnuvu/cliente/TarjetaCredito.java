package jaime.testnuvu.cliente;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author jaimeMejia
 */
@JsonRootName("tarjeta_credito")
public class TarjetaCredito {

    private Long id;
    @JsonProperty("banco")
    private String banco;
    @JsonProperty("numero_tarjeta")
    private String numeroDeTarjeta;
    @JsonProperty("fecha_expiracion")
    private String fechaExpiracion;
    @JsonProperty("cvc_code")
    private String cvcCode;
    @JsonProperty("id_cliente")
    private Long idCliente;

    public TarjetaCredito(Long id, String banco, String numeroDeTarjeta, String fechaExpiracion, String cvcCode, Long idCliente) {
        this.id = id;
        this.banco = banco;
        this.numeroDeTarjeta = numeroDeTarjeta;
        this.fechaExpiracion = fechaExpiracion;
        this.cvcCode = cvcCode;
        this.idCliente = idCliente;
    }

    public TarjetaCredito() {
    }

    /**
     * @return the banco
     */
    public String getBanco() {
        return banco;
    }

    /**
     * @param banco the banco to set
     */
    public void setBanco(String banco) {
        this.banco = banco;
    }

    /**
     * @return the numeroDeTarjeta
     */
    public String getNumeroDeTarjeta() {
        return numeroDeTarjeta;
    }

    /**
     * @param numeroDeTarjeta the numeroDeTarjeta to set
     */
    public void setNumeroDeTarjeta(String numeroDeTarjeta) {
        this.numeroDeTarjeta = numeroDeTarjeta;
    }

    /**
     * @return the fechaExpiracion
     */
    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    /**
     * @param fechaExpiracion the fechaExpiracion to set
     */
    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    /**
     * @return the cvcCode
     */
    public String getCvcCode() {
        return cvcCode;
    }

    /**
     * @param cvcCode the cvcCode to set
     */
    public void setCvcCode(String cvcCode) {
        this.cvcCode = cvcCode;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the idCliente
     */
    public Long getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("banco:").append(banco);
        sb.append("|numeroDeTarjeta:").append(numeroDeTarjeta);
        sb.append("|fechaExpiracion:").append(fechaExpiracion);
        sb.append("|cvcCode:").append(cvcCode);
        return sb.toString();
    }

    /**
     * @return validate of fields
     */
    public String validate() {
        if (isNotEmpty(banco) && isNotEmpty(numeroDeTarjeta) && isNotEmpty(fechaExpiracion)
                && isNotEmpty(cvcCode)) {
            return null;
        }

        return "Fields tipo_documento, nombres_cliente, apellidos_cliente, direccion, ciudad, tarjeta_credito, cannot be empty";
    }

    static boolean isNotEmpty(String field) {
        return field != null && field.trim().length() > 0;
    }

}
