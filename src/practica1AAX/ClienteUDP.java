package practica1AAX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;

public class ClienteUDP {
	private String IP = "";
	private int puerto = 0;

	public ClienteUDP(String IP, int puerto) {
		this.IP = IP;
		this.puerto = puerto;

	}

	public void run() {

		DatagramSocket socketUDP = null;
		byte[] buffer = new byte[1024];
		InetAddress addressIP = null;
		Scanner teclado = new Scanner(System.in);

		try {

			System.out.print("INTRODUCE TU REGION: ");

			/* ENVIAMOS MENSAJE */
			socketUDP = new DatagramSocket();
			addressIP = InetAddress.getByName(IP);
			String mensaje = teclado.next();
			buffer = mensaje.getBytes();
			DatagramPacket serverUbi = new DatagramPacket(buffer, buffer.length, addressIP, puerto);
			socketUDP.send(serverUbi);

			/* RECIVIMOS IP Y PUERTO DEL SERVIDOR EDGE O DEL ORIGIN */

			byte[] new_buffer = new byte[1024];
			DatagramPacket respuestaServerUDP = new DatagramPacket(new_buffer, new_buffer.length);
			socketUDP.receive(respuestaServerUDP);
			String mensajeServerUDP = new String(respuestaServerUDP.getData());

			String[] partesString = mensajeServerUDP.split("-"); // DIVIDIMOS LOS DATOS

			String ipMensajeServerUDP = partesString[0]; // IP DEL SERVER

			String puertoMensajeServerUPD = partesString[1];
			int puertoServer = Integer.parseInt(puertoMensajeServerUPD); // PUERTO DEL SERVER

			if (puertoServer == 5555)
				System.out.println("SE TE HA PROPORCIONADO CONEXION CON EL EDGE SERVER DE MADRID");
			if (puertoServer == 2222)
				System.out.println("SE TE HA PROPORCIONADO CONEXION CON EL EDGE SERVER DE BERLIN");
			if (puertoServer == 1111)
				System.out.println("SE TE HA PROPORCIONADO CONEXION CON EL EDGE SERVER DE CHICAGO");
			if (puertoServer == 3333)
				System.out.println("SE TE HA PROPORCIONADO CONEXION CON EL ORIGIN SERVER");

			menu(); // MENU DEL CLIENTE UDP

			int opcion;

			do {
				System.out.print("OPCION: ");
				opcion = teclado.nextInt();
				switch (opcion) {

				case 1:
					/* INICIAMOS CLIENTE TCP */
					System.out.println("CONECTANDO CON SERVIDOR...");
					ClienteTCP clienteTCP = new ClienteTCP(ipMensajeServerUDP, puertoServer);
					clienteTCP.run();
					break;

				case 2:
					System.out.println("LA IP DEL SERVER ES: " + mensajeServerUDP);
					break;

				case 3:
					System.out.println("CONECTANDO CON SERVIDOR...");
					ClienteSubirArchivo clienteSubir = new ClienteSubirArchivo("192.168.1.46", 3333);
					clienteSubir.run();
					break;

				case 4:
					socketUDP.close();
					System.out.println("TE HAS DESCONECTADO");
					break;

				}
			} while (opcion != 4);

		} catch (SocketException e) {
			System.out.println("NO ES HA PODIDO ESCUCHAR EN EL PUERTO: " + puerto);
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public void menu() {
		System.out.println("\t******************");
		System.out.println("\tMENU DEL CLIENTE\n");
		System.out.println("\t******************");
		System.out.println("\t1. CONECTAR CON SERVIDOR");
		System.out.println("\t2. VER IP DEL SERVIDOR");
		System.out.println("\t3. SUBIR ARCHIVO AL ORIGIN SERVER");
		System.out.println("\t4. DESCONECTAR\n");

	}
}
