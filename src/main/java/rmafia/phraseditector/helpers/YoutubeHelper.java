package rmafia.phraseditector.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import rmafia.phraseditector.helpers.constructors.SubtitleData;

import java.util.ArrayList;
import java.util.List;

public class YoutubeHelper {
    private static final String THUMBNAIL_URL = "https://img.youtube.com/vi/@videoId/hqdefault.jpg"; //ref: https://stackoverflow.com/questions/2068344/how-do-i-get-a-youtube-video-thumbnail-from-the-youtube-api
    private static final String VIDEO_INFO_URL = "http://video.google.com/timedtext?lang=en&v=@videoId";
    private static final String DIRECT_URL = "https://www.youtube.com/watch?v=@videoId&t=@startTime";
    private static final String EMBED_URL = "https://www.youtube.com/embed/@videoId?start=@startTime";

    public static Document getSubtitlesData(String videoId) {
        String url = VIDEO_INFO_URL.replaceAll("@videoId", videoId);

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

    public static String composeThumbnailUrl(String videoId){
        return THUMBNAIL_URL.replaceAll("@videoId", videoId);
    }

    @Deprecated //meant to get video title
    public static StringBuffer getVideoInfo(String videoId){
        StringBuffer res = MyFileUtil.fileGetContents("http://youtube.com/get_video_info?video_id="+videoId);

        return res;
    }

    public static String composeUrl(String videoId, String startTime) {
        return DIRECT_URL.replaceAll("@videoId", videoId).replaceAll("@startTime", startTime);
    }
    public static String composeUrl(String videoId, float startTime) {
        return DIRECT_URL.replaceAll("@videoId", videoId).replaceAll("@startTime", Integer.toString((int)startTime));
    }
    public static String composeUrlEmbed(String videoId, float startTime) {
        return EMBED_URL.replaceAll("@videoId", videoId).replaceAll("@startTime", Integer.toString((int)startTime));
    }
}
