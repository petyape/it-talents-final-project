package com.example.goodreads.services;

import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.model.dto.genreDTO.GenreResponseDTO;
import com.example.goodreads.model.entities.Genre;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.GenreRepository;
import com.example.goodreads.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;

    public GenreResponseDTO reactOnGenre(long genreId, long userId) {
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> (new NotFoundException("Genre not found!")));
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));

        Set<Genre> favouriteGenres = user.getFavoriteGenres();
        if (favouriteGenres.contains(genre)) {
            // Remove from favourites
            favouriteGenres.remove(genre);
        } else {
            // Add to favourites
            favouriteGenres.add(genre);
        }
        user.setFavoriteGenres(favouriteGenres);
        userRepository.save(user);
        return mapper.map(genre, GenreResponseDTO.class);
    }

    public List<GenreResponseDTO> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        List<GenreResponseDTO> dtoList = new ArrayList<>();
        for (Genre genre : genres) {
            dtoList.add(mapper.map(genre, GenreResponseDTO.class));
        }
        return dtoList;
    }

    public List<GenreResponseDTO> getFavGenres(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        Set<Genre> genres = user.getFavoriteGenres();
        List<GenreResponseDTO> dtoList = new ArrayList<>();
        for (Genre genre : genres) {
            dtoList.add(mapper.map(genre, GenreResponseDTO.class));
        }
        return dtoList;
    }

}
