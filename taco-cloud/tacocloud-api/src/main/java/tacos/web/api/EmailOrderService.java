package tacos.web.api;

import org.springframework.stereotype.Service;
import tacos.*;
import tacos.data.IngredientRepository;
import tacos.data.PaymentMethodRepository;
import tacos.data.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmailOrderService {

    private UserRepository userRepository;
    private IngredientRepository ingredientRepository;
    private PaymentMethodRepository paymentMethodRepository;

    public EmailOrderService(UserRepository userRepository, IngredientRepository ingredientRepository, PaymentMethodRepository paymentMethodRepository) {
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public Order convertEmailOrderToDomainOrder(EmailOrder emailOrder) {

        User user = userRepository.findByEmail(emailOrder.getEmail());
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserId(user.getId());

        Order order = new Order();
        order.setUser(user);
        order.setCcNumber(paymentMethod.getCcNumber());
        order.setCcCVV(paymentMethod.getCcCVV());
        order.setCcExpiration(paymentMethod.getCcExpiration());
        order.setDeliveryName(user.getFullname());
        order.setDeliveryStreet(user.getStreet());
        order.setDeliveryCity(user.getCity());
        order.setDeliveryState(user.getState());
        order.setDeliveryZip(user.getZip());
        order.setPlacedAt(new Date());

        // TODO: Handle unhappy case where a given ingredient doesn't match

        List<EmailOrder.EmailTaco> emailTacos = emailOrder.getTacos();
        for (EmailOrder.EmailTaco emailTaco : emailTacos) {
            Taco taco = new Taco();
            taco.setName(emailTaco.getName());
            List<String> ingredientsId = emailTaco.getIngredients();
            List<Ingredient> ingredients = new ArrayList<>();
            for (String ingredientId : ingredientsId) {
                Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientId);
                if (optionalIngredient.isPresent()) {
                    ingredients.add(optionalIngredient.get());
                }
            }
            taco.setIngredients(ingredients);
            order.addDesign(taco);
        }
        return order;
    }
}
