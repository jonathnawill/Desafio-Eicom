package com.servico.pedidos.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servico.pedidos.entities.Cliente;
import com.servico.pedidos.entities.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

	// Método para encontrar pedidos por cliente
	List<Pedido> findByCliente(Cliente cliente);

	// Método para encontrar pedidos por data de cadastro
	List<Pedido> findByDataCadastro(LocalDate dataCadastro);

}
