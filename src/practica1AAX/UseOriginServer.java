package practica1AAX;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class UseOriginServer {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(3333);
		System.out.println("SERVER ORIGIN INICIADO...");
		while (true) {

			Socket socket = serverSocket.accept();
			System.out.println("CLIENTE CONECTADO: " + socket.getInetAddress().getHostAddress());
			new OriginServer(socket).start();

		}

	}
}