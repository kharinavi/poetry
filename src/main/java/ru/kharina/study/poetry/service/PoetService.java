package ru.kharina.study.poetry.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kharina.study.poetry.dto.PoemDto;
import ru.kharina.study.poetry.dto.PoetDto;
import ru.kharina.study.poetry.model.Poem;
import ru.kharina.study.poetry.model.Poet;
import ru.kharina.study.poetry.model.Status;
import ru.kharina.study.poetry.model.User;
import ru.kharina.study.poetry.repository.PoemRepository;
import ru.kharina.study.poetry.repository.PoetRepository;
import ru.kharina.study.poetry.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PoetService {
    private final PoetRepository poetRepository;
    private final PoemRepository poemRepository;
    private final UserRepository userRepository;

    public PoetService(PoetRepository poetRepository, PoemRepository poemRepository, UserRepository userRepository) {
        this.poetRepository = poetRepository;
        this.poemRepository = poemRepository;
        this.userRepository = userRepository;
    }

    private List<PoemDto> convertPoemListFromEntityToDto(List<Poem> poems) {
        List<PoemDto> result = new ArrayList<>();
        for (Poem poem : poems) {
            PoemDto dto = new PoemDto();
            dto.setId(poem.getId());
            dto.setName(poem.getName());
            dto.setText(poem.getText());
            result.add(dto);
        }
        return result;
    }

    public PoetDto convertEntityToDtoMin(Poet poet){
        PoetDto result = new PoetDto();
        result.setId(poet.getId());
        result.setEmail(poet.getEmail());
        result.setFirstName(poet.getFirstName());
        result.setLastName(poet.getLastName());
        result.setSecurityId(poet.getSecurityId());
        if (poet.getFavorites().isEmpty())
            result.setFavorites(new ArrayList<>());
        else
            result.setFavorites(convertPoemListFromEntityToDto(poet.getFavorites()));
        return result;
    }

    public PoetDto convertEntityToDto(Poet poet){
        PoetDto result = convertEntityToDtoMin(poet);
        result.setPoems(convertPoemListFromEntityToDto(poet.getPoems()));
        return result;
    }

    public Poet convertDtoToEntityMin(PoetDto dto){
        Poet result = new Poet();
        result.setId(dto.getId());
        result.setEmail(dto.getEmail());
        result.setFirstName(dto.getFirstName());
        result.setLastName(dto.getLastName());
        result.setSecurityId(dto.getSecurityId());
        result.setFavorites(convertPoemListFromDtoToEntity(dto.getFavorites()));
        return result;
    }

    public Poet convertDtoToEntity(PoetDto dto){
        Poet result = convertDtoToEntityMin(dto);
        result.setPoems(convertPoemListFromDtoToEntity(dto.getPoems()));
        return result;
    }

    public List<PoetDto> getAllDto(){
        return poetRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public PoetDto getPoetById(long id) {
        Poet result = poetRepository.getOne(id);
        PoetDto resDto = convertEntityToDto(result);
        return convertEntityToDto(poetRepository.getOne(id));
    }

    public Poet savePoet(PoetDto dto){
        return poetRepository.save(convertDtoToEntity(dto));
    }

    private List<Poem> convertPoemListFromDtoToEntity(List<PoemDto> dtoList) {
        List<Poem> result = new ArrayList<>();
        for (PoemDto dto : dtoList) {
            Poem poem = new Poem();
            poem.setId(dto.getId());
            poem.setName(dto.getName());
            poem.setText(dto.getText());
            result.add(poem);
        }
        return result;
    }

    public PoetDto updatePoet(PoetDto newDto, long id){
        PoetDto result = getPoetById(id);
        result.setId(id);
        result.setFirstName(newDto.getFirstName());
        result.setLastName(newDto.getLastName());
        result.setEmail(newDto.getEmail());
        result.setSecurityId(newDto.getSecurityId());
        result.setPoems(newDto.getPoems());
        result.setFavorites(newDto.getFavorites());
        poetRepository.save(convertDtoToEntity(result));
        return result;
    }

    public void deletePoet(long id){
        Poet currentUser = poetRepository.getOne(id);
        long secId = currentUser.getSecurityId();
        User securityUser = userRepository.getOne(secId);
        securityUser.setStatus(Status.BANNED);
        userRepository.save(securityUser);
    }

    public PoetDto addFavorite(long userId, long favoriteId) {
        Poet currentUser = poetRepository.getOne(userId);
        List<Poem> currentFavorites = currentUser.getFavorites();
        Poem newFavPoem = poemRepository.getOne(favoriteId);
        currentFavorites.add(newFavPoem);
        currentUser.setFavorites(currentFavorites);
        poetRepository.save(currentUser);
        return convertEntityToDto(currentUser);
    }

    public boolean isCurrentUserAuthorized(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int securityUserId = 0;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User currentUser = userRepository.findByEmail(currentUserName).get();
            securityUserId = currentUser.getId().intValue();
        }
        return (securityUserId == poetRepository.getOne(id).getSecurityId());
    }
}
