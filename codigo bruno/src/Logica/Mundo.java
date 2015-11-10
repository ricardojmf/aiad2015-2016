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
	
	public void verLojas()
	{
		Auxiliar.writeln("Lojas");
		Auxiliar.writeln("--------------------------------------");
		for(Loja lj: lojas)
		{
			Auxiliar.writeln(lj.nome + " em (" + lj.linha + " , " + lj.coluna + ")");
			for(ProductoLoja pj: lj.productos)
			{
				Auxiliar.writeln("+ " + pj.p.nome + " " + pj.preco + "€");
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
	
	/*
	 * DEFINICAO DE FUNCOES GENERALIZADAS
	 */
	
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
}
