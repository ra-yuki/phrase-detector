package rmafia.phraseditector.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmafia.phraseditector.entities.Video;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    List<String> findByTitle(String title);
}
