package com.sashank.payment.auth.repo;

import com.sashank.payment.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long>
{

    Optional<User> findByEmail(String email);
}
