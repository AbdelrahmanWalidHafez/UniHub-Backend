package com.unihub.announcement.like.controller;

import com.unihub.announcement.like.service.ILikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {

    private final ILikeService likeService;

    @PostMapping("/{id}/like-toggle")
    public ResponseEntity<Void> toggleLike(@PathVariable UUID id, HttpServletRequest request) {
        likeService.toggleLike(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<String>> getLikes(@PathVariable UUID postId, HttpServletRequest request,
                                                 @RequestParam(value = "page_num", defaultValue = "1")int page_num) {
        List<String> users = likeService.getUsersWhoLiked(postId, request,page_num);
        return ResponseEntity.ok(users);
    }

}
