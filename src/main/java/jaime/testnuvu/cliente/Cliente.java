package jaime.testnuvu.cliente;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author jaimeMejia
 */
@JsonRootName("cliente")
public class Cliente {

    private Long id;
    @JsonProperty("tipo_documento")
    private String tipoDocumento;
    @JsonProperty("nombres_cliente")
    private String nombresCliente;
    @JsonProperty("apellidos_cliente")
    private String apellidosCliente;
    private String direccion;
    private String ciudad;
    @JsonProperty(value = "tarjetas")
    @JsonDeserialize(using = TarjetaCreditoDeserializer.class)
    private List<TarjetaCredito> tarjetas = new ArrayList<>();

    public Cliente(String tipoDocumento, String nombresCliente, String apellidosCliente, String direccion, String ciudad) {
        this.tipoDocumento = tipoDocumento;
        this.nombresCliente = nombresCliente;
        this.apellidosCliente = apellidosCliente;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public Cliente(Long id, String tipoDocumento, String nombresCliente, String apellidosCliente, String direccion, String ciudad) {
        this.id = id;
        this.tipoDocumento = tipoDocumento;
        this.nombresCliente = nombresCliente;
        this.apellidosCliente = apellidosCliente;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public Cliente() {
    }

    /**
     * @return the tipoDocumento
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the tipoDocumento to set
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the nombresCliente
     */
    public String getNombresCliente() {
        return nombresCliente;
    }

    /**
     * @param nombresCliente the nombresCliente to set
     */
    public void setNombresCliente(String nombresCliente) {
        this.nombresCliente = nombresCliente;
    }

    /**
     * @return the apellidosCliente
     */
    public String getApellidosCliente() {
        return apellidosCliente;
    }

    /**
     * @param apellidosCliente the apellidosCliente to set
     */
    public void setApellidosCliente(String apellidosCliente) {
        this.apellidosCliente = apellidosCliente;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the ciudad
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * @param ciudad the ciudad to set
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * @return the tarjetaCredito
     */
    public List<TarjetaCredito> getTarjetaCredito() {
        if (tarjetas == null) {
            tarjetas = new ArrayList();
        }
        return tarjetas;
    }

    /**
     * @param tarjetaCredito the tarjetaCredito to set
     */
    public void setTarjetaCredito(List<TarjetaCredito> tarjetas) {
        this.tarjetas = tarjetas;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("tipoDocumento:").append(tipoDocumento);
        sb.append("|nombresCliente:").append(nombresCliente);
        sb.append("|apellidosCliente:").append(apellidosCliente);
        sb.append("|direccion:").append(direccion);
        sb.append("|ciudad:").append(ciudad);
        if (tarjetas != null && tarjetas.isEmpty()) {
            for (TarjetaCredito tarjetaCredito1 : tarjetas) {
                sb.append("|tarjetaCredito:").append(tarjetaCredito1.toString());
            }
        }
        return sb.toString();
    }

    /**
     * @return validate of fields
     */
    public String validate() {
        if (isNotEmpty(tipoDocumento) && isNotEmpty(nombresCliente) && isNotEmpty(apellidosCliente)
                && isNotEmpty(direccion) && isNotEmpty(ciudad) && tarjetas != null && !tarjetas.isEmpty()) {
            return null;
        }

        return "Fields tipo_documento, nombres_cliente, apellidos_cliente, direccion, ciudad, tarjeta_credito, cannot be empty";
    }

    public boolean validateTarjetas() {
        boolean val = true;
        if (tarjetas != null && !tarjetas.isEmpty()) {
            for (TarjetaCredito tarjeta : tarjetas) {
                if (tarjeta.getId() == null) {
                    val = false;
                }
            }
        }
        return val;
    }

    public String validateUpdate() {
        if (id != null && isNotEmpty(tipoDocumento) && isNotEmpty(nombresCliente) && isNotEmpty(apellidosCliente)
                && isNotEmpty(direccion) && isNotEmpty(ciudad) && tarjetas != null && !tarjetas.isEmpty() && validateTarjetas()) {
            return null;
        }

        return "Fields tipo_documento, nombres_cliente, apellidos_cliente, direccion, ciudad, tarjeta_credito, cannot be empty";
    }

    static boolean isNotEmpty(String field) {
        return field != null && field.trim().length() > 0;
    }

}

class TarjetaCreditoDeserializer extends JsonDeserializer<List<TarjetaCredito>> {

    @Override
    public List<TarjetaCredito> deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonParser);
        List<TarjetaCredito> tarjetas = mapper.convertValue(node.findValues("tarjeta_credito"), new TypeReference<List<TarjetaCredito>>() {
        });
        return tarjetas;
    }
}
