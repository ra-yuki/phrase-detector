package rmafia.phraseditector.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import rmafia.phraseditector.helpers.constructors.SubtitleData;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YoutubeHelper {
    private static final String THUMBNAIL_URL = "https://img.youtube.com/vi/@videoId/hqdefault.jpg"; //ref: https://stackoverflow.com/questions/2068344/how-do-i-get-a-youtube-video-thumbnail-from-the-youtube-api
    private static final String SUBTITLE_URL = "http://video.google.com/timedtext?lang=en&v=@videoId";
    private static final String SUBTITLE_URL_FLEX = "http://video.google.com/timedtext?lang=@lang_code&v=@videoId&name=@name";
    private static final String SUBTITLE_METADATA_URL = "http://video.google.com/timedtext?type=list&v=@videoId";
    private static final String YOUTUBE_PAGE_URL = "https://www.youtube.com/watch?v=@videoId&t=@startTime";
    private static final String EMBED_URL = "https://www.youtube.com/embed/@videoId?start=@startTime";
    private static final String VIDEO_METADATA_URL = "http://youtube.com/get_video_info?video_id=@videoId";

    public static List<HashMap<String, String>> extractVideosWithEnglishSubtitle(JsonNode videosJNode){
        List<HashMap<String, String>> videosEngSub = new ArrayList<HashMap<String, String>>();

        for(JsonNode videoJNode : videosJNode.get("items")){
            //handling both JsonNodes derived from 'search' api get method & 'videos' api get method.
            String videoId = (videoJNode.get("id").path("videoId").isMissingNode())
                    ? videoJNode.get("id").asText()
                    : videoJNode.get("id").get("videoId").asText();

            //send http request to get subtitle metadata
            String url = SUBTITLE_METADATA_URL.replaceAll("@videoId", videoId);
            String data = MyFileUtil.fileGetContents(url).toString();

            //parse the metadata (XML) to Document
            Document doc = Jsoup.parse(data, "", Parser.xmlParser());

            //search for en sub
            for(Element track : doc.select("track")){
                //exception handling
                if(track.attr("lang_code").indexOf("en") < 0) continue;

                //== store video info ==
                HashMap<String, String> videoInfo = new HashMap<String, String>();
                videoInfo.put("videoId", videoId);
                videoInfo.put("name", track.attr("name").replaceAll(" ", "%20")); //converting space to ascii
                videoInfo.put("lang_code", track.attr("lang_code"));
                videoInfo.put("lang_original", track.attr("lang_original"));
                //store video title/description/thumbnail too
                videoInfo.put("title", videoJNode.get("snippet").get("title").asText());
                videoInfo.put("description", videoJNode.get("snippet").get("description").asText());
                String thumbnail = "";
                if(!videoJNode.get("snippet").get("thumbnails").path("maxres").isMissingNode()){ //trying to get the best thumbnail
                    thumbnail = videoJNode.get("snippet").get("thumbnails").get("maxres").get("url").asText();
                }
                else if(!videoJNode.get("snippet").get("thumbnails").path("high").isMissingNode()){
                    thumbnail = videoJNode.get("snippet").get("thumbnails").get("high").get("url").asText();
                }
                else if(!videoJNode.get("snippet").get("thumbnails").path("medium").isMissingNode()){
                    thumbnail = videoJNode.get("snippet").get("thumbnails").get("medium").get("url").asText();
                }
                else if(!videoJNode.get("snippet").get("thumbnails").path("default").isMissingNode()){
                    thumbnail = videoJNode.get("snippet").get("thumbnails").get("default").get("url").asText();
                }
                videoInfo.put("thumbnail", thumbnail);

                //== add the info to returning var ==
                videosEngSub.add(videoInfo);

                //break for loop (because en sub already found)
                break;
            }
        }

        return videosEngSub;
    }

    public static HashMap<String, String> extractVideoInfoById(String videoId){
        HashMap<String, String> videoInfo = new HashMap<String, String>();

        //send http request to get subtitle metadata
        String url = SUBTITLE_METADATA_URL.replaceAll("@videoId", videoId);
        String data = MyFileUtil.fileGetContents(url).toString();

        //parse the metadata (XML) to Document
        Document doc = Jsoup.parse(data, "", Parser.xmlParser());

        //search for en sub
        for(Element track : doc.select("track")){
            //exception handling
            if(track.attr("lang_code").indexOf("en") < 0) continue;

            //== store video info ==
            videoInfo.put("videoId", videoId);
            videoInfo.put("name", track.attr("name").replaceAll(" ", "%20")); //converting space to ascii
            videoInfo.put("lang_code", track.attr("lang_code"));
            videoInfo.put("lang_original", track.attr("lang_original"));

            //break for loop (because en sub already found)
            break;
        }

        return videoInfo;
    }

    @Deprecated
    public static Document getSubtitlesData(String videoId) {
        String url = SUBTITLE_URL.replaceAll("@videoId", videoId);

        StringBuffer htmlBuffer = MyFileUtil.fileGetContents(url);
        String data = htmlBuffer.toString();

        Document doc = Jsoup.parse(data, "", Parser.xmlParser());
        return doc;
    }

    public static Document getSubtitlesData(HashMap<String, String> videoInfo) {
        String url = SUBTITLE_URL_FLEX
                .replaceAll("@lang_code", videoInfo.get("lang_code"))
                .replaceAll("@videoId", videoInfo.get("videoId"))
                .replaceAll("@name", videoInfo.get("name"));

        StringBuffer htmlBuffer = MyFileUtil.fileGetContents(url);
        String data = htmlBuffer.toString();

        Document doc = Jsoup.parse(data, "", Parser.xmlParser());
        return doc;
    }

    public static List<HashMap<String, String>> organizeSubtitles(Document document){
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for(Element e : document.select("text")){
            HashMap<String, String> submap = new HashMap<String, String>();
            submap.put("text", e.text());
            submap.put("start", e.attr("start"));
            submap.put("dur", e.attr("dur"));

            list.add(submap);
        }

        return list;
    }

    public static List<SubtitleData> extractSubtitlesByStartTimes(String videoId, List<Float> startTimes){
        Document document = getSubtitlesData(extractVideoInfoById(videoId));

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

    public static String composeThumbnailUrl(String videoId){
        return THUMBNAIL_URL.replaceAll("@videoId", videoId);
    }

    @Deprecated //meant to get video title
    public static StringBuffer getVideoInfo(String videoId){
        StringBuffer res = MyFileUtil.fileGetContents("http://youtube.com/get_video_info?video_id="+videoId);

        return res;
    }

    public static String composeUrl(String videoId, String startTime) {
        return YOUTUBE_PAGE_URL.replaceAll("@videoId", videoId).replaceAll("@startTime", startTime);
    }
    public static String composeUrl(String videoId, float startTime) {
        return YOUTUBE_PAGE_URL.replaceAll("@videoId", videoId).replaceAll("@startTime", Integer.toString((int)startTime));
    }
    public static String composeUrlEmbed(String videoId, float startTime) {
        return EMBED_URL.replaceAll("@videoId", videoId).replaceAll("@startTime", Integer.toString((int)startTime));
    }
}
