package com.smartcv.smartcv.repository;

import com.smartcv.smartcv.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {

    @Query("SELECT u FROM Users u WHERE u.email = :email AND u.password = :password ")
    Optional<Users> findByEmailAndPassword(@Param("email")String email, @Param("password") String password);

    @Query("SELECT u FROM Users u WHERE u.username = :username")
    Optional<Users> findByUser(@Param("username")String username);

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Optional<Users> findByEmail(@Param("email")String email);

    @Query("SELECT u FROM Users u WHERE u.id = :id")
    Optional<Users> findById(@Param("id") Long id);

    @Query("SELECT u FROM Users u WHERE u.email = :email AND u.username = :username ")
    Optional<Users> findByEmailAndUsername(@Param("email")String email, @Param("username") String username);

}