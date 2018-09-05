package rmafia.phraseditector.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import rmafia.phraseditector.entities.Video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhraseDetector {


    public static String searchWord(String word, Document document) {
        word = word.toLowerCase();
        for(Element e : document.select("text")){
            int ind = e.text().toLowerCase().indexOf(word);
            if(ind >= 0)
                return e.attr("start").split("\\.")[0];
        }

        return "-1";
    }

    public static List<Float> searchWordAll(String keyword, Document document){
        keyword = keyword.toLowerCase();
        List<Float> resList = new ArrayList<Float>();

        for(Element e : document.select("text")){
            int ind = e.text().toLowerCase().indexOf(keyword);
            if(ind >= 0) {
                resList.add(Float.parseFloat(e.attr("start")));
            }
        }

        return resList;
    }

    //video table case
    public static HashMap<String, Float> searchWordFromSubtitles(String word, List<Video> videos){

        HashMap<String, Float> results = new HashMap<String, Float>();

        for (int i=0; i<videos.size(); i++) {
            Document doc = YoutubeHelper.getSubtitlesData(videos.get(i).getVideoId());
            String startTimeStr = PhraseDetector.searchWord(word+" ", doc);

            float startTime = Float.parseFloat(startTimeStr);

            if (startTime > 0) {
                results.put(videos.get(i).getVideoId(), startTime);
            }
        }

        return results;
    }

    //youtube data api case
    public static HashMap<String, Float> searchWordFromSubtitles(String word, JsonNode youtubeSearchListResponse){

        HashMap<String, Float> results = new HashMap<String, Float>();

        for (int i=0; i<youtubeSearchListResponse.get("items").size(); i++) {
            JsonNode videoNode = youtubeSearchListResponse.get("items").get(i);
            Document doc = YoutubeHelper.getSubtitlesData(videoNode.get("id").get("videoId").asText());
            String startTimeStr = PhraseDetector.searchWord(word+" ", doc);

            float startTime = Float.parseFloat(startTimeStr);

            if (startTime > 0) {
                results.put(videoNode.get("id").get("videoId").asText(), startTime);
            }
        }

        return results;
    }

    //video table case
    public static HashMap<String, List<Float>> searchWordFromSubtitlesAll(String keyword, List<Video> videos){
        HashMap<String, List<Float>> results = new HashMap<String, List<Float>>();

        for (int i=0; i<videos.size(); i++) {
            Document doc = YoutubeHelper.getSubtitlesData(videos.get(i).getVideoId());
            List<Float> startTimes = PhraseDetector.searchWordAll(keyword+" ", doc);

            if(startTimes.size() > 0) {
                results.put(videos.get(i).getVideoId(), startTimes);
            }
        }

        return results;
    }

    //youtube data api case
    public static HashMap<String, List<Float>>searchWordFromSubtitlesAll(String keyword, JsonNode youtubeSearchListResponse){
        HashMap<String, List<Float>> results = new HashMap<String, List<Float>>();

        for (int i=0; i<youtubeSearchListResponse.get("items").size(); i++) {
            JsonNode videoNode = youtubeSearchListResponse.get("items").get(i);
            Document doc = YoutubeHelper.getSubtitlesData(videoNode.get("id").get("videoId").asText());
            List<Float> startTimes = PhraseDetector.searchWordAll(keyword+" ", doc);

            if(startTimes.size() > 0) {
                results.put(videoNode.get("id").get("videoId").asText(), startTimes);
            }
        }

        return results;
    }

    //youtube data api with eng sub video extracted case
    public static HashMap<String, List<Float>>searchWordFromSubtitlesAll(boolean thisOneIs4YTAPIExtracted, String keyword, List<HashMap<String, String>> videosEngSub){
        HashMap<String, List<Float>> results = new HashMap<String, List<Float>>();
        keyword = keyword.replaceAll("%20", " "); //converting back from ascii to space

        for (HashMap<String, String> video : videosEngSub) {
            Document doc = YoutubeHelper.getSubtitlesData(video);
            List<Float> startTimes = PhraseDetector.searchWordAll(keyword+" ", doc);

            if(startTimes.size() > 0) {
                results.put(video.get("videoId"), startTimes);
            }
        }

        return results;
    }
}
