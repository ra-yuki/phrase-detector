package rmafia.phraseditector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller

public class PageController {
    @Autowired
    WordRepository wordRepository;
    @Autowired
    VideoRepository videoRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Word> words = wordRepository.findAll();
        List<Video> videos = videoRepository.findAll();
        model.addAttribute("words",words);
        model.addAttribute("videos",videos);
        return "index";
    }

    @PostMapping("/search")
    public String search(Model model) {

    }

}
