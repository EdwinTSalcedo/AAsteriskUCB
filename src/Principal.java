/**
 * @author Edwin Salcedo a.k.a TuRi
 */
import javax.swing.JFrame;

public class Principal {

	public static void main(String[] args) {

		Interfaz frame = new Interfaz("Rompecabezas");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();

		frame.setResizable(false);

		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

	}
}
