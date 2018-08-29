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
    VideoRepository videoRepository;
    public void addNewVideo(String videoId, int filter, String title){
        Video video = new Video();
        video.setVideoId(videoId);
        video.setFilter(filter);
        video.setTitle(title);
        videoRepository.save(video);
    }

}
