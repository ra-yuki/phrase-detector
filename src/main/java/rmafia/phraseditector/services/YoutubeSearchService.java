package rmafia.phraseditector.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmafia.phraseditector._Config_;
import rmafia.phraseditector.helpers.JsonParser;
import rmafia.phraseditector.helpers.PhraseDetector;
import rmafia.phraseditector.helpers.YoutubeHelper;
import rmafia.phraseditector.helpers.apiHandlers.PurgoMalumAPIHandler;
import rmafia.phraseditector.helpers.apiHandlers.YoutubeDataAPIHandler;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class YoutubeSearchService {
    public List<HashMap<String, String>> searchEnglishSubVidsOnYoutube(String ytSearchQuery, int maxResults){
        YoutubeDataAPIHandler youtubeDataAPIHandler = new YoutubeDataAPIHandler();
        youtubeDataAPIHandler.addParam("key", _Config_.API_KEY);
        youtubeDataAPIHandler.addParam("part", "snippet");
        youtubeDataAPIHandler.addParam("maxResults", Integer.toString(maxResults));
        youtubeDataAPIHandler.addParam("q", ytSearchQuery);
        youtubeDataAPIHandler.addParam("relevanceLanguage", "en");
        youtubeDataAPIHandler.addParam("type", "video");
        youtubeDataAPIHandler.addParam("safeSearch", "strict");
        youtubeDataAPIHandler.addParam("videoCaption", "closedCaption");

        youtubeDataAPIHandler.appendToQuery("search");
        String response = youtubeDataAPIHandler.executeGetRequest();
        JsonNode videosNode = JsonParser.string2JsonNode(response);

        //extract en sub
        List<HashMap<String, String>> extractedVids = YoutubeHelper.extractVideosWithEnglishSubtitle(videosNode);

        return extractedVids;
    }

    public List<HashMap<String, String>> getEnglishSubVidOnYoutube(String videoId){
        YoutubeDataAPIHandler youtubeDataAPIHandler = new YoutubeDataAPIHandler();
        youtubeDataAPIHandler.addParam("key", _Config_.API_KEY);
        youtubeDataAPIHandler.addParam("part", "snippet");
        youtubeDataAPIHandler.addParam("id", videoId);

        youtubeDataAPIHandler.appendToQuery("videos");
        String response = youtubeDataAPIHandler.executeGetRequest();
        JsonNode videoNode = JsonParser.string2JsonNode(response);

        //extract en sub
        List<HashMap<String, String>> extractedVids = YoutubeHelper.extractVideosWithEnglishSubtitle(videoNode);

        return extractedVids;
    }

    public HashMap<String, List<Float>> searchVidsContainingKeyword(String keyword, List<HashMap<String, String>> extractedVids){
        return PhraseDetector.searchWordFromSubtitlesAll(true, keyword, extractedVids);
    }
}
