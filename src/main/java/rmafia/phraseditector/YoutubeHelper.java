package rmafia.phraseditector;

import jdk.nashorn.internal.runtime.URIUtils;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

public class YoutubeHelper {
    //url ref: https://stackoverflow.com/questions/2068344/how-do-i-get-a-youtube-video-thumbnail-from-the-youtube-api
    private static final String THUMBNAIL_URL = "https://img.youtube.com/vi/@videoId/hqdefault.jpg";

    public static String composeThumbnailUrl(String videoId){
        return THUMBNAIL_URL.replaceAll(
                "(https://img.youtube.com/vi/)(@videoId)(/[a-zA-Z0-9.]+)",
                "$1"+videoId+"$3"
        );
    }

    @Deprecated
    public static StringBuffer getVideoInfo(String videoId){
        StringBuffer res = MyFileUtil.fileGetContents("http://youtube.com/get_video_info?video_id="+videoId);

        return res;
    }

    //get title from url
    //https://stackoverflow.com/questions/1216029/get-title-from-youtube-videos
}
