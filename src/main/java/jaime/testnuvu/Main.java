package jaime.testnuvu;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import jaime.testnuvu.cliente.ClienteProviderFactory;

import java.io.IOException;
import java.net.URI;

/**
 *
 * @author jaimeMejia
 */
public class Main {

    public static final String BASE_URI = "http://162.241.132.14:8282/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("jaime.testnuvu");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 2 && args[0].equals("--token")) {
            ClienteProviderFactory.getInstance();
        }
        final HttpServer server = startServer();
        System.out.println("Jersey app started available at " + BASE_URI + "cliente/registro\n  " + BASE_URI + "cliente/actualiza \n  " + BASE_URI + "cliente/consulta  Hit enter to stop it...");
        System.in.read();
        server.shutdownNow();
    }
}
