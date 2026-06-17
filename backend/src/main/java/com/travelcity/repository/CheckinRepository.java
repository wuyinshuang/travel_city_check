package com.travelcity.repository;

import com.travelcity.entity.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    List<Checkin> findByUserId(Long userId);
    Optional<Checkin> findByUserIdAndCityId(Long userId, Long cityId);
    boolean existsByUserIdAndCityId(Long userId, Long cityId);
    void deleteByUserIdAndCityId(Long userId, Long cityId);
}