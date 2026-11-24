package com.GestionSNKR.SnearksSource.service;

import com.GestionSNKR.SnearksSource.DTO.GoogleLoginResponse;
import com.GestionSNKR.SnearksSource.exception.GoogleAuthException;
import com.GestionSNKR.SnearksSource.exception.GoogleConfigurationException;
import com.GestionSNKR.SnearksSource.exception.GoogleConflictException;
import com.GestionSNKR.SnearksSource.model.Cliente;
import com.GestionSNKR.SnearksSource.repository.ClienteRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
public class GoogleAuthService {
    private final ClienteRepository repository;
    private final String clientId;

    public GoogleAuthService(ClienteRepository repository,
                             @Value("${google.oauth.client-id:}") String clientId) {
        this.repository = repository;
        this.clientId = clientId == null ? "" : clientId.trim();
    }

    public GoogleLoginResponse login(String credential) {
        if (clientId.isBlank()) {
            throw new GoogleConfigurationException();
        }

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(credential);
            if (idToken == null) {
                throw new GoogleAuthException();
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String issuer = payload.getIssuer();
            if (!("accounts.google.com".equals(issuer)
                    || "https://accounts.google.com".equals(issuer))) {
                throw new GoogleAuthException();
            }

            Object audience = payload.getAudience();
            if (!(audience instanceof String) || !audience.equals(clientId)) {
                throw new GoogleAuthException();
            }

            String sub = payload.getSubject();
            String email = payload.getEmail();
            if (sub == null || sub.isBlank() || email == null || email.isBlank()) {
                throw new GoogleAuthException();
            }
            if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
                throw new GoogleAuthException();
            }

            email = email.trim().toLowerCase();
            String name = payload.get("name") instanceof String
                    ? ((String) payload.get("name")).trim()
                    : "";
            if (name.isBlank()) {
                name = email.substring(0, email.indexOf('@') > 0
                        ? email.indexOf('@') : email.length());
            }

            Cliente cliente = resolveUser(sub, email, name);
            String firstName = name.contains(" ") ? name.substring(0, name.indexOf(' ')) : name;
            return new GoogleLoginResponse(cliente.getId(), firstName,
                    cliente.getNombreCompleto(), cliente.getEmail(), cliente.getRol());
        } catch (GoogleAuthException | GoogleConflictException e) {
            throw e;
        } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            throw new GoogleAuthException();
        } catch (Exception e) {
            throw new GoogleAuthException();
        }
    }

    private Cliente resolveUser(String sub, String email, String name) {
        Optional<Cliente> bySub = repository.findByGoogleSub(sub);
        if (bySub.isPresent()) {
            Cliente cliente = bySub.get();
            Optional<Cliente> byEmail = repository.findByEmail(email);
            if (byEmail.isPresent() && !byEmail.get().getId().equals(cliente.getId())) {
                throw new GoogleConflictException();
            }
            cliente.setEmail(email);
            cliente.setNombreCompleto(name);
            cliente.setRol("CLIENTE");
            return repository.save(cliente);
        }

        Optional<Cliente> byEmail = repository.findByEmail(email);
        if (byEmail.isPresent()) {
            Cliente cliente = byEmail.get();
            if (cliente.getGoogleSub() != null && !cliente.getGoogleSub().isBlank()
                    && !cliente.getGoogleSub().equals(sub)) {
                throw new GoogleConflictException();
            }
            cliente.setGoogleSub(sub);
            if (cliente.getNombreCompleto() == null || cliente.getNombreCompleto().isBlank()) {
                cliente.setNombreCompleto(name);
            }
            cliente.setRol("CLIENTE");
            return repository.save(cliente);
        }

        Cliente cliente = new Cliente();
        cliente.setNombreCompleto(name);
        cliente.setEmail(email);
        cliente.setGoogleSub(sub);
        cliente.setContrasena(null);
        cliente.setRol("CLIENTE");
        return repository.save(cliente);
    }
}
