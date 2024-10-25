package com.univesp.projeto_integrador.controller;

import com.univesp.projeto_integrador.model.Usuario;
import com.univesp.projeto_integrador.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Usuario> getAllUsers() {
        return userService.findAll();
    }

}
