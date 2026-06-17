package com.travelcity.repository;

import com.travelcity.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByNoteId(Long noteId);
    void deleteByNoteId(Long noteId);
}