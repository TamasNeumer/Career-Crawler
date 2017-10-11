package Scraper;

import java.util.ArrayList;

import Scraper.KarriereAtScraper;

public interface CareerWebsiteScraper {
    /**
     * The function produces a list of URLs that contain a list of jobs.
     * (https://www.karriere.at/jobs/wien?page=1, https://www.karriere.at/jobs/wien?page=2 etc.)
     * @return List of URLs that will be scraped later.
     */
    ArrayList<String> getUrlsOfPagesContainingJobLists();

    /**
     * Parses all the jobs on the given page
     * @param url URL that contains the job lists
     * @return A lost of JobInfo objects containing each job's details
     */
    ArrayList<JobInfo> parseJobsOnPage(String url);
}
