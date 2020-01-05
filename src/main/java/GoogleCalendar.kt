import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.Events
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader

/**
 * GoogleCalendar.kt
 *  Wrapper around the Google Calendar API.
 *  Calendar API Javadoc: https://developers.google.com/resources/api-libraries/documentation/calendar/v3/java/latest/
 */
object GoogleCalendar {

    private const val APPLICATION_NAME = "Calendar Colorist"
    private const val TOKENS_DIRECTORY_PATH = "tokens"
    private const val CREDENTIALS_FILE_PATH = "/credentials.json"

    private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()

    // If modifying these scopes, delete your previously saved tokens/ folder.
    private val SCOPES = listOf(CalendarScopes.CALENDAR)

    private val calendarService: Calendar

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    @Throws(IOException::class)
    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential { // Load client secrets.
        val credentials = GoogleCalendar::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH) ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(credentials))

        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build()

        val receiver = LocalServerReceiver.Builder().setPort(8888).build()

        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    init {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        calendarService = Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME)
                .build()
    }

    fun events(maxResults: Int = 100, timeMinimum: DateTime = DateTime(System.currentTimeMillis()), orderBy: String = "startTime"): Events {
        return calendarService.events().list("primary")
                .setMaxResults(maxResults)
                .setTimeMin(timeMinimum)
                .setOrderBy(orderBy)
                .setSingleEvents(true)
                .execute()
    }

    fun patchEvent(event: Event) {
        calendarService.events().patch("primary", event.id, event).execute()
    }
}
