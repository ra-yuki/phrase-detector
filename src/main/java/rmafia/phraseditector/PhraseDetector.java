package rmafia.phraseditector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.util.List;

public class PhraseDetector {
    public static Document getSubtitlesData(String videoId) {
        String url = "http://video.google.com/timedtext?lang=en&v="+videoId;

        StringBuffer htmlBuffer = MyFileUtil.fileGetContents(url);
        String data = htmlBuffer.toString();

        Document doc = Jsoup.parse(data, "", Parser.xmlParser());
        return doc;
    }
    public static String searchWord(String word, Document document) {
        for(Element e : document.select("text")){
            int ind = e.text().indexOf(word);
            if(ind >= 0)
                return e.attr("start").split("\\.")[0];
        }

        return "__ERROR__";
    }
    public static String composeUrl(String videoId, String startTime) {
        return "https://www.youtube.com/watch?v="+videoId+"&t="+startTime;
    }
}
