package com.example.e_commerce.Controllers;

import com.example.e_commerce.Models.DTOs.RegisterDTO;
import com.example.e_commerce.Models.DTOs.UserDTO;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Exceptions.UsernameAlreadyExists;
import com.example.e_commerce.Services.UserService;
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
    public List<UserDTO>getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(@PathVariable Long id) throws IdNotFound {
        return userService.getUserById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO saveUser(@RequestBody @Valid UserDTO user) throws UsernameAlreadyExists {
        return userService.saveUser(user);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@PathVariable Long id,@RequestBody @Valid RegisterDTO registerDTO) throws IdNotFound {
        return userService.updateUser(id,registerDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }
}


