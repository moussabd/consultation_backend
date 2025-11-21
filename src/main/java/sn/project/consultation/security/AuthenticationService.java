package sn.project.consultation.security;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.AuthenticationRequest;
import sn.project.consultation.api.dto.AuthenticationResponse;
import sn.project.consultation.api.dto.RegisterRequest;
import sn.project.consultation.api.dto.UserDTO;
import sn.project.consultation.data.entities.*;
import sn.project.consultation.data.enums.RoleUser;
import sn.project.consultation.data.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        User user;

        switch (request.getRole()) {
            case PATIENT:
                Patient patient = new Patient();
                // Champs de base
                patient.setNom(request.getNom());
                patient.setPrenom(request.getPrenom());
                patient.setSexe(request.getSexe());
                patient.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
                patient.setRole(RoleUser.PATIENT);

                // Coordonnées
                Coordonnees coordPatient = new Coordonnees();
                coordPatient.setEmail(request.getEmail());
                coordPatient.setAdresse(request.getAdresse());
                coordPatient.setNumeroTelephone(request.getTelephone());
                patient.setCoordonnees(coordPatient);

                // Champs spécifiques Patient
                patient.setMatricule(request.getMatricule());
                patient.setLieuNaissance(request.getLieuNaissance());
                patient.setDateNaissance(LocalDate.parse(request.getDateNaissance()));
                patient.setSituationFamiliale(request.getSituationFamiliale());
                patient.setLatitude(request.getLatitude());
                patient.setLongitude(request.getLongitude());

                user = patient;
                break;

            case PRO_SANTE:
                ProSante pro = new ProSante();
                // Champs de base
                pro.setNom(request.getNom());
                pro.setPrenom(request.getPrenom());
                pro.setSexe(request.getSexe());
                pro.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
                pro.setRole(RoleUser.PRO_SANTE);

                // Coordonnées
                Coordonnees coordPro = new Coordonnees();
                coordPro.setEmail(request.getEmail());
                coordPro.setAdresse(request.getAdresse());
                coordPro.setNumeroTelephone(request.getTelephone());
                pro.setCoordonnees(coordPro);

                // Champs spécifiques ProSante
                pro.setSpecialite(request.getSpecialite());
                pro.setDescription(request.getDescription());
                pro.setTarif(request.getTarif());
                pro.setLatitude(request.getLatitude());
                pro.setLongitude(request.getLongitude());

                user = pro;
                break;

            default:
                throw new IllegalArgumentException("Rôle non pris en charge : " + request.getRole());
        }

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(new CustomUserDetails(user));

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(UserDTO.fromEntity(user)) // Conversion entité → DTO
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );
        User user = userRepository.findByEmail(request.getEmail());
        System.out.println("Nom "+user.getNom());
        System.out.println("Prénom "+user.getPrenom());
        String jwtToken = jwtService.generateToken(new CustomUserDetails(user));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(UserDTO.fromEntity(user)) // Conversion entité → DTO
                .build();
    }

    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Aucun utilisateur connecté");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        System.out.println(userDetails);
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return UserDTO.fromEntity(user);
    }

}
