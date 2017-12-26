package project.advancedmobileapplications.trafficlightsrecognitionandroid.interfaces;

/**
 * Created by Patryk Rutkowski on 24.10.2017.
 */

public interface TTSListener {

    void speak(String text);
    void ttsInit();
    void ttsStop();

}
