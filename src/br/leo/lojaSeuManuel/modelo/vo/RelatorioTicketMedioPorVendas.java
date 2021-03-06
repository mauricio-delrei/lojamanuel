/**
 * 
 */
package br.leo.lojaSeuManuel.modelo.vo;

import br.leo.lojaSeuManuel.util.Formatador;

/**
 * @author leonardo
 *
 */
public class RelatorioTicketMedioPorVendas extends Relatorio {
	
	private int quantidadeVendas;
	
	private double ticketMedio;
	
	
	
	
	public RelatorioTicketMedioPorVendas() {
	}

	
	
	
	/**
	 * @param periodo
	 * @param valorTotalVendas
	 * @param quantidadeVendas
	 */
	public RelatorioTicketMedioPorVendas(Periodo periodo, double valorTotalVendas, int quantidadeVendas) {
		super(periodo, valorTotalVendas);
		this.quantidadeVendas = quantidadeVendas;
		calcularTicketMedio();
	}
	
	
	
	
	
	
	
	public void calcularTicketMedio() {
		
		this.ticketMedio = this.getValorTotalVendas() / this.quantidadeVendas;
		
		this.ticketMedio = Formatador.formatarDoubeParaDoisDecimais(this.ticketMedio);
		
	}

	public double getTicketMedio() {
		return ticketMedio;
	}

	public void setTicketMedio(double ticketMedio) {
		this.ticketMedio = ticketMedio;
	}

	public int getQuantidadeVendas() {
		return quantidadeVendas;
	}

	public void setQuantidadeVendas(int quantidadeVendas) {
		this.quantidadeVendas = quantidadeVendas;
	}

	

}
