package com.RedditClone.RedditClone.repository;

import com.RedditClone.RedditClone.model.Comment;
import com.RedditClone.RedditClone.model.Post;
import com.RedditClone.RedditClone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
