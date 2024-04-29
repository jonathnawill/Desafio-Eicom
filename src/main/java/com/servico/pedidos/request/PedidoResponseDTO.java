package com.servico.pedidos.request;

import com.servico.pedidos.entities.dto.PedidoDTO;

// Classe para retorno de dados, criei uma String de status para informar no corpo da requesição
public class PedidoResponseDTO {

	private String status;

	private PedidoDTO pedido;

	public PedidoResponseDTO(String status, PedidoDTO pedido) {
		this.status = status;
		this.pedido = pedido;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PedidoDTO getPedido() {
		return pedido;
	}

	public void setPedido(PedidoDTO pedido) {
		this.pedido = pedido;
	}

}
