![](https://media.giphy.com/media/lMsEyjXXklEn1ybtYt/giphy.gif)

### Set Up
1. Fork and clone this repo.
1. Install gradle if needed: `brew install gradle`.
1. Turn on the Google Calendar API as shown in [Step 1 of Google's Java Quickstart Guide][1]. 
Download the `credentials.json` file and place it in `calendar-colorist/src/main/java src/main/resources`.
1. Modify `EventCategory.kt` to define your event categories and the desired colors for each of those categories. 
`CalendarColor.kt` lists all available colors. 
1. Modify `classify` in `EventClassifier.kt` to return an EventCategory based on information from the given Event.
1. CalendarColorist will only look at events between now and seven days into the future. 
The intent here is that you'll run this on Monday to configure your week. 
If you want to modify this behavior look at the `main` method in `CalendarColorist.kt`.
1. Run it via `gradle run`! 
The first time you run this it'll launch your web browser to get permissions.
Afterwards and on subsequent runs you'll see output like:

````
Commute to Work - created by markcerq lasting 105 minutes:
        Event classified as Commute
VX Roadmap Sync - created by forrjere lasting 30 minutes:
        Event classified as VX
Midnight Commander X Weekly - created by markcerq lasting 60 minutes:
        Event classified as Mobile
Lunch - created by markcerq lasting 60 minutes:
        Event classified as Lunch
Schedule New 2020 Meetings - created by markcerq lasting 60 minutes:
        Event could not be classified by EventClassifier!
Evan / Mark 1:1 - created by markcerq lasting 30 minutes:
        Event classified as OneOnOne
VX Show and Tell - created by apluong lasting 90 minutes:
        Event classified as VX

Weekly Review - minutes spent per category:
        {Commute=780, Mobile=335, OneOnOne=320, Lunch=300, VE=270, VX=240, Recruiting=180}
````

Worth noting:
 * Events that cannot be classified by `EventClassifier.classify` will show `Event could not be classified by EventClassifier!` below them.
 * At the end of the output you'll see how many minutes you will spend over the coming week for each category. 

### Need help? Got an improvement?
If you need help reach out on [Twitter][2]. If you want to make an improvement, PRs are always welcome! Cheers!

[1]: https://developers.google.com/calendar/quickstart/java
[2]: https://twitter.com/markmcerqueira
