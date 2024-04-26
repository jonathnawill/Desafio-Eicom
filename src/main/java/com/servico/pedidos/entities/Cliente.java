package com.servico.pedidos.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidade que representa um cliente no sistema. Um cliente pode ter vários
 * pedidos associados a ele. Futuramente pode ser uma classe a ser utilizada não
 * só para pedidos
 */
@Entity
@Table(name = "tb_cliente")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	// Um cliente pode ter muitos pedidos.
	// Usei CascadeType.ALL para garantir que
	// se um cliente for removido, seus pedidos também sejam removidos.
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Pedido> pedidos = new ArrayList<>();

	public Cliente() {
	}

	public Cliente(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}
}
