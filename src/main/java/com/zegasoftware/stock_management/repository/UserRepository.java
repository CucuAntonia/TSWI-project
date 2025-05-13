package com.zegasoftware.stock_management.repository;

import com.zegasoftware.stock_management.model.dto.user.UserDetails;
import com.zegasoftware.stock_management.model.dto.user.UserSummary;
import com.zegasoftware.stock_management.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("select new com.zegasoftware.stock_management.model.dto.user.UserSummary(u.username, u.role ) from User u " +
            "where u.isDeleted = false")
    List<UserSummary> findAllUsers();

    @Query("select new com.zegasoftware.stock_management.model.dto.user.UserSummary(u.username, u.role ) from User u " +
            "where u.username = :username and u.isDeleted = false")
    Optional<UserSummary> findUserByUsername(@Param("username") String username);

    @Query("select new com.zegasoftware.stock_management.model.dto.user.UserSummary(u.username, u.role ) from User u " +
            "where u.id = :id and u.isDeleted = false")
    Optional<UserSummary> findUserById(@Param("id") UUID id);


    @Query("select new com.zegasoftware.stock_management.model.dto.user.UserDetails(u.username, u.password, u.role ) from User u " +
            "where u.username = :username and u.isDeleted = false")
    Optional<UserDetails> findByUsername(@Param("username") String username);
}
