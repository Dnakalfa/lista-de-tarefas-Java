package br.com.dnak.listatotal.usuario;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepos extends JpaRepository<UsuarioModelo, UUID> {

    UsuarioModelo findByUsername(String username);
    
} 
