package com.blog.like;

import com.blog.post.PostRepository;
import com.blog.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostLikeRepository postLikeRepo;
    private final UserRepository userRepo;
    private final PostRepository postRepo;

    public long like(Long postId, Authentication auth){
        var user = userRepo.findByUsername(auth.getName()).orElseThrow();
        var post = postRepo.findById(postId).orElseThrow();
        if(!postLikeRepo.existsByUserIdAndPostId(user.getId(), postId)){
            postLikeRepo.save(
                    PostLike.builder()
                            .user(user)
                            .post(post)
                            .build()
            );
        }
        return postLikeRepo.countByPostId(postId);
    }

    public long unlike(Long postId, Authentication auth){
        var user = userRepo.findByUsername(auth.getName()).orElseThrow();
        postLikeRepo.findByUserIdAndPostId(user.getId(), postId).ifPresent(postLikeRepo::delete);
        return postLikeRepo.countByPostId(postId);
    }
}
