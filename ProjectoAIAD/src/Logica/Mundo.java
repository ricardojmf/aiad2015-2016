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
	
	public Vector<Produzir> producoes;
	//public Vector<Trabalhador> trabalhadores;
	
	private void inicializar()
	{
		productos = new Vector<Producto>();
		
		lojas = new Vector<Loja>();
		armazens = new Vector<Armazem>();
		lixeiras = new Vector<Lixeira>();
		estacoes = new Vector<Recarga>();
		
		producoes = new Vector<Produzir>();
		//trabalhadores = new Vector<Trabalhador>();
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
			for(int k = 0; k < (varios.length - 2); k++)
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
			
			String novo = nome.substring(0, nome.length() - 1);
			
			int peso = Integer.parseInt(varios[varios.length - 2]);
			int preco = Integer.parseInt(varios[varios.length - 1]);
			
			productos.addElement(new Producto(novo, preco, peso));
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
					
					Producto p = productos.elementAt(index);
					
					lj.productos.add(p);
					
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
		
		// LEITURA DOS RECARGAS
		if (!linhas.elementAt(i).equals("RECARGAS"))
		{
			System.exit(0);
		}
		
		i++;
		while(i < linhas.size() && !linhas.elementAt(i).equals("END RECARGAS"))
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
			
			Recarga ar = new Recarga(linha, coluna, nome);
			estacoes.addElement(ar);
			i++;
		}
		i++;
		
		
		
		
		
		
		
		
		
		
		
		// LEITURA DAS LOJAS
		if (!linhas.elementAt(i).equals("PRODUCOES"))
		{
			System.exit(0);
		}
		
		i++;
		while(i < linhas.size() && !linhas.elementAt(i).equals("END PRODUCOES"))
		{
			String s = linhas.elementAt(i);
			if (s.equals("PRODUCAO"))
			{
				i++;
				String info = linhas.elementAt(i);
				i++;
				
				String[] infoo = info.split(" ");
				
				int indexNovo = Integer.parseInt(infoo[0]);
				int quantidadeNovo = Integer.parseInt(infoo[1]);
				String ferramenta = infoo[2];
				
				Vector<String> ferramentas = new Vector<String>();
				ferramentas.addElement(ferramenta);
				
				Producto p = productos.elementAt(indexNovo);
				
				Vector<Ranhura> requisitos = new Vector<Ranhura>();
				
				while(i < linhas.size() && !linhas.elementAt(i).equals("END PRODUCAO"))
				{
					String r = linhas.elementAt(i);
					String[] rr = r.split(" ");
					
					int indexRequerido = Integer.parseInt(rr[0]);
					int quantidadeRequerido = Integer.parseInt(rr[1]);
					
					Producto p2 = productos.elementAt(indexRequerido);
					Ranhura ra = new Ranhura(p2, quantidadeRequerido);
					
					requisitos.addElement(ra);
					
					i++;
				}
				
				Produzir pro = new Produzir(requisitos, ferramentas, p, quantidadeNovo);
				
				producoes.addElement(pro);
			}
			else
			{
				System.exit(0);
			}
			
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
			for(Producto pj: lj.productos)
			{
				Auxiliar.writeln(pj.toString());
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
			String novo = Auxiliar.substituir(linha, edificio.coluna, Auxiliar.letraLoja);
			cidade.matriz.set(edificio.linha, novo);
		}
		
		for(Armazem edificio: armazens)
		{
			String linha = cidade.matriz.elementAt(edificio.linha);
			String novo = Auxiliar.substituir(linha, edificio.coluna, Auxiliar.letraArmazem);
			cidade.matriz.set(edificio.linha, novo);
		}
		
		for(Lixeira edificio: lixeiras)
		{
			String linha = cidade.matriz.elementAt(edificio.linha);
			String novo = Auxiliar.substituir(linha, edificio.coluna, Auxiliar.letraLixeira);
			cidade.matriz.set(edificio.linha, novo);
		}
		
		for(Recarga edificio: estacoes)
		{
			String linha = cidade.matriz.elementAt(edificio.linha);
			String novo = Auxiliar.substituir(linha, edificio.coluna, Auxiliar.letraRecarga);
			cidade.matriz.set(edificio.linha, novo);
		}
		
//		for(Trabalhador tr: trabalhadores)
//		{
//			String linha = cidade.matriz.elementAt(tr.linha);
//			String novo = Auxiliar.substituir(linha, tr.coluna, letraAgente);
//			cidade.matriz.set(tr.linha, novo);
//		}
	}
	
	public void ocultarMundo()
	{
		for(int linha = 0; linha < cidade.matriz.size(); linha++)
		{
			String s = cidade.matriz.elementAt(linha);
			for(int index = 0; index < s.length(); index++)
			{
				char cha = s.charAt(index);
				if(cha != Auxiliar.letraEstrada && cha != Auxiliar.letraParede)
				{
					String novo = Auxiliar.substituir(s, index, Auxiliar.letraEstrada);
					
					cidade.matriz.set(linha, novo);
					s = novo;
				}
			}
		}
	}
	
	/*
	 * DEFINICAO DE FUNCOES OBJECTIVO
	 */
	public Loja obterLoja(Ponto pos)
	{
		for(Loja local: lojas)
		{
			if(local.mesmaPosicao(pos))
			{
				return local;
			}
		}
		return null;
	}
	
	public Armazem obterArmazem(Ponto pos)
	{
		for(Armazem local: armazens)
		{
			if(local.mesmaPosicao(pos))
			{
				return local;
			}
		}
		return null;
	}
	
	public Recarga obterEstacao(Ponto pos)
	{
		for(Recarga local: estacoes)
		{
			if(local.mesmaPosicao(pos))
			{
				return local;
			}
		}
		return null;
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

	public boolean trocarProductos(Trabalhador tr1, Producto p1, int quantidade1, Trabalhador tr2, Producto p2, int quantidade2)
	{
		if ( !tr1.trabalhadorTemProductoQuantidade(p1, quantidade1) || !tr2.trabalhadorTemProductoQuantidade(p2, quantidade2) )
		{
			return false;
		}
		
		int peso1 = p1.peso*quantidade1;
		int peso2 = p2.peso*quantidade2;
		
		if ( ( (tr1.carga + peso2) < tr1.cargaMax ) && ( (tr2.carga + peso1) < tr2.cargaMax ) )
		{
			boolean estadoAdicionar1 = tr1.adicionarContentor(p2, quantidade2);
			boolean estadoAdicionar2 = tr2.adicionarContentor(p1, quantidade1);
			boolean estadoRemover1 = tr1.removerContentor(p1, quantidade1);
			boolean estadoRemover2 = tr2.removerContentor(p2, quantidade2);
			
			return (estadoAdicionar1 && estadoAdicionar2 && estadoRemover1 && estadoRemover2);
		}
		
		return false;
	}
	
	public boolean trocarProductoDinheiro(Trabalhador tr1, Producto p1, int quantidade1, Trabalhador tr2, int dinheiro)
	{
		if ( !tr1.trabalhadorTemProductoQuantidade(p1, quantidade1))
		{
			return false;
		}
		
		int peso1 = p1.peso*quantidade1;
		
		if ( (tr2.carga + peso1) < tr2.cargaMax )
		{
			boolean estadoAdicionar = tr2.adicionarContentor(p1, quantidade1);
			boolean estadoRemover = tr1.removerContentor(p1, quantidade1);
			
			tr2.riqueza -= dinheiro;
			tr1.riqueza += dinheiro;
			
			return (estadoAdicionar && estadoRemover);
		}
		
		return false;
	}

	public Vector<Loja> obterLojasProducto(Producto p)
	{
		Vector<Loja> r = new Vector<Loja>();
		
		for(Loja l: lojas)
		{
			for(Producto p2: l.productos)
			{
				if(p2.nome.equals(p.nome))
				{
					r.addElement(l);
					break;
				}
			}
		}
		
		return r;
	}
	
	public Vector<Armazem> obterArmazensProducto(Trabalhador tr, Producto p)
	{
		Vector<Armazem> r = new Vector<Armazem>();
		
		for(Armazem ar: armazens)
		{
			ContentorArmazem contentor = ar.obterContentor(tr);
			if (contentor != null)
			{
				ProductoArmazenado pa = contentor.existeProducto(p);
				if(pa != null)
				{
					r.addElement(ar);
				}
			}
		}
		
		return r;
	}

	public Producto obterLista(String src)
	{
		for(Producto p: productos)
		{
			if(p.nome.equals(src))
			{
				return p;
			}
		}
		return null;
	}
}
