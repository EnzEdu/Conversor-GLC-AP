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
	
	public String[] computar(String estadoAtual, String palavra, String conteudoPilha, String entrada) {

		String[][] matrizResultados = new String[listaTransicoesEstado.size()][2];
		
		// 0 = estado alcancado pela transicao, 1 = novo conteudo da pilha
		String[] dados = {"", ""};
		
		for (int k = 0; k < listaTransicoesEstado.size(); k++)
		{
			Transicao tr = listaTransicoesEstado.get(k);
			
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
									
									// Caso o simbolo a ser empilhado seja vazio
									if (conteudoEmpilhar.equals("#") == true)
									{
										/* Se a pilha tiver apenas esse simbolo,
										 * ela se torna vazia
										 */
										if (conteudoPilha.length() == 1)
										{
											dados[1] = "#";
										}
										
										else
										{
											// Apaga a variavel da pilha
											dados[1] = conteudoPilha.substring(0, i);
											
											// Evitando ArrayOutOfBounds
											if (i+1 < conteudoPilha.length())
											{
												dados[1] += conteudoPilha.substring(i+1);
											}
										}
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
											int numTerminais = 0;
											for (char simbolo : conteudoPilha.toCharArray())
											{
												if ((int) simbolo > 96 && (int) simbolo < 123)
												{
													numTerminais++;
												}
											}
											
											if (numTerminais > entrada.length())
											{
												dados[0] = "";
												dados[1] = "";
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
					}
				
//					break;
				// Fim da computacao
			}

			/* Se nenhuma transicao foi utilizada,
			 * informa a rejeicao da palavra
			 */
			if (dados[0].equals("") == true)
			{
				dados[0] = "REJ";
				dados[1] = "REJ";
			}
			
			/* Salva na matriz de resultados 
			 * os resultados de cada transicao
			 */
			matrizResultados[k][0] = dados[0];
			matrizResultados[k][1] = dados[1];
			
			dados[0] = "";
			dados[1] = "";
		}
		
		

		
		
		/* Contabiliza o numero de transicoes que levam a
		 * rejeicao da palavra
		 */
		int contadorCaminhosRejeicao = 0;
		int indexTransicaoUnitaria = 0;
		for (int i = 0; i < listaTransicoesEstado.size(); i++)
		{
			if (matrizResultados[i][0].equals("REJ") == true)
			{
				contadorCaminhosRejeicao++;
			}
			else
			{
				indexTransicaoUnitaria = i;
			}
		}
		
		/* Caso o numero de transicoes que levam a rejeicao seja exato
		 * o numero total de transicoes - 1
		 * 
		 * Se houver apenas um caminho possivel que nao seja de rejeicao,
		 * se trata de uma transicao de leitura e consumo 
		 * do mesmo simbolo, pelas regras de construcao do automato
		 * 
		 * Logo, escolhe-se esse caminho possivel dentre os demais
		 */
		if (contadorCaminhosRejeicao == listaTransicoesEstado.size() - 1)
		{
			return matrizResultados[indexTransicaoUnitaria];
		}
		
		
		// Caso haja mais de um caminho possivel
		else
		{
			int indexTransicaoEscolhida = -1;
			for (int i = 0; i < listaTransicoesEstado.size(); i++)
			{
				String possivelNovaPilha = matrizResultados[i][1];

				/* Procura uma transicao que resulte em uma
				 * nova pilha igual a palavra de entrada,
				 * e a escolhe dentre as demais
				 */
				if (possivelNovaPilha.equals(entrada) == true)
				{
					indexTransicaoEscolhida = i;
					return matrizResultados[indexTransicaoEscolhida];
				}
			}
			
			
			/* Se nao houver uma transicao
			 * que gere uma pilha igual a palavra analisada
			 */
			if (indexTransicaoEscolhida == -1)
			{
				/* Salva o index das novas pilhas cujo topo
				 * eh o mesmo simbolo inicial da entrada
				 */
				ArrayList<Integer> indexPilhasComMesmoSimboloInicial = new ArrayList<Integer>();
				for (int k = 0; k < listaTransicoesEstado.size(); k++)
				{
					if (matrizResultados[k][1].charAt(0) == entrada.charAt(0))
					{
						indexPilhasComMesmoSimboloInicial.add(k);
					}
				}
				
				
				// Procura dentro dessas novas pilhas salvas
				ArrayList<Integer> indexRemovidos = new ArrayList<Integer>();
				for (int index : indexPilhasComMesmoSimboloInicial)
				{
					char simboloPisoPilha = matrizResultados[index][1].charAt(matrizResultados[index][1].length()-1);

					/* Se o ultimo simbolo da pilha for igual do 
					 * ultimo simbolo da entrada
					 */
//					if (simboloPisoPilha == entrada.charAt(entrada.length()-1) && matrizResultados[index][1].length() >= entrada.length())
					if (simboloPisoPilha == entrada.charAt(entrada.length()-1))
					{
						/* Retorna a primeira transicao que 
						 * resulta em uma pilha cujo primeiro e
						 * ultimo simbolo sao os mesmos da palavra
						 */
						
						int indexRecursaoDireita = -1;
						for (char simbolo : matrizResultados[index][1].toCharArray())
						{
							if ((int) simbolo > 96 && (int) simbolo < 123)
							{
								indexRecursaoDireita++;
							}
						}
						
						if (indexRecursaoDireita != matrizResultados[index][1].length()-1)
						{
							boolean verifRecursaoDireita = true;
							String substrNovaPilha = matrizResultados[index][1].substring(indexRecursaoDireita);
							for (char simbolo : substrNovaPilha.toCharArray())
							{
								if ((int) simbolo > 96 && (int) simbolo < 123)
								{
									verifRecursaoDireita = false;
									break;
								}
							}
							
							if (verifRecursaoDireita == true)
							{
								return matrizResultados[index];
							}
						}					
					}
					
					
					// Caso contrario
					else
					{
						/* Se o ultimo simbolo da pilha for uma variavel, 
						 * deixa salva
						 */
						if ((int) simboloPisoPilha > 64 && (int) simboloPisoPilha < 91)
						{
							continue;
						}
						
						/* Se o ultimo simbolo for um outro terminal,
						 * salva o index da transicao para remocao
						 */
						else
						{
							indexRemovidos.add(index);
						}
					}
				}
				
				// Remove os index das transicoes nao-interessantes				
				for (int indexRemocao : indexRemovidos)
				{
					boolean f = indexPilhasComMesmoSimboloInicial.remove(Integer.valueOf(indexRemocao));
				}
			
				
				
				/* Se nao houver, procura pela transicao que resulte
				 * em uma nova pilha com o numero de terminais o
				 * mais proximo possivel do numero de terminais
				 * da palavra de entrada,
				 * e a escolhe dentre as demais
				 */
				int numMenorTerminais = 0;
				
				/* Caso exista transicoes com o topo da pilha igual ao
				 * simbolo inicial da entrada, procura entre essas
				 * transicoes
				 */
				if (indexPilhasComMesmoSimboloInicial.size() != 0)
				{
					/*
					for (int k : indexPilhasComMesmoSimboloInicial)
					{
						int cont = 0;
						for (char simbolo : matrizResultados[k][1].toCharArray())
						{
							if ((int) simbolo > 96 && (int) simbolo < 123)
							{
								cont++;
							}
						}
						
						if (cont < numMenorTerminais && cont >= entrada.length())
						{
							numMenorTerminais = cont;
							indexTransicaoEscolhida = k;
						}
						System.out.println("k = " + k);
					}
					System.out.println("INDEX: " + indexTransicaoEscolhida);
					*/
					
					// Placeholder: ele pega a primeira transicao que
					// gera uma pilha com o topo igual ao simbolo
					// inicial da entrada
					
					int indexMaisParecido = -1;
					int contMaior = 0;
					for (int ind : indexPilhasComMesmoSimboloInicial)
					{
						int cont = 0;
						for (int k = 0; k < matrizResultados[ind][1].length(); k++)
						{
							if (k >= entrada.length() || k >= matrizResultados[ind][1].length())
							{
								break;
							}
							
							if (matrizResultados[ind][1].charAt(k) == entrada.charAt(k))
							{
								cont++;
							}
						}
						
						if (indexMaisParecido == -1 || cont >= contMaior)
						{
							indexMaisParecido = ind;
							contMaior = cont;
						}
					}
					
					indexTransicaoEscolhida = indexMaisParecido;
				}
				
				// Se nao, procura entre todas
				else
				{
					/* Se nao houver, procura pela transicao que resulte
					 * em uma nova pilha com o numero de terminais o
					 * mais proximo possivel do numero de terminais
					 * da palavra de entrada,
					 * e a escolhe dentre as demais
					 */
					for (int i = 0; i < listaTransicoesEstado.size(); i++)
					{
						String possivelNovaPilha = matrizResultados[i][1];
						int cont = 0;
						for (char simbolo : possivelNovaPilha.toCharArray())
						{
							if ((int) simbolo > 96 && (int) simbolo < 123)
							{
								cont++;
							}
						}
						
						if (cont < numMenorTerminais && cont >= entrada.length())
						{
							numMenorTerminais = cont;
							indexTransicaoEscolhida = i;
						}
					}
					
					if (indexTransicaoEscolhida == -1)
					{
						indexTransicaoEscolhida = 0;
					}
				}
				
				return matrizResultados[indexTransicaoEscolhida];
			}
			
			else
			{	
				return matrizResultados[indexTransicaoEscolhida];
			}
		}
		

		
		
		//return dados;
	}
}
