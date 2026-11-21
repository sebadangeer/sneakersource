package com.GestionSNKR.SnearksSource.service;

import com.GestionSNKR.SnearksSource.model.Cliente;
import com.GestionSNKR.SnearksSource.repository.ClienteRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }

    public List<Cliente> findAll() { return repo.findAll(); }

    public Optional<Cliente> findById(Long id) { return repo.findById(id); }

    public Cliente save(Cliente c) {
        normalizeDefaults(c);
        hashPasswordIfNeeded(c);
        return repo.save(c);
    }

    public Cliente update(Long id, Cliente c) {
        c.setId(id);
        normalizeDefaults(c);
        hashPasswordIfNeeded(c);
        return repo.save(c);
    }

    private void normalizeDefaults(Cliente cliente) {
        if (cliente.getRol() == null || cliente.getRol().isBlank()) {
            cliente.setRol("CLIENTE");
        } else {
            cliente.setRol(cliente.getRol().trim().toUpperCase());
        }
    }

    private void hashPasswordIfNeeded(Cliente cliente) {
        String password = cliente.getContrasena();
        if (password != null && !password.isBlank()
                && !password.startsWith("$2a$") && !password.startsWith("$2b$")
                && !password.startsWith("$2y$")) {
            cliente.setContrasena(passwordEncoder.encode(password));
        }
    }

    public void deleteById(Long id) { repo.deleteById(id); }

    // Método para buscar al cliente por correo
    public Optional<Cliente> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public Optional<Cliente> login(String email, String contrasena) {
        Optional<Cliente> clienteOpt = repo.findByEmail(email);

        if (clienteOpt.isEmpty()) {
            System.out.println("DEBUG: No se encontró ningún cliente con el correo: " + email);
            return Optional.empty();
        }

        Cliente cliente = clienteOpt.get();
        if (cliente.getContrasena() == null) {
            return Optional.empty();
        }

        boolean matches;
        if (cliente.getContrasena().startsWith("$2") ) {
            matches = passwordEncoder.matches(contrasena, cliente.getContrasena());
        } else {
            matches = cliente.getContrasena().equals(contrasena);
        }
        if (!matches) {
            return Optional.empty();
        }

        return Optional.of(cliente);
    }
}
