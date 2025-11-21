package sn.project.consultation.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sn.project.consultation.data.entities.User;
import sn.project.consultation.data.repositories.UserRepository;

import java.util.List;

//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepo;
//
////    @Override
////    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
////        User user = userRepo.findByEmail(email)
////                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
////
////        return new org.springframework.security.core.userdetails.User(
////                user.getEmail(),
////                user.getMotDePasse(),
////                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
////        );
////    }
//}
