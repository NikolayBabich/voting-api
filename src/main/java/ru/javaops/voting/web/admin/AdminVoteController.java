package ru.javaops.voting.web.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.voting.model.Vote;
import ru.javaops.voting.repository.VoteRepository;
import ru.javaops.voting.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO;
import static ru.javaops.voting.util.ValidationUtil.isValidId;

@RestController
@Slf4j
@AllArgsConstructor
public class AdminVoteController {
    static final String ADMIN_VOTES_URL = "/api/admin/votes";

    private final VoteRepository voteRepository;

    @GetMapping(path = ADMIN_VOTES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getAll(@RequestParam(name = "rid", required = false) Integer restaurantId,
                             @RequestParam(name = "uid", required = false) Integer userId,
                             @RequestParam(name = "start", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
                             @RequestParam(name = "end", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
        LocalDate startDateOrMin = DateTimeUtil.getOrMin(startDate);
        LocalDate endDateOrMax = DateTimeUtil.getOrMax(endDate);
        return getFilteredByParams(restaurantId, userId, startDateOrMin, endDateOrMax);
    }

    private List<Vote> getFilteredByParams(@Nullable Integer restaurantId, @Nullable Integer userId, LocalDate startDate, LocalDate endDate) {
        log.info("filter votes between dates ({} <-> {})", startDate, endDate);
        if (isValidId(restaurantId, userId)) {
            log.info("get votes for restaurant {} from user {}", restaurantId, userId);
            return voteRepository.findByRestaurantIdAndUserId(restaurantId, userId, startDate, endDate);
        } else {
            if (isValidId(restaurantId)) {
                log.info("get votes for restaurant {}", restaurantId);
                return voteRepository.findByRestaurantIdBetweenDates(restaurantId, startDate, endDate);
            }
            if (isValidId(userId)) {
                log.info("get votes from user {}", userId);
                return voteRepository.findByUserIdBetweenDates(userId, startDate, endDate);
            }
        }
        log.info("get all votes");
        return voteRepository.findAllBetweenDates(startDate, endDate);
    }
}
