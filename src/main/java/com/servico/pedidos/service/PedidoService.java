package com.servico.pedidos.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servico.pedidos.entities.Cliente;
import com.servico.pedidos.entities.Pedido;
import com.servico.pedidos.entities.dto.PedidoDTO;
import com.servico.pedidos.repositories.ClienteRepository;
import com.servico.pedidos.repositories.PedidoRepository;
import com.servico.pedidos.request.PedidoResponse;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

	@Transactional
	public PedidoResponse criarPedido(PedidoDTO pedidoDTO) {
		// Verificar se o cliente existe
		Cliente cliente;
		try {
			cliente = clienteRepository.findById(pedidoDTO.getCliente().getId())
					.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
		} catch (IllegalArgumentException e) {
			logger.error("Erro ao encontrar cliente: {}", pedidoDTO.getCliente().getId(), e);
			throw e; // Relança a exceção para manter a propagação correta
		}

		// Verificar se o número de controle é único
		boolean numeroControleExiste = pedidoRepository.findByNumeroControle(pedidoDTO.getNumeroControle()).isPresent();
		if (numeroControleExiste) {
			logger.warn("Número de controle já cadastrado: {}", pedidoDTO.getNumeroControle());
			throw new IllegalArgumentException("Número de controle já cadastrado.");
		}

		// Criar a entidade Pedido a partir do DTO
		Pedido pedido = new Pedido();
		pedido.setNumeroControle(pedidoDTO.getNumeroControle());
		pedido.setDataCadastro(pedidoDTO.getDataCadastro() != null ? pedidoDTO.getDataCadastro() : LocalDate.now());
		pedido.setNome(pedidoDTO.getNome());
		pedido.setValor(pedidoDTO.getValor());
		pedido.setQuantidade(pedidoDTO.getQuantidade() != null ? pedidoDTO.getQuantidade() : 1);
		pedido.setCliente(cliente);

		// Aplicar a lógica de desconto
		int quantidade = pedido.getQuantidade();
		BigDecimal desconto = BigDecimal.ZERO;

		// se a quantidade for maior ou igual a 10 da uma desconto de 10/100
		if (quantidade >= 10) {
			desconto = new BigDecimal("0.10");
			// Se a quantidade for maior que 5 da um desconto de 5/100
		} else if (quantidade > 5) {
			desconto = new BigDecimal("0.05");
		}

		// Calculo de valorTotal com desconto se assim o tiver
		// pega o valor e multiplica pela quantidade se houver um desconto ele
		// multiplica o valor e substrai do valor total
		BigDecimal valorTotal = pedido.getValor().multiply(BigDecimal.valueOf(quantidade))
				.multiply(BigDecimal.ONE.subtract(desconto)).setScale(2, RoundingMode.HALF_UP);

		// Settando o valor e salvando no banco
		pedido.setValor(valorTotal);

		logger.info("Criando pedido com número de controle: {}", pedido.getNumeroControle());

		Pedido pedidoSalvo = pedidoRepository.save(pedido);

		return new PedidoResponse("Sucesso", new PedidoDTO(pedidoSalvo));

	}

	@Transactional(readOnly = true)
	public Optional<PedidoDTO> findById(Long id) {
		Optional<PedidoDTO> pedido = pedidoRepository.findById(id).map(PedidoDTO::new);
		if (pedido.isEmpty()) {
			logger.warn("Pedido não encontrado com ID: {}", id);
		} else {
			logger.info("Pedido encontrado com ID: {}", id);
		}
		return pedido;
	}

	@Transactional(readOnly = true)
	public List<PedidoDTO> findAll() {
		List<Pedido> result = pedidoRepository.findAll();
		logger.info("Encontrados {} pedidos", result.size());
		List<PedidoDTO> dto = result.stream().map(PedidoDTO::new).toList();
		return dto;
	}

	// retorna um lista de pedidos por data de cadastro
	@Transactional(readOnly = true)
	public List<PedidoDTO> findByDataCadastro(LocalDate dataCadastro) {
		logger.info("Buscando pedidos por data de cadastro: {}", dataCadastro);

		List<Pedido> result = pedidoRepository.findByDataCadastro(dataCadastro);

		if (result.isEmpty()) {
			logger.warn("Nenhum pedido encontrado para a data: {}", dataCadastro);
		} else {
			logger.info("Encontrados {} pedidos para a data: {}", result.size(), dataCadastro);
		}

		List<PedidoDTO> dtoList = result.stream().map(PedidoDTO::new).toList();
		return dtoList;
	}

}
