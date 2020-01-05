/**
 * StringExtension.kt
 *  Extension functions on String to make logic in EventClassifier more succinct.
 */
fun String.containsFromSet(vararg strings: String): Boolean {
    for (string in strings) {
        if (this.contains(string, ignoreCase = true)) {
            return true
        }
    }
    return false
}
