package tacos;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import tacos.data.IngredientRepository;
import tacos.data.OrderRepository;
import tacos.data.TacoRepository;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//import org.junit.Test;
//import org.junit.runner.RunWith;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)                   // Web test for HomeController - arranges the test to run in the context of a Spring MVC application
@Ignore("Unsure how to test with @DataJpaTest")
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;                        // injects MockMvc - which performs an HTTP GET request for '/'

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private TacoRepository designRepository;

    @MockBean
    private OrderRepository orderRepository;

    //@Test
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))                           // performs GET /
                .andExpect(status().isOk())                             // expects HTTP 200
                .andExpect(view().name("home"))         // expects 'home' view
                .andExpect(content().string(containsString("Welcome to...")));      // expects "Welcome to..."
    }
}
