package org.boot.growup.product.persist.repository;

import org.boot.growup.product.persist.entity.Search;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long> {
    Optional<Search> findByKeyword(String word);

    List<Search> findByOrderBySearchedCountDesc(Pageable pageable);
}
