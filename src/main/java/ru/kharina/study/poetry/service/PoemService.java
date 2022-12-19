package ru.kharina.study.poetry.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kharina.study.poetry.dto.PoemDto;
import ru.kharina.study.poetry.dto.PoetDto;
import ru.kharina.study.poetry.model.Poem;
import ru.kharina.study.poetry.model.Poet;
import ru.kharina.study.poetry.model.User;
import ru.kharina.study.poetry.repository.PoemRepository;
import ru.kharina.study.poetry.repository.PoetRepository;
import ru.kharina.study.poetry.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PoemService {
    private final PoemRepository poemRepository;
    private final PoetRepository poetRepository;
    private final UserRepository userRepository;

    public PoemService(PoemRepository poemRepository,
                       PoetRepository poetRepository,
                       UserRepository userRepository) {
        this.poemRepository = poemRepository;
        this.poetRepository = poetRepository;
        this.userRepository = userRepository;
    }

    public PoemDto convertEntityToDto(Poem poem){
        PoemDto result = new PoemDto();
        result.setId(poem.getId());
        result.setAuthor(convertAuthorEntityToDto(poem.getAuthor()));
        result.setName(poem.getName());
        result.setText(poem.getText());
        return result;
    }

    private PoetDto convertAuthorEntityToDto(Poet author) {
        PoetDto result = new PoetDto();
        result.setFirstName(author.getFirstName());
        result.setLastName(author.getLastName());
        result.setId(author.getId());
        return result;
    }

    private Poet convertAuthorDtoToEntity(PoetDto dto) {
        Poet result = new Poet();
        result.setFirstName(dto.getFirstName());
        result.setLastName(dto.getLastName());
        result.setId(dto.getId());
        return result;
    }

    public Poem convertDtoToEntity(PoemDto dto){
        Poem result = new Poem();
        result.setId(dto.getId());
        result.setAuthor(convertAuthorDtoToEntity(dto.getAuthor()));
        result.setName(dto.getName());
        result.setText(dto.getText());
        return result;
    }

    public List<PoemDto> getAllDto(){
        return poemRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public PoemDto getPoemById(long id) {
        return convertEntityToDto(poemRepository.getOne(id));
    }

    public Poem savePoem(PoemDto dto){
        return poemRepository.save(convertDtoToEntity(dto));
    }

    public Poem updatePoem(PoemDto newDto, long id){
        Poem result = poemRepository.getOne(id);
        result.setId(id);
        result.setAuthor(convertAuthorDtoToEntity(newDto.getAuthor()));
        result.setName(newDto.getName());
        result.setText(newDto.getText());
        result.setAuthor(convertAuthorDtoToEntity(newDto.getAuthor()));
        poemRepository.save(result);
        return result;
    }

    public void deletePoem(long id){
        poemRepository.deleteById(id);
    }


    public List<PoemDto> getAllDtoText(String text) {
        List<Poem> entityList = poemRepository.findAll().stream()
                .filter(poem -> poem.getText().contains(text))
                .collect(Collectors.toList());
        return entityList.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public List<PoemDto> getAllDtoAuthor(String author) {
        List<Poem> entityList = poemRepository.findAll().stream()
                .filter(poem -> poem.getAuthor().getLastName().contains(author))
                .collect(Collectors.toList());
        return entityList.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private int getSecurityUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int securityUserId = 0;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User currentUser = userRepository.findByEmail(currentUserName).get();
            securityUserId = currentUser.getId().intValue();
        }
        return securityUserId;
    }

    public boolean isCurrentUserAuthorized(long id) {
        int securityUserId = getSecurityUserId();
        return (securityUserId == poemRepository.getOne(id).getAuthor().getSecurityId());
    }

    public boolean isCurrentUserAuthorizedAcceptedToUpdate(PoemDto newPoem) {
        int securityUserId = getSecurityUserId();
        Poet author = poetRepository.getReferenceById(newPoem.getAuthor().getId());
        return (securityUserId == author.getSecurityId());
    }
}
