package servidor;

import java.io.Serializable;
import java.util.List;

import modelo.Archivo;

/***
 * Clase que permite intercambiar datos entre el Cliente y el servidor
 * @author cesco
 *
 */
public class IntercambioDatos implements Serializable{

	private static final long serialVersionUID = 8020764957352413016L;
	private List<Archivo> archivos;
	
	/***
	 * La clase recibe una lista de archivos y trabaja con ella
	 * @param archivos
	 */
	public IntercambioDatos(List<Archivo> archivos) {
		this.archivos = archivos;
	}
	
	public IntercambioDatos() {
		
	}

	/***
	 * Permite recuperar la lista de archivos del objeto
	 * @return una Lista de Archivos.
	 */
	public List<Archivo>getArchivos() {
		return archivos;
	}

	/***
	 * Permite establecer la lista de archivos del objeto.
	 * @param archivos Lista de archivos que va a tener el objeto.
	 */
	public void setArchivos(List<Archivo> archivos) {
		this.archivos = archivos;
	}

}
