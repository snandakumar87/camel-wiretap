// camel-k: language=java property-file=tls.properties secret=trust
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestParamType;
import org.apache.camel.component.kafka.KafkaComponent;

import org.apache.camel.Exchange;



public final class TransactionService extends RouteBuilder {
      String kafkaBootstrap = "my-cluster-kafka-brokers:9092";
    private String consumerMaxPollRecords = "50000";
    private String consumerCount = "3";
    private String consumerSeekTo = "end";
    private String consumerGroup = "process-elastic";
    public void configure() {


        rest("/transaction")
                .post()
                .route()
                .to("kafka:"+"investigation"+ "?brokers=" + kafkaBootstrap)
                .wireTap("kafka:"+"reporting"+ "?brokers=" + kafkaBootstrap);

        from("kafka:" + "reporting" + "?brokers=" + kafkaBootstrap + "&maxPollRecords="
                + consumerMaxPollRecords + "&seekTo=" + "beginning"
                + "&groupId=" + "task")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader("Authorization",constant("Basic XXXX"))
                .setHeader("Content-Type",constant("application/json"))
                .to("https://elasticsearch-sample-es-http:9200/audit/audit")
                .log("${body}");
    }


    // Transaction Object

}



