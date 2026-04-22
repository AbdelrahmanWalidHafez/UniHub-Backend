package com.unihub.announcement.like.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface ILikeService {

    void toggleLike(UUID id, HttpServletRequest request);

    List<String> getUsersWhoLiked(UUID postId, HttpServletRequest request, int pageNum);
}
