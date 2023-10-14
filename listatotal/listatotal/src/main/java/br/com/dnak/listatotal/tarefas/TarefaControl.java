package br.com.dnak.listatotal.tarefas;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dnak.listatotal.utilitario.Util;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tarefas")
public class TarefaControl {

    @Autowired
    private ItarefaRepos tarefaRepos;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TarefaModelo tarefaModelo, HttpServletRequest request) {
        
        var idUsuario = request.getAttribute("idUsuario");
        tarefaModelo.setIdUsuario((UUID) idUsuario);

        var correnteData = LocalDateTime.now();
        if(correnteData.isAfter(tarefaModelo.getInicioAgora()) || correnteData.isAfter(tarefaModelo.getFimAgora())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data incio/final deve ser maior que data atual!");

        }
        if(tarefaModelo.getInicioAgora().isAfter(tarefaModelo.getFimAgora())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data inicio deve ser menor que data final!");

        }
        
        var tarefa = this.tarefaRepos.save(tarefaModelo);
        return ResponseEntity.status(HttpStatus.OK).body(tarefa);
    }
    
    @GetMapping("/")
    public List<TarefaModelo> lista(HttpServletRequest request) {
        var idUsuario = request.getAttribute("idUsuario");
        var tarefas = this.tarefaRepos.findByIdUsuario((UUID) idUsuario);
        return tarefas;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TarefaModelo tarefaModelo, HttpServletRequest request, @PathVariable UUID id){

        
        var tarefa = this.tarefaRepos.findById(id).orElse(null);

        if(tarefa == null){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa nao existe.");
        }

        var idUsuario = request.getAttribute("idUsuario");
        
        if(!tarefa.getIdUsuario().equals(idUsuario)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario não pode auterar.");
        }

        Util.copiaNaoNulo(tarefaModelo, tarefa);
        
        var tarefaAtual = this.tarefaRepos.save(tarefa);
        
        return ResponseEntity.ok().body(tarefaAtual);

    }
}
