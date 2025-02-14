package com.users.auth_api.repository;

import com.users.auth_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNumeroIdentificacionAndActivoTrue(String numeroIdentificacion);
    Optional<Usuario> findByIdAndActivoTrue(Long id);
    Optional<Usuario> findByCorreoAndActivoTrue(String email);

}
