package escudeler.example.apispeak.grpc;

import escudeler.example.apispeak.grpc.SpeakGrpcServiceGrpc.SpeakGrpcServiceImplBase;
import escudeler.example.apispeak.service.TalkService;
//import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;

@Log4j2
@RequiredArgsConstructor
@GrpcService
public class SpeakGrpcService extends SpeakGrpcServiceImplBase {

	private final TalkService talkService;
	
	@Override
	public void hello(PersonRequest request, StreamObserver<HelloResponse> responseObserver) {
		try {
			HelloResponse response = talkService.hello(request);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            log.warn("StatusError on TalkGrpc.", e);
            responseObserver.onError(e);
        } catch (Exception e) {
            log.warn("Error on TalkGrpc.", e);
            responseObserver.onError(e);
        }
	}
}
