package com.example.e_commerce.Controllers;

import com.example.e_commerce.DTOs.UserDTO;
import com.example.e_commerce.Entities.User;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Exceptions.UsernameAlreadyExists;
import com.example.e_commerce.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>>getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    //cuando se crea un user crear a la vez su carrito vacio.
    @PostMapping("/add")
    public ResponseEntity<UserDTO>saveUser(@RequestBody @Valid UserDTO user) throws UsernameAlreadyExists {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO>updateUser(@PathVariable Long id,@RequestBody @Valid UserDTO userDTO) throws IdNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id,userDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
    }

}


