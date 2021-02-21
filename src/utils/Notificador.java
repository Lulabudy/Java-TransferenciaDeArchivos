package utils;

import javax.swing.JOptionPane;

/***
 * Clase estatica que lanza notificaciones.
 * Funcionamiento sencillo pero hace el codigo mas legible.
 * @author Francesco De Amicis
 *
 */
public class Notificador {
	
	public static void notificaError(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void notificaExito(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "", JOptionPane.INFORMATION_MESSAGE);
	}


}
