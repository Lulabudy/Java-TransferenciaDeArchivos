package modelo;

import java.io.File;
import java.io.Serializable;

/***
 * POJO Archivo. Clase que almacena datos sobre cada archivo compartido.
 * @author Francesco De Amicis Caballero
 *
 */
public class Archivo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6821303598716265986L;
	private File file;
	private String nombre;
	private String formato;
	private long bytes;
	private String path;
	
	/***
	 * Constructor de Archivo
	 * @param file File al que hace referencia.
	 * @param nombre Nombre del archivo
	 * @param formato Extension del archivo.
	 * @param bytes Tamanyo del archivo.
	 */
	public Archivo(File file, String nombre, String formato, long bytes) {
		this.file = file;
		this.nombre = nombre;
		this.formato = formato;
		this.bytes = bytes;
		this.path = file.getAbsolutePath();
	}
	
	/***
	 * Constructor de Archivo
	 * @param nombre Nombre del archivo
	 * @param formato Extension del archivo
	 * @param bytes Tamanyo del archivo.
	 */
	public Archivo (String nombre, String formato, long bytes) {
		this.nombre = nombre;
		this.formato = formato;
		this.bytes = bytes;
	}
	
	/***
	 * Constructor de Archivo
	 * @param nombre Nombre del archivo
	 * @param formato Extension del archivo
	 */
	public Archivo (String nombre, String formato) {
		this.nombre = nombre;
		this.formato = formato;
		this.bytes = 0;
		this.file = null;
	}

	/***
	 * Devuelve el File Asociado al archivo.
	 * @return File asociado al archivo.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * 
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public long getBytes() {
		return bytes;
	}

	public void setBytes(long bytes) {
		this.bytes = bytes;
	}

	/***
	 * Toma los atributos del Archivo y lo convierte a cadena.
	 */
	@Override
	public String toString() {
		return "Archivo [file=" + file + ", nombre=" + nombre + ", formato=" + formato + ", bytes=" + bytes + "]";
	}

	/***
	 * Un archivo es igual a otro si su nombre y su extension son iguales.
	 */
	@Override
	public boolean equals(Object obj) {
		Archivo a = (Archivo) obj;	
		return this.formato.equals(a.formato) && this.nombre.equals(a.nombre);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
