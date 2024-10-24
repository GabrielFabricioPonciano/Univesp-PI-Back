package com.univesp.projeto_integrador.controller;

import com.univesp.projeto_integrador.config.TokenService;
import com.univesp.projeto_integrador.dto.LoginRequestDTO;
import com.univesp.projeto_integrador.dto.RegisterRequestDTO;
import com.univesp.projeto_integrador.dto.ResponseDTO;
import com.univesp.projeto_integrador.model.User;
import com.univesp.projeto_integrador.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    // Tratamento do login
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO body) {
        Optional<Object> userOptional = repository.findByEmail(body.email());

        // Verifica se o usuário com o email fornecido existe
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO("Usuário não encontrado com o email " + body.email(), null));
        }

        User user = (User) userOptional.get();

        // Verifica se a senha está correta
        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO("Senha incorreta", null));
        }

        // Gera o token se a autenticação for bem-sucedida
        String token = tokenService.generateToken(user);
        return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
    }

    // Tratamento do registro
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterRequestDTO body) {
        Optional<Object> existingUser = repository.findByEmail(body.email());

        // Verifica se já existe um usuário com o email fornecido
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO("Email já cadastrado", null));
        }

        // Criação do novo usuário
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(body.password()));
        newUser.setEmail(body.email());
        newUser.setName(body.name());

        repository.save(newUser);

        // Gera o token para o novo usuário registrado
        String token = tokenService.generateToken(newUser);
        return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
    }

    // Tratamento global de exceções (opcional)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalExceptions(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Ocorreu um erro inesperado: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
