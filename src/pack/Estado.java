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
	
	public String[] computar(String estadoAtual, String simbolo, String conteudoPilha) {
		// 1 = estado alcancado pela transicao, 2 = novo conteudo da pilha
		String[] dados = {"", ""};
		
		for (Transicao tr : listaTransicoesEstado)
		{
			if (tr.getSimboloLidoEntrada().equals(simbolo) == true)
			{
				// Computacao
					// Desempilhamento do simbolo
						String conteudoConsumido = tr.getSimboloConsumidoPilha();
					
						// Verifica se o simbolo consumido na transicao eh vazio
						// Se for, mantem o conteudo da pilha
						if (conteudoConsumido.length() == 0)
						{
							dados[0] = tr.getEstadoAlcancado();
							dados[1] = conteudoPilha;
						}
						
						// Verifica se o simbolo consumido na transicao eh um teste
						// Se a pilha estiver vazia, segue pro proximo estado
						else if (conteudoConsumido.charAt(0) == '?')
						{
							if (conteudoPilha.length() == 0)
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
//----------------------------PLACEHOLDER----------------------------//
							/*
							 * Partindo do pressuposto que *nao* existe transicao
							 * com multiplos simbolos sendo consumidos
							 * ex: (a, XYZ, A)
							 * 
							 * Mudar a logica de consumo caso seja possivel
							 */
//----------------------------PLACEHOLDER----------------------------//
						
							// Verifica se o simbolo a ser consumido na transicao
							// e o simbolo no topo da pilha sao iguais
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
							
							dados[0] = tr.getEstadoAlcancado();
							dados[1] = conteudoPilha;
						}
				
						
						
					// Empilhamento na pilha
					String conteudoEmpilhar = tr.getSimboloEmpilhadoPilha();
					conteudoPilha = conteudoEmpilhar + conteudoPilha;
					dados[1] = conteudoPilha;
						
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
