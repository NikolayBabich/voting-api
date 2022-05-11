package com.github.nikolaybabich.voting.repository;

import com.github.nikolaybabich.voting.model.User;
import com.github.nikolaybabich.voting.model.Vote;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    Optional<Vote> findByUserAndActualDate(User user, LocalDate actualDate);

    List<Vote> findByUserAndActualDateBetween(User user, LocalDate fromDate, LocalDate toDate);
}
