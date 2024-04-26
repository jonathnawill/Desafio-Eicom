package com.servico.pedidos.request;

import com.servico.pedidos.entities.dto.PedidoDTO;

// Classe para retorno de dados
// Escolhi um record, pois o retorno vai ser imutavel e o record já me da todos os metodos que preciso
public record PedidoResponse(String status, PedidoDTO pedido) {
}
