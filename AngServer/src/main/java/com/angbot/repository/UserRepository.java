package com.angbot.repository;

import org.springframework.data.repository.CrudRepository;
import com.angbot.domain.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}
