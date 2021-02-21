package utils;

import java.util.List;
import java.util.ListIterator;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import modelo.Archivo;



/***
 * Clase con diferentes métodos para llenar un JList con ListModels
 * @author Francesco De Amicis
 *
 */
public class JListUtils {
	/***
	 * Convierte una lista de Archivos en un DefaultListModel que llenará un JList
	 * @param archivos Lista con Archivos
	 * @return Un DefaultListModel
	 */
	public static DefaultListModel<String> toJList(List<Archivo> archivos) {

		DefaultListModel<String> archivosModel = new DefaultListModel();

		ListIterator<Archivo> itr = archivos.listIterator();

		while (itr.hasNext()) {
			Archivo a = itr.next();
			archivosModel.addElement(a.getNombre() + "." + a.getFormato());
		}

		return archivosModel;
	}



	/***
	 * Convierte una lista de Archivos en un DefaultListModel que llenará un JList.
	 * @param archivos Lista con Archivos
	 * @param filtroBusqueda filtro de busqueda del usuario
	 * @return Un DefaultListModel
	 */
	public static ListModel<String> toJList(List<Archivo> archivos, String filtroBusqueda) {
		DefaultListModel<String> archivosModel = new DefaultListModel();

		ListIterator<Archivo> itr = archivos.listIterator();

		while (itr.hasNext()) {
			Archivo a = itr.next();
			String nombre = a.getNombre().toLowerCase();
			if (nombre.contains(filtroBusqueda.toLowerCase())) {
				archivosModel.addElement(a.getNombre() + "." + a.getFormato());
			}

		}

		return archivosModel;
	}
}
