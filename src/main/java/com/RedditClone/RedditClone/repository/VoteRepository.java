package com.RedditClone.RedditClone.repository;

import com.RedditClone.RedditClone.model.Post;
import com.RedditClone.RedditClone.model.User;
import com.RedditClone.RedditClone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
ḍ