package servidor;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import modelo.Archivo;
import utils.LogServidor;

/***
 * Clase Servidor
 * @author Francesco De Amicis
 *
 */

public class Servidor {

	public static void main(String[] args) {
		final int PORT = 44444;
	
		try {
			ServerSocket servidor = new ServerSocket(PORT);
			
			LogServidor.create();
			LogServidor.i("Servidor iniciado en puerto: "+PORT);
			List<Archivo> archivos = inicializaDirectorio();
			System.out.println("Servidor iniciado en puerto: "+PORT);
			
			/**
			 * Cada vez que se conecte un Cliente creamos un Hilo que hara las operaciones con el.
			 */
			while (true) {
				Socket socket = new Socket();
				socket = servidor.accept();
				System.out.println("Cliente: "+socket.getInetAddress() +socket.getPort()+" conectado");
				LogServidor.i("Cliente: "+socket.getInetAddress() +socket.getPort()+" conectado");
				HiloServidor hilo = new HiloServidor(socket, archivos);
				hilo.start();

			}
		} catch (IOException e) {
			LogServidor.i(e.getMessage());
		}
	}

	/***
	 * Método que crea un directorio donde se guardaran las descargas del servidor. Al mismo tiempo, carga un arraylist con los archivos contenidos
	 * @return un arrayList con los Archivos del directorio
	 */
	private static List<Archivo> inicializaDirectorio() {
		
		// Creo el directorio para alojar archivos
		File directorio = new File("." + File.separator + "archivosServidor");
		directorio.mkdirs(); //si existe no lo crea

		// Recupero los archivos del servidor en un array
		File[] files = directorio.listFiles();
		List<Archivo> archivos = new ArrayList<>();
		String nombre[];
		
		//Lo paso a una lista de mi clase Archivo
		for (int i = 0; i < files.length; i++) {
			nombre = files[i].getName().split("\\.");
			Archivo archivo = new Archivo(files[i], nombre[0], nombre[1], files[i].length());
			System.out.println("Creando archivo: "+archivo.getNombre());
			LogServidor.i("Creando archivo: "+archivo.getNombre());
			archivos.add(archivo);
		}
		
		return archivos;
	}
}
