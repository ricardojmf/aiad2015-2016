package Logica;

import java.util.Vector;

public class Armazem extends Local
{
	public Vector<ContentorArmazem> clientes;
	
	public Armazem(int li, int col)
	{
		super(li, col, 'W');
		clientes = new Vector<ContentorArmazem>();
	}
	
	public Armazem(int li, int col, String nome)
	{
		super(li, col, 'W', nome);
		clientes = new Vector<ContentorArmazem>();
	}

	public ContentorArmazem obterContentor(Trabalhador tr)
	{
		for(ContentorArmazem ca: clientes)
		{
			if (ca.trabalhador.nome.equals(tr.nome))
				return ca;
		}
		return null;
	}
	
	public ProductoArmazenado obterProducto(Trabalhador tr, Producto p)
	{
		ContentorArmazem ca = obterContentor(tr);
		ProductoArmazenado pa = ca.existeProducto(p);
		
		return pa;
	}
	
	public String stringEnviarEstado()
	{
		return( localToString() );
	}
	
	public void ver()
	{
		for(ContentorArmazem ca: clientes)
		{
			Auxiliar.writeln(ca.trabalhador.nome);
			for(ProductoArmazenado pa: ca.contentor)
			{
				Auxiliar.writeln(pa.productoArmazeadoToString());
			}
		}
	}
	
	public void verDetalhes()
	{
		System.out.println("------------------------------");
		System.out.println(nome + " (" + linha + ", " + coluna + ")");
		System.out.println("------------------------------");
		for(ContentorArmazem ca: clientes)
		{
			System.out.print("+ ");
			Auxiliar.writeln(ca.trabalhador.nome);
			for(ProductoArmazenado pa: ca.contentor)
			{
				Auxiliar.writeln(pa.productoArmazeadoToString());
			}
		}
	}
	
	public String stringEnviarArmazem()
	{
		String r = "[";
		
		for(int index = 0; index < clientes.size(); index++)
		{
			ContentorArmazem ca = clientes.elementAt(index);
			String a = ca.trabalhador.nome + "| [";
			
			for(int index2 = 0; index2 < ca.contentor.size(); index2++)
			{
				ProductoArmazenado pa = ca.contentor.elementAt(index2);
				
				String aa = pa.productoArmazeadoToString();
				if(index2 != (ca.contentor.size() - 1))
				{
					a = a + aa + ",";
				}
				else
				{
					a = a + aa;
				}
			}
			a = a + "]";
			
			if(index != (clientes.size() - 1))
			{
				r = r + a + ",";
			}
			else
			{
				r = r + a;
			}
		}
		
		r = r + "]";
		return( r );
	}
}
