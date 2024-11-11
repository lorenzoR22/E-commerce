package com.example.e_commerce.Services;

import com.example.e_commerce.Models.DTOs.RegisterDTO;
import com.example.e_commerce.Models.DTOs.UserDTO;
import com.example.e_commerce.Models.Entities.Carrito;
import com.example.e_commerce.Models.Entities.Erole;
import com.example.e_commerce.Models.Entities.Role;
import com.example.e_commerce.Models.Entities.User;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Exceptions.UsernameAlreadyExists;
import com.example.e_commerce.Repositories.CarritoRepository;
import com.example.e_commerce.Repositories.RoleRepository;
import com.example.e_commerce.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final CarritoRepository carritoRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(this::userToDTO
                ).toList();
    }
    @Transactional
    public UserDTO saveUser(UserDTO userDTO) throws UsernameAlreadyExists {

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UsernameAlreadyExists();
        }

        Set<Role> roles = userDTO.getRoles().stream()
                .map(role -> roleRepository.findByRole(Erole.valueOf(role))
                        .orElseGet(() -> {
                            Role newRole = new Role(Erole.valueOf(role));
                            return roleRepository.save(newRole);
                        }))
                .collect(Collectors.toSet());

        User newUser = new User(
                userDTO.getUsername(),
                passwordEncoder.encode(userDTO.getPassword()),
                userDTO.getEmail(),
                userDTO.getTelefono(),
                roles);
        newUser=userRepository.save(newUser);

        carritoRepository.save(newUser.getCarrito());

        userDTO.setPassword(newUser.getPassword());

        return userDTO;
    }
    @Transactional
    public UserDTO updateUser(Long id, RegisterDTO registerDTO) throws IdNotFound {
        User userExistente=userRepository.findById(id)
                .orElseThrow(IdNotFound::new);

        Set<Role>roles=(registerDTO.getRoles().stream()
                .map(role-> {
                    try {
                        return roleRepository.findByRole(Erole.valueOf(role))
                                .orElseThrow(()->new RoleNotFoundException("No se encontro el rol"));
                    } catch (RoleNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet()));

        userExistente.setEmail(registerDTO.getEmail());
        userExistente.setTelefono(registerDTO.getTelefono());
        userExistente.setRoles(roles);

        userExistente=userRepository.save(userExistente);
        return userToDTO(userExistente);

    }

    public UserDTO getUserById(Long id) throws IdNotFound {
        User user= userRepository.findById(id)
                .orElseThrow(IdNotFound::new);
        return userToDTO(user);
    }

    public Boolean deleteUser(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public UserDTO userToDTO(User user){
        return new UserDTO(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getTelefono(),
                user.getRoles().stream()
                        .map(role->role.getRole().name())
                        .collect(Collectors.toSet())
        );
    }
}
