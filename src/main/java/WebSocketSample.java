import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Tcp;
import scala.concurrent.duration.FiniteDuration;

import static java.util.concurrent.TimeUnit.SECONDS;
import akka.stream.javadsl.Flow;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.model.ws.TextMessage;

import akka.http.javadsl.server.AllDirectives.*;

import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import akka.http.javadsl.server.HttpApp;

public class WebSocketSample extends HttpApp{


    public void method(){
        final ActorSystem system = ActorSystem.create("MySystem");
        final Materializer materializer = ActorMaterializer.create(system);
        final Http http = Http.get(system);






                final Route route=routes();


        final CompletionStage<ServerBinding> bindingCompletionStage= http.bindAndHandle(route.flow(system,materializer),ConnectHttp.toHost("localhost", 80),materializer);

    }

    @Override
    protected Route routes() {

        final Flow<Message,Message, NotUsed> measurementFlow=
                Flow.of(Message.class).flatMapConcat((Message message)->message.asTextMessage().getStreamedText().fold("",(acc,elem)->acc+elem))
                        .groupedWithin(1000, FiniteDuration.create(1,SECONDS))
                    //    .mapAsync(5, Arrays.asList(12)).
                        .map(written->TextMessage.create("Wrote up"));

        return route(
                path("measurement", () ->
                        get(() -> handleWebSocketMessages(measurementFlow))
                )/*,

                pathPrefix("chatroom", () ->
                        path(PathMatchers.segment(), username ->
                                get(() -> handleWebSocketMessages(chatRoomSocketFlow(username)))
                        )
                )*/
        );
    }
}
