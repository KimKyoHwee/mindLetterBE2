package com.likelion.letterBox.repository;

import com.likelion.letterBox.domain.PostBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostBoxRepository extends JpaRepository<PostBox,Long> {
    Optional<PostBox> findById(Long id);
}
