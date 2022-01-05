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
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

public class OriginServer extends Thread {
	Socket clientSocket;

	public OriginServer(Socket socket) {
		this.clientSocket = socket;
	}

	public void run() {
		DataInputStream in = null;
		DataOutputStream out = null;

		try {

			out = new DataOutputStream(clientSocket.getOutputStream());
			in = new DataInputStream(clientSocket.getInputStream());

			/* LE LLEGA UNA FRASE */
			String frase = in.readUTF();

			if (frase.equals("SUBIR ARCHIVO")) {

				System.out.println("INICIANDO DESCARGA DEL ARCHIVO...");
				int tamañoArchivo = in.readInt();
				String nombreArchivo = in.readUTF();

				File archivoBuscado = new File("C:/Users/Oscar/Desktop/ServerOrigin", nombreArchivo);

				if (archivoBuscado.exists()) {
					System.out.println("ARCHIVO REPETIDO");
					out.writeUTF("ARCHIVO REPETIDO");
					clientSocket.close();

				} else {
					out.writeUTF("ARCHIVO NO REPETIDO");
					System.out.println(tamañoArchivo);
					FileOutputStream fos = new FileOutputStream("C:/Users/Oscar/Desktop/ServerOrigin/" + nombreArchivo);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream());
					byte[] buffer = new byte[tamañoArchivo];

					bis.read(buffer, 0, buffer.length);
					bos.write(buffer, 0, buffer.length);
					bos.flush();

					bos.close();
					bis.close();

					System.out.println("DESCARGA COMPLETADA");
				}

			} else {
				String archivo = in.readUTF();
				System.out.println("CLIENTE " + clientSocket.getInetAddress().getHostAddress()
						+ " QUIERE DESCARGAR: " + archivo);
				File archivoBuscado = new File("C:/Users/Oscar/Desktop/ServerOrigin", archivo);
				System.out.println(archivoBuscado.getAbsolutePath());

				if (archivoBuscado.exists()) {
					out.writeUTF("LO TENGO");
					int tamañoArchivo = (int) archivoBuscado.length();
					out.writeInt(tamañoArchivo);
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(archivoBuscado));
					BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());
					byte[] buffer = new byte[tamañoArchivo];
					bis.read(buffer, 0, buffer.length);

					bos.write(buffer, 0, buffer.length);
					bos.flush();

					bos.close();
					bis.close();

					System.out.println("ARCHIVO ENVIADO: " + archivoBuscado.getName() + "\n");
				} else {
					out.writeUTF("NO LO TENGO");
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
