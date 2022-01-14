package ru.javaops.voting.web.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.voting.error.IllegalRequestDataException;
import ru.javaops.voting.error.UpdateRestrictionException;
import ru.javaops.voting.error.UpdateRestrictionException.RestrictionType;
import ru.javaops.voting.model.Restaurant;
import ru.javaops.voting.model.User;
import ru.javaops.voting.model.Vote;
import ru.javaops.voting.repository.RestaurantRepository;
import ru.javaops.voting.repository.VoteRepository;
import ru.javaops.voting.util.DateTimeUtil;
import ru.javaops.voting.web.AuthUser;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static ru.javaops.voting.util.ValidationUtil.assureIdConsistent;
import static ru.javaops.voting.util.ValidationUtil.checkNew;
import static ru.javaops.voting.util.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class UserVoteController {
    static final String USER_VOTES_URL = "/api/votes";
    static final LocalTime DEADLINE_TO_CHANGE_VOTE = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping(USER_VOTES_URL)
    public List<Vote> getBetween(@AuthenticationPrincipal AuthUser authUser,
                                 @RequestParam(name = "start", required = false) @DateTimeFormat(iso = DATE) LocalDate startDate,
                                 @RequestParam(name = "end", required = false) @DateTimeFormat(iso = DATE) LocalDate endDate) {
        log.info("get votes between dates ({} <-> {}) for user {}", startDate, endDate, authUser);
        startDate = DateTimeUtil.getOrMin(startDate);
        endDate = DateTimeUtil.getOrMax(endDate);
        if (endDate.isBefore(startDate)) {
            throw new IllegalRequestDataException("Parameter startDate must be earlier than endDate");
        }
        return voteRepository.findByUserIdBetweenDates(authUser.id(), startDate, endDate);
    }

    @GetMapping(USER_VOTES_URL + "/{id}")
    public Vote getById(@AuthenticationPrincipal AuthUser authUser, @PathVariable("id") int voteId) {
        Integer userId = authUser.id();
        return checkNotFound(voteRepository.findByIdAndUserId(voteId, userId), Vote.class, voteId, userId);
    }

    @PostMapping(path = USER_VOTES_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Vote vote) {
        log.info("create {}", vote);
        checkNew(vote);
        if (!vote.getLunchDate().isEqual(LocalDate.now())) {
            throw new UpdateRestrictionException(RestrictionType.VOTE_DATE_RESTRICTION);
        }
        Vote created = prepareAndSave(vote, authUser.getUser());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(USER_VOTES_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(path = USER_VOTES_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Vote vote, @PathVariable("id") int voteId) {
        Integer userId = authUser.id();
        log.info("update vote {} for user {}", voteId, userId);
        assureIdConsistent(vote, voteId);
        if (!vote.getLunchDate().isEqual(LocalDate.now()) || DateTimeUtil.getCurrentTime().isAfter(DEADLINE_TO_CHANGE_VOTE)) {
            throw new UpdateRestrictionException(RestrictionType.VOTE_TIME_RESTRICTION);
        }
        checkNotFound(voteRepository.findByIdAndUserId(voteId, userId), Vote.class, voteId, userId);
        log.debug("found vote {} for user {}", voteId, userId);
        prepareAndSave(vote, authUser.getUser());
    }

    private Vote prepareAndSave(Vote vote, User user) {
        vote.setUser(user);
        Integer restaurantId = vote.getRestaurant().getId();
        Objects.requireNonNull(restaurantId);
        Restaurant restaurant = checkNotFound(restaurantRepository.findById(restaurantId), Restaurant.class, restaurantId);
        vote.setRestaurant(restaurant);
        return voteRepository.save(vote);
    }
}
