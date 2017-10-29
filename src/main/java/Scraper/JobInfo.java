package Scraper;

public class JobInfo {
    protected String jobTitle;
    protected String jobDecription;

    public String toString(){
        return "Title: " + this.jobTitle + " Description: " + this.jobDecription;
    }
}
