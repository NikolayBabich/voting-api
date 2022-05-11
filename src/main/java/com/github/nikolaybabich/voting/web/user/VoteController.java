package com.github.nikolaybabich.voting.web.user;

import com.github.nikolaybabich.voting.model.User;
import com.github.nikolaybabich.voting.model.Vote;
import com.github.nikolaybabich.voting.repository.VoteRepository;
import com.github.nikolaybabich.voting.service.VoteService;
import com.github.nikolaybabich.voting.to.VoteTo;
import com.github.nikolaybabich.voting.util.DateTimeUtil;
import com.github.nikolaybabich.voting.util.VoteUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {

    static final String REST_URL = "/api/profile/votes";

    static final LocalTime DEADLINE_TO_CHANGE_VOTE = LocalTime.of(23, 0); // TODO change to 11:00

    private final VoteService service;

    private final VoteRepository repository;

    @PostMapping(path = "/today", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> createWithLocation(@RequestBody VoteTo voteTo) {
        int userId = voteTo.getUserId(); // TODO change injected auth user
        log.info("create vote for user {}", userId);
        Vote created = service.create(getAuthUser(userId), voteTo.getRestaurantId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource)
                .body(VoteUtil.createTo(created));
    }

    @PutMapping(path = "/today", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody VoteTo voteTo) {
        if (DateTimeUtil.getCurrentTime().isAfter(DEADLINE_TO_CHANGE_VOTE)) {
            throw new RuntimeException("It's too late to edit your vote"); // TODO change exception type
        }

        int userId = voteTo.getUserId(); // TODO change injected auth user
        log.info("update vote for user {}", userId);
        service.update(getAuthUser(userId), voteTo.getRestaurantId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoteTo> get(@RequestParam("user") int userId, @PathVariable int id) { // TODO change injected auth user
        log.info("get vote {} for user {}", id, userId);
        Optional<VoteTo> vote = repository.findByIdAndUser(id, getAuthUser(userId)).map(VoteUtil::createTo);
        return ResponseEntity.of(vote);
    }

    @GetMapping
    public List<VoteTo> getHistory(@RequestParam("user") int userId, // TODO change injected auth user
                                   @RequestParam("from") @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                   @RequestParam("to") @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        log.info("get vote history for user {} between dates [{} <-> {}]", userId, fromDate, toDate);
        fromDate = DateTimeUtil.getOrMin(fromDate);
        toDate = DateTimeUtil.getOrMax(toDate);
        DateTimeUtil.checkDates(fromDate, toDate);

        return VoteUtil.createTos(service.getBetween(getAuthUser(userId), fromDate, toDate));
    }

    private User getAuthUser(int userId) {
        return new User(userId, "AuthUser");
    }
}