package rmafia.phraseditector;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import rmafia.phraseditector.helpers.PhraseDetector;
import rmafia.phraseditector.helpers.YoutubeHelper;
import rmafia.phraseditector.helpers.apiHandlers.PurgoMalumAPIHandler;
import rmafia.phraseditector.services.YoutubeSearchService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhraesDetectorTests {
    @Test
    public void hasProfanityNoAPI_should_return_true_on_fuck_this_shit(){
        PhraseDetector phraseDetector = new PhraseDetector();

        Assert.assertEquals(true, phraseDetector.hasProfanityNoAPI("fuck this shit"));
    }

    @Test
    public void hasProfanityOnDocumentNOAPI_should_return_true_on_bad_subs(){
        Document doc = YoutubeHelper.getSubtitlesData(YoutubeHelper.extractVideoInfoById("v-Dur3uXXCQ"));

        boolean res = PhraseDetector.hasProfanityOnDocumentNoAPI(doc);
        Assert.assertEquals(true, res);
    }
}
