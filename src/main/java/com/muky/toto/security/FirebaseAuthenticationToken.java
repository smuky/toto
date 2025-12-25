package com.muky.toto.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class FirebaseAuthenticationToken extends AbstractAuthenticationToken {

    private final String uid;

    public FirebaseAuthenticationToken(String uid, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.uid = uid;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return uid;
    }

    @Override
    public String getName() {
        return uid;
    }
}
