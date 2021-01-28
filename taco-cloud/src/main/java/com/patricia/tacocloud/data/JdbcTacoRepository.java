package com.patricia.tacocloud.data;

import com.patricia.tacocloud.Taco;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

// saving data using update()
@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbc;

    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for (String ingredientId : taco.getIngredients()) {
            saveIngredientToTaco(ingredientId, tacoId);
        }
        return taco;
    }
    // this method starts by saving the essential taco details(name, timeOfCreation) & inserts one row into Taco_Ingredients for each ingredient in the Taco object
    // the method starts by calling saveTacoInfo() & then uses the taco ID returned from that method to call saveIngredientToTaco() - which saves each ingredient
    // we cycle through each Ingredient in Taco - calling saveIngredientToTaco

    private void saveIngredientToTaco(String ingredientId, long tacoId) {
        jdbc.update("insert into Taco_Ingredients (taco, ingredient) values (?, ?)", tacoId, ingredientId);
    }
    // this method saves each ingredient for a taco (saves ingredient references to the Taco_Ingredients table)

    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory(
                "insert into Taco (name, createdAt) values (?,?)", Types.VARCHAR, Types.TIMESTAMP);

        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);   // make sure that you set ReturnGeneratedKeys to 'true' otherwise you will get NullPointerException
        PreparedStatementCreator preparedStatementCreator = preparedStatementCreatorFactory
                .newPreparedStatementCreator(Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedAt().getTime()))
        );

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }
    // when you insert a row into Taco -> you need to know the ID generated by the database so that you can reference it in each of the ingredients
    // here we use a different update() method - with a PreparedStatementCreator an a KeyHolder
    // the KeyHolder will provide the generated taco ID
    // in order to use the generated taco ID -> you will need a PreparedStatementCreator
    // creating the PreparedStatementCreator = start by creating a PreparedStatementCreatorFactory, giving it the SQL you want to execute, as well as the types of each query parameter
    // then call newPreparedStatementCreator() on that factory passing in the values needed in the query parameters to produce the PreparedStatementCreator
    // with a PreparedStatementCreator in hand, you call update() passing in PreparedStatementCreator and KeyHolder (a GeneratedKeyHolder instance)
    // when the update() is finished -> you can return the taco ID by returning: keyHolder.getKey().longValue();
}