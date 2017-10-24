import Scraper.KarriereAtScraper;
import org.apache.commons.cli.*;
import java.util.ArrayList;


public class JobCrawlerbackend {

    public static void main(String[] args) {
        //CommandLine cmd = parseArgs(args);
        //if(cmd == null) return;

        //String chosenProgrammingLanguage = cmd.getOptionValue("p");
        String chosenProgrammingLanguage = "Java";

        KarriereAtScraper karriereScraper = new KarriereAtScraper();
        karriereScraper.setCity("wien");
        karriereScraper.setProgrammingLanguage("java");
        ArrayList<String> listOfURLs = karriereScraper.getUrlsOfPagesContainingJobLists();
        for(String pageUrl : listOfURLs)
        {
            System.out.println(karriereScraper.parseJobsOnPage(pageUrl).toString());
        }
    }

    private  static CommandLine parseArgs(String[] args){
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
    }
}
