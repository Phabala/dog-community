package com.ssafy.dog.domain.chat.dto.req;

import java.util.List;

import lombok.Getter;

@Getter
public class ChatRoomReqDto {

	private String roomTitle;
	private List<String> userNicks;
}
