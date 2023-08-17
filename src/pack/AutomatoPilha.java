package pack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AutomatoPilha {

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
		String conteudoRegrasProducao = regrasProducao.substring(
				regrasProducao.indexOf('{') + 1, regrasProducao.indexOf('}'));
		
		
		
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
			
			ArrayList<String> listaRegrasProducao = new ArrayList<String>(
					//
			);
		//
	}
	
	public static void reconhecer(String palavra) {
		//
	}
	
	public static void computacao(
			String simboloFita, String simboloDesempilhar, String simboloEmpilhar) {
		//
	}
}
