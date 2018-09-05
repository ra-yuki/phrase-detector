package rmafia.phraseditector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import rmafia.phraseditector.helpers.DBSchemaHelper;

@SpringBootApplication
public class PhraseDitectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhraseDitectorApplication.class, args);
    }
}

// == Legacy (manual video registration) ==
//        DBSchemaHelper helper = applicationContext.getBean(DBSchemaHelper.class);
//        helper.addNewVideo("5nlG0svf9t4", 1, "COLLEGE KIDS REACT TO TAYLOR SWIFT - REPUTATION (Full Album Reaction)");
//        helper.addNewVideo("foTjlGz-u50", 1, "YouTubers React To Try To Watch This Without Laughing or Grinning #10");
//        helper.addNewVideo("brCmJxyQkNk", 1, "KIDS REACT TO TRY NOT TO MOVE CHALLENGE #2");
//        helper.addNewVideo("0bYETPZXi7w", 1, "DO TEENS KNOW 2000s ANIME? (REACT: Do They Know It?)");
//        //new
//        helper.addNewVideo("ouUoGUqjI2E", 1, "KIDS MAKE JAPANESE CANDY (Popin' Cookin') | Kids Vs. Food");
//        helper.addNewVideo("qxoSGAt4SNc", 1, "KIDS REACT TO TOP 10 TOYS OF ALL TIME (200th Episode!)");
//        helper.addNewVideo("0LEFjquoTxY", 1, "YOUTUBERS REACT TO TOP GIFS OF 2017");
//        helper.addNewVideo("PeARpcDimx4", 1, "YOUTUBERS REACT TO BABYMETAL");
//        helper.addNewVideo("lgBn6qzMlXw", 1, "COLLEGE KIDS REACT TO TOP 10 MOST SUBSCRIBED YOUTUBERS OF ALL TIME");
//        helper.addNewVideo("eGR8VIvtxU4", 1, "COLLEGE KIDS REACT TO FALL OUT BOY");