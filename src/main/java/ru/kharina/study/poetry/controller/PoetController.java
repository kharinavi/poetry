package ru.kharina.study.poetry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kharina.study.poetry.dto.PoetDto;
import ru.kharina.study.poetry.model.Poem;
import ru.kharina.study.poetry.model.Poet;
import ru.kharina.study.poetry.service.PoetService;

import java.util.List;

@RestController
@RequestMapping("/poets")
public class PoetController {
    private final PoetService poetService;

    @Autowired
    public PoetController(PoetService poetService) {
        this.poetService = poetService;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('SCOPE_permission:min')")
    public List<PoetDto> getAllPoet() {
        return poetService.getAllDto();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_permission:min')")
    public PoetDto getPoetDtoById(@PathVariable long id) {
        PoetDto result = new PoetDto();
        if (poetService.isCurrentUserAuthorized(id))
            result = poetService.getPoetById(id);
        return result;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_permission:min')")
    public PoetDto updatePoet(@RequestBody PoetDto newPoet, @PathVariable long id) {
        PoetDto result = new PoetDto();
        if (poetService.isCurrentUserAuthorized(id))
            result = poetService.updatePoet(newPoet, id);
        return result;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_permission:user')")
    void deletePoet(@PathVariable long id) {
        if (poetService.isCurrentUserAuthorized(id))
            poetService.deletePoet(id);
    }

    @PostMapping("/{id}/addFavorite")
    @PreAuthorize("hasAuthority('SCOPE_permission:min')")
    void addFavorite(@PathVariable long id,
                     @RequestParam(required = false, name = "favorite") int favorite) {
        if (poetService.isCurrentUserAuthorized(id)) {
            if (favorite > 0) {
                poetService.addFavorite(id, favorite);
            }
        }
    }

}
