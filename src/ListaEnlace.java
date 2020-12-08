/**
 * @author TuRi
 */
public class ListaEnlace
{
    private ListaNodo primerNodo;
    private ListaNodo ultimoNodo;
    
    public ListaEnlace()
    {
	primerNodo = ultimoNodo = null;
    }
    
    public void insertarFinal(Nodo miNodo)
    {
        if (estaVacio())
            primerNodo = ultimoNodo = new ListaNodo(miNodo);
        else 
	{
            ListaNodo newNode = new ListaNodo(miNodo);
            ultimoNodo.siguienteNodo = newNode;
            newNode.anteriorNodo = ultimoNodo;
            ultimoNodo = newNode;
	}
    }
    
    public void insertarInicio(Nodo node)
    {
	if (estaVacio())
            primerNodo = ultimoNodo = new ListaNodo(node);
	else
        {
            ListaNodo newNode = new ListaNodo(node);
            newNode.siguienteNodo = primerNodo;
            primerNodo.anteriorNodo = newNode;
            primerNodo = newNode;
	}
    }

    public Nodo getNodoMenor()
    {
	Nodo minimumNode = primerNodo.getNodo();
	ListaNodo current = primerNodo.getSiguiente();
	while (current != null)
        {
            if (current.getNodo().getF() < minimumNode.getF())
            minimumNode = current.getNodo();
            current = current.getSiguiente();
	}
	return minimumNode;
    }

    public void eliminarNodo(Nodo node) 
    {
	if (estaVacio())
            return ;
	else 
            if (mismoNodo(node, primerNodo.getNodo())&& primerNodo == ultimoNodo) 
            {
		primerNodo = ultimoNodo = null;
		return;
            }
            else 
                if (mismoNodo(node, primerNodo.getNodo())) 												// komvos
		{
                    primerNodo = primerNodo.getSiguiente();
                    primerNodo.anteriorNodo = null;
                    return;
		} 
                else
                    if (mismoNodo(node, ultimoNodo.getNodo()))
                    {
			ultimoNodo = ultimoNodo.getAnterior();
			ultimoNodo.siguienteNodo = null;
                    }
                    else 
                    {
			ListaNodo current = primerNodo;
			while (current.getSiguiente() != null)
                        {
                            if (mismoNodo(current.getSiguiente().getNodo(), node))
                            {
                                current.siguienteNodo.siguienteNodo.anteriorNodo = current;
                                current.siguienteNodo = current.getSiguiente().getSiguiente();
                                return; 
                            }
                            else
                            current = current.siguienteNodo;
			}
                    }
    }

    public boolean estaVacio()
    {
		return primerNodo == null; 
    }

    public boolean probarExistencia(Nodo node)
    {
	if (!estaVacio())
        {
            ListaNodo current = primerNodo;
            while (current != null)
            {
                if (mismoNodo(current.getNodo(), node))
                    return true;
                else
                    current = current.getSiguiente();
            }
            return false;
	}
	return false;
    }

    public boolean mismoNodo(Nodo A, Nodo B) 
    {
	int[][] stateA = A.getEstado();
	int[][] stateB = B.getEstado();
	for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
		if (stateA[i][j] != stateB[i][j])
                    return false;
	return true;
    }

    public ListaNodo getUltimoNodo() 
    {
	return ultimoNodo;
    }

    public ListaNodo getPrimerNodo()
    {
	return primerNodo;
    }

	
    public void vaciarLista()
    {
    	primerNodo = ultimoNodo = null;
    }
}
