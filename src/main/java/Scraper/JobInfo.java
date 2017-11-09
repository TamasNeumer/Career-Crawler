package Scraper;

public class JobInfo {
    protected String jobTitle;
    protected String jobDescription;

    public String toString() {
        return "Title: " + this.jobTitle + " Description: " + this.jobDescription;
    }
}
