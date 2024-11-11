package com.example.e_commerce.Controllers;

import com.example.e_commerce.Models.DTOs.ProductoDTO;
import com.example.e_commerce.Models.Entities.Productos.ProductoImg;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Services.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ProductoDTO getProducto(@PathVariable Long id) throws IdNotFound {
        return productoService.getProductobyId(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDTO addProducto(@RequestBody @Valid ProductoDTO productoDTO){
        return productoService.savedProducto(productoDTO);
    }

    @PostMapping("/addImg/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductoDTO addImg(@PathVariable Long id,@RequestBody @Valid ProductoImg img) throws IdNotFound {
        return productoService.addProductoImg(id,img);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductoDTO updateProducto(@PathVariable Long id,@RequestBody @Valid ProductoDTO productoDTO) throws IdNotFound {
        return productoService.updateProducto(id,productoDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteProducto(@PathVariable Long id){
        return productoService.deleteProducto(id);
    }
    @DeleteMapping("/deleteImg/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteImg(@PathVariable Long id){
        return productoService.deleteProductoImg(id);
    }

}
