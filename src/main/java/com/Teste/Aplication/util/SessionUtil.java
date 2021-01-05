package com.Teste.Aplication.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionUtil<T extends Object> {

	@Autowired
	private HttpSession session;
	@Autowired
	private HttpServletRequest request;

	public void criarSession(String name, T objeto) {
		request.getSession().setAttribute(name, objeto);
	}

	public T getSession(String name) {
		return (T) session.getAttribute(name);
	}

	public void clearSession() {
		request.getSession().invalidate();
	}
}
