package controlador;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.swing.JFileChooser;

import modelo.Archivo;
import modelo.Usuario;
import servidor.IntercambioDatos;
import utils.JListUtils;
import utils.LogCliente;
import utils.Notificador;
import vista.ClienteFrame;

/***
 * Clase desde la que se controla al Usuario y a la vista
 * @author Francesco De Amicis
 *
 */
public class ControladorUsuario {

	private Usuario usuario;
	private ClienteFrame clienteFrame;
	// private DataInputStream in;
	private DataOutputStream out;
	private BufferedReader in;
	private ObjectInputStream objIn;
	private List<Archivo> archivos;

	/***
	 * Constructor de controlador.
	 * @param usuario Usuario que va a controlar.
	 * @param clienteFrame Frame que va a controlar.
	 */
	public ControladorUsuario(Usuario usuario, ClienteFrame clienteFrame) {
		this.usuario = usuario;
		this.clienteFrame = clienteFrame;
		LogCliente.create(usuario.getIp()+usuario.getSocket().getLocalPort());
		try {
			this.objIn = new ObjectInputStream(usuario.getSocket().getInputStream());
			out = new DataOutputStream(usuario.getSocket().getOutputStream());
			
			// Leo los archivos del servidor por primera vez
			IntercambioDatos d = (IntercambioDatos) objIn.readObject();
			archivos = d.getArchivos();
			if (archivos.isEmpty()) {
				Notificador.notificaError("Aun no hay archivos en el servidor");
			} else {
				// Muestro los archivos en el JList del Frame
				clienteFrame.getJList().setModel(JListUtils.toJList(archivos));
			}

		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
			LogCliente.i(e1.getMessage(), usuario.getIp()+usuario.getSocket().getLocalPort());
		}

		this.clienteFrame.compartirArchivo(e -> {
			byte upload = 1;
			try {
				out.writeByte(upload);
				if (clienteFrame.showFileChooser() == JFileChooser.APPROVE_OPTION) {
					
					File file = clienteFrame.getFileChooser().getSelectedFile();
					String nombre = file.getName();
					System.out.println("Nombre: " + nombre);
					String[] nombreYExtension = nombre.split("\\.");
					Archivo archivo = new Archivo(file, nombreYExtension[0], nombreYExtension[1], file.length());				
					enviarArchivo(archivo);
					actualizarLista();
						

				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
				LogCliente.i(e1.getMessage(), usuario.getIp()+usuario.getSocket().getLocalPort());
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				LogCliente.i(e1.getMessage(), usuario.getIp()+usuario.getSocket().getLocalPort());
			}
		});

		this.clienteFrame.buscarArchivo(e -> {
			String filtroBusqueda = clienteFrame.getFiltroBusqueda();
			buscarArchivo(filtroBusqueda);
			
		});

		this.clienteFrame.verTodo(e -> {
			try {
				out.writeByte(3);
				actualizarLista();
			} catch (IOException | ClassNotFoundException e1) {
				e1.printStackTrace();
				LogCliente.i(e1.getMessage(), usuario.getIp()+usuario.getSocket().getLocalPort());
			}
		});

		this.clienteFrame.descargarArchivo(e -> {
			try {
				//El servidor entiende este byte como una peticion para descargar un archivo. Ahora tengo que decirle al servidor qué archivo quiero descargar
				out.writeByte(2); 
				if (clienteFrame.getJList().getSelectedValue() != null) {
					String[] fullName = clienteFrame.getJList().getSelectedValue().split("\\.");
					String nombre = fullName[0];
					String formato = fullName[1];
					// ListIterator<Archivo> itr = archivos.listIterator();
					Archivo a = new Archivo(nombre, formato);
					out.writeUTF(nombre);
					out.writeUTF(formato);
					if (archivos.contains(a)) {
						a = archivos.get(archivos.indexOf(a));
						System.out.println("Datos del archivo: "+a.getFile());
						
						if (clienteFrame.saveFileChooser() == JFileChooser.APPROVE_OPTION) {
							File destino =  clienteFrame.getFileChooser().getSelectedFile();
							recibirArchivo(destino);
							
						}
						
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				LogCliente.i(e1.getMessage(), usuario.getIp()+usuario.getSocket().getLocalPort());
			}
		});
	}

	/***
	 * Recibe un archivo del servidor
	 * @param destino Ruta donde se va a guardar el archivo
	 */
	private void recibirArchivo(File destino) {
		try {
			DataInputStream dataInputStream = new DataInputStream(usuario.getSocket().getInputStream());
			
			long size = dataInputStream.readLong();
					
			String name = dataInputStream.readUTF();
			String formato = dataInputStream.readUTF();		
			
			System.out.println("Archivo de tamaño: "+size+". "+name+"."+formato);
			System.out.println("Se guardara un archivo en: "+destino.toPath());
			
			LogCliente.i("Archivo de tamaño: "+size+". "+name+"."+formato, usuario.getIp()+usuario.getSocket().getLocalPort());
			LogCliente.i("Se guardara un archivo en: "+destino.toPath(), usuario.getIp()+usuario.getSocket().getLocalPort());
			
			byte[] buffer = new byte[4 * 1024];
			int bytes = 0;
			FileOutputStream fileOutputStream = new FileOutputStream(destino+"."+formato);
			while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
				fileOutputStream.write(buffer, 0, bytes);
				size -= bytes;
				fileOutputStream.flush();
			}
			fileOutputStream.close();
			Notificador.notificaExito("Descarga finalizada");
		} catch (IOException e) {
			e.printStackTrace();
			LogCliente.i(e.getMessage(), usuario.getIp()+usuario.getSocket().getLocalPort());
		}
		
	}

	/***
	 * Busca un archivo de la Lista de Archivos
	 * @param filtroBusqueda Nombre del archivo que se busca
	 */
	private void buscarArchivo(String filtroBusqueda) {
		if (!filtroBusqueda.isEmpty()) {
			System.out.println("Buscando archivos con: " + filtroBusqueda);
			LogCliente.i("Buscando archivos con: " + filtroBusqueda, usuario.getIp()+usuario.getSocket().getLocalPort());
			clienteFrame.getJList().setModel(JListUtils.toJList(archivos, filtroBusqueda));
			if (clienteFrame.getJList().getComponentCount() == 0) {
				Notificador.notificaError("Su busqueda no devolvio resultados");
			}
		}		
	}


	/***
	 * Metodo que actualiza la Lista de Archivos con todos los archivos que haya en el servidor
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void actualizarLista() throws ClassNotFoundException, IOException {
		System.out.println("Entrando en actualizar lista");
		IntercambioDatos d = (IntercambioDatos) objIn.readObject();
		System.out.println("Objeto recibido");
		LogCliente.i("Objeto recibido", usuario.getIp()+usuario.getSocket().getLocalPort());		
		this.archivos = d.getArchivos();
		clienteFrame.getJList().setModel(JListUtils.toJList(archivos));
		
	}


	/***
	 * Envia archivos mediante FileInputStream
	 * @param archivo Archivo a enviar
	 */
	private void enviarArchivo(Archivo archivo) {
		try {
			// Primero mando el tamaño
			if (archivo != null) {
				int bytes = 0;

				System.out.println("Escribiendo archivo de tamaño: " + archivo.getBytes());
				LogCliente.i("Escribiendo archivo de tamaño: " + archivo.getBytes(), usuario.getIp()+usuario.getSocket().getLocalPort());
				out.writeLong(archivo.getBytes());

				// Mando el nombre
				out.writeUTF(archivo.getNombre());
				out.writeUTF(archivo.getFormato());

				FileInputStream fileInputStream = new FileInputStream(archivo.getFile());

				byte[] buffer = new byte[4 * 1024];

				while ((bytes = fileInputStream.read(buffer)) != -1) {
					out.write(buffer, 0, bytes);
					out.flush();
				}
				Notificador.notificaExito("Archivo enviado con exito");

				fileInputStream.close();
			}

		} catch (IOException e) {
			Notificador.notificaError("No es posible subir este archivo");
			LogCliente.i(e.getMessage(), usuario.getIp()+usuario.getSocket().getLocalPort());
		}

	}
}
