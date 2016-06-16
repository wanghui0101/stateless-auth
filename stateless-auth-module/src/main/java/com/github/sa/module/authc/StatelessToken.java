package com.github.sa.module.authc;

import org.apache.shiro.authc.AuthenticationToken;

public class StatelessToken implements AuthenticationToken {
	
	private static final long serialVersionUID = -7311171787311961528L;
	
	private String token;
    
    public StatelessToken(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}