package jaime.testnuvu.cliente;

import jaime.testnuvu.cliente.datos.ClienteDaoJDBC;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

/**
 *
 * @author jaimeMejia
 */
public class BaseClienteProvider implements ClienteProvider {

    ClienteDaoJDBC jdbc = new ClienteDaoJDBC();

    @Override
    public ClienteStatus registroCliente(Cliente cliente) {
        ClienteStatus status = new ClienteStatus();
        try {
            Integer id = null;
            if (jdbc.insertarCliente(cliente) == 1) {
                id = jdbc.getIdCliente();
                for (TarjetaCredito tarjetaCredito : cliente.getTarjetaCredito()) {
                    jdbc.insertarTarjeta(id, tarjetaCredito);
                }
            }
            status.statusCode = 200;
            status.details = "Cliente creado id: " + id;
            status.statusMessage = "Cliente creado";
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            status.details = e.getMessage();
            status.statusCode = 500;
            status.statusMessage = e.getSQLState();
        }
        return status;
    }

    @Override
    public ClienteStatus consultaCliente(Cliente cliente) {
        ClienteStatus clienteStatus = new ClienteStatus();

        try {
            clienteStatus.cliente = jdbc.getCliente(cliente.getId());
            if (clienteStatus.cliente != null) {
                List<TarjetaCredito> tarjetas = jdbc.getTarjetas(clienteStatus.cliente.getId());
                clienteStatus.cliente.setTarjetaCredito(tarjetas);
                clienteStatus.statusCode = 200;
                clienteStatus.details = "details Cliente id=" + cliente.getId() + " consultado satisfactoriamente";
                clienteStatus.statusMessage = "Mensaje Cliente  id=" + cliente.getId() + " satisfactoriamente";
            } else {
                clienteStatus.statusCode = 204;
                clienteStatus.details = "Cliente no encontrado";
                clienteStatus.statusMessage = "Cliente no encontrado";
            }
        } catch (SQLException ex) {
            clienteStatus.statusCode = 500;
            clienteStatus.details = ex.getMessage();
            clienteStatus.statusMessage = ex.getSQLState();
        }

        return clienteStatus;
    }

    @Override
    public ClienteStatus editarCliente(Cliente cliente) {
        ClienteStatus clienteStatus = new ClienteStatus();

        try {
            if (jdbc.updateCliente(cliente) > 0) {
                for (TarjetaCredito tarjetaCredito : cliente.getTarjetaCredito()) {
                    jdbc.updateTarjeta(tarjetaCredito);
                }
                clienteStatus.cliente = jdbc.getCliente(cliente.getId());
                clienteStatus.cliente.setTarjetaCredito(jdbc.getTarjetas(cliente.getId()));
                clienteStatus.statusCode = 200;
                clienteStatus.details = "Cliente editado satisfactoriamente";
                clienteStatus.statusMessage = "Cliente editado satisfactoriamente";
            } else {
                clienteStatus.statusCode = 204;
                clienteStatus.details = "Cliente no encontrado";
                clienteStatus.statusMessage = "Cliente no encontrado";
            }
        } catch (SQLException e) {
            clienteStatus.statusCode = 500;
            clienteStatus.details = e.getMessage();
            clienteStatus.statusMessage = e.getSQLState();
        }

        return clienteStatus;
    }

    @Override
    public ClienteStatus obtenerToken(Usuario usuario) {
        ClienteStatus clienteStatus = new ClienteStatus();
        String token = null;

        try {
            usuario = jdbc.getUsuario(usuario);
            DateTime dt = DateTime.now();
            if (usuario != null) {
                token = DigestUtils.sha256Hex(usuario.getPassword()+dt.getMillis()).toString();
                usuario.setToken(token);
                jdbc.actualizaAcceso(usuario);
                clienteStatus.statusCode = 200;
                clienteStatus.details = "Token generado con exito " + token;
                clienteStatus.statusMessage = "Token generado con exito " + token;
                clienteStatus.usuario = usuario;
            } else {
                clienteStatus.statusCode = 401;
                clienteStatus.details = "Credenciales invalidas";
                clienteStatus.statusMessage = "Credenciales invalidas";
            }

        } catch (SQLException e) {
            clienteStatus.statusCode = 500;
            clienteStatus.details = e.getMessage();
            clienteStatus.statusMessage = e.getSQLState();
        }

        return clienteStatus;
    }

    @Override
    public boolean validaToken(String token) {
        ClienteStatus clienteStatus = new ClienteStatus();
        Usuario usuario = null;
        DateTime ahora = DateTime.now();
        boolean valid = false;
        try {
            usuario = jdbc.validToken(token);
          
                if (usuario != null && usuario.getVigencia().isAfter(ahora)) {
                    valid = true;
                } else {
                    valid = false;
                }
            
        } catch (SQLException e) {
            clienteStatus.statusCode = 500;
            clienteStatus.details = e.getMessage();
            clienteStatus.statusMessage = e.getSQLState();
        }
        return valid;
    }

}
