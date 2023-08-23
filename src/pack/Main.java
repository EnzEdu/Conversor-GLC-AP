package pack;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
	private static String nomeArquivo = "glc3.txt";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("----------MAIN----------\n" +
						   "Exemplo:\n" +
						   "G = ({S},{a,b},P,S)\n" +
						   "P = {S=>aSb|ab}\n\n");
		System.out.println("Abrindo o arquivo com a GLC nao-recursiva a esquerda...");
		
		

		String[] infos = new String[2];
		try {
			// Cria o FileInputStream para ler o arquivo passado como parametro
			//FileInputStream fis = new FileInputStream(args[0]);
			FileInputStream fis = new FileInputStream(nomeArquivo);
			
			// Cria o BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
			// Faz a leitura linha por linha
			String linha;
			int numLinha = -1;
			while ((linha = br.readLine()) != null)
			{
				numLinha++;
				
				// Salva apenas as duas primeiras linhas do arquivo
				if (numLinha < 2)
				{
					infos[numLinha] = linha;
				}
			}
			
			// Fecha a stream de leitura
			fis.close();
			
			
			for (String info : infos)
			{
				System.out.println(info);
			}
			
			
			/* Cria o automato com as informacoes do arquivo
			 * (gramatica, regras de producao)
			 */
			System.out.println("Criando automato com pilha...");
			AutomatoPilha ap = new AutomatoPilha(infos);
			System.out.println("Criado automato com pilha!\n");
			
			
			// Recebe a palavra de entrada pro automato
			Scanner leitor = new Scanner(System.in);
			System.out.print("Informe a palavra para reconhecimento: ");
			String entrada = leitor.nextLine();
			
			while (estaCorreta(entrada) == false)
			{
				System.out.println("\nPalavra com formato invalido.");
				System.out.print("Informe nova palavra para reconhecimento: ");
				entrada = leitor.nextLine();
			}

			ap.reconhecer(entrada);
			
			leitor.close();
			
		} catch (FileNotFoundException e) {
//			System.out.println("Arquivo \""  + args[0] + "\"  nao encontrado!");
			System.out.println("Arquivo \""  + nomeArquivo + "\"  nao encontrado!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	
	
	public static boolean estaCorreta(String palavra)
	{
		if (palavra.equals("") == true)
		{
			return false;
		}
		
		if (palavra.indexOf("?") != -1)
		{
			return false;
		}
		
		return true;
	}
}
