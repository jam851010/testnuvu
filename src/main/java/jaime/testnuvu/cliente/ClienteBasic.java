package jaime.testnuvu.cliente;

/**
 *
 * @author jaimeMejia
 */
public class ClienteBasic extends BaseClienteProvider {

    public ClienteBasic() {
    }

    private static class LazySingletonContainer {

        private static final ClienteBasic SINGLETON = new ClienteBasic();
    }

    public static ClienteBasic getInstance() {
        return LazySingletonContainer.SINGLETON;
    }

}
