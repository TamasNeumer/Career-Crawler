import Scraper.KarriereAtScraper;

import java.util.ArrayList;
import Scraper.JobInfo;

public class JobCrawlerbackend {

    public static void main(String[] args) {
        KarriereAtScraper karriereScraper = new KarriereAtScraper();
        ArrayList<String> listOfURLs = karriereScraper.getUrlsOfPagesContainingJobLists();
        for(String pageUrl : listOfURLs)
        {
            ArrayList<JobInfo> result = karriereScraper.parseJobsOnPage(pageUrl);
            System.out.println("GG");
        }
    }
}
