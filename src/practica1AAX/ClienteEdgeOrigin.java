package practica1AAX;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClienteEdgeOrigin {
	private String IP = "";
	private int puerto = 0;
	private String archivo = "";

	public ClienteEdgeOrigin(String IP, int puerto, String archivo) {
		this.IP = IP;
		this.puerto = puerto;
		this.archivo = archivo;
	}

	public void run() {

		Socket socket = null;
		DataOutputStream out = null;
		DataInputStream in = null;

		try {
			socket = new Socket(IP, puerto);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			out.writeUTF("DESCARGAR ARCHIVO");
			out.writeUTF(archivo); // LO ENVIAMOS AL SERVER EDGE
			String mensajeServerOrigin = in.readUTF(); // NOS LLEGA UNA RESPUESTA

			if (mensajeServerOrigin.equals("LO TENGO")) {
				System.out.println("\nSERVER DICE: " + mensajeServerOrigin);

				/* DESCARGA EL ARCHIVO */
				System.out.println("INICIANDO DESCARGA DEL ARCHIVO...");
				int tamañoArchivo = in.readInt();
				System.out.println(tamañoArchivo);
				FileOutputStream fos = new FileOutputStream("C:/Users/Oscar/Desktop/Cliente/" + archivo);
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
				System.out.println("\nSERVER DICE: " + mensajeServerOrigin);
				System.out.println("DESCONECTANDO...");
				socket.close();

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
