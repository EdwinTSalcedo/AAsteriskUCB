/**
 * @author TuRi
 */

public class Nodo
{
    private int[][] estado = new int[3][3];
    private int h; 
    private int g;
    private int id;
    private int parentID;
    private int vacioX;
    private int vacioY;

    public Nodo()
    {
        g = -1;
        id = -1;
	parentID = -1;
    }

    public Nodo(int g, int[][] estado, int id, int parentID, int flag) 
    {
	this.g = g;
	switch (flag)
        {
            case 1:
            setEstado_amplitud(estado);
            break;
            case 2:
            setEstado_profundidad(estado);
            break;
	}
	this.id = id;
	this.parentID = parentID;
    }

    public void copiarTodo(Nodo n, int flag) 
    {
        switch (flag)
        {
            case 1:
            setEstado_amplitud(n.getEstado());
            break;
            case 2:
            setEstado_profundidad(n.getEstado());
            break;
	}
	h = n.getH();
	g = n.getG();
	id = n.id;
	parentID = n.getParentID();
	vacioX = n.getEmptyX();
	vacioY = n.getEmptyY();
    }

    public int[][] getEstado()
    {
	return estado;
    }

    public void setEstado_profundidad(int[][] miEstado)
    {

	for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
		this.estado[i][j] = miEstado[i][j];
	calcularXY();
	calcularF_profundidad();

    }

    public void setEstado_amplitud(int[][] state)
    {
	for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
		this.estado[i][j] = state[i][j];
	calcularXY();
	calcularF_amplitud();
    }

    public void calcularXY() 
    {
	for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
		if (estado[i][j] == 9)
                {
                    vacioX = i;
                    vacioY = j;
                    break;
		}
    }

    public int getID() 
    {
	return id;
    }

    public int getParentID() 
    {
        return parentID;
    }

    public int getG() 
    {
	return g;
    }

    public int getH() 
    {
	return h;
    }

    public int getEmptyX() 
    {
	return vacioX;
    }

    public int getEmptyY() 
    {
	return vacioY;
    }

    public int getF() 
    {
	return g + h;
    }

    public void calcularF_amplitud()
    {
        int tempH = 0;
	int estadoMetaX = 0, estadoMetaY = 0;
	for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
            {
		switch (estado[i][j])
                {
                    case 1:
                    estadoMetaX = 0;
                    estadoMetaY = 0;
                    break;
                    case 2:
                    estadoMetaX = 0;
                    estadoMetaY = 1;
                    break;
                    case 3:
                    estadoMetaX = 0;
                    estadoMetaY = 2;
                    break;
                    case 4:
                    estadoMetaX = 1;
                    estadoMetaY = 0;
                    break;
                    case 5:
                    estadoMetaX = 1;
                    estadoMetaY = 1;
                    break;
                    case 6:
                    estadoMetaX = 1;
                    estadoMetaY = 2;
                    break;
                    case 7:
                    estadoMetaX = 2;
                    estadoMetaY = 0;
                    break;
                    case 8:
                    estadoMetaX = 2;
                    estadoMetaY = 1;
                    break;
                    case 9:
                    estadoMetaX = 2;
                    estadoMetaY = 2;
                    break;
                }
		tempH += Math.abs(estadoMetaX - i) + Math.abs(estadoMetaY - j);
            }

            this.h = tempH;
	}

    public void calcularF_profundidad() 
    {
	int tempF = 0;
	int estadoMetaX = 0, estadoMetaY = 0;
	for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
            {
            	switch (estado[i][j])
                {
                    case 1:
                    estadoMetaX = 0;
                    estadoMetaY = 0;
                    break;
                    case 2:
                    estadoMetaX = 0;
                    estadoMetaY = 1;
                    break;
                    case 3:
                    estadoMetaX = 0;
                    estadoMetaY = 2;
                    break;
                    case 4:
                    estadoMetaX = 1;
                    estadoMetaY = 0;
                    break;
                    case 5:
                    estadoMetaX = 1;
                    estadoMetaY = 1;
                    break;
                    case 6:
                    estadoMetaX = 1;
                    estadoMetaY = 2;
                    break;
                    case 7:
                    estadoMetaX = 2;
                    estadoMetaY = 0;
                    break;
                    case 8:
                    estadoMetaX = 2;
                    estadoMetaY = 1;
                    break;
                    case 9:
                    estadoMetaX = 2;
                    estadoMetaY = 2;
                    break;
		}
		int tempAm = (estadoMetaX - i);
		int tempBm = (estadoMetaY - j);
                if(tempAm!=0||tempBm!=0)
                    tempF++;
            }
            this.h = tempF;
	}
}