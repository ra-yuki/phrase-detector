package rmafia.phraseditector.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rmafia.phraseditector._Config_;
import rmafia.phraseditector.helpers.YoutubeHelper;
import rmafia.phraseditector.helpers.apiHandlers.YoutubeDataAPIHandler;
import rmafia.phraseditector.helpers.JsonParser;
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

    @GetMapping("/youtube/test/video")
    public String searchVideoOnYoutube(
            @RequestParam("v") String videoId
    ){
        YoutubeDataAPIHandler youtubeDataAPIHandler = new YoutubeDataAPIHandler();
        youtubeDataAPIHandler.addParam("key", _Config_.API_KEY);
        youtubeDataAPIHandler.addParam("part", "snippet");
        youtubeDataAPIHandler.addParam("id", videoId);

        youtubeDataAPIHandler.appendToQuery("videos");
        return youtubeDataAPIHandler.executeGetRequest();
    }

    @GetMapping("/youtube/test")
    public HashMap<String, List<Float>> searchVideosOnYoutube(
            @RequestParam("q") String query,
            @RequestParam("k") String keyword
    ){
        YoutubeDataAPIHandler youtubeDataAPIHandler = new YoutubeDataAPIHandler();
        youtubeDataAPIHandler.addParam("key", _Config_.API_KEY);
        youtubeDataAPIHandler.addParam("part", "snippet");
        youtubeDataAPIHandler.addParam("maxResults", "10");
        youtubeDataAPIHandler.addParam("q", query);
        youtubeDataAPIHandler.addParam("relevanceLanguage", "en");
        youtubeDataAPIHandler.addParam("type", "video");
        youtubeDataAPIHandler.addParam("safeSearch", "strict");
        youtubeDataAPIHandler.addParam("videoCaption", "closedCaption");

        youtubeDataAPIHandler.appendToQuery("search");
        String response = youtubeDataAPIHandler.executeGetRequest();
        JsonNode videosNode = JsonParser.string2JsonNode(response);

        //search word
//        HashMap<String, List<Float>> resultSet = PhraseDetector.searchWordFromSubtitlesAll("how", videosNode);

        //extract en sub
        List<HashMap<String, String>> extractedVids = YoutubeHelper.extractVideosWithEnglishSubtitle(videosNode);

        //search word
        HashMap<String, List<Float>> resultSet = PhraseDetector.searchWordFromSubtitlesAll(true, keyword, extractedVids);

        System.out.println("q: " + query);
        System.out.println("k: " + keyword);

        return resultSet;
    }

}


