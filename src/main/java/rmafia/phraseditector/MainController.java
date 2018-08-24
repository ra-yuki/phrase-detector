package rmafia.phraseditector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {
    @Autowired
    VideoRepository videoRepository;
    @GetMapping("/test")
    public String getSubtitles(){
        List<Video> videos = videoRepository.findAll();
        Document doc = PhraseDetector.getSubtitlesData(videos.get(0).getVideoId());
        String startTime = PhraseDetector.searchWord("this", doc);
        String result = PhraseDetector.composeUrl(videos.get(0).getVideoId(), startTime);
        return result;
    }

}
