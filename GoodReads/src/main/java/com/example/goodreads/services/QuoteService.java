package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.exceptions.UnauthorizedException;
import com.example.goodreads.model.dto.quoteDTO.AddQuoteDTO;
import com.example.goodreads.model.dto.quoteDTO.QuoteResponseDTO;
import com.example.goodreads.model.entities.Author;
import com.example.goodreads.model.entities.Quote;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.AuthorRepository;
import com.example.goodreads.model.repository.QuoteRepository;
import com.example.goodreads.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ModelMapper mapper;

    @Transactional
    public QuoteResponseDTO addQuote(AddQuoteDTO quoteDTO, long userId) {
        if (quoteDTO == null || quoteDTO.getQuote() == null || quoteDTO.getQuote().isBlank()) {
            throw new BadRequestException("Invalid quote parameters provided!");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        Quote quote = Quote.builder()
                .quote("\"" + quoteDTO.getQuote() + "\"")
                .tags(quoteDTO.getTags()).user(user).build();

        if (quoteDTO.getAuthor() != null) {
            String quoteAuthorName = quoteDTO.getAuthor().getAuthorName();
            long authorId = quoteDTO.getAuthor().getAuthorId();
            Optional<Author> optionalAuthor = Optional.empty();

            if (quoteAuthorName != null) {
                optionalAuthor = authorRepository.findByAuthorNameAndAuthorId(quoteAuthorName, authorId);
            }
            if (optionalAuthor.isEmpty()) {
                optionalAuthor = authorRepository.findById(authorId);
                if (optionalAuthor.isEmpty() && quoteAuthorName != null) {
                    optionalAuthor = authorRepository.findByAuthorName(quoteAuthorName);
                }
            }

            if (optionalAuthor.isPresent()) {
                    quote.setAuthor(optionalAuthor.get());
            } else {
                if (quoteAuthorName != null) {
                    Author newAuthor = addNewAuthor(quoteAuthorName);
                    quote.setAuthor(newAuthor);
                }
            }
        }
        return mapper.map(quoteRepository.save(quote), QuoteResponseDTO.class);
    }

    private Author addNewAuthor(String name) {
        Author newAuthor = new Author();
        newAuthor.setAuthorName(name.trim());
        return authorRepository.save(newAuthor);
    }

    public QuoteResponseDTO reactOnQuote(long quoteId, long userId) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(() -> (new NotFoundException("Quote not found!")));
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));

        Set<Quote> likedQuotes = user.getFavoriteQuotes();
        if (likedQuotes.contains(quote)) {
            // Dislike
            likedQuotes.remove(quote);
        } else {
            // Like
            likedQuotes.add(quote);
        }
        user.setFavoriteQuotes(likedQuotes);
        userRepository.save(user);
        return mapper.map(quote, QuoteResponseDTO.class);
    }

    public List<QuoteResponseDTO> getAllQuotes() {
        List<Quote> quotes = quoteRepository.findAll();
        List<QuoteResponseDTO> dtoList = new ArrayList<>();
        for (Quote quote : quotes) {
            dtoList.add(mapper.map(quote, QuoteResponseDTO.class));
        }
        return dtoList;
    }

    public List<QuoteResponseDTO> getFavQuotes(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        Set<Quote> quotes = user.getFavoriteQuotes();
        List<QuoteResponseDTO> dtoList = new ArrayList<>();
        for (Quote quote : quotes) {
            dtoList.add(mapper.map(quote, QuoteResponseDTO.class));
        }
        return dtoList;
    }

    public List<QuoteResponseDTO> getMyQuotes(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        Set<Quote> quotes = user.getQuotes();
        List<QuoteResponseDTO> dtoList = new ArrayList<>();
        for (Quote quote : quotes) {
            dtoList.add(mapper.map(quote, QuoteResponseDTO.class));
        }
        return dtoList;
    }

    public QuoteResponseDTO deleteQuote(long quoteId, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(() -> (new NotFoundException("Quote not found!")));
        if (quote.getUser() != user) {
            throw new UnauthorizedException("Quotes can be deleted by their publishers only!");
        }
        quoteRepository.delete(quote);
        QuoteResponseDTO dto = mapper.map(quote, QuoteResponseDTO.class);
        return dto;
    }

}
