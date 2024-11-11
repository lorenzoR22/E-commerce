package com.example.e_commerce.Services;

import com.example.e_commerce.Models.DTOs.PedidoDTO;
import com.example.e_commerce.Models.DTOs.ProductoCarritoDTO;
import com.example.e_commerce.Models.Entities.Carrito;
import com.example.e_commerce.Models.Entities.Pedido;
import com.example.e_commerce.Models.Entities.Productos.ProductoCarrito;
import com.example.e_commerce.Models.Entities.Productos.ProductoPedido;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Repositories.CarritoRepository;
import com.example.e_commerce.Repositories.PedidoRepository;
import com.example.e_commerce.Repositories.ProductoCarritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final CarritoRepository carritoRepository;

    private final ProductoService productoService;

    private final ProductoCarritoRepository productoCarritoRepository;

    @Transactional
    public PedidoDTO comprarCarrito(Long id_carrito) throws IdNotFound {
        Carrito carrito=carritoRepository.findById(id_carrito)
                .orElseThrow(IdNotFound::new);

        Pedido newPedido =new Pedido();

        newPedido.setProductos(setProductosToPedido(carrito.getProductosCarrito(), newPedido));
        newPedido.setTotal(newPedido.totalFactura());

        productoCarritoRepository.deleteByCarrito(carrito);
        carrito.getProductosCarrito().clear();

        carritoRepository.save(carrito);

        newPedido = pedidoRepository.save(newPedido);

        return pedidoToDTO(newPedido);
    }

    private Set<ProductoPedido> setProductosToPedido(Set<ProductoCarrito>productosCarrito, Pedido pedido){
        return productosCarrito.stream()
                .map(pc -> new ProductoPedido(
                        pc.getProducto(),
                        pc.getCantidad(),
                        pedido
                )).collect(Collectors.toSet());
    }

    public List<PedidoDTO> getAllPedidos(){
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidos.stream()
                .map(this::pedidoToDTO
                ).toList();
    }
    public PedidoDTO pedidoToDTO(Pedido pedido){
        return new PedidoDTO(
                pedido.getId(),
                pedido.getProductos().stream()
                        .map(product->new ProductoCarritoDTO(
                                product.getId(),
                                productoService.productoToDTO(product.getProducto()),
                                product.getCantidad()
                        )).collect(Collectors.toSet()),
                pedido.getFecha().toString(),
                pedido.getTotal()
        );
    }

}
