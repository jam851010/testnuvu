package jaime.testnuvu;

import com.google.gson.Gson;
import jaime.testnuvu.cliente.Cliente;
import jaime.testnuvu.cliente.ClienteProviderFactory;
import jaime.testnuvu.cliente.ClienteStatus;
import jaime.testnuvu.cliente.Usuario;
import java.io.IOException;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author jaimeMejia
 */
@Path("cliente")
public class ProcesadorCliente {

    private ClienteProviderFactory clienteProviderFactory;

    public ProcesadorCliente() {
        this.clienteProviderFactory = getClienteProviderFactory();
    }

    ClienteProviderFactory getClienteProviderFactory() {
        return ClienteProviderFactory.getInstance();
    }

    @Path("oauth")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response oauth(final String usuarioData) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            final Usuario usuario = mapper.readValue(usuarioData, Usuario.class);
            final String validationError = usuario.validate();
            if (validationError != null) {
                return Response.notAcceptable(null).entity(validationError).build();
            }
            ClienteStatus status = clienteProviderFactory.getProvider().obtenerToken(usuario);

            if (!status.success()) {
                return Response.serverError().status(status.statusCode)
                        .entity(status.details).build();
            }

            Response.ResponseBuilder rb = Response.ok(status.details);
            Response response = rb.header("key", status.usuario.getToken())
                    .header("Access-Control-Allow-Origin", "*")
                    .build();

            return response;

        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @Path("registro")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registro(final String clienteData, @Context HttpHeaders headers) {
        ObjectMapper mapper = new ObjectMapper();
        if (clienteProviderFactory.getProvider().validaToken(getTokenHeader(headers))) {
            try {
                final Cliente cliente = mapper.readValue(clienteData, Cliente.class);
                final String validationError = cliente.validate();
                if (validationError != null) {
                    return Response.notAcceptable(null).entity(validationError).build();
                }

                ClienteStatus status = clienteProviderFactory.getProvider().registroCliente(cliente);
                if (!status.success()) {
                    return Response.serverError().status(status.statusCode)
                            .entity(status.details).build();
                }

                return Response.ok().entity(status.details).build();

            } catch (IOException e) {
                e.printStackTrace();
                return Response.serverError().entity(e.getMessage()).build();
            }
        } else {
            return Response.notAcceptable(null).entity("Token invalido o expirado").build();
        }

    }

    @Path("consulta")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consulta(final String clienteData, @Context HttpHeaders headers) {
        ObjectMapper mapper = new ObjectMapper();
        if (clienteProviderFactory.getProvider().validaToken(getTokenHeader(headers))) {
            try {
                final Cliente cliente = mapper.readValue(clienteData, Cliente.class);

                ClienteStatus status = clienteProviderFactory.getProvider().consultaCliente(cliente);
                if (!status.success()) {
                    return Response.serverError().status(status.statusCode)
                            .entity(status.details).build();
                }
                final Gson gson = new Gson();
                final String clienteString = gson.toJson(status.cliente);

                return Response.ok().entity(clienteString).build();
            } catch (IOException e) {
                // TODO logs, emit-metrics
                e.printStackTrace();
                return Response.serverError().entity(e.getMessage()).build();
            }
        } else {
            return Response.notAcceptable(null).entity("Token invalido o expirado").build();
        }
    }

    @Path("actualiza")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualiza(final String clienteData, @Context HttpHeaders headers) {
        ObjectMapper mapper = new ObjectMapper();
        if (clienteProviderFactory.getProvider().validaToken(getTokenHeader(headers))) {
            try {
                final Cliente cliente = mapper.readValue(clienteData, Cliente.class);
                final String validationError = cliente.validateUpdate();
                if (validationError != null) {
                    return Response.notAcceptable(null).entity(validationError).build();
                }

                ClienteStatus status = clienteProviderFactory.getProvider().editarCliente(cliente);
                if (!status.success()) {
                    return Response.serverError().status(status.statusCode)
                            .entity(status.details).build();
                }

                final Gson gson = new Gson();
                final String clienteString = gson.toJson(status.cliente);

                return Response.ok().entity(clienteString).build();
            } catch (IOException e) {
                e.printStackTrace();
                return Response.serverError().entity(e.getMessage()).build();
            }
        } else {
            return Response.notAcceptable(null).entity("Token invalido o expirado").build();
        }
    }

    private String getTokenHeader(@Context HttpHeaders headers) {

        String keyHeader = "";
        for (Map.Entry entry : headers.getRequestHeaders().entrySet()) {
            if (entry.getKey().equals("key") || entry.getKey().equals("KEY")) {
                keyHeader = entry.getValue().toString();
                break;
            }
        }
        return keyHeader.replaceAll("\\[", "").replaceAll("\\]", "");
    }

}
