package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * La clase LogServidor	proporciona una forma de añadir información a un fichero
 * \log.txt de forma sencilla y que hace el resto del código mas legible.
 * @author Francesco De Amicis
 */
public class LogServidor {

	/***
	 * Se genera el fichero por primera vez, indicando la fechaç
	 * y hora del momento de creación.
	 */
	public static void create() {
		try {
			
			File log = new File("log.txt");
			FileWriter fw = new FileWriter(log, true);
			Date d= Calendar.getInstance().getTime();
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
	public static void i(String mensaje) {
		try (FileWriter fw = new FileWriter("log.txt", true)){
			fw.append(mensaje+"\n");
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
}
