import com.tranbaquan.voice.command.audio.AudioPlayer;
import com.tranbaquan.voice.command.filter.frame.FramingFunction;
import com.tranbaquan.voice.command.filter.window.HammingWindow;
import com.tranbaquan.voice.command.filter.emphasis.PreEmphasisFunction;
import com.tranbaquan.voice.command.filter.window.WindowFunction;
import com.tranbaquan.voice.command.transform.fourier.FastFourierTransform;
import com.tranbaquan.voice.command.transform.fourier.FourierTransform;
import com.tranbaquan.voice.command.utils.Complex;

import java.util.Arrays;

public class VoiceCommandRunner {
    public static void main(String[] args) {
//        String audioPath = "E:\\Music\\all-the-way.wav";
//        AudioPlayer player = new AudioPlayer(audioPath);
//        player.play();
//        PreEmphasisFunction preEmphasis = new PreEmphasisFunction();
//        float[] f = preEmphasis.transform(player.getFloatsData());
//        FramingFunction frame = new FramingFunction();
//        float[][] framed = frame.framing(f, player.getFormat().getSampleRate());
//        WindowFunction window = new HammingWindow();
//        FastFourierTransform fourier = new FastFourierTransform();
//        for (int i = 0; i < framed.length; i++) {
//            float[] windowed = window.windowing(framed[i]);
//            Complex[] fouriered = fourier.transform(windowed);
//        }

        FastFourierTransform fourier = new FastFourierTransform();
        float[] h = new float[]{-0.03480425839330703f,0.07910192950176387f,0.7233322451735928f,0.1659819820667019f};
        Complex[] f = new Complex[h.length];
        for (int i = 0; i < h.length; i++) {
            f[i] = new Complex(h[i]);
        }

        Complex[] k1 = fourier.transform(h);

//        Complex[] k2 = fourier.invert(k1);
        System.out.println(Arrays.toString(k1));
//        System.out.println(Arrays.toString(k2));
    }
}
