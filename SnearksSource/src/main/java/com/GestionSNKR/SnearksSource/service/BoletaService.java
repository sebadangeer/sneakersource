package com.GestionSNKR.SnearksSource.service;

import com.GestionSNKR.SnearksSource.model.Boleta;
import com.GestionSNKR.SnearksSource.model.Carrito;
import com.GestionSNKR.SnearksSource.model.Cliente;
import com.GestionSNKR.SnearksSource.repository.BoletaRepository;
import com.GestionSNKR.SnearksSource.repository.CarritoRepository;
import com.GestionSNKR.SnearksSource.repository.ClienteRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.GestionSNKR.SnearksSource.model.ItemCarrito;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final CarritoRepository carritoRepository;
    private final ClienteRepository clienteRepository;
    private final JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(BoletaService.class);

    @Value("${spring.mail.username}")
    private String mailFrom;

    public BoletaService(BoletaRepository boletaRepository, CarritoRepository carritoRepository, ClienteRepository clienteRepository, JavaMailSender mailSender) {
        this.boletaRepository = boletaRepository;
        this.carritoRepository = carritoRepository;
        this.clienteRepository = clienteRepository;
        this.mailSender = mailSender;
    }

    public Boleta crearDesdeCarrito(Long clienteId, String metodoPago, String direccion) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado."));

        Carrito carrito = carritoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado para el cliente."));

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío.");
        }

        Boleta boleta = new Boleta();
        boleta.setCliente(cliente);
        List<ItemCarrito> itemsBoleta = new ArrayList<>();
        carrito.getItems().forEach(item -> itemsBoleta.add(item.copy()));
        boleta.setItems(itemsBoleta);
        boleta.setImpuestos(carrito.getImpuestos());
        boleta.setMetodoPago(metodoPago);
        boleta.setDireccion(direccion);
        boleta.setFecha(LocalDateTime.now());
        boleta.setEstado("CREADA");
        boleta.calcularTotal();

        Boleta guardada = boletaRepository.save(boleta);

        // Enviar boleta por email de forma asíncrona si existe email
        if (guardada.getCliente() != null && guardada.getCliente().getEmail() != null && !guardada.getCliente().getEmail().isBlank()) {
            enviarBoletaPorEmailAsync(guardada);
        }

        // Vaciar carrito después de crear boleta
        carrito.getItems().clear();
        carrito.calcularTotal();
        carritoRepository.save(carrito);

        return guardada;
    }

    @Async
    public void enviarBoletaPorEmailAsync(Boleta boleta) {
        try {
            enviarBoletaPorEmail(boleta);
        } catch (Exception ex) {
            logger.error("Error enviando boleta por email", ex);
        }
    }

    private void enviarBoletaPorEmail(Boleta boleta) throws Exception {
        String destino = boleta.getCliente().getEmail();
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
        helper.setTo(destino);
        helper.setFrom(mailFrom);
        helper.setSubject("Tu boleta - Pedido #" + boleta.getId());

        Cliente cliente = boleta.getCliente();
        String direccionEntrega = obtenerDireccionEntrega(boleta, cliente);
        String regionComuna = obtenerRegionComuna(cliente);

        StringBuilder html = new StringBuilder();
        html.append("<h2>Gracias por tu compra</h2>");
        html.append("<p><b>Pedido #").append(boleta.getId()).append("</b> - ").append(boleta.getFecha()).append("</p>");

        html.append("<div style='margin-bottom: 20px; padding: 12px; border: 1px solid #ddd; border-radius: 8px;'>");
        html.append("<h3>Datos del cliente</h3>");
        html.append("<p><b>Nombre:</b> ").append(valorHtml(cliente != null ? cliente.getNombreCompleto() : "-" )).append("</p>");
        html.append("<p><b>Email:</b> ").append(valorHtml(cliente != null ? cliente.getEmail() : "-" )).append("</p>");
        html.append("<p><b>Teléfono:</b> ").append(valorHtml(cliente != null ? cliente.getNumero() : "-" )).append("</p>");
        html.append("</div>");

        html.append("<div style='margin-bottom: 20px; padding: 12px; border: 1px solid #ddd; border-radius: 8px;'>");
        html.append("<h3>Información de entrega</h3>");
        html.append("<p><b>Dirección:</b> ").append(valorHtml(direccionEntrega)).append("</p>");
        html.append("<p><b>Región / Comuna:</b> ").append(valorHtml(regionComuna)).append("</p>");
        html.append("<p><b>Método de pago:</b> ").append(valorHtml(boleta.getMetodoPago())).append("</p>");
        html.append("</div>");

        html.append("<h3>Resumen del pedido</h3>");
        html.append("<table border='1' cellpadding='6' cellspacing='0' style='border-collapse: collapse; width: 100%;'>");
        html.append("<tr style='background: #f2f2f2;'><th>Producto</th><th>Talla</th><th>Cantidad</th><th>Precio</th><th>Subtotal</th></tr>");
        boleta.getItems().forEach(i -> {
            html.append("<tr>");
            html.append("<td>").append(valorHtml(i.getNombreProducto())).append("</td>");
            html.append("<td>").append(valorHtml(i.getTalla())).append("</td>");
            html.append("<td>").append(i.getCantidad()).append("</td>");
            html.append("<td>$ ").append(String.format("%.2f", i.getPrecioUnitario())).append("</td>");
            html.append("<td>$ ").append(String.format("%.2f", i.getSubtotal())).append("</td>");
            html.append("</tr>");
        });
        html.append("</table>");
        html.append("<p style='margin-top: 16px;'><b>Subtotal: </b>$ ").append(String.format("%.2f", boleta.getSubtotal() != null ? boleta.getSubtotal() : 0.0)).append("</p>");
        html.append("<p><b>Impuestos: </b>$ ").append(String.format("%.2f", boleta.getImpuestos() != null ? boleta.getImpuestos() : 0.0)).append("</p>");
        html.append("<p><b>Total: </b>$ ").append(String.format("%.2f", boleta.getTotal() != null ? boleta.getTotal() : 0.0)).append("</p>");

        helper.setText(html.toString(), true);
        mailSender.send(msg);
        logger.info("Boleta enviada por email a {}", destino);
    }

    private String obtenerDireccionEntrega(Boleta boleta, Cliente cliente) {
        if (boleta != null && boleta.getDireccion() != null && !boleta.getDireccion().isBlank()) {
            return boleta.getDireccion();
        }
        if (cliente != null && cliente.getDireccion() != null && !cliente.getDireccion().isBlank()) {
            return cliente.getDireccion();
        }
        return "No especificada";
    }

    private String obtenerRegionComuna(Cliente cliente) {
        if (cliente == null) {
            return "No especificada";
        }
        String region = cliente.getRegion() != null ? cliente.getRegion().trim() : "";
        String comuna = cliente.getComuna() != null ? cliente.getComuna().trim() : "";
        if (region.isEmpty() && comuna.isEmpty()) {
            return "No especificada";
        }
        if (region.isEmpty()) {
            return comuna;
        }
        if (comuna.isEmpty()) {
            return region;
        }
        return region + " / " + comuna;
    }

    private String valorHtml(String valor) {
        if (valor == null || valor.isBlank()) {
            return "-";
        }
        return valor
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public void enviarBoletaDePrueba(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
        if (cliente == null || cliente.getEmail() == null || cliente.getEmail().isBlank()) {
            logger.warn("No se puede enviar boleta de prueba: cliente o email faltante (id={})", clienteId);
            return;
        }
        Boleta boleta = new Boleta();
        boleta.setCliente(cliente);
        ItemCarrito item = new ItemCarrito(null, 1L, "Producto de prueba", 1, 1000.0, "39");
        boleta.setItems(List.of(item));
        boleta.setImpuestos(0.0);
        boleta.setMetodoPago("Efectivo");
        boleta.setDireccion("Direccion de prueba");
        boleta.setFecha(LocalDateTime.now());
        boleta.setEstado("CREADA");
        boleta.calcularTotal();
        try {
            enviarBoletaPorEmail(boleta);
        } catch (Exception ex) {
            logger.error("Error enviando boleta de prueba", ex);
        }
    }

    public List<Boleta> listarTodas() {
        return boletaRepository.findAllByOrderByFechaDesc();
    }

    public List<Boleta> listarPorCliente(Long clienteId) {
        return boletaRepository.findByClienteId(clienteId);
    }

    public Boleta obtener(Long clienteId, Long boletaId) {
        return boletaRepository.findById(boletaId)
                .filter(b -> b.getCliente() != null && b.getCliente().getId().equals(clienteId))
                .orElseThrow(() -> new IllegalArgumentException("Boleta no encontrada para ese cliente."));
    }

    public Boleta obtenerPorId(Long boletaId) {
        return boletaRepository.findById(boletaId)
                .orElseThrow(() -> new IllegalArgumentException("Boleta no encontrada."));
    }
}
