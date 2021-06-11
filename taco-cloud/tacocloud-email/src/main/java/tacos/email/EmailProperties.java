package tacos.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.mail.URLName;

// EmailProperties - has properties that are used to produce an IMAP URL - the flow uses this URL to connect to the Taco Cloud email server & poll for emails

@Data
@ConfigurationProperties(prefix = "tacocloud.email")
@Component
public class EmailProperties {

    private String username;
    private String password;
    private String host;
    private int port;
    private String mailbox;
    private long pollRate = 30000;
    private static final String PROTOCOL = "imaps";

    public String getImapUrl() {
        URLName url = new URLName(PROTOCOL, host, port, mailbox, username, password);
        System.out.println("ImapUrl is: " + url.toString());
        return url.toString();
    }
}
