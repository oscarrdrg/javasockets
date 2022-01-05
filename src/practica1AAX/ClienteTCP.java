package practica1AAX;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteTCP {

	private String IP = "";
	private int puerto = 0;

	public ClienteTCP(String IP, int puerto) {
		this.IP = IP;
		this.puerto = puerto;
	}

	public void run() {

		Socket socket = null;
		DataOutputStream out = null;
		DataInputStream in = null;

		try {
			socket = new Socket(IP, puerto);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());

			BufferedReader tecladoTCP = new BufferedReader(new InputStreamReader(System.in));
			String archivoBuscado;

			if (puerto == 3333)
				out.writeUTF("DESCARGAR ARCHIVO");

			/* INTRODUCIMOS EL NOMBRE DEL ARCHIVO */
			System.out.print("INTRODUCE EL ARCHIVO QUE QUIERES DESCARGAR: ");
			archivoBuscado = tecladoTCP.readLine();

			out.writeUTF(archivoBuscado); // LO ENVIAMOS AL SERVER EDGE
			String mensajeServerEdge = in.readUTF(); // NOS LLEGA UNA RESPUESTA

			if (mensajeServerEdge.equals("LO TENGO")) {
				System.out.println("\nSERVER DICE: " + mensajeServerEdge);

				/* DESCARGA EL ARCHIVO */
				System.out.println("INICIANDO DESCARGA DEL ARCHIVO...");
				int tamañoArchivo = in.readInt();
				System.out.println(tamañoArchivo);
				FileOutputStream fos = new FileOutputStream("C:/Users/Oscar/Desktop/Cliente/" + archivoBuscado);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
				byte[] buffer = new byte[tamañoArchivo];

				bis.read(buffer, 0, buffer.length);
				bos.write(buffer, 0, buffer.length);
				bos.flush();

				bos.close();
				bis.close();

				System.out.println("DESCARGA COMPLETADA");

			} else {
				System.out.println("\nSERVER DICE: " + mensajeServerEdge);
				String mensajeNoTengoServer = in.readUTF();
				System.out.println(mensajeNoTengoServer);
				String ipOriginServer = in.readUTF(); // NOS ENVIA LA IP DEL SERVER ORIGIN
				System.out.println("\tIP: " + ipOriginServer);
				int puertoOriginServer = in.readInt(); // NOS ENVIA EL PUERTO DEL SERVER ORIGIN
				System.out.println("\tPUERTO: " + puertoOriginServer);

				/* EJECUTAMOS EL CLIENTE QUE SE CONECTA AL SERVER ORIGIN */
				ClienteEdgeOrigin cliente_origin = new ClienteEdgeOrigin(ipOriginServer, puertoOriginServer,
						archivoBuscado);
				cliente_origin.run();

			}

		} catch (UnknownHostException e) {
			System.err.println("NO SE ENCUENTRA EL HOST: " + IP);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + IP);
			System.exit(1);
		}

		try {
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			System.err.println("Close failed.");
			System.exit(1);
		}

	}

}
