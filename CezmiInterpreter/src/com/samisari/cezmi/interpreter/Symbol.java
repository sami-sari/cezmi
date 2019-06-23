package com.samisari.cezmi.interpreter;

public class Symbol {
	private String		token;
	private int			tokeId;
	private Class<?>	handler;

	public Symbol() {
		super();
	}

	public Symbol(String token, int tokenId, Class<?> handler) {
		setToken(token);
		setTokeId(tokenId);
		setHandler(handler);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getTokeId() {
		return tokeId;
	}

	public void setTokeId(int tokeId) {
		this.tokeId = tokeId;
	}

	public Class<?> getHandler() {
		return handler;
	}

	public void setHandler(Class<?> handler) {
		this.handler = handler;
	}
}
