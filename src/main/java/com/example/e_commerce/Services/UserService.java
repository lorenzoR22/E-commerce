package com.example.e_commerce.Services;

import com.example.e_commerce.DTOs.RegisterDTO;
import com.example.e_commerce.DTOs.UserDTO;
import com.example.e_commerce.Entities.Carrito;
import com.example.e_commerce.Entities.Erole;
import com.example.e_commerce.Entities.Role;
import com.example.e_commerce.Entities.User;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Exceptions.UsernameAlreadyExists;
import com.example.e_commerce.Repositories.CarritoRepository;
import com.example.e_commerce.Repositories.RoleRepository;
import com.example.e_commerce.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(user->userToDTO(user)
                ).toList();
    }

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
                userDTO.getId(),
                userDTO.getUsername(),
                passwordEncoder.encode(userDTO.getPassword()),
                userDTO.getEmail(),
                userDTO.getTelefono(),
                roles);
        newUser=userRepository.save(newUser);

        carritoRepository.save(new Carrito(newUser));

        userDTO.setId(newUser.getId());
        userDTO.setPassword(newUser.getPassword());

        return userDTO;
    }

    public UserDTO updateUser(Long id, RegisterDTO registerDTO) throws IdNotFound {
        User userExistente=userRepository.findById(id)
                .orElseThrow(()->new IdNotFound());

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
                .orElseThrow(()->new IdNotFound());
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
                user.getId(),
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
