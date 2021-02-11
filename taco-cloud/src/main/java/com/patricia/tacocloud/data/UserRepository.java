package com.patricia.tacocloud.data;

import com.patricia.tacocloud.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
// in addition to the CRUD operations provided by extending CrudRepository, we also have a findByUsername() method - which will be used in the user details service
// Spring Data JPA will automatically generate the implementation of this interface at runtime
