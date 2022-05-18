package com.github.nikolaybabich.voting.util;

import com.github.nikolaybabich.voting.model.Vote;
import com.github.nikolaybabich.voting.to.VoteTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class VoteUtil {

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getActualDate(), vote.getUser().getId(), vote.getRestaurant().getId());
    }

    public static List<VoteTo> createTos(Vote... votes) {
        return createTos(List.of(votes));
    }

    public static List<VoteTo> createTos(Collection<Vote> votes) {
        return votes.stream().map(VoteUtil::createTo).toList();
    }
}
