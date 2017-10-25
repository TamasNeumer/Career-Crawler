import Scraper.KarriereAtScraper;
import org.apache.commons.cli.*;

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

        KarriereAtScraper karriereScraper = new KarriereAtScraper(city, keyword);
        karriereScraper.setCity(city);
        karriereScraper.setProgrammingLanguage(keyword);
        ArrayList<String> listOfURLs = karriereScraper.getUrlsOfPagesContainingJobLists();

        ArrayList<String> jobTexts = new ArrayList<>();
        for(String pageUrl : listOfURLs)
        {
            jobTexts.add(karriereScraper.parseJobsOnPage(pageUrl).toString().toLowerCase());
        }

        ArrayList<String> skills;
        try{
           skills = getSkillsFromFile("/home/puppy/IdeaProjects/JobCrawlerbackend/skills.txt");
        } catch (Exception e){
            System.out.println("Couldn't open the file containing the skills! " + e.getMessage());
            return;
        }

        HashMap<String, Integer> frequencies = new HashMap<>();

        for(String skill : skills){
            for(String text: jobTexts){
                int count = 0;
                Pattern p = Pattern.compile(skill);
                Matcher m = p.matcher(text);
                while (m.find()){
                    count +=1;
                }
                if(count != 0){
                    if(frequencies.containsKey(skill)){
                        Integer temp = frequencies.get(skill);
                        temp++;
                        frequencies.put(skill, temp);
                    }
                    else
                        frequencies.put(skill, count);
                }
            }
        }
        System.out.println(frequencies);
        System.out.println("GG");
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

    private static ArrayList<String> getSkillsFromFile(String pathToFile) throws IOException {
      return new ArrayList<>(Files.readAllLines(Paths.get(pathToFile)));
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
