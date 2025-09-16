package it.univr.glicemiewebapp.service;

import org.springframework.stereotype.Service;

import it.univr.glicemiewebapp.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService{
    private final MessaggioRepository messaggioRepository;
    private final UtenteRepository utenteRepository;
    
    @Autowired
    private LogService logger;

    public MessaggioService(MessaggioRepository messaggioRepository,
    UtenteRepository utenteRepository){
        this.messaggioRepository=messaggioRepository;
        this.utenteRepository= utenteRepository;
    }

    @Transactional
    public ResponseEntity<String> salvaMessaggio(MessaggioDTO messaggioDTO){
        try{
            Utente mittente = utenteRepository.findById(messaggioDTO.getIdMittente())
            .orElsethrow(()->new ResponseStatusException(HTTPStatus.NOT_FOUND, "non trovato"));

            Utente destinatario = utenteRepository.findById(messaggioDTO.getIdDestinatario())
            .orElseThrow(()->new ResponseStatusException(HTTPStatus.NOT_FOUND, "non trovato"));

            Messaggio messaggio = new Messaggio();
            messaggio.setSoggetto(MessaggioDTO.getSoggetto());
            messaggio.setMessaggio(messaggioDTO.getmessaggio());
            messaggio.setMittente(mittente);
            messaggio.setDestinatatio(destinatario);
            messaggio.setLetto(false);

            messaggioRepository.save(messaggio);

            return new ResponsEntity<>("Messaggio Salvato", HttpStatus.CREATED);
        }catch(Exception e){
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "errore durante il salvataggio");
        }
    }
}
