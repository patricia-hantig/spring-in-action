package com.patricia;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.MessageChannel;

import java.io.File;

@Configuration
public class FileWriterIntegrationConfig {

    // ----------------------- define integration flows with XML -----------------------

    @Profile("xmlconfig")
    @Configuration
    @ImportResource("classpath:/filewriter-config.xml")
    public static class XmlConfiguration {}

    // ----------------------- define integration flows with Java -----------------------

    // - declare a transformer
    @Profile("javaconfig")
    @Bean
    @Transformer(inputChannel = "textInChannel",
                outputChannel = "fileWriterChannel")
    public GenericTransformer<String, String> upperCaseTransformer() {
        return text -> text.toUpperCase();
    }

    // - declare a file writer
    @Profile("javaconfig")
    @Bean
    @ServiceActivator(inputChannel = "fileWriterChannel")
    public FileWritingMessageHandler fileWriter() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("simple-flow/output"));

        handler.setExpectReply(false);
        handler.setFileExistsMode(FileExistsMode.APPEND);
        handler.setAppendNewLine(true);

        return handler;
    }

    // the "textInChannel" and "fileWriterChannel" will be created automatically if no beans with those names exist
    // if you want more control over how the channels are configured -> you can construct them as beans:
    // uncomment the next 2 beans to test it out

    /*@Profile("javaconfig")
    @Bean
    public MessageChannel textInChannel() {
        System.out.println("Creating textInChannel");
        return new DirectChannel();
    }

    @Profile("javaconfig")
    @Bean
    public MessageChannel fileWriterChannel() {
        System.out.println("Creating fileWriterChannel");
        return new DirectChannel();
    }*/

    // ----------------------- define integration flows with Java using DSL -----------------------
    @Profile("javadsl")
    @Bean
    public IntegrationFlow fileWriterFlow() {
        return IntegrationFlows
                .from(MessageChannels.direct("textInChannel"))                              // inbound channel
                .<String, String>transform(text -> text.toUpperCase())                      // declare a transformer
                .handle(                                                                    // handle writing to a file
                        Files.outboundAdapter(new File("simple-flow/output"))
                        .fileExistsMode(FileExistsMode.APPEND)
                        .appendNewLine(true))
                .get();
    }

    // we don't need to explicitly declare the channels
    // we reference "textInChannel" channel - but it's automatically created by Spring Integration
    // we can declare the channel manually if we want:
    // uncomment the next code:

    /*@Profile("javadsl")
    @Bean
    public IntegrationFlow fileWriterFlow() {
        return IntegrationFlows
                .from(MessageChannels.direct("textInChannel"))                          // inbound channel
                .<String, String>transform(text -> text.toUpperCase())                      // declare a transformer
                .channel(MessageChannels.direct("fileWriterChannel"))                   // declare the channel
                .handle(                                                                    // handle writing to a file
                        Files.outboundAdapter(new File("simple-flow/output"))
                                .fileExistsMode(FileExistsMode.APPEND)
                                .appendNewLine(true))
                .get();
    }*/
}
