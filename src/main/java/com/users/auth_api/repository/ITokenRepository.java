package com.users.auth_api.repository;

import com.users.auth_api.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITokenRepository extends JpaRepository<Token, Long> {

    List<Token> findAllValidIsFalseOrRevokedIsFalseByUsuarioId(Long userId);
    Optional<Token> findByToken(String jwtToken);

}
