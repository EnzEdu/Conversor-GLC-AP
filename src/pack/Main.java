package pack;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("----------MAIN----------\n" +
						   "Exemplo:\n" +
						   "G = ({S},{a,b},P,S)\n" +
						   "P = {S=>aSb|ab}\n\n");
		System.out.println("Escreva a GLC nao-recursiva a esquerda: ");
		
		

		String[] infos = new String[2];
		try {
			// Cria o FileInputStream para ler o arquivo passado como parametro
			//FileInputStream fis = new FileInputStream(args[0]);
			FileInputStream fis = new FileInputStream("glc.txt");
			
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
			
			for (String infalso : infos)
			{
				System.out.println(infalso);
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
		
		AutomatoPilha ap = new AutomatoPilha(infos);
//		System.out.println(regex);
	}

}
