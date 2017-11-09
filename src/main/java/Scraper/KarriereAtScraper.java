package Scraper;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.WebClient;

public class KarriereAtScraper implements CareerWebsiteScraper {

    /**
     * Constructor, taking city and search keywords as arguments
     *
     * @param city    City in which the job will be searched for.
     * @param keyword Keywords that will be searched for.
     */
    public KarriereAtScraper(String city, String keyword) {
        this.city = city;
        this.programmingLanguage = keyword;
    }

    /**
     * Searches for city and keyword and returns a list of URLs that point to "search-result" pages.
     * (i.e. www...page=1, page=2...)
     *
     * @return List of page URLs
     */
    public ArrayList<String> getUrlsOfPagesContainingJobLists() {
        int numberOfPages = getNumberOfPages();
        ArrayList<String> jobURLPages = new ArrayList<String>();
        for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++) {
            jobURLPages.add(baseURL + "/" + programmingLanguage + "/" + city + pageNumberFilter + pageNumber + itJobFilter);
        }
        return jobURLPages;
    }

    /**
     * Parses all the jobs (title + description) that are on the given page.
     *
     * @param url URL that contains the job lists
     * @return List of JobInfos containing title+description for all the jobs on the URL.
     */
    public ArrayList<JobInfo> parseJobsOnPage(String url) {
        List<HtmlElement> jobTitleHtmlElements = getListOfJobTitlesOnPage(webClient, url);
        if (jobTitleHtmlElements.isEmpty()) {
            System.out.println("No JobTitles found");
            return new ArrayList<JobInfo>();
        }

        ArrayList<JobInfo> jobs = new ArrayList<JobInfo>();
        for (HtmlElement jobElement : jobTitleHtmlElements) {
            JobInfo job = createJobInfoWithTitle(jobElement);
            fillJobInfoWithDescription(webClient, jobElement.getAttribute("href"), job);
            jobs.add(job);
        }
        return jobs;
    }


    private WebClient createWebClient() {
        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        return webClient;
    }

    /**
     * Searches for city + keywords and returns the number of pages found for the query.
     *
     * @return Number of pages found for the query.
     */
    private int getNumberOfPages() {
        try {
            String pageNumber = "1";
            HtmlPage page = webClient.getPage(baseURL + "/" + programmingLanguage + "/" + city + pageNumberFilter + pageNumber + itJobFilter);
            HtmlElement htmlSpanContainingPageNumber = page.getFirstByXPath("//span[@class='m-pagination__meta']");
            String[] pageNumbers = htmlSpanContainingPageNumber.asText().split("von");
            return Integer.parseInt(pageNumbers[1].replaceAll("\\s", "").replaceAll(",", ""));
        } catch (Exception e) {
            System.out.println("span[@class='m-pagination__meta'] not found on the website. Exception: " + e.getMessage());
            System.out.println("Continuing, assuming that all the results fit on one page.");
            return 1;
        }
    }

    /**
     * Parses the given URL for jobtitles (HTML elements) that also contain a link (@href) to their description.
     *
     * @param webClient
     * @param url       URL to be scraped for job titles
     * @return List of job-title HTML elements.
     */
    private List<HtmlElement> getListOfJobTitlesOnPage(WebClient webClient, String url) {
        try {
            HtmlPage currentPage = webClient.getPage(url);
            return (List<HtmlElement>) currentPage.getByXPath("//a[@class='m-jobItem__titleLink']");
        } catch (Exception e) {
            System.out.println("a[@class='m-jobItem__titleLink'] not found on the website. Exception:" + e.getMessage());
        }
        return new ArrayList<HtmlElement>();
    }

    /**
     * Creates a JobInfo object with it's title filled by the passed jobElement's (HtmlElement) title.
     *
     * @param jobElement HTMLElement containing the job title and the link to the description.
     * @return JobInfo containing the job's title
     */
    private JobInfo createJobInfoWithTitle(HtmlElement jobElement) {
        JobInfo newJob = new JobInfo();
        newJob.jobTitle = jobElement.asText();
        return newJob;
    }

    /**
     * Scrapes the jobDescriptionURL for the description text and adds it to the JobInfo object.
     *
     * @param webClient         WebClient executing the scraping
     * @param jobDescriptionUrl URL containing the job description
     * @param job               JobInfo object filled with the job's description
     */
    private void fillJobInfoWithDescription(WebClient webClient, String jobDescriptionUrl, JobInfo job) {
        try {
            HtmlPage currentPage = webClient.getPage(jobDescriptionUrl);
            List<HtmlElement> jobDescription = (List<HtmlElement>)
                    currentPage.getByXPath("//div[@class='m-jobContent__jobText']");
            job.jobDescription = jobDescription.get(0).asText();
        } catch (Exception e) {
            System.out.println("div[@class='m-jobContent__jobText'] not found in the jobDescription" + e.getMessage());
        }
    }


    /**
     * Encodes the keyword(s) passed as arguments to match karriere.at's URL expectations
     *
     * @param keyword name to encode
     * @return Encoded string / keyword.
     */
    private String encodeKeywordToURL(String keyword) {
        try {
            // Special replacement for karriere.at URLs
            keyword = keyword.replaceAll(" ", "-");
            return URLEncoder.encode(keyword, "UTF-8");
        } catch (Exception e) {
            return keyword;
        }
    }

    // Example: https://www.karriere.at/jobs/java/wien?page=2&jobFields%5B%5D=2172
    private WebClient webClient = createWebClient();
    private String baseURL = "https://www.karriere.at/jobs";
    private String itJobFilter = "&jobFields%5B%5D=2172";
    private String pageNumberFilter = "?page=";


    private String programmingLanguage;

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = encodeKeywordToURL(programmingLanguage);
    }

    private String city;

    public void setCity(String city) {
        this.city = encodeKeywordToURL(city);
    }
}