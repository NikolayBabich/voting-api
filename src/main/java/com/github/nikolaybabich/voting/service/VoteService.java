package com.github.nikolaybabich.voting.service;

import com.github.nikolaybabich.voting.model.User;
import com.github.nikolaybabich.voting.model.Vote;
import com.github.nikolaybabich.voting.repository.RestaurantRepository;
import com.github.nikolaybabich.voting.repository.UserRepository;
import com.github.nikolaybabich.voting.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Vote create(User user, int restaurantId) {
        return prepareAndSave(new Vote(null, LocalDate.now()), user, restaurantId);
    }

    @Transactional
    public void update(User user, int restaurantId) {
        LocalDate actualDate = LocalDate.now();
        Vote vote = voteRepository.findByUserAndActualDate(user, actualDate).orElseThrow(
                () -> new EntityNotFoundException("Vote for user=" + user.id() + " on " + actualDate + " was not found"));
        prepareAndSave(vote, user, restaurantId);
    }

    public List<Vote> getBetween(User user, LocalDate fromDate, LocalDate toDate) {
        return voteRepository.findByUserAndActualDateBetween(userRepository.getById(user.id()), fromDate, toDate);
    }

    private Vote prepareAndSave(Vote vote, User user, int restaurantId) {
        vote.setUser(userRepository.getById(user.id()));
        vote.setRestaurant(restaurantRepository.getById(restaurantId));
        return voteRepository.save(vote);
    }
}
