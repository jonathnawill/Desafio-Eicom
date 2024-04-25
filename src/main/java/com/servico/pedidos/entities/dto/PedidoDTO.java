package com.servico.pedidos.entities.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.BeanUtils;

import com.servico.pedidos.entities.Pedido;

public class PedidoDTO {

	private Long id;
	private LocalDate dataCadastro;
	private String nome;
	private BigDecimal valor;
	private Integer quantidade;
	private ClienteDTO cliente;

	public PedidoDTO() {

	}

	public PedidoDTO(Long id, LocalDate dataCadastro, String nome, BigDecimal valor, Integer quantidade,
			ClienteDTO cliente) {
		this.id = id;
		this.dataCadastro = dataCadastro;
		this.nome = nome;
		this.valor = valor;
		this.quantidade = quantidade;
		this.cliente = cliente;
	}

	public PedidoDTO(Pedido entity) {
		BeanUtils.copyProperties(entity, this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public ClienteDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteDTO cliente) {
		this.cliente = cliente;
	}

}
