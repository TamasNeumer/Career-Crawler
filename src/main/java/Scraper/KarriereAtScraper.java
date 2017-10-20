package Scraper;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.WebClient;


public class KarriereAtScraper implements CareerWebsiteScraper {
    public ArrayList<String> getUrlsOfPagesContainingJobLists() {
        int numberOfPages = getNumberOfPages();
        ArrayList<String> jobURLPages = new ArrayList<String>();
        for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++) {
            jobURLPages.add(baseURL + pageNumber + itJobFilter);
        }
        return jobURLPages;
    }

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

    private int getNumberOfPages() {
        try {
            String pageNumber = "1";
            HtmlPage page = webClient.getPage(baseURL + pageNumber + itJobFilter);
            HtmlElement htmlSpanContainingPageNumber = page.getFirstByXPath("//span[@class='m-pagination__meta']");
            String[] pageNumbers = htmlSpanContainingPageNumber.asText().split("von");
            return Integer.parseInt(pageNumbers[1].replaceAll("\\s", "").replaceAll(",", ""));
        } catch (Exception e) {
            System.out.println("span[@class='m-pagination__meta'] not found on the website. Exception: " + e.getMessage());
            return 0;
        }
    }

    private List<HtmlElement> getListOfJobTitlesOnPage(WebClient webClient, String url) {
        try {
            HtmlPage currentPage = webClient.getPage(url);
            return (List<HtmlElement>) currentPage.getByXPath("//a[@class='m-jobItem__titleLink']");
        } catch (Exception e) {
            System.out.println("a[@class='m-jobItem__titleLink'] not found on the website. Exception:" + e.getMessage());
        }
        return new ArrayList<HtmlElement>();
    }

    private JobInfo createJobInfoWithTitle(HtmlElement jobElement) {
        JobInfo newJob = new JobInfo();
        newJob.jobTitle = jobElement.asText();
        return newJob;
    }

    private void fillJobInfoWithDescription(WebClient webClient, String jobDescriptionUrl, JobInfo job) {
        try {
            HtmlPage currentPage = webClient.getPage(jobDescriptionUrl);
            List<HtmlElement> jobDescription = (List<HtmlElement>)
                    currentPage.getByXPath("//div[@class='m-jobContent__jobText']");
            job.jobDecription = jobDescription.get(0).asText();
        } catch (Exception e) {
            System.out.println("div[@class='m-jobContent__jobText'] not found in the jobDescription" + e.getMessage());
        }
    }

    private WebClient webClient = createWebClient();
    private String baseURL = "https://www.karriere.at/jobs/wien?page=";
    private String itJobFilter = "&jobFields%5B%5D=2172";
}
