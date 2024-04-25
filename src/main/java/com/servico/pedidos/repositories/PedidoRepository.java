package com.servico.pedidos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servico.pedidos.entities.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
