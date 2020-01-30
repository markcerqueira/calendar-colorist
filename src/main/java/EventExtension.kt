import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.Event

/**
 * EventExtension.kt
 *  Extension functions on com.google.api.services.calendar.model.Event to make it usable and user-friendly.
 */
private const val MINUTES_IN_DAY = 24 * 60

fun Event.setColor(color: CalendarColor) {
    colorId = color.colorId
}

fun Event.isAttending(attendeeUsername: String): Boolean {
    return attendees?.find { it.email.startsWith(attendeeUsername) }?.responseStatus.equals("accepted")
}

@Suppress("SENSELESS_COMPARISON")
val Event.startTimeSinceEpoch: Long
    get() {
        var eventStart: DateTime? = start.dateTime
        if (eventStart == null) {
            eventStart = start.date
        }
        return eventStart?.value ?: 0
    }

@Suppress("SENSELESS_COMPARISON")
val Event.endTimeSinceEpoch: Long
    get() {
        var eventEnd: DateTime? = end.dateTime
        if (eventEnd == null) {
            eventEnd = end.date
        }
        return eventEnd?.value ?: 0
    }

val Event.isWholeDayEvent: Boolean
    get() = durationInMinutes % MINUTES_IN_DAY == 0

val Event.durationInMinutes: Int
    get() = ((endTimeSinceEpoch - startTimeSinceEpoch) / 1000 / 60).toInt()

val Event.creatorUsername: String
    get() = creator.email.replace("@justin.tv", "")

val Event.humanReadableColor: String
    get() = (CalendarColor.values().find { it.colorId == colorId } ?: CalendarColor.Lavender).name
