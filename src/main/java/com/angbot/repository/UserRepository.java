package com.angbot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.angbot.domain.User;

public interface UserRepository extends CrudRepository<User, String> {
	public void save(List<User> list);
}
