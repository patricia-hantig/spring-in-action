package tacos;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//import org.junit.runner.RunWith;

@RunWith(SpringRunner.class)					// JUnit annotation - uses the Spring runner
@SpringBootTest									// tells JUnit to bootstrap the test with Spring Boot capabilities
public class TacoCloudApplicationTests {

	// even if @RunWith and @SpringBootTest have to load the Spring application context for the test -> they don't do anything without a test method !!!
	@Test
	public void contextLoads() {				// checks if Spring application context is successfully loaded
	}

}
