package com.RedditClone.RedditClone.service;


import com.RedditClone.RedditClone.dto.SubredditDto;
import com.RedditClone.RedditClone.exception.SpringRedditException;
import com.RedditClone.RedditClone.mapper.SubredditMapper;
import com.RedditClone.RedditClone.model.Subreddit;
import com.RedditClone.RedditClone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final SubredditMapper mapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit subreddit=subredditRepository.save(mapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }
    @Transactional
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(mapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }


    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit=subredditRepository.findById(id).orElseThrow(()->new SpringRedditException("No subreddit found with id: "+id));
        return mapper.mapSubredditToDto(subreddit);
    }
}
