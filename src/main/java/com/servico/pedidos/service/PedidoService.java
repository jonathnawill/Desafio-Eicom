package com.servico.pedidos.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servico.pedidos.entities.Cliente;
import com.servico.pedidos.entities.Pedido;
import com.servico.pedidos.entities.dto.ClienteDTO;
import com.servico.pedidos.entities.dto.PedidoDTO;
import com.servico.pedidos.repositories.ClienteRepository;
import com.servico.pedidos.repositories.PedidoRepository;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Transactional
	public PedidoDTO criarPedido(PedidoDTO pedidoDTO) {
		// Verificar se o cliente existe
		Cliente cliente = clienteRepository.findById(pedidoDTO.getCliente().getId())
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

		// Criar a entidade Pedido a partir do DTO
		Pedido pedido = new Pedido();
		pedido.setDataCadastro(pedidoDTO.getDataCadastro() != null ? pedidoDTO.getDataCadastro() : LocalDate.now());
		pedido.setNome(pedidoDTO.getNome());
		pedido.setValor(pedidoDTO.getValor());
		pedido.setQuantidade(pedidoDTO.getQuantidade() != null ? pedidoDTO.getQuantidade() : 1);
		pedido.setCliente(cliente);

		// Aplicar a lógica de desconto
		int quantidade = pedido.getQuantidade();
		BigDecimal desconto = BigDecimal.ZERO;

		if (quantidade >= 10) {
			desconto = new BigDecimal("0.10");
		} else if (quantidade > 5) {
			desconto = new BigDecimal("0.05"); // Desconto para 6 a 9
		}

		BigDecimal valorTotal = pedido.getValor().multiply(BigDecimal.valueOf(quantidade))
				.multiply(BigDecimal.ONE.subtract(desconto));

		pedido.setValor(valorTotal);

		// Salvar a entidade no banco de dados
		Pedido pedidoSalvo = pedidoRepository.save(pedido);

		// Converter a entidade salva para DTO antes de retornar
		return new PedidoDTO(pedidoSalvo.getId(), pedidoSalvo.getDataCadastro(), pedidoSalvo.getNome(),
				pedidoSalvo.getValor(), pedidoSalvo.getQuantidade(),
				new ClienteDTO(cliente.getId(), cliente.getNome()));
	}

	// Retorna um pedido pelo id
	@Transactional(readOnly = true)
	public Optional<PedidoDTO> findById(Long id) {
		return pedidoRepository.findById(id).map(PedidoDTO::new);
	}

	// Retorna lista de pedidos
	@Transactional(readOnly = true)
	public List<PedidoDTO> findAll() {
		List<Pedido> result = pedidoRepository.findAll();
		List<PedidoDTO> dto = result.stream().map(x -> new PedidoDTO(x)).toList();
		return dto;
	}

	// retorna um lista de pedidos por data de cadastro
	@Transactional(readOnly = true)
	public List<PedidoDTO> findByDataCadastro(LocalDate dataCadastro) {
		List<Pedido> result = pedidoRepository.findByDataCadastro(dataCadastro);
		List<PedidoDTO> dtoList = result.stream().map(PedidoDTO::new).toList(); 
		return dtoList;
	}

}
