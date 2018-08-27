package rmafia.phraseditector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.util.HashMap;
import java.util.List;

@RestController
public class MainController {
    @Autowired
    VideoRepository videoRepository;
    @GetMapping("/test")
    public HashMap<String, Float> searchWordFromSubtitles(){
        List<Video> videos = videoRepository.findAll();
        return PhraseDetector.searchWordFromSubtitles("think", videos);
    }

}


