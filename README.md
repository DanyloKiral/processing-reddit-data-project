# Processing Reggit data project

## How to run 
1. Have **java, docker, sbt** installed.
2. Unarchive "RC_2009-04.bz2" file (or any other file with Reddit comments from http://files.pushshift.io/reddit/comments/) to 'data' folder.
3. Run **start.sh** script.
4. To check statistics - perform HTTP GET request to http://localhost:1234/statistics.

NOTE: Services and Kafka start process could take a while.

## Statistics result
```json
{
    "languageStatistics": [
        {
            "language": "ca",
            "messagesNum": 349
        },
        {
            "language": "cs",
            "messagesNum": 27
        },
        {
            "language": "da",
            "messagesNum": 4318
        },
        {
            "language": "de",
            "messagesNum": 123
        },
        {
            "language": "en",
            "messagesNum": 47115
        },
        {
            "language": "es",
            "messagesNum": 160
        },
        {
            "language": "et",
            "messagesNum": 136
        },
        {
            "language": "fi",
            "messagesNum": 44
        },
        {
            "language": "fr",
            "messagesNum": 276
        },
        {
            "language": "hr",
            "messagesNum": 99
        },
        {
            "language": "hu",
            "messagesNum": 26
        },
        {
            "language": "id",
            "messagesNum": 166
        },
        {
            "language": "it",
            "messagesNum": 391
        },
        {
            "language": "lt",
            "messagesNum": 41
        },
        {
            "language": "lv",
            "messagesNum": 12
        },
        {
            "language": "nl",
            "messagesNum": 261
        },
        {
            "language": "no",
            "messagesNum": 124
        },
        {
            "language": "pl",
            "messagesNum": 71
        },
        {
            "language": "pt",
            "messagesNum": 96
        },
        {
            "language": "ro",
            "messagesNum": 234
        },
        {
            "language": "sq",
            "messagesNum": 156
        },
        {
            "language": "sv",
            "messagesNum": 38
        },
        {
            "language": "tl",
            "messagesNum": 267
        },
        {
            "language": "tr",
            "messagesNum": 81
        },
        {
            "language": "vi",
            "messagesNum": 11
        },
        {
            "language": "zh-tw",
            "messagesNum": 1
        }
    ],
    "sentimentStatistics": [
        {
            "sentiment": "Negative",
            "messagesNum": 157
        },
        {
            "sentiment": "Neutral",
            "messagesNum": 98
        },
        {
            "sentiment": "Positive",
            "messagesNum": 39
        }
    ],
    "top10Keywords": [
        {
            "keyword": "what",
            "messagesNum": 6313
        },
        {
            "keyword": "about",
            "messagesNum": 6441
        },
        {
            "keyword": "like",
            "messagesNum": 7597
        },
        {
            "keyword": "your",
            "messagesNum": 8045
        },
        {
            "keyword": "just",
            "messagesNum": 8068
        },
        {
            "keyword": "they",
            "messagesNum": 8616
        },
        {
            "keyword": "with",
            "messagesNum": 10893
        },
        {
            "keyword": "this",
            "messagesNum": 11542
        },
        {
            "keyword": "have",
            "messagesNum": 11592
        },
        {
            "keyword": "that",
            "messagesNum": 25147
        }
    ]
}
```
