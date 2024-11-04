package com.smartcv.smartcv.repository;

import com.smartcv.smartcv.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

    @Query("SELECT u FROM Users u WHERE u.email = :email AND u.password = :password ")
    Optional<Users> findByEmailAndPassword(@Param("email")String email, @Param("password") String password);

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Optional<Users> findByEmail(@Param("email")String email);

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Users findByEmails(@Param("email")String email);

    @Query("SELECT u FROM Users u WHERE u.id = :id")
    Optional<Users> findById(@Param("id") String id);

    @Query("SELECT u FROM Users u WHERE u.email = :email AND u.username = :username ")
    Optional<Users> findByEmailAndUsername(@Param("email")String email, @Param("username") String username);

}