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

            println("${event.summary} - created by ${event.creatorUsername} lasting ${event.durationInMinutes} minutes:")

            EventClassifier.classify(event)?.let {
                println("\tEvent classified as $it")

                // Only update the color on Google Calendar if it's not correct
                if (event.colorId != it.calendarColor.colorId) {
                    event.setColor(it.calendarColor)
                    GoogleCalendar.patchEvent(event)
                }

                // Add duration of event to our map tracking category -> time spent
                categoryTimeSpentMap.put(it, categoryTimeSpentMap.getOrDefault(it, 0) + event.durationInMinutes)
            } ?: run {
                // TODO This is printing even when EventClassifier.classify above returns non-null
                println("\tEvent could not be classified by EventClassifier!")
            }
        }

        println("\nWeekly Review - minutes spent per category:\n\t${categoryTimeSpentMap.toList().sortedBy { (_, value) -> -value }.toMap()}")
    }
}
