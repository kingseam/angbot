package com.angbot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.angbot.domain.User;

public interface UserRepository extends JpaRepository<User, String>,  JpaSpecificationExecutor<User>{	
}
