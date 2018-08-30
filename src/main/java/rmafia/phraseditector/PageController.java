package rmafia.phraseditector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller

public class PageController {
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    VideoRepositoryCustom videoRepositoryCustom;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("searchWord", new SearchWord());
        return "index";
    }

    @PostMapping("/")
    public String wordSubmit(@ModelAttribute SearchWord searchWord, Model model){
        HashMap<String, List<Float>> results = PhraseDetector.searchWordFromSubtitlesAll(searchWord.getWord(),videoRepository.findAll());
        List<String> videoIds = new ArrayList<String>();
        List<String> titles = new ArrayList<>();
        List<String> counts = new ArrayList<String>();
        List<List<String>> dataset = new ArrayList<List<String>>();

        for (Map.Entry<String, List<Float>> entry : results.entrySet()) {
            String key = entry.getKey();
            List<Float> value = entry.getValue();

            Video v = videoRepositoryCustom.findByVideoId(key);

            List<String> list = new ArrayList<String>();
            list.add(key);
            list.add(v.getTitle());
            list.add(""+value.size());
            list.add(YoutubeHelper.composeThumbnailUrl(v.getVideoId()));
            dataset.add(list);
        }

        model.addAttribute("dataset", dataset);
        return "search2";
    }
    /* archived
    public String wordSubmit(@ModelAttribute SearchWord searchWord, Model model){
        HashMap<String, Float> results = PhraseDetector.searchWordFromSubtitles(searchWord.getWord(),videoRepository.findAll());
        List<String> urls = new ArrayList<String>();
        for (Map.Entry<String, Float> entry : results.entrySet()) {
            String key = entry.getKey();
            float value = entry.getValue();
            urls.add(PhraseDetector.composeUrlEmbed(key, value));
        }
        model.addAttribute("urls", urls);
        return "search";
    }
    * */

    /*
    * what we need
    * - text list
    * - video embed
    * > - video obj
    * */
    @GetMapping("/player/{videoId}/{keyword}")
    public String showPlayer(
            @PathVariable String videoId,
            @PathVariable String keyword,
            Model model
    ){
        System.out.println("hey");
        //get start time list
        List<Video> vids = new ArrayList<Video>();
        vids.add(videoRepositoryCustom.findByVideoId(videoId));
        HashMap<String, List<Float>> results = PhraseDetector.searchWordFromSubtitlesAll(keyword, vids);
        List<Float> startTimes = results.get(videoId);

        //get subtitle data
        List<SubtitleData> subtitleData = PhraseDetector.extractSubtitlesByStartTimes(videoId, startTimes);

        //get video obj
        Video v = videoRepositoryCustom.findByVideoId(videoId);
        String thumbnail = YoutubeHelper.composeThumbnailUrl(videoId);

        model.addAttribute("video", v);
        model.addAttribute("thumbnail", thumbnail);
        model.addAttribute("subtitleData", subtitleData);
        model.addAttribute("keyword", keyword);

        return "player";
    }

}
