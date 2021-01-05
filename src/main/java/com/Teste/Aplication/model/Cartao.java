package com.Teste.Aplication.model;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class Cartao implements Serializable {

	private static final long serialVersionUID = 1L; 	
	
	private String nome;
	private String numero;
	private String cvv;
	
	private int qtd_parcelas;
	
	private double valor_parcelado;

	private int mes;
	
	private int ano;
		
	public Cartao(Long id_cartao, String nome, String numero, String cvv, int qtd_parcelas, double valor_parcelado, int mes,
			int ano, Pagamento compras) {
		super();
		this.nome = nome;
		this.numero = numero;
		this.cvv = cvv;
		this.qtd_parcelas = qtd_parcelas;
		this.valor_parcelado = valor_parcelado;
		this.mes = mes;
		this.ano = ano;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public Cartao() {

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public int getQtd_parcelas() {
		return qtd_parcelas;
	}

	public void setQtd_parcelas(int qtd_parcelas) {
		this.qtd_parcelas = qtd_parcelas;
	}

	public double getValor_parcelado() {
		return valor_parcelado;
	}

	public void setValor_parcelado(double valor_parcelado) {
		this.valor_parcelado = valor_parcelado;
	}

}