package com.ssafy.dog.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.dog.domain.board.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
