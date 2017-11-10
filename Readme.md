# Carreer Crawler

[![Build Status](https://travis-ci.org/TamasNeumer/CareerCrawlerBackend.svg?branch=master)](https://travis-ci.org/TamasNeumer/CareerCrawlerBackend)

#### Goal
The purpose of this project was to found out the most commonly required technologies/frameworks/tools in relation to given programming language / keyword.

#### The project
- When starting the program asks the user for a city and a keyword. These will be used while crawling for jobs.
- Given the city & keyword the program crawls [karriere.at](https://www.karriere.at) for jobs and their description.
- The description of the found jobs are matched against a dictionary that contains the known programming languages / tools. (The list was built up using stackoverflow's top 1000 tags. [Link](https://api.stackexchange.com/docs/tags#page=1&pagesize=100&order=desc&sort=popular&filter=default&site=stackoverflow&run=true))
- When crawled through and parsed all the jobs found with the city & keyword the program outputs the frequency of frameworks/tools that apperaed in the job descriptions. For example given the city "Wien" and the keyword "Java" the top hits are the following:

```
java ==== 399
position ==== 254
design ==== 139
javascript ==== 121
com ==== 112
sql ==== 96
web ==== 80
spring ==== 79
frameworks ==== 71
mobile ==== 65
hibernate ==== 62
html ==== 59
c# ==== 58
service ==== 56
oracle ==== 55
testing ==== 55
css ==== 54
html5 ==== 49
git ==== 49
server ==== 46
maven ==== 46
rest ==== 46
```

#### Build
- `mvn package`

#### Run
- ???