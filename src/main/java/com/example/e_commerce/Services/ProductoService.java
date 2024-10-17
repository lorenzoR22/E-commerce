package com.example.e_commerce.Services;

import com.example.e_commerce.DTOs.ProductoDTO;
import com.example.e_commerce.Entities.Categoria;
import com.example.e_commerce.Entities.Productos.Producto;
import com.example.e_commerce.Entities.Productos.ProductoImg;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Repositories.CategoriaRepository;
import com.example.e_commerce.Repositories.ProductoImgRepository;
import com.example.e_commerce.Repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoImgRepository imgRepository;

    public List<ProductoDTO>getAllProductos(){
        List<Producto>productos=productoRepository.findAll();
        return productos.stream()
                .map(product->(productoToDTO(product))
                ).toList();
    }

    public ProductoDTO savedProducto(ProductoDTO productoDTO){

        Producto newProducto=DTOtoProducto(productoDTO);

        Categoria categoria=categoriaRepository.findByNombre(productoDTO.getCategoria())
                .orElseGet(()->categoriaRepository.save(new Categoria(productoDTO.getCategoria())));

        newProducto.setCategoria(categoria);

        newProducto=productoRepository.save(newProducto);

        if(productoDTO.getImagenes()!=null){
            newProducto.setImagenes(saveAllImg(productoDTO,newProducto));
        }
        productoDTO.setId(newProducto.getId());
        return productoDTO;
    }

    public Set<ProductoImg>saveAllImg(ProductoDTO productoDTO,Producto newProducto){
        return productoDTO.getImagenes().stream()
                .map(url -> {
                    ProductoImg img = new ProductoImg(url,newProducto);
                    return imgRepository.save(img);
                })
                .collect(Collectors.toSet());
    }
    public ProductoDTO updateProducto(Long id,ProductoDTO productoDTO) throws IdNotFound {
        Producto producto=productoRepository.findById(id)
                .orElseThrow(()->new IdNotFound());

        Categoria categoria=categoriaRepository.findByNombre(productoDTO.getCategoria())
                .orElseGet(()->categoriaRepository.save(new Categoria(productoDTO.getCategoria())));

        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setCategoria(categoria);
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());

        producto=productoRepository.save(producto);

        return productoToDTO(producto);
    }
    public ProductoDTO addProductoImg(Long id_producto,ProductoImg productoImg) throws IdNotFound {
        Producto producto=productoRepository.findById(id_producto)
                .orElseThrow(()->new IdNotFound());

        productoImg.setProducto(producto);
        producto.addImg(productoImg);

        imgRepository.save(productoImg);
        producto=productoRepository.save(producto);

        return productoToDTO(producto);
    }
    public Boolean deleteProductoImg(Long id_img){
        if(imgRepository.existsById(id_img)){
            imgRepository.deleteById(id_img);
            return true;
        }
        return false;
    }

    public Boolean deleteProducto(Long id){
        if(productoRepository.existsById(id)){
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Producto getProducto(Long id) throws IdNotFound {
        return productoRepository.findById(id)
                .orElseThrow(()->new IdNotFound());
    }

    public ProductoDTO getProductobyId(Long id) throws IdNotFound {
        Producto producto= productoRepository.findById(id)
                .orElseThrow(()->new IdNotFound());
        return productoToDTO(producto);
    }

    public Producto DTOtoProducto(ProductoDTO productoDTO){
        return new Producto(
                productoDTO.getNombre(),
                productoDTO.getDescripcion(),
                productoDTO.getStock(),
                productoDTO.getPrecio()
        );
    }

    public ProductoDTO productoToDTO(Producto producto){
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCategoria().getNombre(),
                producto.getImagenes().stream()
                        .map(img->img.getUrl())
                        .collect(Collectors.toSet()),
                producto.getStock(),
                producto.getPrecio()
        );
    }


}
