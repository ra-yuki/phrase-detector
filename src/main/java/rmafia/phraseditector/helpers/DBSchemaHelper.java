package rmafia.phraseditector.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rmafia.phraseditector.entities.Video;
import rmafia.phraseditector.repositories.VideoRepository;

@Component
public class DBSchemaHelper {
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
