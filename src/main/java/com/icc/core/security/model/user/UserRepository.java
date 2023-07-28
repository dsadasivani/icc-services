package com.icc.core.security.model.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
    @Query("""
            update User u set u.firstName = :firstName, u.lastName = :lastName, u.userPassword = :userPassword
            where u.email = :email""")
    int updateUserDetails(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("userPassword") String userPassword, @Param("email") String email);

    Optional<User> findByEmail(String email);
}
