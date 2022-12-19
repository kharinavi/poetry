package ru.kharina.study.poetry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kharina.study.poetry.dto.PoemDto;
import ru.kharina.study.poetry.model.Poem;
import ru.kharina.study.poetry.service.PoemService;

import java.util.List;

@RestController
@RequestMapping("/poems")
public class PoemController {
    private final PoemService poemService;

    @Autowired
    public PoemController(PoemService poemService) {
        this.poemService = poemService;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('SCOPE_permission:min')")
    public List<PoemDto> getAllPoem(@RequestParam(required = false,name = "author") String author, 
                                    @RequestParam(required = false,name = "text") String text) {
        if (author != null && !author.isEmpty())
            return poemService.getAllDtoAuthor(author);
        else if (text != null && !text.isEmpty())
            return poemService.getAllDtoText(text);
        return poemService.getAllDto();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_permission:min')")
    public PoemDto getPoemDtoById(@PathVariable long id) {
        return poemService.getPoemById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_permission:poet')")
    public Poem updatePoem(@RequestBody PoemDto newPoem, @PathVariable long id) {
        Poem result = null;
        if (poemService.isCurrentUserAuthorized(id))
            result = poemService.updatePoem(newPoem, id);
        return result;
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SCOPE_permission:poet')")
    public Poem createPoem(@RequestBody PoemDto newPoem) {
        Poem result = null;
        if (poemService.isCurrentUserAuthorizedAcceptedToUpdate(newPoem))
            result = poemService.savePoem(newPoem);
        return result;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_permission:poet')")
    void deletePoem(@PathVariable long id) {
        if (poemService.isCurrentUserAuthorized(id))
            poemService.deletePoem(id);
    }


}
