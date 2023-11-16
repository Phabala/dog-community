package com.ssafy.dog.domain.fcm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.ssafy.dog.domain.fcm.dto.FCMDto;
import com.ssafy.dog.domain.fcm.entity.FcmToken;
import com.ssafy.dog.domain.fcm.repository.FcmTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirebaseService {

	private final FcmTokenRepository fcmTokenRepository;

	public String sendNotification(FCMDto fcmDto) {
		try {
			String token = getFcmToken(fcmDto.getUserId());

			if (token.isEmpty()) {
				log.info("FCM 토큰 없음");
				return "";
			}

			Message message = Message.builder()
				.setToken(token)
				.putData("title", fcmDto.getTitle())
				.putData("content", fcmDto.getContent())
				.build();

			log.info("메시지 전송 시도 : {}", message.toString());
			String response = FirebaseMessaging.getInstance().send(message);
			log.info("메시지 전송 성공 : {}", message.toString());

			return response;

		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			return "Failed";
		}
	}

	private String getFcmToken(Long userId) {
		log.info("user PK : {}", userId);

		Optional<FcmToken> fcmToken = fcmTokenRepository.findById(userId);
		if (!fcmToken.isPresent()) {
			return "";

		}

		return fcmToken.get().getFcmToken();

	}

	public void sendChatMessage() {
		
	}

	// FCM 채팅방 구독
	public void subscribeFCM(List<String> registrationTokens, Long roomId) {
		String topic = "Chat" + roomId;
		try {
			FirebaseMessaging.getInstance().subscribeToTopic(
				registrationTokens, topic);
		} catch (FirebaseMessagingException e) {
			throw new RuntimeException(e);
		}
	}

	// FCM 채팅방 구독해지
	public void unsubscribeFCM(List<String> registrationTokens, Long roomId) {
		String topic = "Chat" + roomId;
		try {
			FirebaseMessaging.getInstance().unsubscribeFromTopic(
				registrationTokens, topic);
		} catch (FirebaseMessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
