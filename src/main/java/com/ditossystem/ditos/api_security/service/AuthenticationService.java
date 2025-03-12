//package com.ditossystem.ditos.api_security.service;
//
//import com.ditossystem.ditos.api_security.security.AuthenticationMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.AuthorityUtils;
//
//public class AuthenticationService {
//
//    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
//    private static final String AUTH_TOKEN = "58de5ad2-a9e2-4788-87a7-e3e6ae4556c2";
//
//    public static Authentication getAuthentication(HttpServletRequest request){
//        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
//
//        if(apiKey == null || !apiKey.equals(AUTH_TOKEN)) {
//            //Retorna uma autenticação mobile
//            return new AuthenticationMapper("mobile", AuthorityUtils.NO_AUTHORITIES);
//        }
//
//        // Retorna uma autenticação completa
//        return new AuthenticationMapper(apiKey, AuthorityUtils.createAuthorityList("desktop"));
//    }
//}