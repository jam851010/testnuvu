package jaime.testnuvu.cliente;

/**
 *
 * @author jaimeMejia
 */
public class ClienteProviderFactory {

    public ClienteProviderFactory() {
    }

    private static class LazySingletonContainer {

        private static final ClienteProviderFactory SINGLETON = new ClienteProviderFactory();
    }

    public static ClienteProviderFactory getInstance() {
        return LazySingletonContainer.SINGLETON;
    }

    public ClienteProvider getProvider() {
        return ClienteBasic.getInstance();
    }

}
