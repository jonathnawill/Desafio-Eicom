package com.servico.pedidos.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	@PostMapping()
	public ResponseEntity<?> criarPedido(@RequestBody PedidoDTO pedidoDTO) {
		try {
			PedidoResponse novoPedido = pedidoService.criarPedido(pedidoDTO);
			return ResponseEntity.ok(novoPedido);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao criar o pedido. Tente novamente mais tarde.");
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscarPedidoPorId(@PathVariable Long id) {
		Optional<PedidoDTO> pedido = pedidoService.findById(id);
		return ResponseEntity.ok(pedido.get());
	}

	@GetMapping("/list")
	public ResponseEntity<List<PedidoDTO>> buscarTodosPedidos() {
		List<PedidoDTO> pedidos = pedidoService.findAll();
		return ResponseEntity.ok(pedidos);
	}

	// Método para buscar pedidos por data de cadastro
	// A URL deve ter a data no formato yyyy-MM-dd, como por exemplo
	// /pedidos/data/2024-04-25
	@GetMapping("/data/{dataCadastro}")
	public ResponseEntity<?> buscarPedidosPorDataCadastro(@PathVariable String dataCadastro) {
		LocalDate data;
		try {
			// Converte a data recebida como string para um objeto LocalDate
			// O formato deve ser yyyy-MM-dd, como 2024-04-25
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			data = LocalDate.parse(dataCadastro, formatter);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Data no formato inválido. Use o formato yyyy-MM-dd.");
		}

		// Busca o pedido no banco de dados com a data que pegamos
		List<PedidoDTO> pedidos = pedidoService.findByDataCadastro(data);

		// se não houver pedidos para a data, retorna um no content e uma mensagem
		if (pedidos.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body("Nenhum pedido encontrado para a data: " + dataCadastro);
		}

		return ResponseEntity.ok(pedidos);
	}
}
