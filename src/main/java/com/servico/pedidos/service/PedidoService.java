package com.servico.pedidos.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.servico.pedidos.request.PedidoResponseDTO;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	// Utilizei logger para rastrear ações como criar o pedido, buscar por Id, etc,
	// facilita minha vida na depuração
	private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

	@Transactional
	public PedidoResponseDTO criarPedido(PedidoDTO pedidoDTO) {
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

		return new PedidoResponseDTO(
				"Pedido criado com sucesso. Número de controle: " + pedidoSalvo.getNumeroControle(),
				new PedidoDTO(pedidoSalvo));
	}

	
	// Retorno um unico pedido pelo id
	@Transactional(readOnly = true)
	public Optional<PedidoDTO> findById(Long id) {
		return pedidoRepository.findById(id).map(PedidoDTO::new);
	}

	//  Retorna todos os pedidos
	@Transactional(readOnly = true)
	public List<PedidoDTO> findAll() {
		return pedidoRepository.findAll().stream().map(PedidoDTO::new).collect(Collectors.toList());
	}

	// retorna um lista de pedidos por data de cadastro
	@Transactional(readOnly = true)
	public List<PedidoDTO> findByDataCadastro(LocalDate dataCadastro) {
		List<Pedido> result = pedidoRepository.findByDataCadastro(dataCadastro);
		return result.isEmpty() ? Collections.emptyList()
				: result.stream().map(PedidoDTO::new).collect(Collectors.toList());
	}

	// Metodo para deletar pedido por Id
	@Transactional
	public void deletaPedidoPorId(Long id) {
		pedidoRepository.deleteById(id);
	}

	// Metodo para buscar pedido pelo número de controle informado pelo usuário
	@Transactional(readOnly = true)
	public Optional<PedidoDTO> findByNumeroControle(Long numeroControle) {
		return pedidoRepository.findByNumeroControle(numeroControle).map(PedidoDTO::new);
	}
}
