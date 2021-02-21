package modelo;

import java.io.IOException;
import java.net.Socket;

/***
 * Clase POJO. Usuario que utiliza el programa
 * @author Francesco De Amicis
 *
 */
public class Usuario {
	
	int puerto;
	String ip;
	Socket socket;

	/***
	 * Constructor de Usuario.
	 * @param puerto Puerto del usuario.
	 * @param ip Ip del usuario.
	 */
	public Usuario(int puerto, String ip) {
		this.puerto = puerto;
		this.ip = ip;
		try {
			this.socket = new Socket(ip, puerto);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
