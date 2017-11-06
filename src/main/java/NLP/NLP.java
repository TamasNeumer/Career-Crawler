package NLP;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

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
    private static HashMap<String, Integer> calcualteSkillFrequencies(ArrayList<String> skills,
                                                                      ArrayList<String> jobTexts) {
        // TODO: Language detection, Tokenizing + POS Tagging -> MAtching only Nouns.
        HashMap<String, Integer> frequencies = new HashMap<>();
        for (String skill : skills) {
            for (String text : jobTexts) {
                int count = 0;
                Pattern p = Pattern.compile(escapeSpecialCharactersInRegex(skill));
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

    private static String escapeSpecialCharactersInRegex(String keyword) {
        return keyword.replaceAll("([^a-zA-Z0-9])", "\\\\$1");
    }
}
