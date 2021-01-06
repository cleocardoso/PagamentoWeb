package com.Teste.Aplication.model;

import java.io.Serializable;

import java.util.Date;

import com.Teste.Aplication.Enuns.Status;
import com.Teste.Aplication.Enuns.TipoPagamento;

public class Pagamento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Cartao cartao;

	private Boleto boleto;

	private double valor;

	private int quantidade;

	private Date dataCompra;

	private TipoPagamento tipoPagamento;

	private Status status;

	public User usuario;
	
	private Long id;

	public Pagamento() {
		super();
	}

	public Pagamento(Long idCompra, Cartao cartao, Boleto boleto, double valor, int quantidade, Date dataCompra,
			TipoPagamento tipoPagamento, Status status, User usuario) {
		super();
		this.cartao = cartao;
		this.boleto = boleto;
		this.valor = valor;
		this.quantidade = quantidade;
		this.dataCompra = dataCompra;
		this.tipoPagamento = tipoPagamento;
		this.status = status;
		this.usuario = usuario;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public Date getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

}
