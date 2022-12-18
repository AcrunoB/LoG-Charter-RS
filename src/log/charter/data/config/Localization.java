package log.charter.data.config;

import java.util.HashMap;
import java.util.Map;

import log.charter.gui.menuHandlers.CharterMenuBar;
import log.charter.util.RW;

public class Localization {
	public enum Label {
		BUTTON_CANCEL("Cancel"), //
		BUTTON_SAVE("Save"), //

		CONFIG_PANE("Config"), //
		CONFIG_MUSIC_FOLDER("Music folder"), //
		CONFIG_SONGS_FOLDER("Songs folder"), //
		CONFIG_MINIMAL_NOTE_DISTANCE("Minimal note distance"), //
		CONFIG_MINIMAL_TAIL_TO_NOTE_DISTANCE("Minimal distance between tail and next note"), //
		CONFIG_MINIMAL_TAIL_LENGTH("Minimal note tail length"), //
		CONFIG_SOUND_DELAY("Sound delay"), //
		CONFIG_MARKER_POSITION("Marker position"), //
		CONFIG_INVERT_STRINGS("Invert strings"), //

		EDIT_MENU("Edit"), //
		EDIT_MENU_UNDO("Undo"), //
		EDIT_MENU_REDO("Redo"), //
		EDIT_MENU_SELECT_ALL("Select all notes"), //
		EDIT_MENU_DELETE("Delete"), //
		EDIT_MENU_COPY("Copy"), //
		EDIT_MENU_PASTE("Paste"), //
		EDIT_MENU_SONG_OPTIONS("Song options"), //
		EDIT_MENU_GRID_OPTIONS("Grid options"), //

		FILE_MENU("File"), //
		FILE_MENU_NEW("New"), //
		FILE_MENU_OPEN("Open"), //
		FILE_MENU_OPEN_RS("Open song from RS arrangement XML"), //
		FILE_MENU_OPEN_AUDIO("Open audio file"), //
		FILE_MENU_IMPORT("Import"), //
		FILE_MENU_IMPORT_RS_GUITAR("RS guitar arrangement XML"), //
		FILE_MENU_IMPORT_RS_VOCALS("RS vocals arrangement XML"), //
		FILE_MENU_SAVE("Save"), //
		FILE_MENU_SAVE_AS("Save as..."), //
		FILE_MENU_EXIT("Exit"), //
		FILE_MENU_OPTIONS("Options"), //

		GRID_PANE("Grid options"), //
		GRID_PANE_GRID_SIZE("Grid size"), //
		GRID_PANE_USE_GRID("Use grid"), //

		GUITAR_MENU("Guitar"), //
		GUITAR_MENU_TOGGLE_MUTES("Toggle mutes"), //
		GUITAR_MENU_TOGGLE_HOPO("Toggle HO/PO"), //
		GUITAR_MENU_SET_SLIDE("Set slide"), //
		GUITAR_MENU_TOGGLE_LINK_NEXT("Toggle link next"), //

		INFO_MENU("Info"), //
		INFO_MENU_VERSION("Version"), //
		INFO_MENU_LANGUAGE("Language"), //

		INSTRUMENT_MENU("Instrument"), //
		INSTRUMENT_MENU_TOGGLE_WAVEFORM("Toggle waveform drawing"), //
		INSTRUMENT_MENU_TOGGLE_CLAPS("Toggle claps on note"), //
		INSTRUMENT_MENU_TOGGLE_METRONOME("Toggle metronome"), //

		NOTES_MENU("Notes"), //
		NOTES_MENU_SNAP("Snap notes to grid"), //
		NOTES_MENU_DOUBLE_GRID("Double grid resolution"), //
		NOTES_MENU_HALVE_GRID("Halve grid resolution"), //

		SLIDE_PANE("Slide options"), //
		SLIDE_PANE_FRET("Slide to fret"), //
		SLIDE_PANE_PITCHED("Pitched"), //

		SONG_OPTIONS_PANE("Song options"), //
		SONG_OPTIONS_TITLE("Title"), //
		SONG_OPTIONS_ARTIST_NAME("Artist name"), //
		SONG_OPTIONS_ARTIST_NAME_SORTING("Artist name (sorting)"), //
		SONG_OPTIONS_ALBUM("Album"), //
		SONG_OPTIONS_YEAR("Year"), //
		SONG_OPTIONS_CROWD_SPEED("Crowd speed"), //

		VOCAL_PANE_CREATION("Vocal creation"), //
		VOCAL_PANE_EDIT("Vocal edit"), //
		VOCAL_PANE_LYRIC("Lyric"), //
		VOCAL_PANE_WORD_PART("Word part"), //
		VOCAL_PANE_PHRASE_END("Phrase end"), //

		VOCALS_MENU("Vocals"), //
		VOCALS_MENU_EDIT_VOCALS("Edit selected vocals"), //
		VOCALS_MENU_TOGGLE_WORD_PART("Toggle word part"), //
		VOCALS_MENU_TOGGLE_PHRASE_END("Toggle phrase end"), //

		CHOOSE_FOLDER_NAME("Choose folder name"), //
		COULDNT_LOAD_ARRANGEMENT("Couldn't load arrangement"), //
		DIRECTORY_DOESNT_EXIST("Directory doesn't exist"), //
		EXIT_POPUP("Exit"), //
		EXIT_MESSAGE("Are you sure you want to exit?"), //
		FOLDER_EXISTS_CHOOSE_DIFFERENT("Given folder already exists, choose different name"), //
		MP3_OR_OGG_FILE("Mp3 (.mp3) or Ogg (.ogg) file"), //
		MUSIC_FILE_COULDNT_BE_LOADED("Music file couldn't be loaded"), //
		MUSIC_DATA_NOT_FOUND(
				"Music file not found in song folder, something went wrong with copying or the file is invalid"), //
		MUSIC_FILE_NOT_FOUND_PICK_NEW("Music file not found in song folder, please choose new file"), //
		NO_PROJECT("No project"), //
		NOT_A_FOLDER("Given path is not a folder"), //
		NOT_MP3_OGG("Not an Mp3 or Ogg file!"), //
		OPERATION_CANCELLED("Operation cancelled"), //
		PROJECT_IS_NEWER_VERSION("Project is newer version than program handles"), //
		ROCKSMITH_CHART_PROJECT("Rocksmith Chart Project"), //
		RS_ARRANGEMENT_FILE("RS arrangmenet file (XML)"), //
		UNSAVED_CHANGES_POPUP("Unsaved changes"), //
		UNSAVED_CHANGES_MESSAGE("You have unsaved changes. Do you want to save?"), //
		UNSUPPORTED_FILE_TYPE("This file type is not supported"), //
		VALUE_MUST_BE_GE("Value must be greater or equal to %s"), //
		VALUE_MUST_BE_LE("Value must be lesser or equal to %s"), //
		VALUE_NUMBER_EXPECTED("Number expected"), //
		WRONG_MUSIC_FILE("Wrong music file"),//
		;

		private final String defaultLabel;

		private Label(final String defaultLabel) {
			this.defaultLabel = defaultLabel;
		}

		public String label() {
			return labels.getOrDefault(name(), defaultLabel);
		}
	}

	public static final String languagesFolder = "languages/";

	private static Map<String, String> labels;

	public static void init() {
		final StringBuilder localizationFileBuilder = new StringBuilder();
		for (final Label label : Label.values()) {
			localizationFileBuilder.append(label.name()).append('=').append(label.defaultLabel).append('\n');
		}
		RW.write(languagesFolder + "English.txt", localizationFileBuilder.toString());

		readLocalizationFile();
	}

	private static void readLocalizationFile() {
		try {
			labels = RW.readConfig(languagesFolder + Config.language + ".txt");
		} catch (final Exception e) {
			Config.language = "English";
			labels = new HashMap<>();
		}
	}

	public static void changeLanguage(final String newLanguage, final CharterMenuBar charterMenuBar) {
		Config.language = newLanguage;
		Config.changed = true;

		readLocalizationFile();
		charterMenuBar.refreshMenus();
	}
}
