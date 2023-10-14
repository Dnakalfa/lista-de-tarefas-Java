package br.com.dnak.listatotal.tarefas;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity(name="tb_tarefas")
public class TarefaModelo {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String descricao;
    
    @Column(length = 50)
    private String titulo;
    private LocalDateTime inicioAgora;
    private LocalDateTime fimAgora;
    private String prioridade;
    
    private UUID idUsuario;
    
    @CreationTimestamp
    private LocalDateTime CriadoAgora;

    public void pegaTitulo(String titulo) throws Exception{
        if(titulo.length() > 50){
        
            throw new Exception("So pode ter 50 carcteres");
        }
        this.titulo = titulo;
    }


}
