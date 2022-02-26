package com.example.goodreads.controller;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.model.dto.PageDTO;
import com.example.goodreads.model.dto.ratingDTO.RateBookDTO;
import com.example.goodreads.model.dto.ratingDTO.RatingResponseDTO;
import com.example.goodreads.model.dto.ratingDTO.RatingWithUserDTO;
import com.example.goodreads.model.dto.ratingDTO.UserRatingsResponseDTO;
import com.example.goodreads.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@Validated
public class RatingController extends BaseController {

    @Autowired
    private RatingService ratingService;

    @PostMapping("/books/rate")
    public ResponseEntity<RatingResponseDTO> rateBook(@RequestBody RateBookDTO ratingDTO,
                                                      HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        RatingResponseDTO dto = ratingService.rateBook(ratingDTO, (long)session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/books/ratings/{id}")
    public ResponseEntity<PageDTO> getBookRatings(@RequestParam
                                                 @NotBlank (message = "Required page number must be positive!") int page,
                                                  @PathVariable long id, HttpSession session) {
        validateSession(session);
//        if  (page <= 0) {
//            throw new BadRequestException("Invalid page parameter!");
//        }
        List<RatingWithUserDTO> responseList = ratingService.getBookRatings(id);

        // Creation
        PagedListHolder pageHolder = new PagedListHolder(responseList);
        pageHolder.setPageSize(2); // number of items per page

        if (page - 1 > pageHolder.getPageCount()) {
            throw new BadRequestException("Invalid page parameter!");
        }
        pageHolder.setPage(page - 1);      // set to first page

        // Retrieval
        //page.getPageCount(); // number of pages
        //page.getPageList();

        PageDTO dto = new PageDTO(page - 1, pageHolder.getPageCount(), pageHolder.getPageList());
        return ResponseEntity.ok(dto);
    }


//    @GetMapping("/books/ratings/{id}")
//    public ResponseEntity<List<RatingWithUserDTO>> getBookRatings(@PathVariable long id, HttpSession session) {
//        validateSession(session);
//        List<RatingWithUserDTO> responseList = ratingService.getBookRatings(id);
//        return ResponseEntity.ok(responseList);
//    }

    @GetMapping("/users/ratings/{id}")
    public ResponseEntity<List<UserRatingsResponseDTO>> getUserRatings(@PathVariable long id, HttpSession session) {
        validateSession(session);
        List<UserRatingsResponseDTO> responseList = ratingService.getUserRatings(id);
        return ResponseEntity.ok(responseList);
    }

}
