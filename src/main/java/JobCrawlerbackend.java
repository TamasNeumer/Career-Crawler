import Scraper.KarriereAtScraper;

import java.util.ArrayList;

public class JobCrawlerbackend {

    public static void main(String[] args) {
        KarriereAtScraper karriereScraper = new KarriereAtScraper();
        ArrayList<String> listOfURLs = karriereScraper.getUrlsOfPagesContainingJobLists();
        System.out.println("haha");
    }
}
