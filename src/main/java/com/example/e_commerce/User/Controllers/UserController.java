package com.example.e_commerce.User.Controllers;

import com.example.e_commerce.User.Exceptions.UserNotFoundException;
import com.example.e_commerce.User.Exceptions.UsernameAlreadyExistsException;
import com.example.e_commerce.User.DTOs.RegisterDTO;
import com.example.e_commerce.User.DTOs.UserDTO;
import com.example.e_commerce.User.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO>getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(@PathVariable Long id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO saveUser(@RequestBody @Valid UserDTO user) throws UsernameAlreadyExistsException {
        return userService.saveUser(user);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@PathVariable Long id,@RequestBody @Valid RegisterDTO userRequestDTO) throws UserNotFoundException {
        return userService.updateUser(id, userRequestDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }
}


