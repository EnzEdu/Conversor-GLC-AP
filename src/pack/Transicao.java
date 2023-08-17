package pack;

public class Transicao {
	private String simboloLidoEntrada;
	private String simboloConsumidoPilha;
	private String simboloEmpilhadoPilha;
	private String estadoAlcancado;
	
	public Transicao(String simbolo1, String simbolo2, String simbolo3, String estado) {
		this.simboloLidoEntrada = simbolo1;
		this.simboloConsumidoPilha = simbolo2;
		this.simboloEmpilhadoPilha = simbolo3;
		this.estadoAlcancado = estado;
	}
	
	public String getSimboloLidoEntrada() { return simboloLidoEntrada; }
	public String getSimboloConsumidoPilha() { return simboloConsumidoPilha; }
	public String getSimboloEmpilhadoPilha() { return simboloEmpilhadoPilha; }
	public String getEstadoAlcancado() { return estadoAlcancado; }
}
