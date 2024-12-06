package com.mycar.business.services.impl;

import com.mycar.business.entities.UserEntity;
import com.mycar.business.repositories.UserRepository;
import com.mycar.business.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    // Método para obtener el UserEntity del usuario actual logueado
    public UserEntity getLoggedInUser(HttpServletRequest request) {
        // Busca la cookie con el token
        String token = null;
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) { // Busca la cookie llamada "token"
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // Si no se encuentra el token, lanza una excepción
        if (token == null) {
            throw new RuntimeException("Token JWT no válido o no proporcionado");
        }

        // Extrae el email del token
        String email = jwtUtils.extractEmail(token);

        // Busca el usuario por email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el email: " + email));
    }
}