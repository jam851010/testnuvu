package jaime.testnuvu.cliente;

/**
 *
 * @author jaimeMejia
 */
public interface ClienteProvider {

    ClienteStatus registroCliente(final Cliente cliente);

    ClienteStatus consultaCliente(final Cliente cliente);

    ClienteStatus editarCliente(final Cliente cliente);

    ClienteStatus obtenerToken(final Usuario usuario);

    boolean validaToken(final String token);

}
