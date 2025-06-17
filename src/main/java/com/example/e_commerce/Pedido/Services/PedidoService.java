package com.example.e_commerce.Pedido.Services;

import com.example.e_commerce.Carrito.Exceptions.CarritoNotFoundException;
import com.example.e_commerce.Carrito.Entities.Carrito;
import com.example.e_commerce.Carrito.Entities.ProductoCarrito;
import com.example.e_commerce.Carrito.Repositories.CarritoRepository;
import com.example.e_commerce.Carrito.Repositories.ProductoCarritoRepository;
import com.example.e_commerce.Pedido.DTOs.PedidoDTO;
import com.example.e_commerce.Pedido.Entities.Pedido;
import com.example.e_commerce.Pedido.Entities.ProductoPedido;
import com.example.e_commerce.Pedido.DTOs.ProductoPedidoDTO;
import com.example.e_commerce.Pedido.Exceptions.PedidoNotFoundException;
import com.example.e_commerce.Pedido.Repositories.PedidoRepository;
import com.example.e_commerce.Producto.Exceptions.ProductoNotFoundException;
import com.example.e_commerce.Producto.Services.ProductoService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoService productoService;
    private final ProductoCarritoRepository productoCarritoRepository;

    @Value("${mercadopago.accessToken}")
    private String mercadoPagoAccessToken;

    public PedidoService(PedidoRepository pedidoRepository, CarritoRepository carritoRepository, ProductoService productoService, ProductoCarritoRepository productoCarritoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.carritoRepository = carritoRepository;
        this.productoService = productoService;
        this.productoCarritoRepository = productoCarritoRepository;
    }

    public PedidoDTO getPedido(Long id){
        Pedido pedido=pedidoRepository.findById(id)
                .orElseThrow(()->new PedidoNotFoundException(id));
        return pedidoToDTO(pedido);
    }

    public String comprarCarrito(Long id_carrito) throws MPException, MPApiException, CarritoNotFoundException {
        Carrito carrito=carritoRepository.findById(id_carrito)
                .orElseThrow(()->new CarritoNotFoundException(id_carrito));

        return getLinkCompra(carrito.getProductosCarrito(),carrito.getId());
    }

    private String getLinkCompra(Set<ProductoCarrito> pedido, Long IdCarrito) throws MPException, MPApiException {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

            List<PreferenceItemRequest>items=new ArrayList<>();

            for(ProductoCarrito i:pedido){
                PreferenceItemRequest itemRequest =
                        PreferenceItemRequest.builder()
                                .id(i.getProducto().getId().toString())
                                .title(i.getProducto().getNombre())
                                .description(i.getProducto().getCategoria().getNombre())
                                .quantity(i.getCantidad())
                                .currencyId("ARS")
                                .unitPrice(BigDecimal.valueOf(i.getProducto().getPrecio()))
                                .build();
                items.add(itemRequest);
            }

            PreferenceBackUrlsRequest backUrls =  PreferenceBackUrlsRequest
                    .builder().success("https://www.youtube.com/success")
                    .failure("https://www.youtube.com/failure")
                    .pending("https://www.youtube.com/pending")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .notificationUrl("url/pedido/checkPago/"+IdCarrito)
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference=client.create(preferenceRequest);

            return preference.getInitPoint();
    }

    @Transactional
    public ResponseEntity<String> handleNotification(Map<String, Object> payload, Long IdCarrito) throws MPException, MPApiException, CarritoNotFoundException {

        if ("payment".equals(payload.get("type"))) {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");

            if (data == null || !data.containsKey("id")) {
                System.err.println("El payload no contiene un campo 'data' válido o no tiene 'id'.");
                return ResponseEntity.badRequest().body("Notificación inválida.");
            }

            String paymentId = (String) data.get("id");

            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(Long.valueOf(paymentId));

            if ("approved".equals(payment.getStatus())) {

                guardarPedido(IdCarrito,payment);

            } else {
                System.out.println("Pago no aprobado: " + payment.getStatus());
            }

        }
        return ResponseEntity.ok("Notificación recibida correctamente.");
    }

    private void guardarPedido(Long IdCarrito,Payment payment) throws CarritoNotFoundException, ProductoNotFoundException {
        Carrito carrito = carritoRepository.findById(IdCarrito)
                .orElseThrow(()->new CarritoNotFoundException(IdCarrito));
        productoCarritoRepository.deleteByCarrito(carrito);
        carrito.getProductosCarrito().clear();
        carritoRepository.save(carrito);

        Pedido pedidoSaved = pedidoRepository.save(new Pedido());

        pedidoSaved.setProductos(payment.getAdditionalInfo().getItems()
                .stream()
                .map(items -> new ProductoPedido(
                        productoService.getProducto(Long.valueOf(items.getId())),
                        items.getQuantity(),
                        pedidoSaved))
                .collect(Collectors.toSet()));
        pedidoSaved.setTotal(pedidoSaved.totalFactura());

        pedidoRepository.save(pedidoSaved);
    }

    public PedidoDTO pedidoToDTO(Pedido pedido){
        return new PedidoDTO(
                pedido.getProductos().stream()
                        .map(product->new ProductoPedidoDTO(
                                product.getId(),
                                productoService.productoToDTO(product.getProducto()),
                                product.getCantidad()
                        )).collect(Collectors.toSet()),
                pedido.getFecha().toString(),
                pedido.getTotal()
        );
    }
}
