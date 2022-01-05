package practica1AAX;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class UseEdgeServer {
	public static void main(String[] args) throws IOException {
		int opcion, puerto = 0;
		Scanner teclado = new Scanner(System.in);
		menu();
		System.out.print("ELIGE EL PUERTO DONDE QUIERES QUE ESCUCHE: ");
		opcion = teclado.nextInt();
		switch (opcion) {
		case 1:
			System.out.println("PUERTO EN MADRID: 5555");
			puerto = 5555;
			break;

		case 2:
			System.out.println("PUERTO EN BERLIN: 2222");
			puerto = 2222;
			break;

		case 3:
			System.out.println("PUERTO EN CHICAGO: 1111");
			puerto = 1111;
			break;

		}
		ServerSocket serverSocket = new ServerSocket(puerto);
		System.out.println("SERVER EDGE INICIADO...");
		while (true) {

			Socket socket = serverSocket.accept();
			System.out.println("CLIENTE CONECTADO: " + socket.getInetAddress().getHostAddress());
			new EdgeServer(socket).start();

		}

	}

	public static void menu() {

		System.out.println("1.MADRID");
		System.out.println("2.ALEMANIA");
		System.out.println("3.CHICAGO");

	}
}
