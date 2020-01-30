/**
 * EventCategory.kt
 *  Enum defining the different categories that events can be classified as. Also where a category can define the
 *  desired color for its events on Google Calendar. e.g. Events classified as Commute will be colored Basil on Google
 *  Calendar.
 */
enum class EventCategory(var calendarColor: CalendarColor?) {
    Commute(CalendarColor.Basil),
    Mobile(CalendarColor.Sage),
    VX(CalendarColor.Lavender),
    VE(CalendarColor.Graphite),
    Recruiting(CalendarColor.Tomato),
    OneOnOne(CalendarColor.Banana),
    Lunch(CalendarColor.Flamingo),
    Unclassified(null)
}
