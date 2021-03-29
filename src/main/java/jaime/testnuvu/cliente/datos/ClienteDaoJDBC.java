package jaime.testnuvu.cliente.datos;

import jaime.testnuvu.cliente.Cliente;
import jaime.testnuvu.cliente.TarjetaCredito;
import jaime.testnuvu.cliente.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author jaimeMejia
 */
public class ClienteDaoJDBC {

    private static final String SQL_SELECT_BY_ID = "SELECT ID, TIPO_DOCUMENTO, NOMBRES_CLIENTE, APELLIDOS_CLIENTE, DIRECCION, CIUDAD FROM CLIENTE WHERE ID = ? ";
    private static final String SQL_SELECT_TRC_ID = "SELECT ID, BANCO, NUMERO_TARJETA, FECHA_EXPIRACION, CVC_CODE, ID_CLIENTE FROM TARJETA_CREDITO WHERE ID_CLIENTE = ?";
    private static final String SQL_INSERT = "INSERT INTO CLIENTE (TIPO_DOCUMENTO, NOMBRES_CLIENTE, APELLIDOS_CLIENTE, DIRECCION, CIUDAD) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_TRC = "INSERT INTO TARJETA_CREDITO (BANCO, NUMERO_TARJETA, FECHA_EXPIRACION, CVC_CODE, ID_CLIENTE) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE CLIENTE SET TIPO_DOCUMENTO = ?, NOMBRES_CLIENTE = ?, APELLIDOS_CLIENTE = ?, DIRECCION = ?, CIUDAD = ? WHERE ID = ?";
    private static final String SQL_UPDATE_TRC = "UPDATE TARJETA_CREDITO SET BANCO = ?, NUMERO_TARJETA = ?, FECHA_EXPIRACION = ?, CVC_CODE = ? WHERE ID = ?";
    private static final String SQL_SELECT_ID_CL = "SELECT (SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'testnuvu' AND  TABLE_NAME   = 'cliente')-1 AS id;";
    private static final String SQL_SELECT_USUARIO = "SELECT ID, USUARIO, PASSWORD, TOKEN, ULTIMO_ACCESO, VIGENCIA_ACCESO FROM USUARIO WHERE UPPER(USUARIO) = UPPER(?) AND PASSWORD = ?";
    private static final String SQL_UPDATE_USUARIO = "UPDATE USUARIO SET ULTIMO_ACCESO = ?, VIGENCIA_ACCESO = ?, TOKEN = ? WHERE ID = ?";
    private static final String SQL_SELECT_TOKEN = "SELECT ID, USUARIO, PASSWORD, TOKEN, ULTIMO_ACCESO, VIGENCIA_ACCESO FROM USUARIO WHERE TOKEN = ?";

    public List<TarjetaCredito> getTarjetas(Long id_cliente) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<TarjetaCredito> listTarjetaCredito = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_TRC_ID);
            stmt.setLong(1, id_cliente);
            rs = stmt.executeQuery();
            while (rs.next()) {

                Long id = rs.getLong("ID");
                String banco = rs.getString("BANCO");
                String numeroTarjeta = rs.getString("NUMERO_TARJETA");
                String fechaExpiracion = rs.getString("FECHA_EXPIRACION");
                String cvcCode = rs.getString("CVC_CODE");
                Long idCliente = rs.getLong("ID_CLIENTE");
                TarjetaCredito tarjetaCredito = new TarjetaCredito(id, banco, numeroTarjeta, fechaExpiracion, cvcCode, idCliente);
                listTarjetaCredito.add(tarjetaCredito);
            }
        } finally {
            Conexion.close(rs);
            stmt.close();
            conn.close();
        }

        return listTarjetaCredito;
    }

    public Cliente getCliente(Long idCliente) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cliente cliente = null;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setLong(1, idCliente);
            rs = stmt.executeQuery();

            if (rs.absolute(1)) {

                Long id = rs.getLong("ID");
                String tipoDocumento = rs.getString("TIPO_DOCUMENTO");
                String nombresCliente = rs.getString("NOMBRES_CLIENTE");
                String apellidosCliente = rs.getString("APELLIDOS_CLIENTE");
                String direccion = rs.getString("DIRECCION");
                String ciudad = rs.getString("CIUDAD");

                cliente = new Cliente(id, tipoDocumento, nombresCliente, apellidosCliente, direccion, ciudad);
            }
        } finally {
            Conexion.close(rs);
            stmt.close();
            conn.close();
        }
        return cliente;
    }

    public int insertarCliente(Cliente cliente) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {

            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, cliente.getTipoDocumento());
            stmt.setString(2, cliente.getNombresCliente());
            stmt.setString(3, cliente.getApellidosCliente());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getCiudad());

            rows = stmt.executeUpdate();

        } finally {
            stmt.close();
            conn.close();
        }
        return rows;
    }

    public int insertarTarjeta(Integer idCliente, TarjetaCredito tarjetaCredito) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {

            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_TRC);
            stmt.setString(1, tarjetaCredito.getBanco());
            stmt.setString(2, tarjetaCredito.getNumeroDeTarjeta());
            stmt.setString(3, tarjetaCredito.getFechaExpiracion());
            stmt.setString(4, tarjetaCredito.getCvcCode());
            stmt.setLong(5, idCliente.longValue());

            rows = stmt.executeUpdate();

        } finally {
            stmt.close();
            conn.close();
        }
        return rows;
    }

    public int updateCliente(Cliente cliente) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, cliente.getTipoDocumento());
            stmt.setString(2, cliente.getNombresCliente());
            stmt.setString(3, cliente.getApellidosCliente());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getCiudad());

            stmt.setLong(6, cliente.getId());

            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDaoJDBC.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return rows;
    }

    public int updateTarjeta(TarjetaCredito tarjetaCredito) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_TRC);
            stmt.setString(1, tarjetaCredito.getBanco());
            stmt.setString(2, tarjetaCredito.getNumeroDeTarjeta());
            stmt.setString(3, tarjetaCredito.getFechaExpiracion());
            stmt.setString(4, tarjetaCredito.getCvcCode());
            stmt.setLong(5, tarjetaCredito.getId());

            rows = stmt.executeUpdate();

        } finally {
            stmt.close();
            conn.close();

        }
        return rows;
    }

    public Integer getIdCliente() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_ID_CL);
            rs = stmt.executeQuery();
            if (rs.absolute(1)) {
                id = rs.getInt("id");
            }
        } finally {
            Conexion.close(rs);
            stmt.close();
            conn.close();
        }
        return id;
    }

    public Usuario getUsuario(Usuario usuario) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
        Usuario user = new Usuario();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_USUARIO);
            stmt.setString(1, usuario.getUsuario());
            stmt.setString(2, usuario.getPassword());
            rs = stmt.executeQuery();

            if (rs.absolute(1)) {
                Long id = rs.getLong("ID");
                String us = rs.getString("USUARIO");
                String password = rs.getString("PASSWORD");
                DateTime ultimoAcceso = dtf.parseDateTime(rs.getString("ULTIMO_ACCESO"));
                DateTime vigenciaAcceso = dtf.parseDateTime(rs.getString("VIGENCIA_ACCESO"));

                user = new Usuario(id, us, password, ultimoAcceso, vigenciaAcceso);
            } else {
                user = null;
            }
        } finally {
            Conexion.close(rs);
            stmt.close();
            conn.close();
        }
        return user;
    }

    public int actualizaAcceso(Usuario usuario) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_USUARIO);
            stmt.setString(1, dtf.print(DateTime.now()));
            stmt.setString(2, dtf.print(DateTime.now().plusHours(1)));
            stmt.setString(3, usuario.getToken());
            stmt.setLong(4, usuario.getId());

            rows = stmt.executeUpdate();

        } finally {
            stmt.close();
            conn.close();
        }
        return rows;
    }

    public Usuario validToken(String token) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
        Usuario usuario = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_TOKEN);
            stmt.setString(1, token);
            rs = stmt.executeQuery();

            if (rs.absolute(1)) {

                usuario = new Usuario();
                usuario.setId(rs.getLong("ID"));
                usuario.setUsuario(rs.getString("USUARIO"));
                usuario.setPassword(rs.getString("PASSWORD"));
                usuario.setToken(token);
                usuario.setUltimoAcceso(dtf.parseDateTime(rs.getString("ULTIMO_ACCESO")));
                usuario.setVigencia(dtf.parseDateTime(rs.getString("VIGENCIA_ACCESO")));
            }

        } finally {
            Conexion.close(rs);
            stmt.close();
            conn.close();
        }

        return usuario;
    }
}
