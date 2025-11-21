package sn.project.consultation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sn.project.consultation.data.entities.CustomUserDetails;
import sn.project.consultation.data.entities.User;
import sn.project.consultation.data.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        sn.project.consultation.data.entities.User user = userRepo.findByEmail(username);
        return new CustomUserDetails(user);
    }
}
