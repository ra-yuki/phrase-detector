package rmafia.phraseditector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import javax.lang.model.util.Elements;
import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhraseDetector {
    public static Document getSubtitlesData(String videoId) {
        String url = "http://video.google.com/timedtext?lang=en&v="+videoId;

        StringBuffer htmlBuffer = MyFileUtil.fileGetContents(url);
        String data = htmlBuffer.toString();

        Document doc = Jsoup.parse(data, "", Parser.xmlParser());
        return doc;
    }

    public static List<SubtitleData> extractSubtitlesByStartTimes(String videoId, List<Float> startTimes){
        Document document = getSubtitlesData(videoId);

        List<SubtitleData> data = new ArrayList<SubtitleData>();

        //add subtitle data only if start time matched
        for(Element e : document.select("text")) {
            for(float startTime : startTimes){
                float eStartTime = Float.parseFloat(e.attr("start"));
                if(eStartTime != startTime) continue;

                data.add(
                        new SubtitleData(
                                e.text(),
                                Float.parseFloat(e.attr("start")),
                                Float.parseFloat(e.attr("dur"))
                        )
                );
            }
        }

        return data;
    }

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

    public static HashMap<String, Float> searchWordFromSubtitles(String word, List<Video> videos){

        HashMap<String, Float> results = new HashMap<String, Float>();

        for (int i=0; i<videos.size(); i++) {
            Document doc = PhraseDetector.getSubtitlesData(videos.get(i).getVideoId());
            String startTimeStr = PhraseDetector.searchWord(word+" ", doc);

            float startTime = Float.parseFloat(startTimeStr);

            if (startTime > 0) {
                results.put(videos.get(i).getVideoId(), startTime);
            }
        }

        return results;
    }

    public static HashMap<String, List<Float>>searchWordFromSubtitlesAll(String keyword, List<Video> videos){
        HashMap<String, List<Float>> results = new HashMap<String, List<Float>>();

        for (int i=0; i<videos.size(); i++) {
            Document doc = PhraseDetector.getSubtitlesData(videos.get(i).getVideoId());
            List<Float> startTimes = PhraseDetector.searchWordAll(keyword+" ", doc);

            if(startTimes.size() > 0) {
                results.put(videos.get(i).getVideoId(), startTimes);
            }
        }

        return results;
    }



    public static String composeUrl(String videoId, String startTime) {
        return "https://www.youtube.com/watch?v="+videoId+"&t="+startTime;
    }
    public static String composeUrl(String videoId, float startTime) {
        return "https://www.youtube.com/watch?v="+videoId+"&t="+(int)startTime;
    }

    public static String composeUrlEmbed(String videoId, float startTime) {
        return "https://www.youtube.com/embed/"+videoId+"?start="+(int)startTime;
    }
}
