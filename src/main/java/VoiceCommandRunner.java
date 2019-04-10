import com.tranbaquan.voice.command.audio.AudioPlayer;
import com.tranbaquan.voice.command.transform.filter.HammingWindow;
import com.tranbaquan.voice.command.transform.filter.PreEmphasisFunction;
import com.tranbaquan.voice.command.transform.filter.WindowFunction;

public class VoiceCommandRunner {
    public static void main(String[] args) {
        String audioPath = "E:\\Music\\all-the-way.wav";
        AudioPlayer player = new AudioPlayer(audioPath);
        player.play();
        PreEmphasisFunction preEmphasis = new PreEmphasisFunction();
        float[] f = preEmphasis.transform(player.getFloatsData());
        WindowFunction window = new HammingWindow();
        f = window.windowing(f, player.getFormat().getSampleRate());
        player.play(f);
        player.writeToFile(f, "D:\\test1.wav");
    }
}
