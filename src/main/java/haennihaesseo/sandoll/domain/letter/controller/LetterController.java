package haennihaesseo.sandoll.domain.letter.controller;

import haennihaesseo.sandoll.domain.letter.dto.ReceiveLetterResponse;
import haennihaesseo.sandoll.domain.letter.dto.OrderStatus;
import haennihaesseo.sandoll.domain.letter.service.LetterService;
import haennihaesseo.sandoll.domain.letter.status.LetterSuccessStatus;
import haennihaesseo.sandoll.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/letter")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;

    @GetMapping("/receiver")
    public ResponseEntity<ApiResponse<List<ReceiveLetterResponse>>> getReceiveLetters(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "status") OrderStatus orderStatus
    ) {
        List<ReceiveLetterResponse> response = letterService.getReceiveLetters(userId, orderStatus);
        return ApiResponse.success(LetterSuccessStatus.SUCCESS_201, response);
    }
}
