package com.likelion.letterBox.repository;

import com.likelion.letterBox.domain.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long> {
}
