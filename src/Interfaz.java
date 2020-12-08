/**
 * @author TuRi
 */
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EtchedBorder;


final class Interfaz extends JFrame implements KeyListener
{

	private Accion miListener = new Accion();
	private Rompecabezas miRompecabezas = new Rompecabezas();
        private Rompecabezas newRompecabezas;
	private JMenu miMenu;
	private JMenuItem nuevoJuego;
        private JMenuItem nuevaImagen;
	private JMenu algoritmo;
	private JMenuItem amplitud;
	private JMenuItem profundidad;
	private JMenuBar menuBar = new JMenuBar();

	Interfaz() {
	};
        
	public Interfaz(String name)
        {
            setTitle(name);
            this.setLayout(new BorderLayout());
            add(miRompecabezas, BorderLayout.EAST);

            // Creación del primer menu
            miMenu = createJMenu("Juego", 'F');            
            nuevoJuego = createJMenuItem("Nuevo Juego    ", 'N', 'N',
				InputEvent.CTRL_MASK, miListener);
            nuevaImagen = createJMenuItem("Nueva Imagen    ", 'I', 'I',
				InputEvent.CTRL_MASK, miListener);
            miMenu.add(nuevoJuego);
            miMenu.add(nuevaImagen);
            
            
            //Creación del segundo menu
            algoritmo = createJMenu("Ordenar", 'G');
            //Manhattan
            amplitud = createJMenuItem("Usando Busqueda por Amplitud (Manhattan) ", 'A', 'A',
				InputEvent.CTRL_MASK, miListener);
            algoritmo.add(amplitud);
            
            //euclidean
            profundidad = createJMenuItem("Usando Busqueda por Profundidad (Euclidiano)  ", 'A',
				'A', InputEvent.CTRL_MASK, miListener);
            algoritmo.add(profundidad);

            menuBar.setBorder(new EtchedBorder());
            menuBar.add(miMenu);
            menuBar.add(algoritmo);

            this.setJMenuBar(menuBar);

            addKeylistener();
            this.setFocusable(true);
	}

	public void actualizarRompecabezas(String archivo) {
            
            try {
                        
                    System.gc();

                    remove(miRompecabezas);
                    
                    miRompecabezas = new Rompecabezas(archivo);
                    System.gc();
                    
                    add(miRompecabezas);

                    
                    miRompecabezas.setVisible(true);

                    miRompecabezas.revalidate();
                    miRompecabezas.repaint();
                    miRompecabezas.validate();
                    
                    this.revalidate();
                    this.repaint();
                    this.validate();
                    System.gc();
                    
                    JOptionPane.showMessageDialog(this,"Iniciar nuevamente la aplicación para"
                            + "cargar la nueva imagen procesada.");  
                    System.exit(0);
                    
                        

                } catch (Exception ex) {
                    Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
            
	}
        
        
        
      
        
        
        
        
        //Clase que reacciona a los eventos
	class Accion extends AbstractAction
        {

            public Accion()
            {
            
            }

            @Override
            public void actionPerformed(ActionEvent event) {

		if (event.getSource() == nuevoJuego) {
                    removeKeylistener();
                    addKeylistener();
                    miRompecabezas.random();
                    
		} else if (event.getSource() == nuevaImagen) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.showOpenDialog(null);
                    File archivo = chooser.getSelectedFile();
                    String nombreArchivo = archivo.getAbsolutePath();
                    
                    actualizarRompecabezas(nombreArchivo);
                    
                    
                }

		if (event.getSource() == amplitud) {
                    miRompecabezas.resolverRompecabezas(1);
                    removeKeylistener();
		} else if (event.getSource() == profundidad) {
                    miRompecabezas.resolverRompecabezas(2);
                    removeKeylistener();
		} 

            } 
	} 
       
        

        //Creador del Menu
	JMenu createJMenu(String JMenuNombre, char mnemonicChar) {
		JMenu miMenu = new JMenu(JMenuNombre);
		miMenu.getPopupMenu().setLightWeightPopupEnabled(false);
		miMenu.setMnemonic(mnemonicChar);

		return miMenu;
	}
        //Creador de los items del menu
	JMenuItem createJMenuItem(String jMenuName, char mnemonicChar, int keyChar,
			int modifierInt, AbstractAction action) {
		JMenuItem menuItem = new JMenuItem(jMenuName);
		menuItem.setMnemonic(mnemonicChar);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(keyChar, modifierInt));
		menuItem.addActionListener(action);

		return menuItem;
	}


        @Override
	public void keyTyped(KeyEvent event) {
	}

        @Override
	public void keyPressed(KeyEvent event) {
	}
        //Funcion para captar el movimiento de teclas arriba, abajo, derecha , izquierda y mover las piezas del rompecabezas
        @Override
	public void keyReleased(KeyEvent event) {

            if ((event.getKeyCode() == KeyEvent.VK_UP)
				|| (event.getKeyCode() == KeyEvent.VK_LEFT)
				|| (event.getKeyCode() == KeyEvent.VK_RIGHT)
				|| (event.getKeyCode() == KeyEvent.VK_DOWN)) {

			miRompecabezas.verificarMovimiento(event.getKeyCode());

			if (miRompecabezas.verificarFinJuego())
				removeKeylistener();
		}

	}

	public void removeKeylistener() {
		this.removeKeyListener(this);
	}

	/**
	 * add keylistener.
	 */

	public void addKeylistener() {
		this.addKeyListener(this);
	}

}
