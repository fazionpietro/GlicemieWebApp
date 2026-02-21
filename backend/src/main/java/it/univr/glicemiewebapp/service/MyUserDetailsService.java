package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom implementation of UserDetailsService for Spring Security.
 * Loads user details from the database by email.
 */
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

  private final UtenteRepository userRepository;
  private final LogService logger;

  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<Utente> user = userRepository.findByEmailAddress(email);
    if (user.isEmpty()) {
      logger.error("User Not Found with username: " + email);
      throw new UsernameNotFoundException("User Not Found with username: " + email);
    }
    return new org.springframework.security.core.userdetails.User(
        user.get().getEmail(),
        user.get().getPasswordHash(),
        user.get().getAuthorities());
  }
}
