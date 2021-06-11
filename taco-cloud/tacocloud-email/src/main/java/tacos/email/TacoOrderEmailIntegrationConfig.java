package tacos.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;

import java.util.Properties;

// This class defines the integration flow

@Configuration
public class TacoOrderEmailIntegrationConfig {

    @Bean
    public IntegrationFlow tacoOrderEmailFlow(
        EmailProperties emailProperties,
        EmailToOrderTransformer emailToOrderTransformer,
        OrderSubmitMessageHandler orderSubmitMessageHandler
    ) {
        return IntegrationFlows
                .from(Mail.imapInboundAdapter(emailProperties.getImapUrl()).simpleContent(true),
                        e -> e.poller(Pollers.fixedDelay(emailProperties.getPollRate())))
                .transform(emailToOrderTransformer)
                .handle(orderSubmitMessageHandler)
                .get();
    }
    // the integration flow has 3 components:
    // - an IMAP email inbound channel adapter  - this channel adapter is created with the IMAP URL generated from getImapUrl() method of EmailProperties and polls a delay set in pollRate property
    //                                          - the emails coming in are handed off to a channel connecting it to the transformer
    // - a transformer: EmailToOrderTransformer - transforms an email into an order object
    // - a handler(acting as an outbound channel adapter) - this handler accepts an order object and submits it to Taco Cloud's REST API
}
