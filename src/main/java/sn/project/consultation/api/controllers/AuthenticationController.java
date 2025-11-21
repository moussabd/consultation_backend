package sn.project.consultation.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.AuthenticationRequest;
import sn.project.consultation.api.dto.AuthenticationResponse;
import sn.project.consultation.api.dto.RegisterRequest;
import sn.project.consultation.api.dto.UserDTO;
import sn.project.consultation.data.entities.User;
import sn.project.consultation.security.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name="Authentification", description = "Endpoints d'inscription et de connexion")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Inscription d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscription réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(summary = "Connexion de l'utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie"),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        System.out.println(authenticationService.getCurrentUser());
        return ResponseEntity.ok(authenticationService.getCurrentUser());
    }

}
