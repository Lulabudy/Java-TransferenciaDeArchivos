package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import modelo.Archivo;
import utils.LogServidor;
import utils.Notificador;

/***
 * Hilo que trabaja con el Servidor atendiendo a cada cliente que se conecta.
 * @author Francesco De Amicis
 *
 */
public class HiloServidor extends Thread {
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private List<Archivo> archivos;
	ObjectInputStream fentrada;
	private ObjectOutputStream objOut;

	/***
	 * Constructor del Hilo
	 * @param socket Socket del usuario que se conecta.
	 * @param archivos Lista de archivos del servidor.
	 */
	public HiloServidor(Socket socket, List<Archivo> archivos) {
		this.socket = socket;
		this.archivos = archivos;
		
		try {
			this.objOut = new ObjectOutputStream(socket.getOutputStream());
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println(e.getMessage());
			LogServidor.i(e.getMessage());
		}
	}

	/***
	 * Método Run del Hilo.
	 */
	@Override
	public void run() {
		IntercambioDatos datos = new IntercambioDatos(archivos);

		try {
			objOut.reset();
			objOut.writeObject(datos);
		} catch (IOException e) {
			e.getMessage();
		}
		while (true) {
			try {
				recibirPeticion();
			} catch (IOException e) {
				System.out.println("Cliente "+ socket.getInetAddress()+" "+ socket.getPort()+" se desconectó de la aplicación");
				LogServidor.i("Cliente "+ socket.getInetAddress()+" "+ socket.getPort()+" se desconectó de la aplicación");
				break;			
			}
		}
	}
	
	/***
	 * Recibe un byte del Usuario y en base a su valor toma una decisión
	 * @throws IOException
	 */
	private void recibirPeticion() throws IOException {

			byte mensaje = in.readByte();
			IntercambioDatos d = new IntercambioDatos();
			
			switch(mensaje) {
				case 1:
					System.out.println("Cliente "+socket.getInetAddress()+" "+ socket.getPort()+ " quiere enviar un archivo");
					LogServidor.i("Cliente "+socket.getInetAddress()+" "+ socket.getPort()+ " quiere enviar un archivo");
					recibirArchivo();
					// Envio la lista de archivos actualizada
					listarArchivos(d);
					break;
				case 2:
					String nombre = in.readUTF();
					String formato = in.readUTF();
					System.out.println("Cliente "+socket.getInetAddress()+" "+ socket.getPort() +" quiere recibir archivo: "+nombre+"."+formato);
					LogServidor.i("Cliente "+socket.getInetAddress()+" "+ socket.getPort() +" quiere recibir archivo: "+nombre+"."+formato);
					enviarArchivo(nombre, formato);
					break;
				case 3:
					System.out.println("Cliente "+socket.getInetAddress()+" "+socket.getPort() + " quiere listar los archivos del servidor");
					LogServidor.i("Cliente "+socket.getInetAddress()+" "+socket.getPort() + " quiere listar los archivos del servidor");
					listarArchivos(d);
					break;
				default:
					System.out.println("Mensaje no decodificado");
					LogServidor.i("Mensaje no decodificado");
					break;
			}
	}

	/***
	 * Metodo que envia un archivo de la lista de archivos del Servidor a un Cliente
	 * @param nombre Nombre del archivo.
	 * @param formato Formato del archivo.
	 */
	private void enviarArchivo(String nombre, String formato) {
		Archivo archivo = new Archivo(nombre, formato);
		if (this.archivos.contains(archivo)) {
			archivo = archivos.get(archivos.indexOf(archivo));
			
			int bytes = 0;

			System.out.println("Escribiendo archivo de tamaño: " + archivo.getBytes());
			LogServidor.i("Escribiendo archivo de tamaño: " + archivo.getBytes());
			try {
				out.writeLong(archivo.getBytes());
				// Mando el nombre
				out.writeUTF(archivo.getNombre());
				out.writeUTF(archivo.getFormato());

				LogServidor.i("Enviando: "+archivo.getNombre()+"."+archivo.getFormato());
				FileInputStream fileInputStream = new FileInputStream(archivo.getFile());

				byte[] buffer = new byte[4 * 1024];

				while ((bytes = fileInputStream.read(buffer)) != -1) {
					out.write(buffer, 0, bytes);
					out.flush();
				}
				System.out.println("Archivo enviado con exito");
				LogServidor.i("Archivo enviado con exito");

				fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				LogServidor.i(e.getMessage());
			}			
		}		
	}

	/***
	 * Envía los metadatos de los archivos alojados en el servidor
	 * @param intercambioDatos Objeto que intercambia los datos
	 * @throws IOException
	 */
	private void listarArchivos(IntercambioDatos intercambioDatos) throws IOException {
		intercambioDatos.setArchivos(this.archivos);		
		objOut.reset();
		objOut.writeObject(intercambioDatos);
	}
	
	/***
	 * Método que recibe un archivo de un cliente mediante FileOutputStream
	 * @return un FileOutputStream (un archivo)
	 * @throws IOException
	 */
	private FileOutputStream recibirArchivo() throws IOException{
			int bytes = 0;

			// Leo el tamaño
			long size = in.readLong();
			System.out.println("tamaño recibido: "+size);
			LogServidor.i("tamaño recibido: "+size);
			if (size > 0) {
				String name = in.readUTF(); // su nombre
				String formato = in.readUTF(); // su extension

				Archivo archivo = new Archivo(name, formato, size);

				FileOutputStream fileOutputStream = new FileOutputStream(
						"." + File.separator + "archivosServidor" + File.separator + name + "." + formato);
				byte[] buffer = new byte[4 * 1024];

				while (size > 0 && (bytes = in.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
					fileOutputStream.write(buffer, 0, bytes);
					size -= bytes;
				}
				File file = new File("."+File.separator+"archivosServidor"+File.separator+name + "." + formato);

				archivo.setFile(file);
				archivo.setPath(file.getAbsolutePath());
				if(!archivos.contains(archivo)) {
					archivos.add(archivo);
				}
				
				System.out.println("Archivo creado");
				System.out.println(archivo.getPath());
				
				LogServidor.i("Archivo creado");
				LogServidor.i(archivo.getPath());
				fileOutputStream.close();
				return fileOutputStream;
			} else {
				System.out.println("devolvemos null");
				LogServidor.i("Ocurrio un fallo, file es null");
				return null;
			}
			
	}
}
