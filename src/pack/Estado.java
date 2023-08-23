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
		// 0 = estado alcancado pela transicao, 1 = novo conteudo da pilha, 2 = nova palavra
		String[] dados = {"", ""};
		
		for (Transicao tr : listaTransicoesEstado)
		{
			if (tr.getSimboloLidoEntrada().equals(palavra.substring(0, 1)) == true)
			{
				// Computacao
					// Desempilhamento do simbolo
						String conteudoConsumido = tr.getSimboloConsumidoPilha();
					
						// Verifica se o simbolo consumido na transicao eh vazio
						// Se for, mantem o conteudo da pilha
						if (conteudoConsumido.charAt(0) == '#')
						{
							dados[0] = tr.getEstadoAlcancado();
							dados[1] = tr.getSimboloEmpilhadoPilha();
						}
						
						// Verifica se o simbolo consumido na transicao eh um teste
						// Se a pilha estiver vazia, segue pro proximo estado
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
							// Verifica se o simbolo a ser consumido na transicao
							// e o simbolo no topo da pilha sao iguais
//							System.out.println("apap " + conteudoPilha);
							/*
							if (conteudoPilha.charAt(0) == conteudoConsumido.charAt(0))
							{
								// Caso sejam, consome o simbolo no topo da pilha
								conteudoPilha = conteudoPilha.substring(1);
							}
							else
							{
								// Tratar caso sejam diferentes
								// Excecao?
							}
							*/

							if (palavra.equals("#") == true)
							{
							for (int i = 0; i < conteudoPilha.length(); i++)
							{
								// Quando encontrar o simbolo a ser consumido dentro da pilha
								if (conteudoConsumido.charAt(0) == conteudoPilha.charAt(i))
								{
									String conteudoEmpilhar = tr.getSimboloEmpilhadoPilha();
//									
									// Se a pilha tiver apenas esse simbolo, se torna vazia
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
											// Se a nova pilha tiver mais simbolos que a palavra de entrada,
											// verifica outra transicao
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
							//
							else
							{
								if (conteudoConsumido.charAt(0) == conteudoPilha.charAt(0))
								{
									if (conteudoPilha.length() != 1)
									{
										dados[1] = conteudoPilha.substring(1);
									}
									else
									{
										dados[1] = "#";
									}
								}
							}
							//
							if (dados[1].equals("") == false)
							{
								dados[0] = tr.getEstadoAlcancado();
							}
							else
							{
								continue;
							}
						}
				
						
						/*
					// Empilhamento na pilha
					String conteudoEmpilhar = tr.getSimboloEmpilhadoPilha();
					if (conteudoPilha.equals("#") == true)
					{
						dados[1] = conteudoEmpilhar;
					}
					else
					{
						//dados[1] = conteudoEmpilhar + conteudoPilha;
						for (int i = 0; i < conteudoPilha.length(); i++)
						{
							if (conteudoPilha.charAt(i) == conteudoConsumido.charAt(0))
							{
								dados[1] = conteudoPilha.substring(0, i) +
										conteudoEmpilhar;
								
								// Evitando ArrayOutOfBounds
								if (i + 1 < conteudoPilha.length())
								{
									dados[1] += conteudoPilha.substring(i+1);
								}
								
								break;
							}
						}
						
						System.out.println("ueu:  " + conteudoPilha + "," + conteudoConsumido.charAt(0));
					}
					*/	
					break;
			}
		}
		
		if (dados[0].equals("") == true)
		{
			dados[0] = "REJ";
			dados[1] = "REJ";
		}
		
		
		return dados;
	}
}
