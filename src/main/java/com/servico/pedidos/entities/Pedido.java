package com.servico.pedidos.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidade que representa um pedido no sistema. Um pedido está associado a um
 * cliente.
 */
@Entity
@Table(name = "tb_pedido")
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate dataCadastro;

	private String nome;

	private BigDecimal valor;

	private Integer quantidade;

	// Relacionamento com o cliente. O pedido pertence a um cliente
	// e respectivamente ele possui um codigo unico.
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	public Pedido() {
	}

	public Pedido(Long id, LocalDate dataCadastro, String nome, BigDecimal valor, Integer quantidade, Cliente cliente) {
		this.id = id;
		this.dataCadastro = dataCadastro;
		this.nome = nome;
		this.valor = valor;
		this.quantidade = quantidade;
		this.cliente = cliente;
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

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
