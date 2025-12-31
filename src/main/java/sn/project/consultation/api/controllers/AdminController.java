package sn.project.consultation.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import sn.project.consultation.api.dto.*;
import sn.project.consultation.data.entities.*;
import sn.project.consultation.data.enums.RoleUser;
import sn.project.consultation.data.repositories.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
// @PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Administration", description = "Endpoints d'administration des utilisateurs")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Récupérer tous les utilisateurs avec pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> userPage;

        if (search != null && !search.trim().isEmpty()) {
            // Recherche dans nom, prénom et email
            userPage = userRepository.findBySearchTerm(search.toLowerCase(), pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        List<AdminUserDTO> userDTOs = userPage.getContent().stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("content", userDTOs);
        response.put("currentPage", userPage.getNumber());
        response.put("totalItems", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());
        response.put("size", userPage.getSize());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Récupérer les statistiques des utilisateurs")
    @GetMapping("/stats")
    public ResponseEntity<UserStatsDTO> getUserStats() {
        long totalUsers = userRepository.count();
        long patients = userRepository.countByRole(RoleUser.PATIENT);
        long pros = userRepository.countByRole(RoleUser.PRO_SANTE);
        long admins = userRepository.countByRole(RoleUser.ADMIN);
        long activeUsers = userRepository.countByEnabledTrue();

        UserStatsDTO stats = UserStatsDTO.builder()
                .totalUsers(totalUsers)
                .patients(patients)
                .pros(pros)
                .admins(admins)
                .activeUsers(activeUsers)
                .build();

        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Modifier le statut d'un utilisateur")
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody Map<String, Boolean> request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));

        Boolean enabled = request.get("enabled");
        if (enabled == null) {
            return ResponseEntity.badRequest().body("Le champ 'enabled' est requis");
        }

        user.setEnabled(enabled);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Modifier le rôle d'un utilisateur")
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));

        String roleStr = request.get("role");
        if (roleStr == null) {
            return ResponseEntity.badRequest().body("Le champ 'role' est requis");
        }

        try {
            RoleUser newRole = RoleUser.valueOf(roleStr);
            user.setRole(newRole);
            userRepository.save(user);

            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Rôle invalide: " + roleStr);
        }
    }

    @Operation(summary = "Supprimer un utilisateur")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Créer un nouvel utilisateur")
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest baseRequest) {
        try {
            // Validation de base
            if (baseRequest.getEmail() == null || baseRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("L'email est obligatoire");
            }

            if (baseRequest.getRole() == null) {
                return ResponseEntity.badRequest().body("Le rôle est obligatoire");
            }

            // Vérifier si l'email existe déjà
            if (userRepository.existsByEmail(baseRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un utilisateur avec cet email existe déjà");
            }

            User user;

            switch (baseRequest.getRole()) {
                case PATIENT:
                    if (!(baseRequest instanceof CreatePatientRequest)) {
                        return ResponseEntity.badRequest()
                                .body("Les données spécifiques au patient sont requises");
                    }
                    user = createPatient((CreatePatientRequest) baseRequest);
                    break;

                case PRO_SANTE:
                    if (!(baseRequest instanceof CreateProSanteRequest)) {
                        return ResponseEntity.badRequest()
                                .body("Les données spécifiques au professionnel de santé sont requises");
                    }
                    user = createProSante((CreateProSanteRequest) baseRequest);
                    break;

                case ADMIN:
                    if (!(baseRequest instanceof CreateAdminRequest)) {
                        return ResponseEntity.badRequest()
                                .body("Les données spécifiques à l'administrateur sont requises");
                    }
                    user = createAdmin((CreateAdminRequest) baseRequest);
                    break;

                default:
                    return ResponseEntity.badRequest().body("Rôle non supporté: " + baseRequest.getRole());
            }

            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(convertToAdminDTO(savedUser));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création de l'utilisateur: " + e.getMessage());
        }
    }

    private Patient createPatient(CreatePatientRequest request) {
        Patient patient = new Patient();
        setCommonUserAttributes(patient, request);

        // Champs spécifiques au Patient
        patient.setMatricule(generateMatricule());
        patient.setLieuNaissance(request.getLieuNaissance());
        patient.setDateNaissance(request.getDateNaissance());
        patient.setSituationFamiliale(request.getSituationFamiliale());
        patient.setLatitude(request.getLatitude());
        patient.setLongitude(request.getLongitude());

        return patient;
    }

    private String generateMatricule() {
        // Générer un matricule unique (ex: PAT202400001)
        String prefix = "PAT";
        String year = String.valueOf(java.time.Year.now().getValue());

        // Compter les patients existants pour cette année
        long patientCount = userRepository.countByRoleAndYear(RoleUser.PATIENT, java.time.Year.now().getValue());

        String sequence = String.format("%05d", patientCount + 1);
        return prefix + year + sequence;
    }

    private ProSante createProSante(CreateProSanteRequest request) {
        ProSante proSante = new ProSante();
        setCommonUserAttributes(proSante, request);

        // Champs spécifiques au ProSante
        proSante.setSpecialite(request.getSpecialite());
        proSante.setDescription(request.getDescription());
        proSante.setTarif(request.getTarif());
        proSante.setLatitude(request.getLatitude());
        proSante.setLongitude(request.getLongitude());


        return proSante;
    }

    private Admin createAdmin(CreateAdminRequest request) {
        Admin admin = new Admin();
        setCommonUserAttributes(admin, request);

        // Champs spécifiques à l'Admin
        admin.setNiveauAcces(request.getNiveauAcces() != null ? request.getNiveauAcces() : "COMPLET");
        admin.setEstSuperAdmin(request.getEstSuperAdmin() != null ? request.getEstSuperAdmin() : false);
        admin.setDepartement(request.getDepartement());

        return admin;
    }

    private void setCommonUserAttributes(User user, CreateUserRequest request) {
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setSexe(request.getSexe());
        user.setRole(request.getRole());
        user.setEnabled(true);

        // Générer un mot de passe par défaut
        String defaultPassword = generateTemporaryPassword();
        user.setMotDePasse(passwordEncoder.encode(defaultPassword));

        // Coordonnées
        Coordonnees coordonnees = new Coordonnees();
        coordonnees.setEmail(request.getEmail());
        coordonnees.setNumeroTelephone(request.getTelephone());
        coordonnees.setAdresse(request.getAdresse());
        user.setCoordonnees(coordonnees);
    }

    private String generateTemporaryPassword() {
        // Générer un mot de passe temporaire plus sécurisé
        return "Temp" + UUID.randomUUID().toString().substring(0, 8) + "!";
    }


    private AdminUserDTO convertToAdminDTO(User user) {
        return AdminUserDTO.builder()
                .id(user.getId())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .email(user.getCoordonnees() != null ? user.getCoordonnees().getEmail() : null)
                .role(user.getRole())
                .enabled(user.isEnabled())
                .coordonnees(user.getCoordonnees())
                .dateCreation(user.getDateCreation())
                .build();
    }
}