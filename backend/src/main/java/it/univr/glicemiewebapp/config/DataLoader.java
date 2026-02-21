package it.univr.glicemiewebapp.config;

import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import it.univr.glicemiewebapp.repository.TerapiaRepository;
import it.univr.glicemiewebapp.repository.AssunzioneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UtenteRepository utenteRepository,
            PazienteRepository pazienteRepository,
            TerapiaRepository terapiaRepository,
            AssunzioneRepository assunzioneRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {

            // Admin
            if (utenteRepository.findByEmailAddress("admin@test.com").isEmpty()) {
                Utente admin = Utente.builder()
                        .email("admin@test.com")
                        .passwordHash(passwordEncoder.encode("password"))
                        .nome("Admin")
                        .cognome("User")
                        .dataNascita(LocalDate.of(1980, 1, 1))
                        .ruolo("ROLE_ADMIN")
                        .build();
                utenteRepository.save(admin);
            }

            // Medico
            Utente medico = utenteRepository.findByEmailAddress("medico@test.com").orElse(null);
            if (medico == null) {
                medico = Utente.builder()
                        .email("medico@test.com")
                        .passwordHash(passwordEncoder.encode("password"))
                        .nome("Dottor")
                        .cognome("Rossi")
                        .dataNascita(LocalDate.of(1975, 5, 15))
                        .ruolo("ROLE_MEDICO")
                        .build();
                medico = utenteRepository.save(medico);
            }

            // Paziente
            if (utenteRepository.findByEmailAddress("paziente@test.com").isEmpty()) {
                Paziente paziente = new Paziente(
                        "paziente@test.com",
                        passwordEncoder.encode("password"),
                        "Mario",
                        "Bianchi",
                        LocalDate.of(1990, 10, 20),
                        "Nessuno",
                        "Nessuna",
                        "Nessuna");
                paziente.setIdMedico(medico);
                pazienteRepository.save(paziente);
            }

            // Paziente Non Adempiente
            if (utenteRepository.findByEmailAddress("paziente.na@test.com").isEmpty()) {
                Paziente pazienteNa = new Paziente(
                        "paziente.na@test.com",
                        passwordEncoder.encode("password"),
                        "Luigi",
                        "Verdi",
                        LocalDate.of(1985, 3, 10),
                        "Ipertensione",
                        "Diabete Tipo 2",
                        "Nessuna");
                pazienteNa.setIdMedico(medico);
                pazienteRepository.save(pazienteNa);

                // Crea una terapia per il paziente non adempiente
                it.univr.glicemiewebapp.entity.Terapia terapia = it.univr.glicemiewebapp.entity.Terapia.builder()
                        .farmaco("Metformina")
                        .numAssunzioni(2)
                        .dosaggio("500mg")
                        .indicazioni("Dopo i pasti")
                        .idPaziente(pazienteNa)
                        .medicoCurante(medico)
                        .build();
                terapiaRepository.save(terapia);

                // Crea assunzioni vecchie (fino a 4 giorni fa) per simulare non adempienza (> 3
                // giorni senza assunzioni)
                // Assumiamo che abbia preso il farmaco regolarmente fino a 4 giorni fa
                java.time.Instant now = java.time.Instant.now();
                java.time.Instant fourDaysAgo = now.minus(4, java.time.temporal.ChronoUnit.DAYS);

                // Un'assunzione 4 giorni fa
                it.univr.glicemiewebapp.entity.Assunzione assunzioneVecchia = it.univr.glicemiewebapp.entity.Assunzione
                        .builder()
                        .idTerapia(terapia)
                        .timestamp(fourDaysAgo)
                        .build();
                assunzioneRepository.save(assunzioneVecchia);

                // Un'altra assunzione 5 giorni fa
                it.univr.glicemiewebapp.entity.Assunzione assunzionePiuVecchia = it.univr.glicemiewebapp.entity.Assunzione
                        .builder()
                        .idTerapia(terapia)
                        .timestamp(now.minus(5, java.time.temporal.ChronoUnit.DAYS))
                        .build();
                assunzioneRepository.save(assunzionePiuVecchia);
            }
        };
    }
}
