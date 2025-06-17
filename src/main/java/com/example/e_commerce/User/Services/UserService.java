package com.example.e_commerce.User.Services;

import com.example.e_commerce.User.Entities.Erole;
import com.example.e_commerce.User.DTOs.RegisterDTO;
import com.example.e_commerce.User.DTOs.UserDTO;
import com.example.e_commerce.Carrito.Repositories.CarritoRepository;
import com.example.e_commerce.User.Entities.Role;
import com.example.e_commerce.User.Entities.User;
import com.example.e_commerce.User.Exceptions.UserNotFoundException;
import com.example.e_commerce.User.Exceptions.UsernameAlreadyExistsException;
import com.example.e_commerce.User.Repositories.RoleRepository;
import com.example.e_commerce.User.Repositories.UserRepository;
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
    public UserDTO saveUser(UserDTO userDTO) throws UsernameAlreadyExistsException {

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UsernameAlreadyExistsException();
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
    public UserDTO updateUser(Long id, RegisterDTO userRequestDTO) throws UserNotFoundException {
        User userExistente=userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));

        Set<Role>roles=(userRequestDTO.getRoles().stream()
                .map(role-> {
                    try {
                        return roleRepository.findByRole(Erole.valueOf(role))
                                .orElseThrow(()->new RoleNotFoundException("No se encontro el rol"));
                    } catch (RoleNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet()));

        userExistente.setEmail(userRequestDTO.getEmail());
        userExistente.setTelefono(userRequestDTO.getTelefono());
        userExistente.setRoles(roles);

        userExistente=userRepository.save(userExistente);
        return userToDTO(userExistente);

    }

    public UserDTO getUserById(Long id) throws UserNotFoundException {
        User user= userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));
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
