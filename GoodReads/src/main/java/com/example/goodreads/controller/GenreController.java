package com.example.goodreads.controller;

import com.example.goodreads.model.dto.PageDTO;
import com.example.goodreads.model.dto.genreDTO.GenreResponseDTO;
import com.example.goodreads.services.GenreService;
import com.example.goodreads.services.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class GenreController extends BaseController {

    @Autowired
    private GenreService genreService;

    @PutMapping("/genres/react/{genre_id}")
    public ResponseEntity<GenreResponseDTO> reactOnGenre(@PathVariable long genre_id,
                                                         HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        GenreResponseDTO dto = genreService.reactOnGenre(genre_id, (long)session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/genres")
    public ResponseEntity<PageDTO> getAllGenres(@RequestParam int page, HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        Helper.validatePage(page);
        List<GenreResponseDTO> dtoList = genreService.getAllGenres();
        PageDTO pageDTO = Helper.createPage(dtoList, page);
        return ResponseEntity.ok(pageDTO);
    }

    @GetMapping("/genres/fav")
    public ResponseEntity<List<GenreResponseDTO>> getFavGenres(HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        List<GenreResponseDTO> dtoList = genreService.getFavGenres((long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(dtoList);
    }

}
