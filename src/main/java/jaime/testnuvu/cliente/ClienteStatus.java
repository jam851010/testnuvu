package jaime.testnuvu.cliente;

/**
 *
 * @author jaimeMejia
 */
public class ClienteStatus {

    public int statusCode;
    public String statusMessage;
    public String details;
    public Cliente cliente;
    public Usuario usuario;

    public boolean success() {
        return statusCode == 200;
    }

}
