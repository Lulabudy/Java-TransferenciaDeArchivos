import controlador.ControladorUsuario;
import modelo.Usuario;
import vista.ClienteFrame;

/***
 * Clase Main Del Cliente
 * @author Francesco De Amicis
 *
 */
public class MainCliente {
	
	/***
	 * Main del Programa Cliente
	 * @param args
	 */
	public static void main(String[] args) {
		Usuario usuario = new Usuario(44444, "localhost");
		ClienteFrame clienteFrame = new ClienteFrame();
		clienteFrame.setVisible(true);
		
		ControladorUsuario controlador = new ControladorUsuario(usuario, clienteFrame);
	}

}
