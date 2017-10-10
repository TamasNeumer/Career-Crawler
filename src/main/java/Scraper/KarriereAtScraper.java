package Scraper;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;


public class KarriereAtScraper implements CareerWebsiteScraper {
    public ArrayList<String> getUrlsOfPagesContainingJobLists() {
        int numberOfPages = getNumberOfPages();
        ArrayList<String> jobURLPages = new ArrayList<String>();
        for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++)
        {
            jobURLPages.add(baseURL + pageNumber + itJobFilter);
        }
        return jobURLPages;
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
            System.out.println(e.getMessage());
            return 0;
        }
    }

    private WebClient webClient = createWebClient();
    private String baseURL = "https://www.karriere.at/jobs/wien?page=";
    private String itJobFilter = "&jobFields%5B%5D=2172";
}
