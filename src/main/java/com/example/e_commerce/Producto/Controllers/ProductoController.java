package com.example.e_commerce.Producto.Controllers;

import com.example.e_commerce.Producto.DTOs.ProductoDTO;
import com.example.e_commerce.Producto.Entities.ProductoImg;
import com.example.e_commerce.Producto.Exceptions.ProductoNotFoundException;
import com.example.e_commerce.Producto.Services.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producto")
@RequiredArgsConstructor
public class ProductoController{

    private final ProductoService productoService;

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoDTO>getAllProductos(){
        return productoService.getAllProductos();
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductoDTO getProducto(@PathVariable Long id) throws ProductoNotFoundException {
        return productoService.getProductobyId(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoDTO addProducto(@RequestBody @Valid ProductoDTO productoDTO){
        return productoService.savedProducto(productoDTO);
    }

    @PostMapping("/addImg/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoDTO addImg(@PathVariable Long id,@RequestBody @Valid ProductoImg img) throws ProductoNotFoundException {
        return productoService.addProductoImg(id,img);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoDTO updateProducto(@PathVariable Long id,@RequestBody @Valid ProductoDTO productoDTO) throws ProductoNotFoundException {
        return productoService.updateProducto(id,productoDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteProducto(@PathVariable Long id){
        return productoService.deleteProducto(id);
    }

    @DeleteMapping("/deleteImg/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteImg(@PathVariable Long id){
        return productoService.deleteProductoImg(id);
    }
}
