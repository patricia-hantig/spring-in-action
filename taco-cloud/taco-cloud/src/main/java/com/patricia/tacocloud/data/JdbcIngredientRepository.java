package com.patricia.tacocloud.data;

import com.patricia.tacocloud.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcIngredientRepository implements IngredientRepository {

    private JdbcTemplate jdbc;

    @Autowired
    public JdbcIngredientRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbc.query("select id, name, type from Ingredient", this::mapRowToIngredient);
    }

    @Override
    public Ingredient findById(String id) {
        return jdbc.queryForObject("select id, name, type from Ingredient where id=?", this::mapRowToIngredient, id);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbc.update("insert into Ingredient (id, name, type) values (?, ?, ?)", ingredient.getId(), ingredient.getName(), ingredient.getType().toString());
        return ingredient;
    }

    private Ingredient mapRowToIngredient(ResultSet resultSet, int rowNum) throws SQLException {
        return new Ingredient(resultSet.getString("id"), resultSet.getString("name"), Ingredient.Type.valueOf(resultSet.getString("type")));
    }
    // this method is used for mapping each row in the result set to an Ingredient object

    // implementation for method findById prior Java 8
    public Ingredient findOne(String id) {
        return jdbc.queryForObject("select id, name, type from Ingredient where id=?",
                new RowMapper<Ingredient>() {
                    @Override
                    public Ingredient mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        return new Ingredient(resultSet.getString("id"), resultSet.getString("name"), Ingredient.Type.valueOf(resultSet.getString("type")));
                    }
                }, id);
    }

}

// ■■■ Annotations:
// @Repository = you declare that it should be automatically discovered by Spring component scanning and instantiated as a bean in the Spring application context
// @Autowired = when Spring creates the JdbcIngredientRepository bean -> it injects it with JdbcTemplate
//              - the constructor assigns JdbcTemplate to an instance variable that wil be used in the methods to query and insert into database
