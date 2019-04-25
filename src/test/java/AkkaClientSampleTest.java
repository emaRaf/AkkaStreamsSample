
import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import org.junit.Test;
import org.junit.Ignore;

import java.util.Objects;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AkkaClientSampleTest {

    String message = "Robert";


    @Test
    public void testAkkaStream1() {
        final ActorSystem system = ActorSystem.create("MySystem");
        final Materializer materializer = ActorMaterializer.create(system);

        final Source<Integer, NotUsed> source = Source.range(0, 20);

        final Flow<Integer, String, NotUsed> flow = Flow.fromFunction((Integer n) -> n.toString());
        Sink<String, CompletionStage<Done>> sink = Sink.foreach(str -> System.out.println(str));

        final RunnableGraph<NotUsed> runnable = source.via(flow).to(sink);

        runnable.run(materializer);


    }

    @Test
    public void testAkkaStream2() {
        final ActorSystem system = ActorSystem.create("MySystem");
        final Materializer materializer = ActorMaterializer.create(system);
        Source.range(0, 20).map(Object::toString).runForeach(str -> System.out.println(str), materializer);
    }
}