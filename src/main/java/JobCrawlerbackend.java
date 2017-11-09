import Scraper.JobInfo;
import Scraper.KarriereAtScraper;
import NLP.NLP;

import java.util.*;


public class JobCrawlerbackend {

    public static void main(String[] args) {
        String city = askCity();
        String keyword = askKeyword();

        // Get jobTexts
        ArrayList<String> jobTexts = scrapeKarriereAt(city, keyword);

        // Use NLP to get the frequencies of the most common tools/frameworks
        NLP nlp = new NLP();
        HashMap<String, Integer> frequencies = nlp.getFrequencyOfSkillsInJobTexts(jobTexts);

        // Clean and print frequencies HashMap
        frequencies.entrySet().removeIf(entries -> entries.getValue() == 1);

        Set<Map.Entry<String, Integer>> set = frequencies.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<>(set);
        Collections.sort(list, (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) -> o2.getValue().compareTo(o1.getValue()));
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + " ==== " + entry.getValue());
        }
    }

    private static String askCity() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("In which city are you looking for jobs?");
        return scanner.nextLine().toLowerCase();
    }

    private static String askKeyword() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter keyword, title or company");
        return scanner.nextLine().toLowerCase();
    }

    /**
     * Scrapes karriere.at for jobs in the IT domain, and collects jobs matching the city and keyword.
     * Sleep(5000) solves the issue of 501, where they block us for requesting URLs too fast.
     *
     * @param city    City that is used for the search.
     * @param keyword Keyword that is used for the search.
     * @return ArrayList of Strings containing all the jobs' title and descriptions.
     */
    private static ArrayList<String> scrapeKarriereAt(String city, String keyword) {
        KarriereAtScraper karriereScraper = new KarriereAtScraper(city, keyword);
        karriereScraper.setCity(city);
        karriereScraper.setProgrammingLanguage(keyword);
        ArrayList<String> listOfURLs = karriereScraper.getUrlsOfPagesContainingJobLists();

        ArrayList<String> jobTexts = new ArrayList<>();
        for (String pageUrl : listOfURLs) {
            ArrayList<JobInfo> jobInfos = karriereScraper.parseJobsOnPage(pageUrl);
            for (JobInfo jobInfo : jobInfos) {
                jobTexts.add(jobInfo.toString().toLowerCase());
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                return jobTexts;
            }
        }
        return jobTexts;
    }
}
