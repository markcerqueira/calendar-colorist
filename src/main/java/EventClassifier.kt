import com.google.api.services.calendar.model.Event

/**
 * EventClassifier.kt
 *  Classifier for all Event objects. Given an Event the classify method should return the EventCategory (e.g. Commute)
 *  for that event.
 */
object EventClassifier {

    fun classify(event: Event): EventCategory {
        return when {
            // 1:1s
            event.summary.containsFromSet("/ Mark 1:1", "Ned") -> EventCategory.OneOnOne

            // Commuting
            event.summary in setOf("Commute to Work", "Commute Home") -> EventCategory.Commute

            // Recruiting / Interviewing
            event.summary.contains("Interview", ignoreCase = true) -> EventCategory.Recruiting

            // Lunch
            event.summary.containsFromSet("Lunch", "Almoço") -> return EventCategory.Lunch

            // VX
            event.creatorUsername in setOf("asclarkm", "forrjere", "kllangst") -> return EventCategory.VX
            event.summary.containsFromSet("VX", "Jeremy") -> return EventCategory.VX

            // VE
            event.summary.containsFromSet("Viewer Engagement", "Sharmeen Office") -> EventCategory.VE
            event.summary.containsFromSet("Community Points", "Channel Points") -> EventCategory.VE
            event.summary.containsFromSet("Communications Roadmap", "Trust & Safety") -> EventCategory.VE

            // Mobile
            event.summary.containsFromSet("Mobile", "Midnight Commander X", "Release Planning", "Weekly Documents Update", "Monday Manager Tasks") -> EventCategory.Mobile
            event.creatorUsername in setOf("perezmin", "ravirani") -> EventCategory.Mobile

            else -> EventCategory.Unclassified
        }
    }
}
