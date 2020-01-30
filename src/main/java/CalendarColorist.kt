import java.io.IOException
import java.security.GeneralSecurityException

/**
 * CalendarColorist.kt
 *  Main entry point for CalendarColorist
 *  Adapted from Java quickstart guide here: https://developers.google.com/calendar/quickstart/java
 */
object CalendarColorist {

    private const val SEVEN_DAYS_MILLISECONDS = 7 * 24 * 60 * 60 * 1000

    @Throws(IOException::class, GeneralSecurityException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val events = GoogleCalendar.events()

        // Map used to track the total amount of time spent for each category of event
        val categoryTimeSpentMap = mutableMapOf<EventCategory, Int>()

        for (event in events.items) {
            // Don't look at events beyond 7 days from the current time
            if (event.startTimeSinceEpoch > System.currentTimeMillis() + SEVEN_DAYS_MILLISECONDS) {
                break
            }
			
			// Skip categorizing all-day events
			if (event.isWholeDayEvent) {
				println("${event.summary} - created by ${event.creatorUsername} is an all-day event; skipping classification")
				continue
			}

            val classification = EventClassifier.classify(event)

            println("${event.summary} - created by ${event.creatorUsername} (${event.durationInMinutes} mins) classified as: $classification")

            // Since the Unclassified category has a null calendarColor unclassified events will never have their color modified
            classification.calendarColor?.let {
                // Only update the color on Google Calendar if it's not correct
                if (event.colorId != it.colorId) {
                    event.setColor(it)
                    GoogleCalendar.patchEvent(event)
                }
            }

            // Add duration of event to our map tracking category -> time spent but only for events marked as attending
            if (event.isAttending("markcerq")) {
	            categoryTimeSpentMap[classification] = categoryTimeSpentMap.getOrDefault(classification, 0) + event.durationInMinutes                
            }
        }

        println("\nWeekly Review - minutes spent per category:\n\t${categoryTimeSpentMap.toList().sortedBy { (_, value) -> -value }.toMap()}")
    }
}
