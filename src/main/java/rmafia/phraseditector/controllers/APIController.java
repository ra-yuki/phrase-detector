package rmafia.phraseditector.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rmafia.phraseditector.helpers.PhraseDetector;
import rmafia.phraseditector.entities.Video;
import rmafia.phraseditector.repositories.VideoRepository;
import rmafia.phraseditector.repositories.VideoRepositoryCustom;

import java.util.HashMap;
import java.util.List;

@RestController
public class APIController {
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    VideoRepositoryCustom videoRepositoryCustom;

    @GetMapping("/test")
    public HashMap<String, Float> searchWordFromSubtitles(){
        List<Video> videos = videoRepository.findAll();
        return PhraseDetector.searchWordFromSubtitles("think", videos);
    }

    @GetMapping("/video")
    public Video getVideo(){
        return videoRepositoryCustom.findByVideoId("5nlG0svf9t4");
    }

}


