import com.tranbaquan.voice.command.audio.AudioPlayer;
import com.tranbaquan.voice.command.classify.lloyd.CodeBook;
import com.tranbaquan.voice.command.classify.lloyd.QuantizationVector;
import com.tranbaquan.voice.command.feature.FeatureExtractor;
import com.tranbaquan.voice.command.feature.FeatureVector;

public class VoiceCommandRunner {
    public static void main(String[] args) {
        String audioPath = "E:\\Music\\all-the-way.wav";
        AudioPlayer player = new AudioPlayer(audioPath);
        FeatureExtractor extractor = new FeatureExtractor(player);
        extractor.extractFeature();
        FeatureVector vector = extractor.getFeatureVector();
        QuantizationVector quantization = new QuantizationVector(13);
        for (int i = 0; i < vector.getMfcc().length; i++) {
            quantization.addFeature(vector.getMfcc()[i]);
        }
        CodeBook codeBook = new CodeBook();
        codeBook.setQuantization(quantization);

        codeBook.initialize();
//        System.out.println(player.getFormat().getSampleRate());
//        player.play();
//        PCM pcm = new PCM();
//        float[] pcmData = pcm.nomalize(player.getFloatsData());
//        player.play(pcmData);
//        PreEmphasisFunction preEmphasis = new PreEmphasisFunction();
//        float[] f = preEmphasis.transform(pcmData);
//        FramingFunction frame = new FramingFunction();
//        float[][] framed = frame.framing(f, player.getFormat().getSampleRate());
//        WindowFunction window = new HammingWindow();
//        FastFourierTransform fourier = new FastFourierTransform();
//        FilterBanks filterBanks = new FilterBanks();
//        DiscreteCosineTransform discreteCosineTransform = new DiscreteCosineTransform();
//        for (int i = 0; i < framed.length; i++) {
//            float[] windowed = window.windowing(framed[i]);
//            Complex[] fouriered = fourier.transform(windowed);
//            float[] f1 = fourier.toMagnitude(fouriered);
//            float[] f2 = filterBanks.doFilterBanks(f1, player.getFormat().getSampleRate());
//            float[] f3 = discreteCosineTransform.transform(f2);
//            System.out.println(Arrays.toString(f3));
//        }

//        FastFourierTransform fourier = new FastFourierTransform();
//        float[] h = new float[]{-0.03480425839330703f,0.07910192950176387f,0.7233322451735928f,0.1659819820667019f};
//        Complex[] k1 = fourier.transform(h);
//        Complex[] k2 = fourier.invert(k1);
//        System.out.println(Arrays.toString(k1));
//        System.out.println(Arrays.toString(k2));

    }
}
