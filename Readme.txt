Caracteres:
	Carro(C)
	Drone(H)
	Mota(M)
	Camiao(T)
	
	Obstaculo(O)
	Estrada( )
	Loja(S)
	Armazem(W)
	Recarga(R)
	Lixeira(G)
	
	Agencia de Oferta de Trabalho(A)
	
Aspectos:
- Producto e algo que se comercializa
(ex: lapis)
- Ranhura e um conjunto de Productos
(ex: Uma pessoa tem 5 lapis)
- Trabalhador tem um conjunto de Ranhuras
(ex: Uma pessoa tem 5 lapis e 2 borrachas)

- Ponto tem coordendas linha e coluna
(ex: estou na linha 3, na coluna 6)
- Identidade e um Ponto com um caracter que representa um local/propriedade ou um trabalhador
(ex: estou na linha 3, na coluna 6 cuja identidade S diz que se trata de uma loja)
- Local e uma Identidade que tem um nome para diferenciar dos outros eficicios
(ex: Loja do Manel, linha 3 e coluna 6 e identidade S: Loja)
- Loja, Armazem, Lixeira, Recarga e Agencia sao Locais

- ProductoLoja refere a um producto que uma Loja tem a venda a um preco
(ex: Loja do Manel tem Lapis a 0.20€)
- ProductoArmazenado refere a um producto que um Trabalhador tem guardado
(ex: o Mafarrico e um Trabalhador que tem um lapis guardado no Armazem das Quinas)

- Trabalho e algo que um Trabalhador aceita de uma Agencia para executar e ganhar dinheiro
- Agencia tem conjunto de trabalhos a dar
- Tarefa refere a um Trabalho que um Trabalhador aceitou de uma Agencia

- Produzir e uma accao que um Trabalhor toma para criar novos productos a custa de outros e ferramentas que possua

- Mapa e uma matriz de estradas e obstaculos que tb tem guardado varios locais

Outros:
- Auxiliar e uma classe que tem guardado multiplas funcoes
- Ferramenta uma classe temporaria; Trabalhor tem conjunto de ferramentas para produzir productos
