package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/***
 * Log del cliente. Registra los datos que considera importantes.
 * @author Francesco De Amicis
 *
 */
public class LogCliente {

	/***
	 * Se genera el fichero por primera vez, indicando la fechaç
	 * y hora del momento de creación.
	 */
	public static void create(String cliente) {
		try {
			
			File log = new File("log"+cliente+".txt");
			FileWriter fw = new FileWriter(log, true);
			Date d = Calendar.getInstance().getTime();
			if (log.exists()) {
				fw.append("\n!SESSION: "+ d +"\n");
			} else {
				fw.append("!SESSION: "+ d +"\n");
			}
				
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/***
	 * Añade informacion al fichero log.txt.
	 * @param mensaje Este es el mensaje que se escribira en el fichero.
	 */
	public static void i(String mensaje, String cliente) {
		try (FileWriter fw = new FileWriter("log"+cliente+".txt", true)){
			fw.append(mensaje+"\n");
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
}
