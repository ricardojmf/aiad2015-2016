package Logica;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class Mundo
{
	public Mapa cidade;
	
	public Vector<Producto> productos;
	
	public Vector<Loja> lojas;
	public Vector<Armazem> armazens;
	public Vector<Lixeira> lixeiras;
	public Vector<Recarga> estacoes;
	public Vector<Agencia> agencias;
	
	public Vector<Produzir> producoes;
	public Vector<Trabalhador> trabalhadores;
	
	private char letraAgente = 'T';
	private char letraLoja = 'S';
	private char letraArmazem = 'W';
	private char letraRecarga = 'R';
	private char letraLixeira = 'G';
	private char letraAgencia = 'A';
	
	private void inicializar()
	{
		productos = new Vector<Producto>();
		
		lojas = new Vector<Loja>();
		armazens = new Vector<Armazem>();
		lixeiras = new Vector<Lixeira>();
		estacoes = new Vector<Recarga>();
		agencias = new Vector<Agencia>();
		
		producoes = new Vector<Produzir>();
		trabalhadores = new Vector<Trabalhador>();
	}
	
	private void gestorFicheiro(Vector<String> linhas)
	{
		// LEITURA DAS PRODUCTOS
		
		if (!linhas.elementAt(0).equals("PRODUCTOS"))
		{
			System.exit(0);
		}
		
		int i = 1;
		while(i < linhas.size() && !linhas.elementAt(i).equals("END PRODUCTOS"))
		{
			String s = linhas.elementAt(i);
			String[] varios = s.split(" ");
			
			String nome = "";
			for(int k = 0; k < (varios.length - 1); k++)
			{
				if (k == (varios.length - 2)) // ultimo
				{
					nome += varios[k];
				}
				else
				{
					nome += varios[k] + " ";
				}
			}
			
			int peso = Integer.parseInt(varios[varios.length - 1]);
			
			productos.addElement(new Producto(nome, peso));
			i++;
		}
		i++;
		
		// LEITURA DAS LOJAS
		if (!linhas.elementAt(i).equals("LOJAS"))
		{
			System.exit(0);
		}
		
		i++;
		while(i < linhas.size() && !linhas.elementAt(i).equals("END LOJAS"))
		{
			String s = linhas.elementAt(i);
			if (s.equals("LOJA"))
			{
				i++;
				String info = linhas.elementAt(i);
				i++;
				
				String[] infoo = info.split(" ");
				
				String nome = "";
				for(int k = 0; k < (infoo.length - 2); k++)
				{
					if (k == (infoo.length - 3)) // ultimo
					{
						nome += infoo[k];
					}
					else
					{
						nome += infoo[k] + " ";
					}
				}
				
				int linha = Integer.parseInt(infoo[infoo.length - 2]);
				int coluna = Integer.parseInt(infoo[infoo.length - 1]);
				
				Loja lj = new Loja(linha, coluna, nome);
				
				while(i < linhas.size() && !linhas.elementAt(i).equals("END LOJA"))
				{
					String r = linhas.elementAt(i);
					String[] rr = r.split(" ");
					
					int index = Integer.parseInt(rr[0]);
					int preco = Integer.parseInt(rr[1]);
					
					Producto p = productos.elementAt(index);
					
					lj.productos.add(new ProductoLoja(p, preco));
					
					i++;
				}
				
				lojas.addElement(lj);
			}
			else
			{
				System.exit(0);
			}
			
			i++;
		}
		i++;
		
		// LEITURA DOS ARMAZENS
		if (!linhas.elementAt(i).equals("ARMAZENS"))
		{
			System.exit(0);
		}
		
		i++;
		while(i < linhas.size() && !linhas.elementAt(i).equals("END ARMAZENS"))
		{
			String info = linhas.elementAt(i);
			
			String[] infoo = info.split(" ");
			
			String nome = "";
			for(int k = 0; k < (infoo.length - 2); k++)
			{
				if (k == (infoo.length - 3)) // ultimo
				{
					nome += infoo[k];
				}
				else
				{
					nome += infoo[k] + " ";
				}
			}
			
			int linha = Integer.parseInt(infoo[infoo.length - 2]);
			int coluna = Integer.parseInt(infoo[infoo.length - 1]);
			
			Armazem ar = new Armazem(linha, coluna, nome);
			armazens.addElement(ar);
			i++;
		}
		i++;
	}
	
	private void lerFicheiro(String ficheiro)
	{
		try
		{
			InputStream inputstream = new FileInputStream(Auxiliar.folder + ficheiro);
			Vector<String> file = new Vector<String>();
			
			int data;
			String linha = "";
			
			data = inputstream.read();
			while (data != -1)
			{
				if (data == 10 || data == 13)
				{
					file.addElement(linha);
					linha = "";
				}
				else
				{					
					linha += ((char)data);
				}
				data = inputstream.read();
			}
			file.addElement(linha);
			
			inputstream.close();
			
			int i = 0;
			while(i < file.size())
			{
				if (file.elementAt(i).length() == 0)
					file.removeElementAt(i);
				else
					i++;
			}
			
			gestorFicheiro(file);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public Mundo(String mapa, String objectos)
	{
		cidade = new Mapa(mapa);
		inicializar();
		lerFicheiro(objectos);
	}
	
	public Mundo()
	{
		cidade = new Mapa("mapa.txt");
		inicializar();
		lerFicheiro("objectos.txt");
	}
	
	public void verProductos()
	{
		for(Producto p: productos)
		{
			System.out.println(p.nome);
		}
	}
	
	public void verLojas()
	{
		Auxiliar.writeln("Lojas");
		Auxiliar.writeln("--------------------------------------");
		for(Loja lj: lojas)
		{
			Auxiliar.writeln(lj.nome + " em (" + lj.linha + " , " + lj.coluna + ")");
			for(ProductoLoja pj: lj.productos)
			{
				Auxiliar.writeln("+ " + pj.p.nome + " " + pj.preco + "�");
			}
		}
	}
	
	public void verArmazens()
	{
		Auxiliar.writeln("Armazens");
		Auxiliar.writeln("--------------------------------------");
		for(Armazem ar: armazens)
		{
			Auxiliar.writeln(ar.nome + " em (" + ar.linha + " , " + ar.coluna + ")");
		}
	}
	
	public void mostrarMundo()
	{
		for(Loja edificio: lojas)
		{
			String linha = cidade.matriz.elementAt(edificio.linha);
			String novo = Auxiliar.substituir(linha, edificio.coluna, letraLoja);
			cidade.matriz.set(edificio.linha, novo);
		}
		
		for(Armazem edificio: armazens)
		{
			String linha = cidade.matriz.elementAt(edificio.linha);
			String novo = Auxiliar.substituir(linha, edificio.coluna, letraArmazem);
			cidade.matriz.set(edificio.linha, novo);
		}
		
		for(Lixeira edificio: lixeiras)
		{
			String linha = cidade.matriz.elementAt(edificio.linha);
			String novo = Auxiliar.substituir(linha, edificio.coluna, letraLixeira);
			cidade.matriz.set(edificio.linha, novo);
		}
		
		for(Recarga edificio: estacoes)
		{
			String linha = cidade.matriz.elementAt(edificio.linha);
			String novo = Auxiliar.substituir(linha, edificio.coluna, letraRecarga);
			cidade.matriz.set(edificio.linha, novo);
		}
		
		for(Agencia edificio: agencias)
		{
			String linha = cidade.matriz.elementAt(edificio.linha);
			String novo = Auxiliar.substituir(linha, edificio.coluna, letraAgencia);
			cidade.matriz.set(edificio.linha, novo);
		}
		
		for(Trabalhador tr: trabalhadores)
		{
			String linha = cidade.matriz.elementAt(tr.linha);
			String novo = Auxiliar.substituir(linha, tr.coluna, letraAgente);
			cidade.matriz.set(tr.linha, novo);
		}
	}
	
	public void ocultarMundo()
	{
		for(int linha = 0; linha < cidade.matriz.size(); linha++)
		{
			String s = cidade.matriz.elementAt(linha);
			for(int index = 0; index < s.length(); index++)
			{
				char cha = s.charAt(index);
				if(cha != ' ' && cha != 'O')
				{
					String novo = Auxiliar.substituir(s, index, ' ');
					
					cidade.matriz.set(linha, novo);
					s = novo;
				}
			}
		}
	}
	
	/*
	 * DEFINICAO DE FUNCOES OBJECTIVO
	 */
	
	public boolean trabalhadorTemProducto(Trabalhador tr, Producto p)
	{
		int index = 0;
		while(index < tr.contentor.size())
		{
			Ranhura ra = tr.contentor.elementAt(index);
			
			if (ra.producto.compareTo(p) == 0)
			{
				return true;
			}
			index++;
		}
		return false;
	}
	
	public boolean trabalhadorTemProductoQuantidade(Trabalhador tr, Producto p, int quantidade)
	{
		int index = 0;
		while(index < tr.contentor.size())
		{
			Ranhura ra = tr.contentor.elementAt(index);
			
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
	
	public boolean adicionarContentor(Trabalhador tr, Producto p, int quantidade)
	{
		// se existir no contentor, acrescentar mais unidades
		int index = 0;
		while(index < tr.contentor.size())
		{
			Ranhura ra = tr.contentor.elementAt(index);
			
			if (ra.producto.compareTo(p) == 0)
			{
				ra.quantidade += quantidade;
				return true;
			}
			index++;
		}
		
		// senao criar uma nova ranhura
		tr.contentor.addElement(new Ranhura(p, quantidade));
		
		return true;
	}
	
	public boolean removerContentor(Trabalhador tr, Producto p, int quantidade)
	{
		// se existir no contentor, remove-lo
		int index = 0;
		while(index < tr.contentor.size())
		{
			Ranhura ra = tr.contentor.elementAt(index);
			
			if (ra.producto.compareTo(p) == 0)
			{
				if (ra.quantidade <= quantidade)
				{
					tr.contentor.remove(index);
				}
				else
				{
					ra.quantidade -= quantidade;
				}
				
				return true;
			}
			index++;
		}
		// senao existir, tanto faz
		
		return true;
	}
	
	public boolean removerContentor(Trabalhador tr, Producto p)
	{
		// se existir no contentor, remove-lo
		int index = 0;
		while(index < tr.contentor.size())
		{
			Ranhura ra = tr.contentor.elementAt(index);
			
			if (ra.producto.compareTo(p) == 0)
			{
				tr.contentor.remove(index);
				
				return true;
			}
			index++;
		}
		// senao existir, tanto faz
		
		return true;
	}

	public boolean comprar(Trabalhador tr, Loja lj, int indexProductoLoja, int quantidade)
	{
		ProductoLoja pl = lj.productos.elementAt(indexProductoLoja);
		
		if (tr.riqueza > (pl.preco*quantidade))
		{
			if ( (tr.carga + (pl.p.peso*quantidade)) < tr.cargaMax)
			{
				boolean estado = adicionarContentor(tr, pl.p, quantidade);
				
				if (estado)
				{
					tr.riqueza = tr.riqueza - (pl.preco*quantidade);
					tr.carga = tr.carga + (pl.p.peso*quantidade);
				}
				
				return estado;
			}
		}
		
		return false;
	}
	
	public boolean armazenar(Trabalhador tr, Armazem ar, int indexRanhura, int quantidade)
	{
		Ranhura ra = tr.contentor.elementAt(indexRanhura);
		
		if (ra.quantidade < quantidade)
		{
			return false;
		}
		
		removerContentor(tr, ra.producto, quantidade);
		
		int i = 0;
		while (i < ar.clientes.size())
		{
			// procurar o deposito do trabalhador no armazem
			ContentorArmazem ca = ar.clientes.elementAt(i);
			if(ca.trabalhador.nome.equals(tr.nome))
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
		ContentorArmazem ca = new ContentorArmazem(tr);
		ca.contentor.addElement(new ProductoArmazenado(ra.producto, quantidade));
		ar.clientes.addElement(ca);
		
		return true;
	}

	public boolean aceitarTrabalhoPreco(Trabalhador tr, Agencia ag, int index)
	{
		Vector<TrabalhoPreco> lista = ag.trabalhosPrecos;
		if (index < 0 || index >= lista.size())
		{
			return false;
		}
		
		TrabalhoPreco tra = lista.elementAt(index);
		
		TarefaPreco nova = new TarefaPreco(tra);
		
		tr.tarefasPrecos.addElement(nova);
		
		return true;
	}
	
	public boolean removerTarefaPreco(Trabalhador tr, int index)
	{
		Vector<TarefaPreco> lista = tr.tarefasPrecos;
		
		if (index < 0 || index >= lista.size())
		{
			return false;
		}
		
		tr.tarefasPrecos.removeElementAt(index);
		
		return true;
	}
	
	public boolean aceitarTrabalhoLeiloado(Trabalhador tr, Agencia ag, int index, int proposta)
	{
		Vector<TrabalhoLeiloado> lista = ag.trabalhosLeiloados;
		if (index < 0 || index >= lista.size())
		{
			return false;
		}
		
		TrabalhoLeiloado tra = lista.elementAt(index);
		
		tra.novoTrabalhador(tr, proposta);
		
		return true;
	}
	
	public boolean removerTarefaLeiloado(Trabalhador tr, int index)
	{
		Vector<TarefaLeiloada> lista = tr.tarefasLeiloadas;
		
		if (index < 0 || index >= lista.size())
		{
			return false;
		}
		
		TrabalhoLeiloado tl = tr.tarefasLeiloadas.elementAt(index).tb;		
		tr.tarefasLeiloadas.removeElementAt(index);
		
		// TODO mudar o trabalho na agencia, recomecar o leilao
		
		return true;
	}

	public boolean produzir(Trabalhador tr, Produzir pd)
	{
		// verificar se trabalhador tem as ferramentas necessarias
		for(String fe: pd.ferramentas)
		{
			int index = 0;
			for (; index < tr.ferramentas.size() && tr.ferramentas.elementAt(index).compareTo(fe) != 0; index++) { }
			
			if (index >= tr.ferramentas.size())
				return false;
		}
		
		Auxiliar.writeln("1");
		
		// verificar se trabalhador tem todos os productos
		for (Ranhura ra : pd.pedido)
		{
			int index = 0;
			
			while(index < tr.contentor.size())
			{
				Ranhura ra2 = tr.contentor.elementAt(index);
				
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
		
		Auxiliar.writeln("2");
		
		// remover do contentor do trabalhador todos os productos
		for (Ranhura ra : pd.pedido)
		{
			if (!removerContentor(tr, ra.producto, ra.quantidade))
			{
				return false;
			}
		}
		
		Auxiliar.writeln("3");
		
		// colocar no contentor do trabalhor o novo producto
		return adicionarContentor(tr, pd.recompensa, pd.quantidade);
	}

	public boolean trocarProductos(Trabalhador tr1, Producto p1, int quantidade1, Trabalhador tr2, Producto p2, int quantidade2)
	{
		if ( !trabalhadorTemProductoQuantidade(tr1, p1, quantidade1) || !trabalhadorTemProductoQuantidade(tr2, p2, quantidade2) )
		{
			return false;
		}
		
		int peso1 = p1.peso*quantidade1;
		int peso2 = p2.peso*quantidade2;
		
		if ( ( (tr1.carga + peso2) < tr1.cargaMax ) && ( (tr2.carga + peso1) < tr2.cargaMax ) )
		{
			boolean estadoAdicionar1 = adicionarContentor(tr1, p2, quantidade2);
			boolean estadoAdicionar2 = adicionarContentor(tr2, p1, quantidade1);
			boolean estadoRemover1 = removerContentor(tr1, p1, quantidade1);
			boolean estadoRemover2 = removerContentor(tr2, p2, quantidade2);
			
			return (estadoAdicionar1 && estadoAdicionar2 && estadoRemover1 && estadoRemover2);
		}
		
		return false;
	}
	
	public boolean trocarProductoDinheiro(Trabalhador tr1, Producto p1, int quantidade1, Trabalhador tr2, int dinheiro)
	{
		if ( !trabalhadorTemProductoQuantidade(tr1, p1, quantidade1))
		{
			return false;
		}
		
		int peso1 = p1.peso*quantidade1;
		
		if ( (tr2.carga + peso1) < tr2.cargaMax )
		{
			boolean estadoAdicionar = adicionarContentor(tr2, p1, quantidade1);
			boolean estadoRemover = removerContentor(tr1, p1, quantidade1);
			
			tr2.riqueza -= dinheiro;
			tr1.riqueza += dinheiro;
			
			return (estadoAdicionar && estadoRemover);
		}
		
		return false;
	}

	
}
