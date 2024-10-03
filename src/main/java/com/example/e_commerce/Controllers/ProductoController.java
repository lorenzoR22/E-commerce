package com.example.e_commerce.Controllers;

import com.example.e_commerce.DTOs.ProductoDTO;
import com.example.e_commerce.Entities.Productos.ProductoImg;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producto")
public class ProductoController{
    @Autowired
    private ProductoService productoService;

    @GetMapping("/getAll")
    public ResponseEntity<List<ProductoDTO>>getAllProductos(){
        return ResponseEntity.status(HttpStatus.OK).body(productoService.getAllProductos());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductoDTO>getProducto(@PathVariable Long id) throws IdNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.getProductobyId(id));
    }

    @PostMapping("/add")
    public ResponseEntity<ProductoDTO>addProducto(@RequestBody @Valid ProductoDTO productoDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.savedProducto(productoDTO));
    }

    @PostMapping("/addImg/{id}")
    public ResponseEntity<ProductoDTO>addImg(@PathVariable Long id,@RequestBody @Valid ProductoImg img) throws IdNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.addProductoImg(id,img));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductoDTO>updateProducto(@PathVariable Long id,@RequestBody @Valid ProductoDTO productoDTO) throws IdNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.updateProducto(id,productoDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean>deleteProducto(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(productoService.deleteProducto(id));
    }
    @DeleteMapping("/deleteImg/{id}")
    public ResponseEntity<Boolean>deleteImg(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(productoService.deleteProductoImg(id));
    }

}
