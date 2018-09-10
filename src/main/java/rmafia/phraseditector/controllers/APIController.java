package rmafia.phraseditector.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rmafia.phraseditector._Config_;
import rmafia.phraseditector.helpers.YoutubeHelper;
import rmafia.phraseditector.helpers.apiHandlers.YoutubeDataAPIHandler;
import rmafia.phraseditector.helpers.JsonParser;
import rmafia.phraseditector.helpers.PhraseDetector;
import rmafia.phraseditector.entities.Video;
import rmafia.phraseditector.helpers.constructors.IndexFormDataset;
import rmafia.phraseditector.helpers.constructors.SubtitleData;
import rmafia.phraseditector.repositories.VideoRepository;
import rmafia.phraseditector.repositories.VideoRepositoryCustom;
import rmafia.phraseditector.services.YoutubeSearchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class APIController {
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    VideoRepositoryCustom videoRepositoryCustom;
    @Autowired
    YoutubeSearchService youtubeSearchService;

    ////////////////////////////////////////////
    // Proper API Code
    ////////////////////////////////////////////
    /*
    * return
    * [
    *   {"wordHitCount": "5",
    *   "thumbnail":"https://i.ytimg.com/vi/Um7pMggPnug/hqdefault.jpg",
    *   "videoId":"Um7pMggPnug",
    *   "title":"Katy Perry - Chained To The Rhythm (Official) ft. Skip Marley"}
    * ]
    * */
    @GetMapping("/search")
    public List<HashMap<String, String>> search(
            @RequestParam("q") String query,
            @RequestParam("k") String keyword
    ){
        List<HashMap<String, String>> ytVids =
                youtubeSearchService.searchEnglishSubVidsOnYoutube(query, 5);

        HashMap<String, List<Float>> ytVidIdsWithKeywordTimes =
                youtubeSearchService.searchVidsContainingKeyword(keyword, ytVids);

        List<String> videoIds = new ArrayList<String>();
        List<String> titles = new ArrayList<>();
        List<String> counts = new ArrayList<String>();
        List<HashMap<String, String>> dataset = new ArrayList<HashMap<String, String>>();

        for(HashMap<String, String> ytVid : ytVids){
            //var declarations
            String videoId = ytVid.get("videoId");
            String title = ytVid.get("title");
            String thumbnail = ytVid.get("thumbnail");

            System.out.println(ytVid);

            //exception handling
            if(!ytVidIdsWithKeywordTimes.containsKey(videoId)) continue;

            //storing relevant data to pass to view
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("videoId", videoId);
            map.put("title", title);
            map.put("wordHitCount", Integer.toString(ytVidIdsWithKeywordTimes.get(videoId).size()));
            map.put("thumbnail", thumbnail);
            dataset.add(map);
        }

        return dataset;
    }

    @GetMapping("/subtitle")
    public List<HashMap<String, String>> getSubtitles(
            @RequestParam("v") String videoId
    ){
        HashMap<String, String> videoInfo = YoutubeHelper.extractVideoInfoById(videoId);
        Document subtitlesDocument = YoutubeHelper.getSubtitlesData(videoInfo);
        return YoutubeHelper.organizeSubtitles(subtitlesDocument);
    }

    @GetMapping("/subtitle/matches")
    public List<HashMap<String, String>> getMatchedSubStartTimes(
            @RequestParam("v") String videoId,
            @RequestParam("k") String keyword
    ){
        List<HashMap<String, String>> ytVids = youtubeSearchService.getEnglishSubVidOnYoutube(videoId);
        HashMap<String, List<Float>> ytVidWithSub = youtubeSearchService.searchVidsContainingKeyword(keyword, ytVids);
        List<Float> list = ytVidWithSub.get(videoId);
        List<HashMap<String, String>> listOrganized = new ArrayList<HashMap<String, String>>();
        for(float val : list){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("start", Float.toString(val));
            listOrganized.add(map);
        }

        return listOrganized;
    }

    @GetMapping("/video")
    public JsonNode getVideoDetails(
            @RequestParam("v") String videoId
    ){
        YoutubeDataAPIHandler youtubeDataAPIHandler = new YoutubeDataAPIHandler();
        youtubeDataAPIHandler.addParam("key", _Config_.API_KEY);
        youtubeDataAPIHandler.addParam("part", "snippet");
        youtubeDataAPIHandler.addParam("id", videoId);

        youtubeDataAPIHandler.appendToQuery("videos");
        String response = youtubeDataAPIHandler.executeGetRequest();

        //parse to json node
        JsonNode jsonedResponse = JsonParser.string2JsonNode(response);

        return jsonedResponse.get("items");
    }

    ////////////////////////////////////////////
    // Legacy Code
    ////////////////////////////////////////////
//    @GetMapping("/test")
//    public HashMap<String, Float> searchWordFromSubtitles(){
//        List<Video> videos = videoRepository.findAll();
//        return PhraseDetector.searchWordFromSubtitles("think", videos);
//    }
//
//    @GetMapping("/youtube/test/video")
//    public String searchVideoOnYoutube(
//            @RequestParam("v") String videoId
//    ){
//        YoutubeDataAPIHandler youtubeDataAPIHandler = new YoutubeDataAPIHandler();
//        youtubeDataAPIHandler.addParam("key", _Config_.API_KEY);
//        youtubeDataAPIHandler.addParam("part", "snippet");
//        youtubeDataAPIHandler.addParam("id", videoId);
//
//        youtubeDataAPIHandler.appendToQuery("videos");
//        return youtubeDataAPIHandler.executeGetRequest();
//    }
//
//    @GetMapping("/youtube/test")
//    public HashMap<String, List<Float>> searchVideosOnYoutube(
//            @RequestParam("q") String query,
//            @RequestParam("k") String keyword
//    ){
//        YoutubeDataAPIHandler youtubeDataAPIHandler = new YoutubeDataAPIHandler();
//        youtubeDataAPIHandler.addParam("key", _Config_.API_KEY);
//        youtubeDataAPIHandler.addParam("part", "snippet");
//        youtubeDataAPIHandler.addParam("maxResults", "10");
//        youtubeDataAPIHandler.addParam("q", query);
//        youtubeDataAPIHandler.addParam("relevanceLanguage", "en");
//        youtubeDataAPIHandler.addParam("type", "video");
//        youtubeDataAPIHandler.addParam("safeSearch", "strict");
//        youtubeDataAPIHandler.addParam("videoCaption", "closedCaption");
//
//        youtubeDataAPIHandler.appendToQuery("search");
//        String response = youtubeDataAPIHandler.executeGetRequest();
//        JsonNode videosNode = JsonParser.string2JsonNode(response);
//
//        //search word
////        HashMap<String, List<Float>> resultSet = PhraseDetector.searchWordFromSubtitlesAll("how", videosNode);
//
//        //extract en sub
//        List<HashMap<String, String>> extractedVids = YoutubeHelper.extractVideosWithEnglishSubtitle(videosNode);
//
//        //search word
//        HashMap<String, List<Float>> resultSet = PhraseDetector.searchWordFromSubtitlesAll(true, keyword, extractedVids);
//
//        System.out.println("q: " + query);
//        System.out.println("k: " + keyword);
//
//        return resultSet;
//    }

}


