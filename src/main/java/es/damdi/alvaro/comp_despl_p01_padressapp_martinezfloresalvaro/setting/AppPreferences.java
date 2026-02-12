package es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.setting;

import java.util.Optional;
import java.util.prefs.Preferences;

public final class AppPreferences {
    private static final Preferences PREFS =
            Preferences.userRoot().node("es.damdi.isabel.addressappv2");
    private static final String KEY_PERSON_FILE = "personFile";
    private AppPreferences() {}
    public static Optional<String> getPersonFile() {
        String v = PREFS.get(KEY_PERSON_FILE, null);
        return (v == null || v.isBlank()) ? Optional.empty() : Optional.of(v);
    }
    public static void setPersonFile(String absolutePathOrNull) {
        if (absolutePathOrNull == null || absolutePathOrNull.isBlank()) {
            PREFS.remove(KEY_PERSON_FILE);
        } else {
            PREFS.put(KEY_PERSON_FILE, absolutePathOrNull);
        }
    }
}