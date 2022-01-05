package practica1AAX;

public class UseClienteTCP {
	public static void main(String[] args) {
		ClienteTCP objetoCliente = new ClienteTCP("192.168.1.42", 5555);
		objetoCliente.run();
	}

}
