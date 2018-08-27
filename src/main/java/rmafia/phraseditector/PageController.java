package rmafia.phraseditector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller

public class PageController {
    @Autowired
    WordRepository wordRepository;
    @Autowired
    VideoRepository videoRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("searchWord", new SearchWord());
        return "index";
    }

    @PostMapping("/")
    public String wordSubmit(@ModelAttribute SearchWord searchWord, Model model){
        HashMap<String, Float> results = PhraseDetector.searchWordFromSubtitles(searchWord.getWord(),videoRepository.findAll());
        List<String> urls = new ArrayList<String>();
        for (Map.Entry<String, Float> entry : results.entrySet()) {
            String key = entry.getKey();
            float value = entry.getValue();
            urls.add(PhraseDetector.composeUrlEmbed(key, value));
        }
        model.addAttribute("urls", urls);
        return "search";
    }

}
