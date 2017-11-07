import Scraper.JobInfo;
import Scraper.KarriereAtScraper;
import NLP.NLP;
import org.apache.commons.cli.*;

import java.lang.reflect.Array;
import java.util.*;



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

        //Remove elements with single hits
        frequencies.entrySet().removeIf(entries->entries.getValue() == 1);

        Set<Map.Entry<String, Integer>> set = frequencies.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(set);
        Collections.sort( list, (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 ) -> o2.getValue().compareTo(o1.getValue()));
        for(Map.Entry<String, Integer> entry:list){
            System.out.println(entry.getKey()+" ==== "+entry.getValue());
        }
        System.out.println("Done");
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
            ArrayList<JobInfo> jobInfos = karriereScraper.parseJobsOnPage(pageUrl);
            for(JobInfo ji : jobInfos){
                // TODO CLEAN! No lower case for POS!
                jobTexts.add(ji.toString().toLowerCase());
            }
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
