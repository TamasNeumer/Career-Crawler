import Scraper.KarriereAtScraper;
import NLP.NLP;
import org.apache.commons.cli.*;

import java.lang.reflect.Array;
import java.util.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobCrawlerbackend {

    public static void main(String[] args) {
        /*CommandLine cmd = parseArgs(args);
        if(cmd == null) return;
        String chosenProgrammingLanguage = cmd.getOptionValue("p");*/

        String city = askCity();
        String keyword = askKeyword();

        ArrayList<String> jobTexts = scrapeKarriereAt(city, keyword);
        NLP nlp = new NLP();
        HashMap<String, Integer> frequencies = nlp.getFrequencyOfSkillsInJobTexts(jobTexts);
        System.out.println(frequencies);
        System.out.println("Ended");
    }

    private static String askCity(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("In which city are you looking for jobs?");
        return scanner.nextLine().toLowerCase();
    }


    private static String askKeyword(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter keyword, title or company");
        return scanner.nextLine().toLowerCase();
    }

    /**
     * Scrapes karriere.at for jobs in the IT domain, and collects jobs matching the city and keyword
     * @param city City that is used for the search.
     * @param keyword Keyword that is used for the search.
     * @return ArrayList of Strings containing all the jobs' title and descriptions.
     */
    private static ArrayList<String> scrapeKarriereAt(String city, String keyword){
        KarriereAtScraper karriereScraper = new KarriereAtScraper(city, keyword);
        karriereScraper.setCity(city);
        karriereScraper.setProgrammingLanguage(keyword);
        ArrayList<String> listOfURLs = karriereScraper.getUrlsOfPagesContainingJobLists();

        ArrayList<String> jobTexts = new ArrayList<>();
        for(String pageUrl : listOfURLs)
        {
            jobTexts.add(karriereScraper.parseJobsOnPage(pageUrl).toString().toLowerCase());
        }
        return jobTexts;
    }

    /*private  static CommandLine parseArgs(String[] args){
        Options options = new Options();

        Option programmingLanguage = new Option("p", "programmingLanguage",
                true, "Name of the programming language, that is used to search for jobs.");
        programmingLanguage.setRequired(true);
        options.addOption(programmingLanguage);

        Option jsonOutputFile = new Option("j", "jsonOutputFile", true,
                "Output file containing the full jobs (title + description) in json format.");
        programmingLanguage.setRequired(false);
        options.addOption(jsonOutputFile);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try{
            cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e){
            System.out.println(e.getMessage());
            formatter.printHelp("JobCrawler", options);

            System.exit(1);
        }
        return null;
    }*/
}
