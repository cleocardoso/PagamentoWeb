package com.Teste.Aplication.model;

public class User {
	public User() {
	}

	private Long id;

	private String nome;

	private String email;

	private String senha;

	private String token;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setUsername(String username) {
		this.email = username;
	}

	public void setPassword(String password) {
		this.senha = password;
	}

	/*
	 * public String getRoleString() { String txt=""; if(role!=null) { for(Role r:
	 * role) { txt+= r.getNome() + ", "; } } return txt; }
	 */

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
