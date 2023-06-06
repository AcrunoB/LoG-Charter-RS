package log.charter.gui.panes;

import static log.charter.gui.components.TextInputSelectAllOnFocus.addSelectTextOnFocus;
import static log.charter.gui.components.TextInputWithValidation.ValueValidator.createBigDecimalValidator;

import java.io.File;
import java.math.BigDecimal;

import javax.swing.JTextField;

import log.charter.data.ChartData;
import log.charter.data.config.Localization.Label;
import log.charter.gui.CharterFrame;
import log.charter.gui.components.ParamsPane;
import log.charter.sound.MusicData;
import log.charter.sound.StretchedFileLoader;
import log.charter.sound.ogg.OggWriter;

public class AddSilencePane extends ParamsPane {
	private static final long serialVersionUID = -4754359602173894487L;

	private static PaneSizes getSizes() {
		final PaneSizes sizes = new PaneSizes();
		sizes.labelWidth = 80;
		sizes.width = 250;

		return sizes;
	}

	private final ChartData data;

	private BigDecimal time;

	public AddSilencePane(final CharterFrame frame, final ChartData data) {
		super(frame, Label.SLIDE_PANE, 4, getSizes());
		this.data = data;

		addLabel(0, 20, Label.ADD_SILENCE_SECONDS);

		addConfigValue(1, 20, 0, null, "", 100,
				createBigDecimalValidator(new BigDecimal(0.1), new BigDecimal(1000), false),
				val -> time = new BigDecimal(val), false);
		final JTextField input = (JTextField) components.getLast();
		addSelectTextOnFocus(input);

		addDefaultFinish(3, this::saveAndExit);
	}

	private void saveAndExit() {
		final MusicData songMusicData = data.music;
		data.music = MusicData.generateSilence(time.doubleValue(), songMusicData.outFormat.getSampleRate())//
				.join(songMusicData);

		if (!data.songChart.musicFileName.endsWith(".ogg")) {
			data.songChart.musicFileName = "guitar.ogg";
		}

		OggWriter.writeOgg(new File(data.path, data.songChart.musicFileName).getAbsolutePath(), data.music);

		StretchedFileLoader.stopAllProcesses();
		for (final File oldWav : new File(data.path).listFiles(s -> s.getName().matches("guitar_(tmp|[0-9]*).wav"))) {
			oldWav.delete();
		}
	}
}