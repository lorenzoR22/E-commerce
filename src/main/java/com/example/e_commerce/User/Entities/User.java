package com.example.e_commerce.User.Entities;

import com.example.e_commerce.Carrito.Entities.Carrito;
import com.example.e_commerce.Pedido.Entities.Pedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String telefono;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role>roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Carrito carrito;

    @OneToMany(mappedBy = "user")
    private List<Pedido> pedidos;

    public User(String username, String password, String email, String telefono, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.telefono = telefono;
        this.roles = roles;
        this.carrito=new Carrito(this);
    }
}
