package vista;

import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

/***
 * Vista del cliente. Extiende de JFrame.
 * @author Francesco De Amicis
 *
 */
public class ClienteFrame extends JFrame{
	private JPanel contentPane;
	private JTextField textFieldNombre;
	private JButton btnDescargar;
	private JLabel lblNombre;
	private JButton btnBuscar;
	private JButton btnTodo;
	private JScrollPane scrollPane;
	private JButton btnCompartirArchivo;
	private JFileChooser fileChooser;
	private JList<String> list;

	/***
	 * Constructor del Frame. Inicializa la vista.
	 */
	public ClienteFrame() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 745, 586);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnDescargar = new JButton("Descargar archivo");
		
		lblNombre = new JLabel("Nombre:");
		
		textFieldNombre = new JTextField();
		textFieldNombre.setColumns(10);
		
		btnBuscar = new JButton("Buscar");
		
		btnTodo = new JButton("Ver todo");
		
		scrollPane = new JScrollPane();
		
		btnCompartirArchivo = new JButton("Compartir archivo");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNombre)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textFieldNombre, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnBuscar)
					.addPreferredGap(ComponentPlacement.RELATED, 187, Short.MAX_VALUE)
					.addComponent(btnCompartirArchivo)
					.addGap(75))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(296)
					.addComponent(btnDescargar)
					.addContainerGap(258, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(330, Short.MAX_VALUE)
					.addComponent(btnTodo)
					.addGap(290))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(47, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 651, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNombre)
						.addComponent(textFieldNombre, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBuscar)
						.addComponent(btnCompartirArchivo))
					.addPreferredGap(ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE)
					.addGap(33)
					.addComponent(btnTodo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDescargar)
					.addGap(29))
		);
		
		list = new JList<>();
		
		scrollPane.setViewportView(list);
		contentPane.setLayout(gl_contentPane);
		fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	}
	
	//Eventos
	/***
	 * Añade al boton de buscar archivo un evento ActionListener.
	 * @param actionListener Evento.
	 */
	public void buscarArchivo(ActionListener actionListener) {
		this.btnBuscar.addActionListener(actionListener);
	}
	
	/***
	 * Añade al boton de ver archivos un evento ActionListener.
	 * @param actionListener Evento.
	 */
	public void verTodo(ActionListener actionListener) {
		this.btnTodo.addActionListener(actionListener);
	}
	
	/***
	 * Añade al boton de descargar archivos un evento ActionListener.
	 * @param actionListener Evento.
	 */
	public void descargarArchivo(ActionListener actionListener) {
		this.btnDescargar.addActionListener(actionListener);
	}
	
	/***
	 * Añade al boton de subir archivos un evento ActionListener.
	 * @param actionListener Evento.
	 */
	public void compartirArchivo(ActionListener actionListener) {
		this.btnCompartirArchivo.addActionListener(actionListener);
	}
	
	/***
	 * Muestra un JFileChooser para elegir archivo.
	 * @return un numero, dependiendo del boton que haya presionado el usuario.
	 */
	public int showFileChooser() {
		return this.fileChooser.showOpenDialog(this);
	}
	
	/***
	 * Muestra un JFileChooser para guardar archivos.
	 * @return un numero, dependiendo del boton que haya presionado el usuario.
	 */
	public int saveFileChooser() {
		return this.fileChooser.showSaveDialog(this);
	}
	
	//Getters
	public JList<String> getJList() {
		return this.list;
	}
	
	public String getFiltroBusqueda() {
		return this.textFieldNombre.getText();
	}
	
	public JFileChooser getFileChooser() {
		return this.fileChooser;
	}
}
