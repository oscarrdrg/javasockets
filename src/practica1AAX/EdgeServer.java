package practica1AAX;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;

public class EdgeServer extends Thread {

	Socket socket;

	public EdgeServer(Socket socket) {
		this.socket = socket;
	}

	public void run() {

		DataInputStream in = null;
		DataOutputStream out = null;

		try {
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());

			/* NOS LLEGA EL NOMBRE DEL ARCHIVO QUE QUIERE DESCARGAR */
			String archivo = in.readUTF(); // NOMBRE DEL ARCHIVO
			System.out.println("CLIENTE " + socket.getInetAddress().getHostAddress() + " QUIERE DESCARGAR: " + archivo);
			File archivoBuscado = new File("C:/Users/Oscar/Desktop/ServerEdge", archivo); // ARCHIVO
																							// QUE
																							// QUEREMOS
			System.out.println(archivoBuscado.getAbsolutePath());

			if (archivoBuscado.exists()) {
				out.writeUTF("LO TENGO");
				int tamañoArchivo = (int) archivoBuscado.length();
				out.writeInt(tamañoArchivo); // LE ENVIAMOS EL TAMAÑO
				FileInputStream fis = new FileInputStream(archivoBuscado);
				BufferedInputStream bis = new BufferedInputStream(fis);
				BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
				byte[] buffer = new byte[tamañoArchivo];
				bis.read(buffer, 0, buffer.length);

				bos.write(buffer, 0, buffer.length);
				bos.flush();

				bos.close();
				bis.close();

				System.out.println("ARCHIVO ENVIADO: " + archivoBuscado.getName());
			} else {
				out.writeUTF("NO LO TENGO");
				out.writeUTF("PROPORCIONANDO IP Y PUERTO DEL ORIGIN SERVER...");
				String IpOriginServer = "192.168.1.46"; // IP DEL ORIGIN
				int puertoOriginServer = 3333; // PUERTO DEL ORIGIN
				out.writeUTF(IpOriginServer); // ENVIAMOS LA IP DEL SERVER ORIGIN AL SERVER EDGE
				out.writeInt(puertoOriginServer); // ENVIAMOS EL PUERTO DEL SERVER ORIGIN AL SERVER EDGE

				/* EJECUTAMOS EL CLIENTE DEL SERVER ORIGIN CON EL FICHERO A DESCARGAR */
				ClienteServerOrigin ServerEdgeOrigin = new ClienteServerOrigin(IpOriginServer, puertoOriginServer,
						archivoBuscado.getName());
				ServerEdgeOrigin.run();
				/*
				 * File rutaArchivos = new
				 * File("C:/Users/Oscar/eclipse-workspace/practica1AAX/ServerEdge", archivo);
				 * rutaArchivos.createNewFile();
				 */

			}

		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
