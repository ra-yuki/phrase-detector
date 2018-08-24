package rmafia.phraseditector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Component
public class Helper {
    @Autowired
    WordRepository repository;
    public void addNew(String name){
        Word word = new Word();
        word.setName(name);
        repository.save(word);
    }
    @Autowired
    VideoRepository videoRepository;
    public void addNewVideo(String videoId, String genre){
        Video video = new Video();
        video.setVideoId(videoId);
        video.setGenre(genre);
        videoRepository.save(video);
    }

}
