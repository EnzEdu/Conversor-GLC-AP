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
		for (String f : elementosGramatica) {
			System.out.println(f);
		}
		
		/* Regras de
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
			
			//
			System.out.println("Deu certo? - AlfabetoPilha");
			for (Character simbolo : alfabetoPilha)
			{
				System.out.println(simbolo);
			}
			//
			
		
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
						new Transicao("", "", "S", estados.get(1))
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
							new Transicao("", ladoEsquerdo, ladoDireito, estados.get(1))
					);
				}
				
				// Segundo tipo
				for (Character terminal : terminais)
				{
					listaEstados[i].adicionaTransicao(
							new Transicao(terminal.toString(), terminal.toString(), "", estados.get(1))
					);
				}
				
				// Indo pro estado final
				listaEstados[i].adicionaTransicao(
						new Transicao("?", "?", "", estados.get(2))
				);
			}
		}
		
		System.out.println("\n");
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
		
		
		
		
		String entrada = "abab";
		System.out.println(reconhecer("abab"));
		
		
		
	}
	
	public static String reconhecer(String palavra) {
	}
	
	public static void computacao(
			String simboloFita, String simboloDesempilhar, String simboloEmpilhar) {
		//
	}
}
