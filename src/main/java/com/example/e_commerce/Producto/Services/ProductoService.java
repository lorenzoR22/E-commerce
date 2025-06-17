package com.example.e_commerce.Producto.Services;

import com.example.e_commerce.Producto.DTOs.ProductoDTO;
import com.example.e_commerce.Producto.Entities.Categoria;
import com.example.e_commerce.Producto.Entities.Producto;
import com.example.e_commerce.Producto.Entities.ProductoImg;
import com.example.e_commerce.Producto.Exceptions.ProductoNotFoundException;
import com.example.e_commerce.Producto.Repositories.CategoriaRepository;
import com.example.e_commerce.Producto.Repositories.ProductoImgRepository;
import com.example.e_commerce.Producto.Repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoImgRepository imgRepository;

    public List<ProductoDTO>getAllProductos(){
        List<Producto>productos=productoRepository.findAll();
        return productos.stream()
                .map(this::productoToDTO
                ).toList();
    }

    @Transactional
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

    public Set<ProductoImg>saveAllImg(ProductoDTO productoDTO, Producto newProducto){
        return productoDTO.getImagenes().stream()
                .map(url -> {
                    ProductoImg img = new ProductoImg(url,newProducto);
                    return imgRepository.save(img);
                })
                .collect(Collectors.toSet());
    }

    @Transactional
    public ProductoDTO updateProducto(Long id,ProductoDTO productoDTO) throws ProductoNotFoundException {
        Producto producto=productoRepository.findById(id)
                .orElseThrow(()->new ProductoNotFoundException(id));

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

    public ProductoDTO addProductoImg(Long id_producto,ProductoImg productoImg) throws ProductoNotFoundException {
        Producto producto=productoRepository.findById(id_producto)
                .orElseThrow(()->new ProductoNotFoundException(id_producto));

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

    public Producto getProducto(Long id) throws ProductoNotFoundException {
        return productoRepository.findById(id)
                .orElseThrow(()->new ProductoNotFoundException(id));
    }

    public ProductoDTO getProductobyId(Long id) throws ProductoNotFoundException {
        Producto producto= productoRepository.findById(id)
                .orElseThrow(()->new ProductoNotFoundException(id));
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
                        .map(ProductoImg::getUrl)
                        .collect(Collectors.toSet()),
                producto.getStock(),
                producto.getPrecio()
        );
    }

}
