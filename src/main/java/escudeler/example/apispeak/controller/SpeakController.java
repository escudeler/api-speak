package escudeler.example.apispeak.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import escudeler.example.apispeak.request.HelloRequest;
import escudeler.example.apispeak.response.SpeakHelloResponse;
import escudeler.example.apispeak.service.TalkService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SpeakController {
	private final TalkService talkService;
	
	@PostMapping("/hello")
	public ResponseEntity<SpeakHelloResponse> hello(@RequestBody HelloRequest helloRequest) {
		SpeakHelloResponse helloResponse = talkService.helloHttp(helloRequest);
		return ResponseEntity.ok(helloResponse);
		
	}
}
