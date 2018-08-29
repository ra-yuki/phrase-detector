package rmafia.phraseditector;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class YoutubeHelperTest {
    @Test
    public void composeThumbnail_should_return_correct_url(){
        Assert.assertEquals("https://img.youtube.com/vi/test/hqdefault.jpg", YoutubeHelper.composeThumbnailUrl("test"));
    }

    @Test
    public void test_getVideoInfo(){

        try {
            URL url = new URL("http://youtube.com/get_video_info?video_id=Dw8B1q1tKgs");
            Scanner s = new Scanner(url.openStream());
            // read from your scanner
            System.out.println(s);
        }
        catch(IOException ex) {
            // there was some connection problem, or the file did not exist on the server,
            // or your URL was not in the right format.
            // think about what to do now, and put it here.
            ex.printStackTrace(); // for now, simply output it.
        }
    }
}
