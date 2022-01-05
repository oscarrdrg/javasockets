package practica1AAX;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerHub {

	private int puerto = 0;

	public ServerHub(int puerto) {
		this.puerto = puerto;
	}

	public void run() {

		DatagramSocket socket = null;
		byte[] buffer = new byte[1024];

		try {
			System.out.println("INICIADO EL SERVIDOR HUB...");
			socket = new DatagramSocket(puerto);

			/* RECIVIMOS PETICION DEL CLIENTE */
			DatagramPacket petitionClientUDP = new DatagramPacket(buffer, buffer.length);
			socket.receive(petitionClientUDP);
			String mensajeClientUDP = new String(petitionClientUDP.getData());

			String[] partesString = mensajeClientUDP.split("-"); // DIVIDIMOS LOS DATOS
			String regionMensaje = partesString[0];
			String paisMensaje = partesString[1];

			System.out.println("CLIENTE CONECTADO DESDE: " + paisMensaje);

			/* COGEMOS IP Y PUERTO DEL CLIENTE */
			int puertoClienteUDP = petitionClientUDP.getPort();
			InetAddress ipClienteUDP = petitionClientUDP.getAddress();

			/* ENVIAMOS LA RESPUESTA */

			String mensajeUbiServerUDPMadrid = "192.168.1.44-5555-";
			String mensajeUbiServerUDPBerlin = "192.168.1.46-2222-";
			String mensajeUbiServerUDPChicago = "192.168.1.46-1111-";
			String mensajeUbiServerUDPOrigin = "192.168.1.46-3333-";

			DatagramPacket mensajeServerEdge;

			if (regionMensaje.equals("Europa") || regionMensaje.equals("America")) {

				if (paisMensaje.equals("España")) {
					buffer = mensajeUbiServerUDPMadrid.getBytes();
					mensajeServerEdge = new DatagramPacket(buffer, buffer.length, ipClienteUDP, puertoClienteUDP);
					socket.send(mensajeServerEdge);

				} else {

					if (paisMensaje.equals("Alemania")) {
						buffer = mensajeUbiServerUDPBerlin.getBytes();
						mensajeServerEdge = new DatagramPacket(buffer, buffer.length, ipClienteUDP, puertoClienteUDP);
						socket.send(mensajeServerEdge);

					} else {

						if (paisMensaje.equals("EstadosUnidos")) {
							buffer = mensajeUbiServerUDPChicago.getBytes();
							mensajeServerEdge = new DatagramPacket(buffer, buffer.length, ipClienteUDP,
									puertoClienteUDP);
							socket.send(mensajeServerEdge);
						} else {

							buffer = mensajeUbiServerUDPOrigin.getBytes();
							mensajeServerEdge = new DatagramPacket(buffer, buffer.length, ipClienteUDP,
									puertoClienteUDP);
							socket.send(mensajeServerEdge);

						}
					}

				}

			} else {

				buffer = mensajeUbiServerUDPOrigin.getBytes();
				mensajeServerEdge = new DatagramPacket(buffer, buffer.length, ipClienteUDP, puertoClienteUDP);
				socket.send(mensajeServerEdge);

			}
			
			socket.close();

		} catch (SocketException e) {
			System.out.println("NO SE HA PODIDO ESCUCHAR EN EL PUERTO: " + puerto);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
