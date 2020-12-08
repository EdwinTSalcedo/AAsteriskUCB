import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class Rompecabezas extends JPanel implements ActionListener
{
    private JLabel[][] pieza = new JLabel[3][3];
    private Nodo nodoInicio, nodoMenor, nodoRama;
    private ListaEnlace listaAbierta = new ListaEnlace();
    private ListaEnlace listaCerrada = new ListaEnlace();
    private ListaEnlace rutaFinal = new ListaEnlace();
    private ListaNodo listaCorriente;
    private int[][] matrizDePiezas = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
    public int[][] estadoFinal = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
    private Icon imagen = new ImageIcon();
    private int idCont;
    private int tempEnt;
    private Random miRandom = new Random();
    private int contadorDesorden;
    private Timer time, time1;
    private TimerRutaFinal miTiempoRutaFinal = new TimerRutaFinal();
    private int iVacio = 2, jVacio = 2;

    Rompecabezas()
    {
	setLayout(new GridLayout(3, 3, 5, 5));
        setBorder(new EtchedBorder());

        time = new Timer(0, this);
        time1 = new Timer(400, miTiempoRutaFinal);

        for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                        pieza[i][j] = createJLabel(String.format("Imagenes/%d.jpg",
                                        matrizDePiezas[i][j]));

        agregarPiezas();
    }
    
    Rompecabezas(String rutaImagen) throws Exception
    {
        guardarImagen(3, 3, rutaImagen);
	setLayout(new GridLayout(3, 3, 5, 5));
	setBorder(new EtchedBorder());

	time = new Timer(0, this);
	time1 = new Timer(400, miTiempoRutaFinal);

	for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
		pieza[i][j] = createJLabel(String.format("Imagenes/%d.jpg",
		matrizDePiezas[i][j]));
            }
        }

	agregarPiezas();
    }
    
        
    public static void guardarImagen(int columnas, int filas, String ruta) throws IOException, Exception {
        try{
            File archivo = new File(ruta);
            FileInputStream input = new FileInputStream(archivo);
            BufferedImage imagen = ImageIO.read(input);

            int secciones = columnas * filas;

            int seccionAnchura = imagen.getWidth() / columnas; 
            int seccionAltura = imagen.getHeight() / filas;

            int c = 0;
            BufferedImage imagenes[] = new BufferedImage[secciones]; 
            for (int x = 0; x < filas; x++) {
                for (int y = 0; y < columnas; y++) {
                    
                    imagenes[c] = new BufferedImage(seccionAnchura, seccionAltura, BufferedImage.TYPE_INT_RGB);
                    if(c == 8)
                        break;
                    Graphics2D gr = imagenes[c].createGraphics();
                    gr.drawImage(imagen, 0, 0, seccionAnchura, seccionAltura, seccionAnchura * y, seccionAltura * x, seccionAnchura * y + seccionAnchura, seccionAltura * x + seccionAltura, null);
                    gr.dispose();
                    
                    c++;
                }
            }

            for (int i = 0; i < imagenes.length; i++) {
                ImageIO.write(imagenes[i], "jpg", new File("src/Imagenes/" + (i+1) + ".jpg"));
            }

            System.out.println("Nueva imagen guardada!");

        }catch(Exception e){System.out.println(e);}             
    }


    public JLabel createJLabel(String ruta)
    {
	JLabel newLabel = new JLabel();
	newLabel.setIcon(new ImageIcon(getClass().getResource(ruta)));
	newLabel.setBorder(new BevelBorder(BevelBorder.RAISED));

	return newLabel;
    }

    public void verificarMovimiento(int tecla)
    {
	switch (tecla) {
	case KeyEvent.VK_UP:
            if (iVacio == 2)
            {
            break;
            } 
            else
            {
            moverPiezaVaciaAbajo();
            iVacio += 1;
            }
	break;
	case KeyEvent.VK_DOWN:
            if (iVacio == 0)
            {
            break;
            }
            else
            {
            moverPiezaVaciaArriba();
            iVacio -= 1;
            }
            break;

            case KeyEvent.VK_LEFT:
			if (jVacio == 2) {
				break;
			} else {
				moverPiezaVaciaDerecha();
				jVacio += 1;
			}
			break;

		case KeyEvent.VK_RIGHT:

			if (jVacio == 0) {
				break;
			} else {
				moverPiezaVaciaIzquierda();
				jVacio -= 1;
			}
			break;
		}

	}


    private void moverPiezaVaciaIzquierda()
    {
        imagen = pieza[iVacio][jVacio].getIcon();
        tempEnt = matrizDePiezas[iVacio][jVacio];

        pieza[iVacio][jVacio].setIcon(pieza[iVacio][jVacio - 1].getIcon());
        matrizDePiezas[iVacio][jVacio] = matrizDePiezas[iVacio][jVacio - 1];

        pieza[iVacio][jVacio - 1].setIcon(imagen);
        matrizDePiezas[iVacio][jVacio - 1] = tempEnt;
    }

    private void moverPiezaVaciaDerecha() {
            imagen = pieza[iVacio][jVacio].getIcon();
            tempEnt = matrizDePiezas[iVacio][jVacio];

            pieza[iVacio][jVacio].setIcon(pieza[iVacio][jVacio + 1].getIcon());
            matrizDePiezas[iVacio][jVacio] = matrizDePiezas[iVacio][jVacio + 1];

            pieza[iVacio][jVacio + 1].setIcon(imagen);
            matrizDePiezas[iVacio][jVacio + 1] = tempEnt;
	}

    private void moverPiezaVaciaArriba()
    {
        imagen = pieza[iVacio][jVacio].getIcon();
        tempEnt = matrizDePiezas[iVacio][jVacio];

        pieza[iVacio][jVacio].setIcon(pieza[iVacio - 1][jVacio].getIcon());
        matrizDePiezas[iVacio][jVacio] = matrizDePiezas[iVacio - 1][jVacio];

        pieza[iVacio - 1][jVacio].setIcon(imagen);
        matrizDePiezas[iVacio - 1][jVacio] = tempEnt;
    }

    private void moverPiezaVaciaAbajo() 
    {
        imagen = pieza[iVacio][jVacio].getIcon();
        tempEnt = matrizDePiezas[iVacio][jVacio];

        pieza[iVacio][jVacio].setIcon(pieza[iVacio + 1][jVacio].getIcon());
        matrizDePiezas[iVacio][jVacio] = matrizDePiezas[iVacio + 1][jVacio];

        pieza[iVacio + 1][jVacio].setIcon(imagen);
        matrizDePiezas[iVacio + 1][jVacio] = tempEnt;
    }

    public void resolverRompecabezas(int flag)
    {
        // Recolecta la basura en la memoria
	time1.start();
	System.gc();
	listaAbierta.vaciarLista();
	rutaFinal.vaciarLista();
	listaCerrada.vaciarLista();
	idCont = 0;
	nodoInicio = new Nodo(0, matrizDePiezas, idCont, -1, flag);
	listaAbierta.insertarFinal(nodoInicio);
	while (!listaAbierta.estaVacio())
        {
            nodoMenor = listaAbierta.getNodoMenor();
            if (mismoEstado(nodoMenor.getEstado(), estadoFinal))
            { 													// sto stoxo
                listaCerrada.insertarFinal(nodoMenor);
                break;
            } 
            else 
            {
			
				listaAbierta.eliminarNodo(nodoMenor);

				listaCerrada.insertarFinal(nodoMenor);
				
				if (nodoMenor.getEmptyX() == 0
						&& nodoMenor.getEmptyY() == 0) {
					

					nodoRama = moverVacioDerecha(nodoMenor, flag);
					checkBranchNodeInLists();
					
					nodoRama = moverVacioAbajo(nodoMenor, flag);

					checkBranchNodeInLists();
				}
				
				else if (nodoMenor.getEmptyX() == 0
						&& nodoMenor.getEmptyY() == 1) {
					nodoRama = moverVacioIzquierda(nodoMenor, flag);
					checkBranchNodeInLists();

					nodoRama = moverVacioAbajo(nodoMenor, flag);

					checkBranchNodeInLists();

					nodoRama = moverVacioDerecha(nodoMenor,
							flag);

					checkBranchNodeInLists();
				}

				
				else if (nodoMenor.getEmptyX() == 0
						&& nodoMenor.getEmptyY() == 2) {
					nodoRama = moverVacioIzquierda(nodoMenor, flag);

					checkBranchNodeInLists();

					nodoRama = moverVacioAbajo(nodoMenor, flag);

					checkBranchNodeInLists();

				}
				else if (nodoMenor.getEmptyX() == 1
						&& nodoMenor.getEmptyY() == 0) {
					nodoRama = moverVacioArriba(nodoMenor, flag);
					checkBranchNodeInLists();

					nodoRama = moverVacioDerecha(nodoMenor,
							flag);
					checkBranchNodeInLists();

					nodoRama = moverVacioAbajo(nodoMenor, flag);
					checkBranchNodeInLists();

				}
				
				else if (nodoMenor.getEmptyX() == 1
						&& nodoMenor.getEmptyY() == 1) {
					nodoRama = moverVacioArriba(nodoMenor, flag);
					checkBranchNodeInLists();

					nodoRama = moverVacioDerecha(nodoMenor,
							flag);
					checkBranchNodeInLists();

					nodoRama = moverVacioAbajo(nodoMenor, flag);

					checkBranchNodeInLists();

					nodoRama = moverVacioIzquierda(nodoMenor, flag);
					checkBranchNodeInLists();

				}
				else if (nodoMenor.getEmptyX() == 1
						&& nodoMenor.getEmptyY() == 2) {
					nodoRama = moverVacioArriba(nodoMenor, flag);

					checkBranchNodeInLists();

					nodoRama = moverVacioIzquierda(nodoMenor, flag);
					checkBranchNodeInLists();

					nodoRama = moverVacioAbajo(nodoMenor, flag);
					checkBranchNodeInLists();

				}
	
				else if (nodoMenor.getEmptyX() == 2
						&& nodoMenor.getEmptyY() == 0) {
					/* 1o kladi */nodoRama = moverVacioArriba(nodoMenor, flag);
					checkBranchNodeInLists();

					/* 2o kladi */nodoRama = moverVacioDerecha(nodoMenor,
							flag);
					checkBranchNodeInLists();

				}
	
				else if (nodoMenor.getEmptyX() == 2
						&& nodoMenor.getEmptyY() == 1) {
					nodoRama = moverVacioIzquierda(nodoMenor, flag);
					checkBranchNodeInLists();

					nodoRama = moverVacioArriba(nodoMenor, flag);
					checkBranchNodeInLists();

					nodoRama = moverVacioDerecha(nodoMenor,
							flag);

					checkBranchNodeInLists();
				}
		
				else if (nodoMenor.getEmptyX() == 2
						&& nodoMenor.getEmptyY() == 2) {
					nodoRama = moverVacioArriba(nodoMenor, flag);

					checkBranchNodeInLists();
					nodoRama = moverVacioIzquierda(nodoMenor, flag);
					checkBranchNodeInLists();

				}

			}

        } 

        backTrackNodes();
        listaCorriente = rutaFinal.getPrimerNodo();
        resetMatrizRompecabezas();
    }

	private void resetMatrizRompecabezas() {
		iVacio = jVacio = 2;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				matrizDePiezas[i][j] = estadoFinal[i][j];

			}
		}

	}

	public void checkBranchNodeInLists() {
		if (listaCerrada.probarExistencia(nodoRama))
			; // agnoei ton komvo
		else // elegxos stin anoixti lista
		{
			if (!listaAbierta.probarExistencia(nodoRama)) {
				listaAbierta.insertarFinal(nodoRama);
			}
		}
	}

	public void backTrackNodes() {

		Nodo insertarNodo = listaCerrada.getUltimoNodo().getNodo();

		rutaFinal.insertarInicio(insertarNodo);

		ListaNodo actual = listaCerrada.getUltimoNodo().anteriorNodo;

		while (actual != null) {
			if (insertarNodo.getParentID() == actual.getNodo().getID()) {
				insertarNodo = actual.getNodo();
				rutaFinal.insertarInicio(insertarNodo);
			}

			actual = actual.anteriorNodo;
		}

	}

	
	public boolean mismoEstado(int[][] A, int[][] B) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (A[i][j] != B[i][j])
					return false;

		return true;
	}

	public void agregarPiezas() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				add(pieza[i][j]);
	}

	public Nodo moverVacioIzquierda(Nodo currentNode, int flag) {
		int[][] tmpState = new int[3][3];
		int i, j;
		for (i = 0; i < 3; i++)
			for (j = 0; j < 3; j++)
				tmpState[i][j] = currentNode.getEstado()[i][j];
		int x = currentNode.getEmptyX();
		int y = currentNode.getEmptyY();

		int tmp = tmpState[x][y];
		tmpState[x][y] = tmpState[x][y - 1];
		tmpState[x][y - 1] = tmp;

		return new Nodo(currentNode.getG() + 1, tmpState, ++idCont,
				currentNode.getID(), flag);
	}

	public Nodo moverVacioDerecha(Nodo currentNode, int flag) {
		int[][] tmpState = new int[3][3];
		int i, j;
		for (i = 0; i < 3; i++)
			for (j = 0; j < 3; j++)
				tmpState[i][j] = currentNode.getEstado()[i][j];

		int x = currentNode.getEmptyX();
		int y = currentNode.getEmptyY();

		int tmp = tmpState[x][y];
		tmpState[x][y] = tmpState[x][y + 1];
		tmpState[x][y + 1] = tmp;

		return new Nodo(currentNode.getG() + 1, tmpState, ++idCont,
				currentNode.getID(), flag);
	}

	public Nodo moverVacioArriba(Nodo currentNode, int flag) {
		int[][] tmpState = new int[3][3];
		int i, j;
		for (i = 0; i < 3; i++)
			for (j = 0; j < 3; j++)
				tmpState[i][j] = currentNode.getEstado()[i][j];

		int x = currentNode.getEmptyX();
		int y = currentNode.getEmptyY();

		int tmp = tmpState[x][y];
		tmpState[x][y] = tmpState[x - 1][y];
		tmpState[x - 1][y] = tmp;

		return new Nodo(currentNode.getG() + 1, tmpState, ++idCont,
				currentNode.getID(), flag);
	}

	
	public Nodo moverVacioAbajo(Nodo currentNode, int flag)
        {
		int[][] tmpState = new int[3][3];
		int i, j;
		for (i = 0; i < 3; i++)
			for (j = 0; j < 3; j++)
				tmpState[i][j] = currentNode.getEstado()[i][j];

		int x = currentNode.getEmptyX();
		int y = currentNode.getEmptyY();

		int tmp = tmpState[x][y];
		tmpState[x][y] = tmpState[x + 1][y];
		tmpState[x + 1][y] = tmp;

		return new Nodo(currentNode.getG() + 1, tmpState, ++idCont,
				currentNode.getID(), flag);
	}

	public void random() {

		contadorDesorden = 0;
		time.start();

	} 
        
	public boolean verificarFinJuego() {
		if (matrizDePiezas[0][0] == 1 && matrizDePiezas[0][1] == 2
				&& matrizDePiezas[0][2] == 3 && matrizDePiezas[1][0] == 4
				&& matrizDePiezas[1][1] == 5 && matrizDePiezas[1][2] == 6
				&& matrizDePiezas[2][0] == 7 && matrizDePiezas[2][1] == 8
				&& matrizDePiezas[2][2] == 9) {
			JOptionPane.showMessageDialog(Rompecabezas.this,
					"Inicie el Juego!!",
					"Advertencia", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}

		return false;
	}

	
    public void actionPerformed(ActionEvent event) 
    {
        int randNum;
	contadorDesorden++;
	if (contadorDesorden < 200)
        {
            if (iVacio == 0 && jVacio == 0)							// tile[1][0]
            {
                randNum = miRandom.nextInt(2); 
		if (randNum == 0) 
		{
                    tempEnt = matrizDePiezas[0][0];
                    imagen = pieza[0][0].getIcon();
                    matrizDePiezas[0][0] = matrizDePiezas[0][1];
                    pieza[0][0].setIcon(pieza[0][1].getIcon());
                    matrizDePiezas[0][1] = tempEnt;
                    pieza[0][1].setIcon(imagen);
                    jVacio = 1;
		}
                else 
		{
                    tempEnt = matrizDePiezas[0][0];
                    imagen = pieza[0][0].getIcon();
                    matrizDePiezas[0][0] = matrizDePiezas[1][0];
                    pieza[0][0].setIcon(pieza[1][0].getIcon());
                    matrizDePiezas[1][0] = tempEnt;
                    pieza[1][0].setIcon(imagen);
                    iVacio = 1;
		}
            }
            else 
            if (iVacio == 0 && jVacio == 1) 
            {
		randNum = miRandom.nextInt(3); 
                if(randNum == 0) 
		{
                    tempEnt = matrizDePiezas[0][1];
                    imagen = pieza[0][1].getIcon();
                    matrizDePiezas[0][1] = matrizDePiezas[0][0];
                    pieza[0][1].setIcon(pieza[0][0].getIcon());
                    matrizDePiezas[0][0] = tempEnt;
                    pieza[0][0].setIcon(imagen);
                    jVacio = 0;
		} 
                else 
                if(randNum == 1) 
                {
                    tempEnt = matrizDePiezas[0][1];
                    imagen = pieza[0][1].getIcon();
                    matrizDePiezas[0][1] = matrizDePiezas[0][2];
                    pieza[0][1].setIcon(pieza[0][2].getIcon());
                    matrizDePiezas[0][2] = tempEnt;
                    pieza[0][2].setIcon(imagen);
                    jVacio = 2;
                }
                else 
                {
                    tempEnt = matrizDePiezas[0][1];
                    imagen = pieza[0][1].getIcon();
                    matrizDePiezas[0][1] = matrizDePiezas[1][1];
                    pieza[0][1].setIcon(pieza[1][1].getIcon());
                    matrizDePiezas[1][1] = tempEnt;
                    pieza[1][1].setIcon(imagen);
                    iVacio = 1;
		}
            }
            else
            if (iVacio == 0 && jVacio == 2)
            {
		randNum = miRandom.nextInt(2); 
                if (randNum == 0) 
		{
                    tempEnt = matrizDePiezas[0][2];
                    imagen = pieza[0][2].getIcon();
                    matrizDePiezas[0][2] = matrizDePiezas[0][1];
                    pieza[0][2].setIcon(pieza[0][1].getIcon());
                    matrizDePiezas[0][1] = tempEnt;
                    pieza[0][1].setIcon(imagen);
                    jVacio = 1;
		}
		else 
		{
                    tempEnt = matrizDePiezas[0][2];
                    imagen = pieza[0][2].getIcon();
                    matrizDePiezas[0][2] = matrizDePiezas[1][2];
                    pieza[0][2].setIcon(pieza[1][2].getIcon());
                    matrizDePiezas[1][2] = tempEnt;
                    pieza[1][2].setIcon(imagen);
                    iVacio = 1;
		}
            }
            else
            if (iVacio == 1 && jVacio == 0)																				// plakidio[2][0]
            {
                randNum = miRandom.nextInt(3);
		if (randNum == 0) 
                {
                    tempEnt = matrizDePiezas[1][0];
                    imagen = pieza[1][0].getIcon();
                    matrizDePiezas[1][0] = matrizDePiezas[0][0];
                    pieza[1][0].setIcon(pieza[0][0].getIcon());
                    matrizDePiezas[0][0] = tempEnt;
                    pieza[0][0].setIcon(imagen);
                    iVacio = 0;
		}
            else
            if (randNum == 1)
            {
                tempEnt = matrizDePiezas[1][0];
		imagen = pieza[1][0].getIcon();
		matrizDePiezas[1][0] = matrizDePiezas[1][1];
		pieza[1][0].setIcon(pieza[1][1].getIcon());
		matrizDePiezas[1][1] = tempEnt;
		pieza[1][1].setIcon(imagen);
		jVacio = 1;
            }
            else 
            {
                tempEnt = matrizDePiezas[1][0];
		imagen = pieza[1][0].getIcon();
		matrizDePiezas[1][0] = matrizDePiezas[2][0];
		pieza[1][0].setIcon(pieza[2][0].getIcon());
		matrizDePiezas[2][0] = tempEnt;
		pieza[2][0].setIcon(imagen);
		iVacio = 2;
            }
        }
        else
        if (iVacio == 1 && jVacio == 1) 									// i plakidio[2][1]
	{
            randNum = miRandom.nextInt(4); 
            if (randNum == 0)
            {
					tempEnt = matrizDePiezas[1][1];
					imagen = pieza[1][1].getIcon();
					matrizDePiezas[1][1] = matrizDePiezas[0][1];
					pieza[1][1].setIcon(pieza[0][1].getIcon());
					matrizDePiezas[0][1] = tempEnt;
					pieza[0][1].setIcon(imagen);

					iVacio = 0;
				}

				else if (randNum == 1) 
				{

					tempEnt = matrizDePiezas[1][1];
					imagen = pieza[1][1].getIcon();
					matrizDePiezas[1][1] = matrizDePiezas[1][0];
					pieza[1][1].setIcon(pieza[1][0].getIcon());
					matrizDePiezas[1][0] = tempEnt;
					pieza[1][0].setIcon(imagen);

					jVacio = 0;
				}

				else if (randNum == 2) 
				{

					tempEnt = matrizDePiezas[1][1];
					imagen = pieza[1][1].getIcon();
					matrizDePiezas[1][1] = matrizDePiezas[1][2];
					pieza[1][1].setIcon(pieza[1][2].getIcon());
					matrizDePiezas[1][2] = tempEnt;
					pieza[1][2].setIcon(imagen);

					jVacio = 2;
				}

				else 
				{

					tempEnt = matrizDePiezas[1][1];
					imagen = pieza[1][1].getIcon();
					matrizDePiezas[1][1] = matrizDePiezas[2][1];
					pieza[1][1].setIcon(pieza[2][1].getIcon());
					matrizDePiezas[2][1] = tempEnt;
					pieza[2][1].setIcon(imagen);

					iVacio = 2;
				}

			}

			else if (iVacio == 1 && jVacio == 2)
			{
				randNum = miRandom.nextInt(3); 

				if (randNum == 0) 
				{

					tempEnt = matrizDePiezas[1][2];
					imagen = pieza[1][2].getIcon();
					matrizDePiezas[1][2] = matrizDePiezas[0][2];
					pieza[1][2].setIcon(pieza[0][2].getIcon());
					matrizDePiezas[0][2] = tempEnt;
					pieza[0][2].setIcon(imagen);

					iVacio = 0;
				}

				else if (randNum == 1)
				{

					tempEnt = matrizDePiezas[1][2];
					imagen = pieza[1][2].getIcon();
					matrizDePiezas[1][2] = matrizDePiezas[1][1];
					pieza[1][2].setIcon(pieza[1][1].getIcon());
					matrizDePiezas[1][1] = tempEnt;
					pieza[1][1].setIcon(imagen);

					jVacio = 1;
				}

				else 
				{

					tempEnt = matrizDePiezas[1][2];
					imagen = pieza[1][2].getIcon();
					matrizDePiezas[1][2] = matrizDePiezas[2][2];
					pieza[1][2].setIcon(pieza[2][2].getIcon());
					matrizDePiezas[2][2] = tempEnt;
					pieza[2][2].setIcon(imagen);

					iVacio = 2;
				}
			}

			else if (iVacio == 2 && jVacio == 0)
												
			{
				randNum = miRandom.nextInt(2);

				if (randNum == 0) 
				{

					tempEnt = matrizDePiezas[2][0];
					imagen = pieza[2][0].getIcon();
					matrizDePiezas[2][0] = matrizDePiezas[1][0];
					pieza[2][0].setIcon(pieza[1][0].getIcon());
					matrizDePiezas[1][0] = tempEnt;
					pieza[1][0].setIcon(imagen);

					iVacio = 1;
				}

				else 
				{

					tempEnt = matrizDePiezas[2][0];
					imagen = pieza[2][0].getIcon();
					matrizDePiezas[2][0] = matrizDePiezas[2][1];
					pieza[2][0].setIcon(pieza[2][1].getIcon());
					matrizDePiezas[2][1] = tempEnt;
					pieza[2][1].setIcon(imagen);

					jVacio = 1;
				}

			}

			else if (iVacio == 2 && jVacio == 1)
													
			{
				randNum = miRandom.nextInt(3); 

				if (randNum == 0) 
				{

					tempEnt = matrizDePiezas[2][1];
					imagen = pieza[2][1].getIcon();
					matrizDePiezas[2][1] = matrizDePiezas[1][1];
					pieza[2][1].setIcon(pieza[1][1].getIcon());
					matrizDePiezas[1][1] = tempEnt;
					pieza[1][1].setIcon(imagen);

					iVacio = 1;

				} else if (randNum == 1) 
				{

					tempEnt = matrizDePiezas[2][1];
					imagen = pieza[2][1].getIcon();
					matrizDePiezas[2][1] = matrizDePiezas[2][0];
					pieza[2][1].setIcon(pieza[2][0].getIcon());
					matrizDePiezas[2][0] = tempEnt;
					pieza[2][0].setIcon(imagen);

					jVacio = 0;
				}

				else 
				{

					tempEnt = matrizDePiezas[2][1];
					imagen = pieza[2][1].getIcon();
					matrizDePiezas[2][1] = matrizDePiezas[2][2];
					pieza[2][1].setIcon(pieza[2][2].getIcon());
					matrizDePiezas[2][2] = tempEnt;
					pieza[2][2].setIcon(imagen);

					jVacio = 2;
				}
			}

			else if (iVacio == 2 && jVacio == 2) 
													
			{
				randNum = miRandom.nextInt(2);

				if (randNum == 0) 
				{

					tempEnt = matrizDePiezas[2][2];
					imagen = pieza[2][2].getIcon();
					matrizDePiezas[2][2] = matrizDePiezas[1][2];
					pieza[2][2].setIcon(pieza[1][2].getIcon());
					matrizDePiezas[1][2] = tempEnt;
					pieza[1][2].setIcon(imagen);

					iVacio = 1;
				}

				else 
				{

					tempEnt = matrizDePiezas[2][2];
					imagen = pieza[2][2].getIcon();
					matrizDePiezas[2][2] = matrizDePiezas[2][1];
					pieza[2][2].setIcon(pieza[2][1].getIcon());
					matrizDePiezas[2][1] = tempEnt;
					pieza[2][1].setIcon(imagen);

					jVacio = 1;
				}
			}
		} else 
		{
			stopTimer();
		}

	}
    public void stopTimer()
    {
        time.stop();
    }

    class TimerRutaFinal implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            if (listaCorriente != null)
            {
                for (int i = 0; i < 3; i++)
                    for (int j = 0; j < 3; j++)
                    {
                        pieza[i][j].setIcon(new ImageIcon(getClass().getResource(String.format("Imagenes/%d.jpg",listaCorriente.getNodo().getEstado()[i][j]))));
                    }
                listaCorriente = listaCorriente.getSiguiente();
            } 
            else
		stopTimer1();
        } 
        public void stopTimer1()
        {
            time1.stop();
        }
    }
}
