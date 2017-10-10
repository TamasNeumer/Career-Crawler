package Scraper;

import java.util.ArrayList;

public interface CareerWebsiteScraper {
    /**
     * The function produces a list of URLs that contain a list of jobs.
     * (https://www.karriere.at/jobs/wien?page=1, https://www.karriere.at/jobs/wien?page=2 etc.)
     * @return List of URLs that will be scraped later.
     */
    ArrayList<String> getUrlsOfPagesContainingJobLists();
}
