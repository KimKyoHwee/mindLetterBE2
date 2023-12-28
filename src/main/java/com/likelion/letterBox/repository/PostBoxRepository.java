package com.likelion.letterBox.repository;

import com.likelion.letterBox.domain.PostBox;
import com.likelion.letterBox.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostBoxRepository extends JpaRepository<PostBox,Long> {
    Optional<PostBox> findById(Long id);
    Optional<PostBox> findByUser(User user);

    Optional<PostBox> findByUuid(String uuid);

    /*
    @Query("select * from PostBox pb where pb.user = :user")
    Optional<PostBox> getByUser(@Param("user") User user);
    */
    Optional<PostBox> findByUserId(Long userId);
}
