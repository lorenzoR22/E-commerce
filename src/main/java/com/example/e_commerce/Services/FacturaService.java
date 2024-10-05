package com.example.e_commerce.Services;

import com.example.e_commerce.DTOs.FacturaDTO;
import com.example.e_commerce.DTOs.ProductoCarritoDTO;
import com.example.e_commerce.Entities.Carrito;
import com.example.e_commerce.Entities.Factura;
import com.example.e_commerce.Entities.Productos.ProductoCarrito;
import com.example.e_commerce.Entities.Productos.ProductoFactura;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Repositories.CarritoRepository;
import com.example.e_commerce.Repositories.FacturaRepository;
import com.example.e_commerce.Repositories.ProductoCarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FacturaService {
    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoCarritoRepository productoCarritoRepository;

    @Transactional
    public FacturaDTO comprarCarrito(Long id_carrito) throws IdNotFound {
        Carrito carrito=carritoRepository.findById(id_carrito)
                .orElseThrow(()->new IdNotFound());

        Factura newFactura=new Factura();

        newFactura.setProductos(setProductosToFactura(carrito.getProductosCarrito(),newFactura));
        newFactura.setTotal(newFactura.totalFactura());

        productoCarritoRepository.deleteByCarrito(carrito);
        carrito.getProductosCarrito().clear();

        carritoRepository.save(carrito);

        newFactura= facturaRepository.save(newFactura);

        return facturaToDTO(newFactura);
    }

    private Set<ProductoFactura>setProductosToFactura(Set<ProductoCarrito>productosCarrito,Factura factura){
        return productosCarrito.stream()
                .map(pc -> new ProductoFactura(
                        pc.getProducto(),
                        pc.getCantidad(),
                        factura
                )).collect(Collectors.toSet());
    }

    public List<FacturaDTO> getAllFacturas(){
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
