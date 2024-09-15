package com.mycar.business.services;

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
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remover el prefijo "Bearer "
            String email = jwtUtils.extractEmail(token);

            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el email: " + email));
        }

        throw new RuntimeException("Token JWT no válido o no proporcionado");
    }
}

