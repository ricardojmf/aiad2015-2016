package Logica;

import java.util.Vector;

public class Trabalhador extends Identidade
{
	public String nome;
	public int riqueza;
	
	public int velocidade;
	public int bateriaMax;
	public int bateria;
	public int cargaMax;
	public int carga;
	
	public int transporte; // 0 ate 4
	public boolean meioTransporte; // false: estradas ; true: ar
	public Vector<String> ferramentas;
	
	public Vector<Ranhura> contentor;
	
	public Vector<TarefaPreco> tarefasPrecos;
	public Vector<TarefaLeiloada> tarefasLeiloadas;
	
	private static int riquezaInicio = 1500;
	
	private void inicializarTransporte()
	{
		switch(transporte)
		{
		case (1):
			letra = 'C';
			velocidade = 3;
			bateriaMax = 500;
			cargaMax = 550;
			meioTransporte = false;
			ferramentas.addElement("f1");
			ferramentas.addElement("f2");
			break;
		case (2):
			letra = 'H';
			velocidade = 5;
			bateriaMax = 250; // 250 , 73: gasolineira
			cargaMax = 100;
			meioTransporte = true;
			ferramentas.addElement("f1");
			break;
		case (3):
			letra = 'M';
			velocidade = 4;
			bateriaMax = 350;
			cargaMax = 300;
			meioTransporte = false;
			ferramentas.addElement("f3");
			ferramentas.addElement("tool1");
			break;
		case (4):
			letra = 'T';
			velocidade = 1;
			bateriaMax = 3000;
			cargaMax = 1000;
			meioTransporte = false;
			ferramentas.addElement("f2");
			ferramentas.addElement("f3");
			break;
		}
		
		bateria = bateriaMax;
		carga = 0;
	}
	
	private void inicializar(String no, int tipoAgente)
	{
		nome = no;
		riqueza = riquezaInicio;
		
		transporte = tipoAgente;
		
		ferramentas = new Vector<String>();
		inicializarTransporte();
		
		tarefasPrecos = new Vector<TarefaPreco>();
		tarefasLeiloadas = new Vector<TarefaLeiloada>();
		contentor = new Vector<Ranhura>();
	}
	
	public Trabalhador(String no, int tipoAgente)
	{
		super(0, 0);
		
		inicializar(no, tipoAgente);
	}
	
	public Trabalhador(String no, int tipoAgente, int li, int col)
	{
		super(li, col);
		
		inicializar(no, tipoAgente);
	}
	
	public int compareTo(Trabalhador obj)
	{
		return (nome.compareTo(obj.nome));
	}

	public void verDetalhesTransporte()
	{
		Auxiliar.writeln("--------------------------------------");
		Auxiliar.writeln("Detalhes Transporte " + nome);
		Auxiliar.writeln("--------------------------------------");
		Auxiliar.writeln("Velocidade: " + velocidade);
		Auxiliar.writeln("Bateria Max: " + bateriaMax);
		Auxiliar.writeln("Carga Max: " + cargaMax);
		Auxiliar.writeln("Tipo de Transporte: " + transporte);
		Auxiliar.writeln("Meio de Transporte: " + meioTransporte);
		
		for(String s: ferramentas)
		{
			Auxiliar.writeln("Ferramenta: " + s);
		}
		Auxiliar.writeln("--------------------------------------");
	}
	
	public void verContentor()
	{
		Auxiliar.writeln("--------------------------------------");
		Auxiliar.writeln("Contentor " + nome);
		Auxiliar.writeln("--------------------------------------");
		for(Ranhura ra: contentor)
		{
			Auxiliar.writeln(ra.producto.nome + " x" + ra.quantidade );
		}
		Auxiliar.writeln("--------------------------------------");
	}
	
	public void verActual()
	{
		Auxiliar.writeln("--------------------------------------");
		Auxiliar.writeln("Actualmente " + nome);
		Auxiliar.writeln("--------------------------------------");
		Auxiliar.writeln("Coordenadas: (" + this.linha + " , " + this.coluna + ")");
		Auxiliar.writeln("Riqueza: " + riqueza);
		Auxiliar.writeln("Carga: " + carga);
		Auxiliar.writeln("Bateria: " + bateria);
		Auxiliar.writeln("--------------------------------------");
	}
	
	public void ver()
	{
		verDetalhesTransporte();
		verActual();
		verContentor();
	}
	
	public boolean trabalhadorTemProducto(Producto p)
	{
		int index = 0;
		while(index < contentor.size())
		{
			Ranhura ra = contentor.elementAt(index);
			
			if (ra.producto.compareTo(p) == 0)
			{
				return true;
			}
			index++;
		}
		return false;
	}
	
	public boolean trabalhadorTemProductoQuantidade(Producto p, int quantidade)
	{
		int index = 0;
		while(index < contentor.size())
		{
			Ranhura ra = contentor.elementAt(index);
			
			if (ra.producto.compareTo(p) == 0)
			{
				if (ra.quantidade >= quantidade)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			index++;
		}
		return false;
	}
	
	public boolean trabalhadorTemProductoQuantidade(Ranhura productos)
	{
		int index = 0;
		while(index < contentor.size())
		{
			Ranhura ra = contentor.elementAt(index);
			
			if (ra.producto.compareTo(productos.producto) == 0)
			{
				if (ra.quantidade >= productos.quantidade)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			index++;
		}
		return false;
	}
	
	public boolean adicionarContentor(Producto p, int quantidade)
	{
		return adicionarContentor(new Ranhura(p, quantidade));
	}
	
	public boolean adicionarContentor(Ranhura productos)
	{		
		// se existir no contentor, acrescentar mais unidades
		int index = 0;
		while(index < contentor.size())
		{
			Ranhura ra = contentor.elementAt(index);
			
			if (ra.producto.compareTo(productos.producto) == 0)
			{
				ra.quantidade += productos.quantidade;
				carga += productos.obterTamanhoTotal();
				
				return true;
			}
			index++;
		}
		
		// senao criar uma nova ranhura
		contentor.addElement(productos);
		carga += productos.obterTamanhoTotal();
		
		return true;
	}
	
	public boolean removerContentor(Ranhura productos)
	{
		// se existir no contentor, remove-lo
		int index = 0;
		while(index < contentor.size())
		{
			Ranhura ra = contentor.elementAt(index);
			
			if (ra.producto.compareTo(productos.producto) == 0)
			{
				if (ra.quantidade <= productos.quantidade)
				{
					contentor.remove(index);
					carga -= productos.obterTamanhoTotal();
				}
				else
				{
					ra.quantidade -= productos.quantidade;
					carga -= productos.obterTamanhoTotal();
				}
				
				return true;
			}
			index++;
		}
		// senao existir, tanto faz
		
		return true;
	}
	
	public boolean removerContentor(Producto p, int quantidade)
	{
		// se existir no contentor, remove-lo
		return( removerContentor(new Ranhura(p, quantidade)) );
	}
	
	public boolean removerContentor(Producto p)
	{
		// se existir no contentor, remove-lo
		int index = 0;
		while(index < contentor.size())
		{
			Ranhura ra = contentor.elementAt(index);
			
			if (ra.producto.compareTo(p) == 0)
			{
				contentor.remove(index);
				
				return true;
			}
			index++;
		}
		// senao existir, tanto faz
		
		return true;
	}
	
	public boolean comprar(Loja lj, int indexProductoLoja, int quantidade)
	{
		Producto pl = lj.productos.elementAt(indexProductoLoja);
		
		if (riqueza >= (pl.preco*quantidade))
		{			
			if ( (carga + (pl.peso*quantidade)) <= cargaMax)
			{
				boolean estado = adicionarContentor(pl, quantidade);
				
				if (estado)
				{
					riqueza = riqueza - (pl.preco*quantidade);
					carga = carga + (pl.peso*quantidade);
				}
				
				return estado;
			}
		}
		
		return false;
	}
	
	public boolean comprar(Loja lj, Producto producto, int quantidade)
	{
		int indexProductoLoja = 0;
		while(indexProductoLoja < lj.productos.size() && !lj.productos.elementAt(indexProductoLoja).nome.equals(producto.nome))
		{
			indexProductoLoja++;
		}
		
		if(indexProductoLoja >= lj.productos.size() )
		{
			return false;
		}
		
		Producto pl = lj.productos.elementAt(indexProductoLoja);
		
		if (riqueza >= (pl.preco*quantidade))
		{
			if ( (carga + (pl.peso*quantidade)) <= cargaMax)
			{				
				boolean estado = adicionarContentor(pl, quantidade);
				
				if (estado)
				{
					riqueza = riqueza - (pl.preco*quantidade);
				}
				
				return estado;
			}
		}
		
		return false;
	}
	
	public boolean armazenar(Armazem ar, int indexRanhura, int quantidade)
	{
		Ranhura ra = contentor.elementAt(indexRanhura);
		
		if (ra.quantidade < quantidade)
		{
			return false;
		}
		
		boolean estado1 = removerContentor(ra.producto, quantidade);
		
		int i = 0;
		while (i < ar.clientes.size())
		{
			// procurar o deposito do trabalhador no armazem
			ContentorArmazem ca = ar.clientes.elementAt(i);
			if(ca.trabalhador.nome.equals(nome))
			{
				int j = 0;
				while(j < ca.contentor.size())
				{
					// procurar a ranhura do producto a acrescentar em quantidade
					ProductoArmazenado pr = ca.contentor.elementAt(j);
					if (pr.producto.nome.equals(ra.producto.nome))
					{
						pr.quantidade += quantidade;
						
						return true;
					}
					
					j++;
				}
				
				ca.contentor.addElement(new ProductoArmazenado(ra.producto, quantidade));
				
				return true;
			}
			
			i++;
		}
		
		// adicionar novo cliente a lista
		ContentorArmazem ca = new ContentorArmazem(this);
		ca.contentor.addElement(new ProductoArmazenado(ra.producto, quantidade));
		ar.clientes.addElement(ca);
		
		return true;
	}
	
	public boolean armazenar(Armazem ar, Producto producto, int quantidade)
	{
		int indexRanhura = 0;
		while(indexRanhura < contentor.size() && !contentor.elementAt(indexRanhura).producto.nome.equals(producto.nome))
		{
			indexRanhura++;
		}
		
		if(indexRanhura >= contentor.size() )
		{
			return false;
		}
		
		return( armazenar(ar, indexRanhura, quantidade) );
	}
	
	public boolean armazenar(Armazem ar, Ranhura productos)
	{
		return( armazenar(ar, productos.producto, productos.quantidade) );
	}
	
	public boolean podeAdicionarContentor(Producto producto, int quantidade)
	{
		return podeAdicionarContentor(new Ranhura(producto, quantidade));
	}
	
	public boolean podeAdicionarContentor(Ranhura productos)
	{
		return( productos.obterTamanhoTotal() + carga <= cargaMax );
	}
	
	public boolean podeComprar(Producto producto, int quantidade)
	{
		return podeComprar(new Ranhura(producto, quantidade));
	}
	
	public boolean podeComprar(Ranhura productos)
	{
		return( productos.obterPrecoTotal() <= riqueza );
	}
	
	public boolean removerArmazem(Armazem ar, Producto producto, int quantidade)
	{
		int indexRanhura = 0;
		while(indexRanhura < contentor.size() && !contentor.elementAt(indexRanhura).producto.nome.equals(producto.nome))
		{
			indexRanhura++;
		}
		
		if(indexRanhura >= contentor.size() )
		{
			return false;
		}
		
		return( removerArmazem(ar, indexRanhura, quantidade) );
	}
	
	public boolean removerArmazem(Armazem ar, int indexRanhura, int quantidade)
	{
		Ranhura ra = contentor.elementAt(indexRanhura);
		
		return(removerArmazem(ar, ra));
	}
	
	public boolean removerArmazem(Armazem ar, Ranhura productos)
	{	
		if(!podeAdicionarContentor(productos))
		{
			return false;
		}		
		
		ContentorArmazem ca = ar.obterContentor(this);
		if(ca == null)
		{
			return false;
		}
		
		ProductoArmazenado pa = ca.existeProducto(productos.producto);
		if(pa == null)
		{
			return false;
		}
		
		if(productos.quantidade > pa.quantidade)
		{
			return false;
		}
		else
		{
			boolean estado = adicionarContentor( productos );
			
			if(productos.quantidade == pa.quantidade)
			{
				ca.contentor.removeElement(pa);
			}
			else
			{
				pa.quantidade -= productos.quantidade;
			}
			
			return(estado);
		}
	}
	
	public boolean possivelProduzir(Produzir pd)
	{
		// verificar se trabalhador tem as ferramentas necessarias
		for(String fe: pd.ferramentas)
		{
			int index = 0;
			for (; index < ferramentas.size() && ferramentas.elementAt(index).compareTo(fe) != 0; index++) { }
			
			if (index >= ferramentas.size())
				return false;
		}
		
		// verificar se trabalhador tem todos os productos
		for (Ranhura ra : pd.pedido)
		{
			int index = 0;
			
			while(index < contentor.size())
			{
				Ranhura ra2 = contentor.elementAt(index);
				
				if (ra.compareTo(ra2) == 0)
				{
					if (ra.quantidade < ra2.quantidade)
					{
						Auxiliar.writeln("Morreu aqui: " + ra.toString());
						return false;
					}
					
					break;
				}
				index++;
			}
		}
		
		return true;
	}
	
	public boolean produzir(Produzir pd)
	{
		if(!possivelProduzir(pd))
		{
			return false;
		}
		
		// remover do contentor do trabalhador todos os productos
		for (Ranhura ra : pd.pedido)
		{
			if (!removerContentor(ra.producto, ra.quantidade))
			{
				return false;
			}
		}
		
		// colocar no contentor do trabalhor o novo producto
		return adicionarContentor(pd.recompensa, pd.quantidade);
	}
	
	private Vector<Ranhura> naoInterseccao(Vector<Ranhura> request)
	{
		Vector<Ranhura> r = new Vector<Ranhura>();
		
		for(Ranhura guardado: contentor)
		{
			boolean existe = false;
			for(Ranhura evitar: request)
			{
				if(!guardado.producto.nome.equals(evitar.producto.nome))
				{
					existe = true;
					break;
				}
			}
			
			if(existe)
			{
				r.addElement(guardado);
			}
		}
		
		return r;
	}
	
	public boolean armazenarAleatoriamente(Armazem ar, Vector<Ranhura> productosNaoArmazenar)
	{
		Vector<Ranhura> productosArmazenar = naoInterseccao( productosNaoArmazenar );
		
		int total = 0;
		for(Ranhura ra: productosArmazenar)
		{
			total += ra.obterTamanhoTotal();
		}
		
		for(int index = 0; index < productosArmazenar.size(); index++)
		{
			Ranhura productos = productosArmazenar.elementAt(index);
			
			// calcular minimo de tamanho a remover
			int qMax;
			for(qMax = 1; qMax <= productos.quantidade; qMax++)
			{
				if( (carga - qMax*productos.producto.peso) <= cargaMax)
				{
					break;
				}
			}
			
			if(qMax <= productos.quantidade)
			{
				armazenar(ar, productos.producto, qMax);
			}
			else
			{
				armazenar(ar, productos);
			}
			
			if(carga < cargaMax)
			{
				break;
			}
		}
		
		return true;
	}
	
	// MENSAGENS ENTRE SERVIDOR/CLIENTE E CLIENTE/CLIENTE
	public String stringEnviarEstado()
	{
		String r = nome + "|" + transporte + " " + carga + " " + bateria + " " + riqueza;
		return r;
	}
	
	public String stringEnviarContentor()
	{
		String r = "[";
		
		for(int index = 0; index < contentor.size(); index++)
		{
			Ranhura ra = contentor.elementAt(index);
			String a = "";
			
			if(index != (contentor.size() - 1))
			{
				a = ra.producto.nome + " " + ra.quantidade + ",";
			}
			else
			{
				a = ra.producto.nome + " " + ra.quantidade;
			}
			r = r + a;
		}
		r = r + "]";
		
		return r;
	}
}
