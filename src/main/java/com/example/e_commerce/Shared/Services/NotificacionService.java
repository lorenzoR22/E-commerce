package com.example.e_commerce.Shared.Services;

import com.example.e_commerce.Pedido.DTOs.ProductoPedidoDTO;
import com.example.e_commerce.Pedido.Entities.ProductoPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final JavaMailSender mailSender;

    public void enviarConfirmacion(String destinatario, Set<ProductoPedido> productos) {

        StringBuilder cuerpo = new StringBuilder("Tu compra de los productos:\n");

        for (ProductoPedido producto : productos) {
            cuerpo.append("- ").append(producto.getProducto().getNombre()).append("\n");
        }

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Compra confirmada");
        mensaje.setText(cuerpo.toString());
        mailSender.send(mensaje);
    }
}
