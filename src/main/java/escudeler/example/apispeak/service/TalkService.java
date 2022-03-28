package escudeler.example.apispeak.service;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import escudeler.example.apispeak.config.ApplicationConfig;
import escudeler.example.apispeak.grpc.HelloResponse;
import escudeler.example.apispeak.grpc.PersonRequest;
import escudeler.example.apispeak.request.HelloRequest;
import escudeler.example.apispeak.response.AuthResponse;
import escudeler.example.apispeak.response.SpeakHelloResponse;
import escudeler.example.apithirdpart.grpc.AuthorizeResponse;
import escudeler.example.apithirdpart.grpc.ThirdPartGrpcServiceGrpc.ThirdPartGrpcServiceBlockingStub;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Log4j2
@RequiredArgsConstructor
@Service
public class TalkService {

	@GrpcClient("grpc-server")
	private ThirdPartGrpcServiceBlockingStub thirdPartBlockingStub;

	private final RestTemplate restTemplate;
	private final ApplicationConfig applicationConfig;
	
	public HelloResponse hello(PersonRequest request) {
		try {
			log.info("Call gRPC to validate person " + request.getPersonName() + "...");
			AuthorizeResponse authorizeResponse = thirdPartBlockingStub.authorize(request);

			String reply = authorizeResponse.getAuthorized() ? "Hello, How are u?" : "Get out of here!";
			return HelloResponse.newBuilder().setReply(reply).build();

		} catch (Exception e) {
			log.error("Error calling gRPC to validate people.", e);
			throw new IllegalArgumentException(e);
		}
	}

	public SpeakHelloResponse helloHttp(HelloRequest request) {
		try {
            log.info("Call http to validate person " + request.getPersonName() + "...");
            URI uri = new UriTemplate(applicationConfig.getHttpThirdPartAddress()).expand();

            RequestEntity<HelloRequest> requestEntity = RequestEntity.post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request);
            AuthResponse authorizeResponse = restTemplate.exchange(requestEntity, AuthResponse.class).getBody();
            String reply = authorizeResponse != null && authorizeResponse.isAuthorized() ? "Hello, How are u?":"Get out of here!";
            return SpeakHelloResponse.builder().reply(reply).build();
         	
        } catch (RestClientException e) {
            log.error("MS Calling Error -> http ThirdPart : ", e);
            throw new IllegalArgumentException(e);
        }
	}
}
