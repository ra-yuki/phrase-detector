package rmafia.phraseditector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rmafia.phraseditector.helpers.PhraseDetector;
import rmafia.phraseditector.helpers.YoutubeHelper;
import rmafia.phraseditector.repositories.VideoRepositoryCustom;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhraseDitectorApplicationTests {

    @Autowired
    VideoRepositoryCustom videoRepository;

    @Test
    public void contextLoads() {
        System.out.println(YoutubeHelper.getSubtitlesData("5nlG0svf9t4"));
    }

    @Test
    public void find_by_videoId(){
        System.out.println(videoRepository.findByVideoId("5nlG0svf9t4"));
    }
}
