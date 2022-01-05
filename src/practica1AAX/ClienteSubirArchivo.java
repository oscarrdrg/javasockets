package practica1AAX;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClienteSubirArchivo {
	private String IP = "";
	private int puerto = 0;

	public ClienteSubirArchivo(String IP, int puerto) {
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

			/* ENVIAMOS LA FRASE AL ORIGIN */
			String archivoBuscado;
			System.out.print("INTRODUCE EL ARCHIVO QUE QUIERES SUBIR: ");
			archivoBuscado = tecladoTCP.readLine();

			File archivo = new File("C:/Users/Oscar/Desktop/Cliente", archivoBuscado);
			if (archivo.exists()) {
				out.writeUTF("SUBIR ARCHIVO");
				int tamañoArchivo = (int) archivo.length();
				out.writeInt(tamañoArchivo); // ENVIAMOS TAMAÑO
				out.writeUTF(archivoBuscado); // ENVIAMOS NOMBRE DEL ARCHIVO
				System.out.println("ARCHIVO ENVIADO: " + archivoBuscado);
				System.out.println("TAMAÑO DEL ARCHIVO: " + tamañoArchivo);

				String fraseOrigin = in.readUTF(); // NOS LLEGA FRASE DEL ORIGIN

				if (fraseOrigin.equals("ARCHIVO REPETIDO")) {
					System.out.println(fraseOrigin);
				} else {
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(archivo));
					BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
					byte[] buffer = new byte[tamañoArchivo];
					bis.read(buffer, 0, buffer.length);

					bos.write(buffer, 0, buffer.length);
					bos.flush();

					bos.close();
					bis.close();
				}

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
