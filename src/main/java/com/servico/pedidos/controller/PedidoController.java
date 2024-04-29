package com.servico.pedidos.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico.pedidos.entities.dto.PedidoDTO;
import com.servico.pedidos.request.PedidoResponseDTO;
import com.servico.pedidos.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	@PostMapping("criar-pedido")
	public ResponseEntity<?> criarPedido(@RequestBody PedidoDTO pedidoDTO) {
		try {
			PedidoResponseDTO novoPedido = pedidoService.criarPedido(pedidoDTO);
			return ResponseEntity.ok(novoPedido);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao criar o pedido. Tente novamente mais tarde.");
		}
	}

	// Endpoint para deletar pedido por ID
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarPedido(@PathVariable Long id) {
		try {
			pedidoService.deletaPedidoPorId(id);
			return ResponseEntity.ok("Pedido deletado com sucesso.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido com ID " + id + " não encontrado.");
		}
	}

	// Metodo para criar múltiplos pedidos em lote
	@PostMapping("criar-pedido-em-lote")
	public ResponseEntity<?> criarPedidosEmLote(@RequestBody List<PedidoDTO> pedidos) {
		// se a quantidade de item for maior que 10 manda um erro
		if (pedidos.size() > 10) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O número máximo de pedidos permitidos é 10");
		}
		// forEach para criar pedidos individualmente
		try {
			pedidos.forEach(pedidoDTO -> {
				pedidoService.criarPedido(pedidoDTO);
			});
			return ResponseEntity.ok("Pedidos criados com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao criar pedidos. tente novamente mais tarde");
		}
	};

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

	// Endpoint para buscar pedido pelo número de controlle
	@GetMapping("/controle/{numeroControle}")
	public ResponseEntity<?> buscarPedidoPorNumeroControle(@PathVariable Long numeroControle) {
		Optional<PedidoDTO> pedido = pedidoService.findByNumeroControle(numeroControle);

		if (!pedido.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Pedido com número de controle " + numeroControle + " não encontrado.");
		}

		return ResponseEntity.ok(pedido.get());
	}

	// Método para buscar pedidos por data de cadastro
	// A URL deve ter a data no formato dd-MM-yyyy, como por exemplo
	// /pedidos/data/04-05-1999
	@GetMapping("/data/{dataCadastro}")
	public ResponseEntity<?> buscarPedidosPorDataCadastro(@PathVariable String dataCadastro) {
		LocalDate data;
		try {
			// Converte a data recebida como string para um objeto LocalDate
			// O formato deve ser dd-MM-yyyy, como 04-05-1999
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			data = LocalDate.parse(dataCadastro, formatter);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Data no formato inválido. Use o formato dd-MM-yyyy.");
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
