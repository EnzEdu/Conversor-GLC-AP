package pack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AutomatoPilha {
	private static Estado[] listaEstados;

	public AutomatoPilha(String[] infos) {
		
		// Coleta das informacoes do arquivo
		String gramatica      = infos[0].replaceAll(" ", "");
		String regrasProducao = infos[1].replaceAll(" ", "");
		
		
		/* Gramatica
		 * elementosGramatica[0] = variaveis
		 * elementosGramatica[1] = terminais
		 * elementosGramatica[2] = simbolo do conjunto de regras de producao
		 * elementosGramatica[3] = simbolo inicial
		 */
		String conteudoGramatica = gramatica.substring(
				gramatica.indexOf('(') + 1, gramatica.indexOf(')'));
		String[] elementosGramatica = conteudoGramatica.split(";");

		
		/* Regras de Producao
		*/
		String conteudoProducoes = regrasProducao.substring(
				regrasProducao.indexOf('{') + 1, regrasProducao.indexOf('}'));
		String[] listaProducoesConcatenadas = conteudoProducoes.split(",");
		
		
	
		// Elementos do automato
		
			// Terminais "{a,b,c}" = [a, b, c]
			ArrayList<Character> terminais = new ArrayList<Character>(
					elementosGramatica[1]									// "{a,b,c}"
						.substring(1, elementosGramatica[1].length()-1)		// "a,b,c"
						.replaceAll(",", "")								// "abc"
						.chars()											// "[a, b, c]
						.mapToObj(terminal -> (char) terminal)
						.collect(Collectors.toList())
			);
			
			
			// Alfabeto da pilha
			ArrayList<Character> alfabetoPilha = new ArrayList<Character>(
					(
						elementosGramatica[0]
							.substring(1, elementosGramatica[0].length()-1)
							.replaceAll(",", "")
						+
						elementosGramatica[1]
							.substring(1, elementosGramatica[1].length()-1)
							.replaceAll(",", "")
					)
						.chars()
						.mapToObj(simbolo -> (char) simbolo)
						.collect(Collectors.toList())
			);
			
		
			ArrayList<String> estados = new ArrayList<String>(
					Arrays.asList("q0", "q1", "qF")
			);

			
			ArrayList<String> listaRegrasProducao = new ArrayList<String>();
			for (String regraProducao : listaProducoesConcatenadas)
			{
				// Se a variavel (lado esquerdo) nao possuir multiplas producoes
				if (regraProducao.indexOf("|") == -1)
				{
					listaRegrasProducao.add(regraProducao);
				}
				
				else
				{
					// Identifica a variavel
					String ladoEsquerdo = regraProducao.substring(
							0, regraProducao.indexOf(">") + 1);
					
					int indexInicio = regraProducao.indexOf(">");
					
					/* Separa e salva cada producao a partir daquela variavel,
					 * iterando sobre o pipe ("|")
					 */
					String[] ladosDireitos = regraProducao.substring(indexInicio + 1).split("\\|");					
					for (String ladoDireito : ladosDireitos)
					{
						listaRegrasProducao.add(ladoEsquerdo + ladoDireito);
					}
				}
			}
			
			
			
		// Montagem do automato
		listaEstados = new Estado[3];
		for (int i = 0; i < estados.size(); i++)
		{
			// Cria o estado
			listaEstados[i] = new Estado(estados.get(i));

			// Adiciona as transicoes de acordo com o estado
			if (i == 0)
			{
				// Transicao inicial = (vazio, vazio, simbolo inicial "S")
				listaEstados[i].adicionaTransicao(
						new Transicao("#", "#", "S", estados.get(1))
				);
			}
			
			else if (i == 1)
			{
				// Primeiro tipo de transicao = (vazio, variavel, palavra gerada)
				for (String regraProducao : listaRegrasProducao)
				{
					String ladoEsquerdo = regraProducao.substring(0, regraProducao.indexOf("="));
					String ladoDireito = regraProducao.substring(regraProducao.indexOf(">") + 1);
					listaEstados[i].adicionaTransicao(
							new Transicao("#", ladoEsquerdo, ladoDireito, estados.get(1))
					);
				}
				
				// Segundo tipo de transicao = (terminal, terminal, vazio)
				for (Character terminal : terminais)
				{
					listaEstados[i].adicionaTransicao(
							new Transicao(terminal.toString(), terminal.toString(), "#", estados.get(1))
					);
				}
				
				/* Transicao final = 
				 * (teste palavra vazia, teste pilha vazia, vazio)
				 */
				listaEstados[i].adicionaTransicao(
						new Transicao("?", "?", "#", estados.get(2))
				);
			}
		}
		
		
		// Imprime os estados e suas transicoes
		/*
		System.out.println("\n\nAUTOMATO COM PILHA:");
		for (int i = 0; i < estados.size(); i++)
		{
			ArrayList<Transicao> arr = listaEstados[i].getListaTransicoes();
			System.out.println("Estado " + estados.get(i));
			for (Transicao tr : arr) {
				System.out.println(
						"(" + tr.getSimboloLidoEntrada() +
						", " + tr.getSimboloConsumidoPilha() +
						", " + tr.getSimboloEmpilhadoPilha() +
						") => " + tr.getEstadoAlcancado());
			}
			System.out.println();
		}
		*/
		
		
		
		// Entrada hard-coded
		//String entrada = "aaaaabbbbb";
		//System.out.println(reconhecer(entrada));
	}
	
	
	
	
	
	public void reconhecer(String palavra) {
		String estadoAtual = "q0";
		String pilha = "#";
		String result[];
		
		
		System.out.println("\nRECONHECIMENTO:");
		System.out.println("(" + estadoAtual + ", " + palavra + ", " + pilha + ")");

		
		
		// Transicao vazia de q0		
		result = computacao(estadoAtual, "#", pilha, palavra);
		estadoAtual = result[0];
		pilha = result[1];
		
		System.out.println("(" + estadoAtual + ", " + palavra + ", " + pilha + ")");

		
		
		int numTerminais = 0;
		for (char simbolo : pilha.toCharArray())
		{
			if ((int) simbolo > 96 && (int) simbolo < 123)
			{
				numTerminais++;
			}
		}

		/* Transicoes sucessivas em q1
		 * consumindo o simbolo vazio
		 * 
		 * Transicoes sucessivas lendo o simbolo vazio ate que
		 * o numero de terminais da pilha esteja no alcance do
		 * numero de terminais da palavra, e
		 * 		- a pilha seja igual a palavra
		 * 		ou
		 * 		- a palavra seja rejeitada
		 */
		while (numTerminais <= palavra.length() && (pilha.equals(palavra) == false && estadoAtual.equals("REJ") == false))
		{
			result = computacao(estadoAtual, "#", pilha, palavra);
			estadoAtual = result[0];
			pilha = result[1];

			System.out.println("(" + estadoAtual + ", " + palavra + ", " + pilha + ")");
			
			numTerminais = 0;
			for (char simbolo : pilha.toCharArray())
			{
				if ((int) simbolo > 96 && (int) simbolo < 123)
				{
					numTerminais++;
				}
			}
		}

		
		
		/* Transicoes sucessivas em q1 
		 * consumindo terminais da entrada
		 * 
		 * Transicoes sao realizadas ate que
		 * 		- Palavra seja vazia
		 * 		e
		 * 		- Palavra nao seja rejeitada
		 */
		while (palavra.equals("#") == false && estadoAtual.equals("REJ") == false)
		{
			result = computacao(estadoAtual, palavra, pilha, palavra);
			estadoAtual = result[0];
			
			/* Se o tamanho da pilha nova for o tamanho da pilha antiga + 1,
			 * e se a palavra possuir apenas um simbolo,
			 * esvazia a palavra
			 */
			if (result[1].length() + 1 == pilha.length() &&
				palavra.length() == 1)
			{
				palavra = "#";
			}
			
			
			/* Se o tamanho da pilha antiga for o tamanho da pilha nova + 1,
			 * e se a nova pilha for igual a pilha antiga sem o primeiro caractere,
			 * presume-se um consumo de caractere da palavra de entrada
			 * Entao, a palavra eh atualizada
			 */
			else if (result[1].length() + 1 == pilha.length() &&
				result[1].equals(pilha.substring(1)) == true)
			{
				palavra = palavra.substring(1);
			}
			
			/* Se o tamanho da pilha antiga for o mesmo da pilha nova,
			 * e a pilha nova estiver vazia,
			 * presume-se que a palavra tambem ficou vazia
			 * Entao, a palavra eh atualizada
			 */
			else if (pilha.length() == result[1].length() &&
					result[1].equals("#") == true)
			{
				palavra = "#";
			}
			
			pilha = result[1];
		
			System.out.println("(" + estadoAtual + ", " + palavra + ", " + pilha + ")");
		}
		
		
		
		// Caso a palavra nao tenha sido rejeitada no caminho
		if (estadoAtual.equals("REJ") == false)
		{
			// Testa se a pilha esta vazia
			result = computacao(estadoAtual, "?", pilha, palavra);
			estadoAtual = result[0];
			pilha = result[1];
		
			System.out.println("(" + estadoAtual + ", " + palavra + ", " + pilha + ")");
			
			// Se a palavra estiver vazia e o estado atual for o final
			if (palavra.equals("#") == true && estadoAtual.equals("qF") == true)
			{
				System.out.println("ACEITA!");
			}
			else
			{
				System.out.println("REJEITADA!");
			}
		}
		else
		{
			System.out.println("REJEITADA!");
		}
	}
	
	
	
	public static String[] computacao(
			String estadoAtual, String palavra, String pilha, String entrada) {

		String dadosResultantes[] = {"", ""};
		
		for (int i = 0; i < 3; i++)
		{
			if (listaEstados[i].getNomeEstado().equals(estadoAtual) == true)
			{
				dadosResultantes = listaEstados[i].computar(estadoAtual, palavra, pilha, entrada);
				break;
			}
		}
		
		return dadosResultantes;
	}
}
