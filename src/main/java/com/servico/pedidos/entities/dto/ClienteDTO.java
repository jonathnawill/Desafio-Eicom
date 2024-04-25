package com.servico.pedidos.entities.dto;

import org.springframework.beans.BeanUtils;

import com.servico.pedidos.entities.Cliente;

public class ClienteDTO {

	private Long id;
	private String nome;

	public ClienteDTO() {

	}

	public ClienteDTO(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public ClienteDTO(Cliente entity) {
		BeanUtils.copyProperties(entity, this);
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

}
