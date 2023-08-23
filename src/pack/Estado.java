package pack;

import java.util.ArrayList;

public class Estado {
	private String nomeEstado;
	private ArrayList<Transicao> listaTransicoesEstado;
	
	public Estado(String nome) {
		this.nomeEstado = nome;
		this.listaTransicoesEstado = new ArrayList<Transicao>();
	}
	
	public void adicionaTransicao(Transicao tr) {
		this.listaTransicoesEstado.add(tr);
	}
	
	public String getNomeEstado() {
		return this.nomeEstado;
	}
	
	public ArrayList<Transicao> getListaTransicoes() {
		return this.listaTransicoesEstado;
	}
	
	public String[] computar(String estadoAtual, String palavra, String conteudoPilha, int tamEntrada) {

		// 0 = estado alcancado pela transicao, 1 = novo conteudo da pilha
		String[] dados = {"", ""};
		
		for (Transicao tr : listaTransicoesEstado)
		{
			if (tr.getSimboloLidoEntrada().equals(palavra.substring(0, 1)) == true)
			{
				// Computacao
					String conteudoConsumido = tr.getSimboloConsumidoPilha();

					
					/* Verifica se o simbolo consumido na transicao eh vazio
					 * Se for, a pilha se torna o simbolo empilhado
					 * (em teoria, "S")
					 */
					if (conteudoConsumido.charAt(0) == '#')
					{
						dados[0] = tr.getEstadoAlcancado();
						dados[1] = tr.getSimboloEmpilhadoPilha();
					}
					
					
					/* Verifica se o simbolo consumido na transicao eh um teste
					 * Se a pilha estiver vazia, segue pro proximo estado
					 * (em teoria, "qF")
					 */
					else if (conteudoConsumido.charAt(0) == '?')
					{
						if (conteudoPilha.equals("#"))
						{
							dados[0] = tr.getEstadoAlcancado();
						}
						else
						{
							dados[0] = estadoAtual;
						}
							
						dados[1] = conteudoPilha;
					}
					
					
					else
					{
						/* Se o simbolo lido na transicao for vazio,
						 * faz a leitura da pilha de forma continua,
						 * procurando a variavel mesmo abaixo do topo da pilha
						 */
						if (palavra.equals("#") == true)
						{
							for (int i = 0; i < conteudoPilha.length(); i++)
							{
								// Quando encontrar o simbolo a ser consumido pela transicao
								if (conteudoConsumido.charAt(0) == conteudoPilha.charAt(i))
								{
									String conteudoEmpilhar = tr.getSimboloEmpilhadoPilha();
									
									// Se a pilha tiver apenas esse simbolo, ela se torna vazia
									if (conteudoPilha.length() == 1 && conteudoEmpilhar.equals("#") == true)
									{
										dados[1] = "#";
									}
									
									else
									{
										// Remove o simbolo a ser consumido e
										// insere os simbolos a serem empilhados no lugar
										dados[1] = conteudoPilha.substring(0, i);
									
										// Nao empilha o simbolo vazio
										if (conteudoEmpilhar.equals("#") == false)
										{
											dados[1] += conteudoEmpilhar;
										}
										
										// Evita ArrayOutOfBounds
										if (i+1 < conteudoPilha.length())
										{
											dados[1] += conteudoPilha.substring(i+1); 
										}
										
										// Apenas se o simbolo empilhado nao for vazio
										if (conteudoEmpilhar.equals("#") == false)
										{
											/* Se o tamanho da nova pilha for maior que a palavra de entrada,
											 * verifica outra transicao daquele estado
											 */
											if (dados[1].length() > tamEntrada)
											{
												dados[1] = "";
												continue;
											}
										}
									}
									
									break;
								}
							}
						}
						
						/* Se o simbolo lido na transicao nao for vazio,
						 * faz a leitura apenas do topo da pilha
						 */
						else
						{
							// Se o simbolo consumido estiver no topo da pilha
							if (conteudoConsumido.charAt(0) == conteudoPilha.charAt(0))
							{
								/* Consome o topo da pilha, caso a pilha ainda
								 * tenha mais simbolos alem do que esta no topo
								 */
								if (conteudoPilha.length() != 1)
								{
									dados[1] = conteudoPilha.substring(1);
								}
								
								// Se nao tiver mais simbolos, a pilha fica vazia
								else
								{
									dados[1] = "#";
								}
							}
						}
						
						/* Se alguma logica foi utilizada e a pilha mudou,
						 * salva o novo estado e encerra a procura
						 * de transicoes
						 */
						if (dados[1].equals("") == false)
						{
							dados[0] = tr.getEstadoAlcancado();
						}
						
						// Se nao, procura mais transicoes pra analisar
						else
						{
							continue;
						}
					}
				
					break;
				// Fim da computacao
			}
		}
		
		
		/* Se nenhuma transicao foi utilizada,
		 * informa a rejeicao da palavra
		 */
		if (dados[0].equals("") == true)
		{
			dados[0] = "REJ";
			dados[1] = "REJ";
		}
		
		
		return dados;
	}
}
