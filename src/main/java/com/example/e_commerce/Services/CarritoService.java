package com.example.e_commerce.Services;

import com.example.e_commerce.DTOs.CarritoDTO;
import com.example.e_commerce.DTOs.FacturaDTO;
import com.example.e_commerce.DTOs.ProductoCarritoDTO;
import com.example.e_commerce.Entities.*;
import com.example.e_commerce.Entities.Productos.Producto;
import com.example.e_commerce.Entities.Productos.ProductoCarrito;
import com.example.e_commerce.Entities.Productos.ProductoFactura;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Exceptions.NoMoreStock;
import com.example.e_commerce.Repositories.CarritoRepository;
import com.example.e_commerce.Repositories.FacturaRepository;
import com.example.e_commerce.Repositories.ProductoCarritoRepository;
import com.example.e_commerce.Repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoCarritoService productoCarritoService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    public List<CarritoDTO>getAllCarritos(){
        List<Carrito>carritos=carritoRepository.findAll();
        return carritos.stream()
                .map(carrito->carritoToDTO(carrito)
                ).toList();
    }

    public CarritoDTO addProductoCarrito(Long id_carrito,Long id_producto) throws NoMoreStock, IdNotFound {
        Carrito carrito=carritoRepository.findById(id_carrito)
                .orElseThrow(()->new IdNotFound());

        Producto producto=productoService.getProducto(id_producto);
        if(producto.getStock()==0){
            throw new NoMoreStock();
        }

        Optional<ProductoCarrito>productoCarritoOpt=carrito.getProductosCarrito().stream()
                .filter(product->product.getProducto().equals(producto))
                .findFirst();

        if(productoCarritoOpt.isPresent()){
            ProductoCarrito productoCarrito=productoCarritoOpt.get();
            productoCarrito.sumarCantidad();
            productoCarritoService.saveProductoCarrito(productoCarrito);
        }else{
            ProductoCarrito productoCarrito=new ProductoCarrito(producto,carrito);
            productoCarritoService.saveProductoCarrito(productoCarrito);
            carrito.getProductosCarrito().add(productoCarrito);
        }

        producto.restarStock();
        productoRepository.save(producto);
        carrito=carritoRepository.save(carrito);

        return carritoToDTO(carrito);
    }

    public Boolean deleteProductoCarrito(Long id_productoCarrito) throws IdNotFound {
        return productoCarritoService.deleteProductoCarrito(id_productoCarrito);
    }

    public CarritoDTO carritoToDTO(Carrito carrito){
        return new CarritoDTO(
                carrito.getId(),
                userService.userToDTO(carrito.getUser()),
                carrito.getProductosCarrito().stream()
                        .map(product->new ProductoCarritoDTO(
                                product.getId(),
                                productoService.productoToDTO(product.getProducto()),
                                product.getCantidad()))
                        .collect(Collectors.toSet())
        );
    }

    private void deleteAllProductosCarrito(Long id_carrito) throws IdNotFound {
        Carrito carrito = carritoRepository.findById(id_carrito)
                .orElseThrow(() -> new IdNotFound());

        Set<ProductoCarrito> productos = new HashSet<>(carrito.getProductosCarrito());

        for (ProductoCarrito producto : productos) {
            carrito.getProductosCarrito().remove(producto);
            productoCarritoService.comprarProductoCarrito(producto.getId());
        }
        carritoRepository.save(carrito);
    }

    public CarritoDTO getCarritoById(Long id) throws IdNotFound {
        Carrito carrito=carritoRepository.findById(id)
                .orElseThrow(()->new IdNotFound());
        return carritoToDTO(carrito);
    }

    public FacturaDTO comprarCarrito(Long id_carrito) throws IdNotFound {
        Carrito carrito=carritoRepository.findById(id_carrito)
                .orElseThrow(()->new IdNotFound());

        Factura newFactura=new Factura();
        newFactura=facturaRepository.save(newFactura);

        Factura finalNewFactura = newFactura;
        Set<ProductoFactura>productosFactura=carrito.getProductosCarrito().stream()
                .map(pc->new ProductoFactura(
                        pc.getProducto(),
                        pc.getCantidad(),
                        finalNewFactura
                )).collect(Collectors.toSet());

        newFactura.setProductos(productosFactura);
        newFactura.setTotal(newFactura.totalFactura());

        newFactura= facturaRepository.save(newFactura);

        deleteAllProductosCarrito(id_carrito);

        return facturaToDTO(newFactura);
    }

    public List<FacturaDTO>getAllFacturas(){
        List<Factura>facturas=facturaRepository.findAll();
        return facturas.stream()
                .map(factura->facturaToDTO(factura)
                ).toList();
    }
    public FacturaDTO facturaToDTO(Factura factura){
        return new FacturaDTO(
                factura.getId(),
                factura.getProductos().stream()
                        .map(product->new ProductoCarritoDTO(
                                product.getId(),
                                productoService.productoToDTO(product.getProducto()),
                                product.getCantidad()
                                )).collect(Collectors.toSet()),
                factura.getFecha().toString(),
                factura.getTotal()
        );
    }

}
