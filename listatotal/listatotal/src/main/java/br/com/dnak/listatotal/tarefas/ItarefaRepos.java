package br.com.dnak.listatotal.tarefas;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItarefaRepos extends JpaRepository<TarefaModelo, UUID>{

    List<TarefaModelo> findByIdUsuario(UUID idUsuario);

}
