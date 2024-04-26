package com.servico.pedidos.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
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

	// Essa anotação com a constrint unique garante que não haja duplicatas
	@Column(nullable = false, unique = true)
	private Long numeroControle;

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

	public Pedido(Long id, LocalDate dataCadastro, String nome, BigDecimal valor, Integer quantidade, Cliente cliente,
			Long numeroControle) {
		this.id = id;
		this.dataCadastro = dataCadastro;
		this.nome = nome;
		this.valor = valor;
		this.quantidade = quantidade;
		this.cliente = cliente;
		this.numeroControle = numeroControle;
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

	public Long getNumeroControle() {
		return numeroControle;
	}

	public void setNumeroControle(Long numeroControle) {
		this.numeroControle = numeroControle;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataCadastro, id, nome, numeroControle, quantidade, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		return Objects.equals(dataCadastro, other.dataCadastro) && Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome) && Objects.equals(numeroControle, other.numeroControle)
				&& Objects.equals(quantidade, other.quantidade) && Objects.equals(valor, other.valor);
	}

}
