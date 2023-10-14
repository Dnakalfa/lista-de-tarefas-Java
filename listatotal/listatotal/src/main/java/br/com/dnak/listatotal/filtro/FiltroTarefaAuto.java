package br.com.dnak.listatotal.filtro;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.dnak.listatotal.usuario.IUsuarioRepos;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FiltroTarefaAuto extends OncePerRequestFilter {

    @Autowired
    private IUsuarioRepos usuarioRepos;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                var servletPath = request.getServletPath();
                if(servletPath.startsWith("/tarefas/")) {

                //autenticar
                var authorization = request.getHeader("Authorization");
                var usuario_pass = authorization.substring("Basic".length()).trim();

                byte[] authdecode = Base64.getDecoder().decode(usuario_pass);

                var authString = new String(authdecode);
                
                String[] credencial = authString.split(":");
                String username = credencial[0];
                String password = credencial[1];
                //validar
                var usuario = this.usuarioRepos.findByUsername(username);
                if (usuario == null){
                    response.sendError(401);
                }else{
                    var passVerificado = BCrypt.verifyer().verify(password.toCharArray(), usuario.getPassword());
                    if(passVerificado.verified==true) {
                        request.setAttribute("idUsuario", usuario.getId());
                        filterChain.doFilter(request, response);
                    }else{
                        response.sendError(401);
                    }
                }
            }else{
                filterChain.doFilter(request, response);
            }
    }
}
