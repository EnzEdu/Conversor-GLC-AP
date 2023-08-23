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
		/*
		for (String f : elementosGramatica) {
			System.out.println(f);
		}
		*/
		
		/* Regras de Producao
		*/
		String conteudoProducoes = regrasProducao.substring(
				regrasProducao.indexOf('{') + 1, regrasProducao.indexOf('}'));
		String[] listaProducoesConcatenadas = conteudoProducoes.split(",");
		
		
	
		// Montagem do automato
		
			// Terminais "{a,b,c}" = [a, b, c]
			ArrayList<Character> terminais = new ArrayList<Character>(
					elementosGramatica[1]									// "{a,b,c}"
						.substring(1, elementosGramatica[1].length()-1)		// "a,b,c"
						.replaceAll(",", "")								// "abc"
						.chars()											// "[a, b, c]
						.mapToObj(terminal -> (char) terminal)
						.collect(Collectors.toList())
			);
			
			/*
			System.out.println("Deu certo - Terminais?");
			for (Character terminal : terminais)
			{
				System.out.println(terminal);
			}
			*/
			
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
			
			/*
			System.out.println("Deu certo? - AlfabetoPilha");
			for (Character simbolo : alfabetoPilha)
			{
				System.out.println(simbolo);
			}
			*/
			
		
			ArrayList<String> estados = new ArrayList<String>(
					Arrays.asList("q0", "q1", "qF")
			);
			
			/*
			System.out.println("Deu certo? - Estados");
			for (String estado : estados)
			{
				System.out.println(estado);
			}
			*/
			
			ArrayList<String> listaRegrasProducao = new ArrayList<String>();
			for (String regraProducao : listaProducoesConcatenadas)
			{
				if (regraProducao.indexOf("|") == -1)
				{
					listaRegrasProducao.add(regraProducao);
				}
				else
				{
					String ladoEsquerdo = regraProducao.substring(
							0, regraProducao.indexOf(">") + 1);
					
					int indexInicio = regraProducao.indexOf(">");					
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
				listaEstados[i].adicionaTransicao(
						new Transicao("#", "#", "S", estados.get(1))
				);
			}
			else if (i == 1)
			{
				// Primeiro tipo
				for (String regraProducao : listaRegrasProducao)
				{
					String ladoEsquerdo = regraProducao.substring(0, regraProducao.indexOf("="));
					String ladoDireito = regraProducao.substring(regraProducao.indexOf(">") + 1);
					listaEstados[i].adicionaTransicao(
							new Transicao("#", ladoEsquerdo, ladoDireito, estados.get(1))
					);
				}
				
				// Segundo tipo
				for (Character terminal : terminais)
				{
					listaEstados[i].adicionaTransicao(
							new Transicao(terminal.toString(), terminal.toString(), "#", estados.get(1))
					);
				}
				
				// Indo pro estado final
				listaEstados[i].adicionaTransicao(
						new Transicao("?", "?", "#", estados.get(2))
				);
			}
		}
		
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
		
		
		
		
		String entrada = "aaaaabbbbb";
		System.out.println(reconhecer(entrada));
		
		
		
	}
	
	public static String reconhecer(String palavra) {
		String estadoAtual = "q0";
		String pilha = "#";
		String resultado = "placeholder";
		String result[];
		
		
		System.out.println("\nRECONHECIMENTO:");
		System.out.println("(" + estadoAtual + ", " + palavra + ", " + pilha + ")");

		
		// Transicao vazia de q0		
		result = computacao(estadoAtual, "#", pilha, palavra.length());
		estadoAtual = result[0];
		pilha = result[1];
		
		System.out.println("(" + estadoAtual + ", " + palavra + ", " + pilha + ")");

		
		// Transicoes sucessivas ate gerar a pilha ficar igual a palavra
		while (pilha.length() < palavra.length())
		{
			result = computacao(estadoAtual, "#", pilha, palavra.length());
			estadoAtual = result[0];
			pilha = result[1];
		
			System.out.println("(" + estadoAtual + ", " + palavra + ", " + pilha + ")");
		}

		
		// 
		while (palavra.equals("#") != true && estadoAtual.equals("REJ") != true)
		{
			result = computacao(estadoAtual, palavra, pilha, palavra.length());
			estadoAtual = result[0];
			
			// Se o tamanho da pilha antiga for o tamanho da pilha nova + 1,
			// e se a nova pilha for igual a pilha antiga sem o primeiro caractere,
			// presume-se um consumo de caractere da palavra de entrada
			// Entao, a palavra eh atualizada
			if (result[1].length() + 1 == pilha.length() &&
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
		
		
		//
		if (estadoAtual.equals("REJ") == false)
		{
			result = computacao(estadoAtual, "?", pilha, palavra.length());
			estadoAtual = result[0];
			pilha = result[1];
		
			System.out.println("(" + estadoAtual + ", " + palavra + ", " + pilha + ")");
			
			if (palavra.equals("#") == true && estadoAtual.equals("qF") == true) {
				return "ACEITA!";
			}
			else
			{
				return "REJEITADA!";
			}
		}
		else
		{
			return "REJEITADA!";
		}
		
		/*
		if (palavra.equals("#") == true && estadoAtual.equals("qF") == true)
		{
			return "ACEITA!";
		}
//		else if (estadoAtual.equals("REJ") == true)
		else
		{
			return "REJEITADA!";
		}
		*/
		/*
		for (char simbolo : palavra.toCharArray())
		{
			for (int i = 0; i < 3; i++)
			{
				if (listaEstados[i].getNomeEstado().equals(estadoAtual) == true)
				{
					System.out.println("(" + estadoAtual + ", " + palavra + ", " + pilha + ")");
					//System.out.println("Pilha = " + pilha.charAt(0));
					String dadosResultantes[] = listaEstados[i].computar(estadoAtual, Character.toString(simbolo), pilha);
					
					if (dadosResultantes[0].equals("REJ") == true) {
						return "REJEITADA";
					}
					else if (dadosResultantes[0].equals("qF") == true)
					{
						return "ACEITA";
					}
					
					estadoAtual = dadosResultantes[0];
					pilha = dadosResultantes[1];
					break;
				}
			}
		}
		*/
		//return resultado;
	}
	
	public static String[] computacao(
			String estadoAtual, String palavra, String pilha, int tamEntrada) {

		String dadosResultantes[] = {"", ""};
		
		for (int i = 0; i < 3; i++)
		{
			if (listaEstados[i].getNomeEstado().equals(estadoAtual) == true)
			{
				dadosResultantes = listaEstados[i].computar(estadoAtual, palavra, pilha, tamEntrada);
				break;
			}
		}
		
		return dadosResultantes;
	}
}
