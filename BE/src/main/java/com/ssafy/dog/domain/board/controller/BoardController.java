package com.ssafy.dog.domain.board.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.board.dto.BoardDto;
import com.ssafy.dog.domain.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardService;

	@PostMapping("/board")
	public Api<?> createBoard(@RequestBody BoardDto boarddto) {
		return boardService.createBoard(boarddto);
	}

}