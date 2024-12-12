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
import org.springframework.http.HttpStatus;
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

    public String comprarCarrito(Long id_carrito) throws IdNotFound {
        Carrito carrito=carritoRepository.findById(id_carrito)
                .orElseThrow(IdNotFound::new);

        return getLinkCompra(carrito.getProductosCarrito(),carrito.getId());
    }

    public String getLinkCompra(Set<ProductoCarrito>pedido,Long IdCarrito){
        try {
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
                    .notificationUrl("https://puerto/carrito/checkPago/"+IdCarrito)
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return preference.getInitPoint();

        }catch (MPException | MPApiException e){
            return e.toString();
        }
    }
    @Transactional
    public ResponseEntity<String> handleNotification(Map<String, Object> payload, Long IdCarrito) {

        if ("payment".equals(payload.get("type"))) {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");

            if (data == null || !data.containsKey("id")) {
                System.err.println("El payload no contiene un campo 'data' válido o no tiene 'id'.");
                return ResponseEntity.badRequest().body("Notificación inválida.");
            }

            String paymentId = (String) data.get("id");

            try {
                MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

                PaymentClient paymentClient = new PaymentClient();
                Payment payment = paymentClient.get(Long.valueOf(paymentId));

                if ("approved".equals(payment.getStatus())) {
                    Carrito carrito = carritoRepository.findById(IdCarrito)
                            .orElseThrow(IdNotFound::new);
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

                    System.out.println("Factura guardada con éxito para el pago: " + paymentId);
                } else {
                    System.out.println("Pago no aprobado: " + payment.getStatus());
                }

            } catch (MPException | MPApiException e) {
                System.err.println("Error al procesar el pago: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el pago.");
            }
        }
        return ResponseEntity.ok("Notificación recibida correctamente.");
    }

    public List<PedidoDTO> getAllPedidos(){
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidos.stream()
                .map(this::pedidoToDTO
                ).toList();
    }
    public PedidoDTO pedidoToDTO(Pedido pedido){
        return new PedidoDTO(
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
