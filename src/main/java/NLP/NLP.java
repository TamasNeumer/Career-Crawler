package NLP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.io.IOUtils;
import org.apache.tika.language.LanguageIdentifier;

public class NLP {
    public HashMap<String, Integer> getFrequencyOfSkillsInJobTexts(ArrayList<String> jobTexts) {
        ArrayList<String> skills = readSkillsFromFile();
        HashMap<String, Integer> frequencies = calcualteSkillFrequencies(skills, jobTexts);
        return frequencies;
    }

    /**
     * Reads the skills into an arraylist and returns it to the caller. Returns an empty list, if the file
     * was not found.
     *
     * @return
     */
    private ArrayList<String> readSkillsFromFile() {
        String result = "";
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream("skills.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(Arrays.asList(result.split("\\r?\\n")));
    }

    /**
     * @param skills   ArrayList of skills to be matched.
     * @param jobTexts ArrayList of jobTexts
     * @return HashMap containing the found skills (key) and the number of occurrence in text (value)
     */
    private HashMap<String, Integer> calcualteSkillFrequencies(ArrayList<String> skills,
                                                                      ArrayList<String> jobTexts) {

        ArrayList<String> cleanedJobTexts = new ArrayList<>();
        for (String text : jobTexts) {
            cleanedJobTexts.add(cleanTextForSkillRecognition(text));
        }
        // TODO: Language detection, Cleaning, Tokenizing + POS Tagging -> MAtching only Nouns.
        HashMap<String, Integer> frequencies = new HashMap<>();
        for (String skill : skills) {
            for (String text : cleanedJobTexts) {
                // LanguageCode languageOfText = getLanguageOfText(text);
                int count = 0;
                Pattern p = Pattern.compile(addSpacesAroundWord(escapeSpecialCharactersInRegex(skill)));
                Matcher m = p.matcher(text);
                while (m.find()) {
                    count += 1;
                }
                if (count != 0) {
                    if (frequencies.containsKey(skill)) {
                        Integer temp = frequencies.get(skill);
                        temp++;
                        frequencies.put(skill, temp);
                    } else
                        frequencies.put(skill, count);
                }
            }
        }
        return frequencies;
    }

    private String escapeSpecialCharactersInRegex(String keyword) {
        return keyword.replaceAll("([^a-zA-Z0-9])", "\\\\$1");
    }

    private String addSpacesAroundWord(String keyword){
        return " " + keyword + " ";
    }

    private String cleanTextForSkillRecognition(String text){
        return text.replaceAll("[\\\\\\.\\(\\)\\,\\[\\]]"," ");
    }

    private LanguageCode getLanguageOfText(String text){
        LanguageIdentifier identifier = new LanguageIdentifier(text);
        String language = identifier.getLanguage();
        switch(language){
            case "en":
                return LanguageCode.ENGLISH;
            case "de":
                return LanguageCode.GERMAN;
            default:
                return LanguageCode.UNKNOWN;
        }
    }

    private enum LanguageCode {
        ENGLISH,
        GERMAN,
        UNKNOWN
    }
}
