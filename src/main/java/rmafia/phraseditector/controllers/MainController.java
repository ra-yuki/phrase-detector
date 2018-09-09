package rmafia.phraseditector.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rmafia.phraseditector.entities.Video;
import rmafia.phraseditector.helpers.PhraseDetector;
import rmafia.phraseditector.helpers.YoutubeHelper;
import rmafia.phraseditector.helpers.constructors.IndexFormDataset;
import rmafia.phraseditector.helpers.constructors.SubtitleData;
import rmafia.phraseditector.repositories.VideoRepository;
import rmafia.phraseditector.repositories.VideoRepositoryCustom;
import rmafia.phraseditector.services.YoutubeSearchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    VideoRepositoryCustom videoRepositoryCustom;

    @Autowired
    YoutubeSearchService youtubeSearchService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("indexFormDataset", new IndexFormDataset());
        return "index";
    }

    @PostMapping("/")
    public String searchReqWord(@ModelAttribute IndexFormDataset indexFormDataset, Model model){
        HashMap<String, List<Float>> results = PhraseDetector.searchWordFromSubtitlesAll(
                indexFormDataset.getWord(),
                videoRepository.findAll()
        );
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
        return "search";
    }

    @PostMapping("/youtube")
    public String searchReqWordOnYoutube(@ModelAttribute IndexFormDataset indexFormDataset, Model model){
        List<HashMap<String, String>> ytVids = youtubeSearchService.searchEnglishSubVidsOnYoutube(
                indexFormDataset.getYtQuery(),
                20
        );

        HashMap<String, List<Float>> ytVidIdsWithKeywordTimes = youtubeSearchService.searchVidsContainingKeyword(
                indexFormDataset.getWord(),
                ytVids
        );

        List<String> videoIds = new ArrayList<String>();
        List<String> titles = new ArrayList<>();
        List<String> counts = new ArrayList<String>();
        List<List<String>> dataset = new ArrayList<List<String>>();

        for(HashMap<String, String> ytVid : ytVids){
            //var declarations
            String videoId = ytVid.get("videoId");
            String title = ytVid.get("title");
            String thumbnail = ytVid.get("thumbnail");

            System.out.println(ytVid);

            //exception handling
            if(!ytVidIdsWithKeywordTimes.containsKey(videoId)) continue;

            //storing relevant data to pass to view
            List<String> list = new ArrayList<String>();
            list.add(videoId);
            list.add(title);
            list.add( Integer.toString(ytVidIdsWithKeywordTimes.get(videoId).size()) );
            list.add(thumbnail);
            dataset.add(list);
        }

        model.addAttribute("dataset", dataset);

        return "search";
    }

    @GetMapping("/manami")
    public String searchReqWordOnYoutubeFromManami(
            @RequestParam("q") String query,
            @RequestParam("k") String keyword,
            Model model
    ){
        IndexFormDataset indexFormDataset = new IndexFormDataset();
        indexFormDataset.setWord(keyword);
        indexFormDataset.setYtQuery(query);

        //copied from searchReqWordOnYoutube
        List<HashMap<String, String>> ytVids = youtubeSearchService.searchEnglishSubVidsOnYoutube(
                indexFormDataset.getYtQuery(),
                20
        );

        HashMap<String, List<Float>> ytVidIdsWithKeywordTimes = youtubeSearchService.searchVidsContainingKeyword(
                indexFormDataset.getWord(),
                ytVids
        );

        List<String> videoIds = new ArrayList<String>();
        List<String> titles = new ArrayList<>();
        List<String> counts = new ArrayList<String>();
        List<List<String>> dataset = new ArrayList<List<String>>();

        for(HashMap<String, String> ytVid : ytVids){
            //var declarations
            String videoId = ytVid.get("videoId");
            String title = ytVid.get("title");
            String thumbnail = ytVid.get("thumbnail");

            System.out.println(ytVid);

            //exception handling
            if(!ytVidIdsWithKeywordTimes.containsKey(videoId)) continue;

            //storing relevant data to pass to view
            List<String> list = new ArrayList<String>();
            list.add(videoId);
            list.add(title);
            list.add( Integer.toString(ytVidIdsWithKeywordTimes.get(videoId).size()) );
            list.add(thumbnail);
            dataset.add(list);
        }

        model.addAttribute("dataset", dataset);
        model.addAttribute("indexFormDataset", indexFormDataset);

        return "search";
    }

    @Deprecated
    @GetMapping("/player/dep")
    public String showPlayer(
            @RequestParam("v") String videoId,
            @RequestParam("k") String keyword,
            Model model
    ){
        //get start time list
        List<Video> vids = new ArrayList<Video>();
        vids.add(videoRepositoryCustom.findByVideoId(videoId));
        HashMap<String, List<Float>> results = PhraseDetector.searchWordFromSubtitlesAll(keyword, vids);
        List<Float> startTimes = results.get(videoId);

        //get subtitle data
        List<SubtitleData> subtitleData = YoutubeHelper.extractSubtitlesByStartTimes(videoId, startTimes);

        //get video obj
        Video v = videoRepositoryCustom.findByVideoId(videoId);
        String thumbnail = YoutubeHelper.composeThumbnailUrl(videoId);

        model.addAttribute("video", v);
        model.addAttribute("thumbnail", thumbnail);
        model.addAttribute("subtitleData", subtitleData);
        model.addAttribute("keyword", keyword);

        return "player";
    }

    @GetMapping("/player")
    public String showPlayerNew(
            @RequestParam("v") String videoId,
            @RequestParam("k") String keyword,
            Model model
    ){
        //get start time list
        List<HashMap<String, String>> ytVids = youtubeSearchService.getEnglishSubVidOnYoutube(videoId);
        HashMap<String, List<Float>> ytVidWithSub = youtubeSearchService.searchVidsContainingKeyword(keyword, ytVids);
        List<Float> startTimes = ytVidWithSub.get(videoId);

        //get subtitle data
        List<SubtitleData> subtitleData = YoutubeHelper.extractSubtitlesByStartTimes(videoId, startTimes);

        Video video = new Video();
        video.setVideoId(videoId);
        video.setTitle(ytVids.get(0).get("title"));
        model.addAttribute("video", video);
        model.addAttribute("thumbnail", ytVids.get(0).get("thumbnail"));
        model.addAttribute("subtitleData", subtitleData);
        model.addAttribute("keyword", keyword);

        return "player";
    }

}
