package haennihaesseo.sandoll.domain.letter.service;

import haennihaesseo.sandoll.domain.letter.dto.ReceiveLetterResponse;
import haennihaesseo.sandoll.domain.letter.dto.OrderStatus;
import haennihaesseo.sandoll.domain.letter.entity.Letter;
import haennihaesseo.sandoll.domain.letter.status.LetterErrorStatus;
import haennihaesseo.sandoll.domain.letter.exception.LetterException;
import haennihaesseo.sandoll.domain.letter.repository.LetterRepository;
import haennihaesseo.sandoll.domain.letter.repository.ReceiverLetterRepository;
import haennihaesseo.sandoll.domain.user.entity.User;
import haennihaesseo.sandoll.domain.user.repository.UserRepository;
import haennihaesseo.sandoll.global.exception.GlobalException;
import haennihaesseo.sandoll.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LetterService {

    private final LetterRepository letterRepository;
    private final ReceiverLetterRepository receiverLetterRepository;
    private final UserRepository userRepository;

    public List<ReceiveLetterResponse> getReceiveLetters(Long userId, OrderStatus status) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.USER_NOT_FOUND));

        log.info("userId = " + userId);

        List<Long> letterIds = (status.equals(OrderStatus.EARLIEST))
                ? receiverLetterRepository.findIdLetterIdByIdReceiverIdOrderByCreatedAtDesc(userId)
                : receiverLetterRepository.findIdLetterIdByIdReceiverIdOrderByCreatedAtAsc(userId);

        log.info("letterIds 받음");
        List<ReceiveLetterResponse> results = new ArrayList<>();

        for (Long letterId : letterIds) {
            Letter letter = letterRepository.findById(letterId)
                    .orElseThrow(() -> new LetterException(LetterErrorStatus.LETTER_NOT_FOUND));

            results.add(
                    ReceiveLetterResponse.builder()
                            .letterId(letterId)
                            .sender(letter.getSenderName())
                            .createdAt(letter.getCreatedAt().toLocalDate())
                            .build()
            );
        }

        return results;
    }
}
