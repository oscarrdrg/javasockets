package practica1AAX;

public class UseClienteUDP {

	public static void main(String[] args) {
		ClienteUDP objetoCliente = new ClienteUDP("192.168.1.44", 4444);
		objetoCliente.run();
	}

}
