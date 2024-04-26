package com.servico.pedidos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico.pedidos.entities.dto.PedidoDTO;
import com.servico.pedidos.request.PedidoResponse;
import com.servico.pedidos.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	@PostMapping
	public ResponseEntity<PedidoResponse> criarPedido(@RequestBody PedidoDTO pedidoDTO) {
		PedidoResponse novoPedido = pedidoService.criarPedido(pedidoDTO);
		return ResponseEntity.ok(novoPedido);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PedidoDTO> buscarPedidoPorId(@PathVariable Long id) {
		Optional<PedidoDTO> pedido = pedidoService.findById(id);
		return ResponseEntity.ok(pedido.get());
	}

	@GetMapping
	public ResponseEntity<List<PedidoDTO>> buscarTodosPedidos() {
		List<PedidoDTO> pedidos = pedidoService.findAll();
		return ResponseEntity.ok(pedidos);
	}
}
