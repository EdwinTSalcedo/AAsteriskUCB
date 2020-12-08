/**
 * @author TuRi
 */
public class ListaNodo
{
    private Nodo miNodo;
    public ListaNodo siguienteNodo;
    public ListaNodo anteriorNodo;
    
    public ListaNodo() 
    {
        miNodo = new Nodo();
    }

    public ListaNodo(Nodo elNodo)
    {
	this(elNodo, null, null);
    }

    public ListaNodo(Nodo node, ListaNodo previous, ListaNodo next)
    {
	this.miNodo = node;
	anteriorNodo = previous;
	siguienteNodo = next;
    }

    public Nodo getNodo() 
    {
        return miNodo;
    }

    public ListaNodo getSiguiente() 
    {
	return siguienteNodo;
    }

    public ListaNodo getAnterior() 
    {
	return anteriorNodo;
    }
}