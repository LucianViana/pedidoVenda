package pl.piomin.services.aws.order.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RetornoBling {
		private String numero;
		private List volumes;
		private String idPedido;
		private Codigos_rastreamento codigos_rastreamento;
		
	    public RetornoBling() {
	        super();
	    }
	 
	    public RetornoBling(String numero, List<Volumes> volumes, String idPedido, Codigos_rastreamento codigos_rastreamento) {
	        this.numero = numero;
	        this.volumes = volumes;
	        this.idPedido = idPedido;
	        this.codigos_rastreamento = codigos_rastreamento;	        
	    }		

		public String getNumero() {
			return numero;
		}

		public void setNumero(String numero) {
			this.numero = numero;
		}

		public List<Volumes> getVolumes() {
			return volumes;
		}

		public void setVolumes(List<Volumes> volumes) {
			this.volumes = volumes;
		}

				public String getIdPedido() {
			return idPedido;
		}

		public void setIdPedido(String idPedido) {
			this.idPedido = idPedido;
		}

		public Codigos_rastreamento getCodigos_rastreamento() {
			return codigos_rastreamento;
		}
		public void setCodigos_rastreamento(Codigos_rastreamento codigos_rastreamento) {
			this.codigos_rastreamento = codigos_rastreamento;
		}
	}
