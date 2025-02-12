package com.users.auth_api.entity;

import com.users.auth_api.enums.Roles;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroIdetificacion;
    private String nombre;
    private String apellido;
    private String telefono;
    @Column(unique=true)
    private String correo;
    private String contrasena;
    private boolean activo;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Token> tokens;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;

    @PrePersist
    public void init() {
        activo = true;
        roles = Set.of(Roles.USER);
    }

}
