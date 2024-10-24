package com.univesp.projeto_integrador.service;

import com.univesp.projeto_integrador.exception.ResourceNotFoundException;
import com.univesp.projeto_integrador.model.User;
import com.univesp.projeto_integrador.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

}
