import akka.NotUsed;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.japi.Pair;
import akka.stream.javadsl.Source;
import akka.stream.javadsl.Tcp;
import akka.util.ByteString;


import akka.stream.javadsl.Flow;
import akka.http.javadsl.server.directives.*;
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
import akka.http.javadsl.model.HttpResponse;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class AkkaServerSample extends AllDirectives {


    public void method() {

        final ActorSystem system = ActorSystem.create("MySystem");
        final Materializer materializer = ActorMaterializer.create(system);
        final Http http = Http.get(system);

        final Source<ByteString, NotUsed> numbers = Source.unfold(0L, n -> {
            long next = n + 1;
            return Optional.of(Pair.create(next, next));
        }).map(n -> ByteString.fromString(n.toString() + " newline"));

        final Route route= path("numbers", ()->get(()->complete(HttpResponse.create().
                withStatus(StatusCodes.OK).withEntity(HttpEntities.create(ContentTypes.TEXT_PLAIN_UTF8,numbers)))));


        final CompletionStage<ServerBinding> bindingCompletionStage= http.bindAndHandle(route.flow(system,materializer),ConnectHttp.toHost("localhost", 80),materializer);



    }

}
