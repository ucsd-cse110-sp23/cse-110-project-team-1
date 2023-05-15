import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/*
 * Jrecord is an audio recorder that saves audio in a WAV file named record.wav
 * Call Start method to start recording
 * Call finish method to stop recording
 * @default constructor sets Format to WAVE and file to audioFile/record.wav
 * @param AudioFileFormat.Type for file type, 
 * @param String for path string 
*/
public class JRecorder {

    //Customizable fields
    AudioFileFormat.Type fileType;
    File audioFile;

    //Passing fields
    TargetDataLine targetLine = null;
    Thread audioThread = null;
    SafeAudioThreading audioRunnable = null;

    /*
     * @param type for audio file type 
     * @param filePath for path to store audio file
     */
    public JRecorder(AudioFileFormat.Type type, String filePath) {
        fileType = type;
        audioFile = new File(filePath);
    }
    /*
     * Default constructor 
     * Sets type to WAV
     * Sets file path to audioFile/wav
     */
    public JRecorder() {
        fileType = AudioFileFormat.Type.WAVE;
        audioFile = new File("audioFiles/record.wav");
    }

    /*
     * audioformat method pre-sets format 
     * Arranges raw data audio in this format 
     * If you want to change values please use new AudioFormat constructor instead
     */
    AudioFormat audioFormat() {
        float sampleRate = 44100;                           //how fast audio is read
        int sampleSizeInBits = 16;                          //how much of the audio to take
        int channels = 2;                                   //1 for mono 2 for stereo
        int frameSize = sampleSizeInBits * channels / 8;    
        float frameRate = 44100;
        boolean bigEndian = false;                          //data format/ WAV is littleEndian
        AudioFormat format = new AudioFormat(               //Constructs format
                AudioFormat.Encoding.PCM_SIGNED,    
                sampleRate, 
                sampleSizeInBits, 
                channels, 
                frameSize,
                frameRate, 
                bigEndian
        );
        return format;
    }

    public boolean start(){
        try {
            AudioFormat format = audioFormat();                                               //Custom audio Format
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);   //DataLine is a handy class that describes lines and formats
            
            //In case audiosystem used is not supported
            if (!AudioSystem.isLineSupported(info)) {           
                System.out.println("Line not supported");
                System.exit(0);
            }

            //Opens audio line to take in audio
            targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open();                                  // Doesn't record just intializes all ssytem resources for recording

            System.out.println("Starting Capturing...");
            targetLine.start();

            //Safe threading that does not rely on Thread.sleep to close
            audioRunnable = new SafeAudioThreading();
            //audio thread since AudioSystem.write will halt all operations after it
            audioThread = new Thread(audioRunnable);
            audioThread.start();
            return true;
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /*
     * Method to finish recording
     * Simply call after start 
     * Make sure to call start first
     */
    public void finish() {
        if (audioThread != null) {
            audioRunnable.terminate();
            try {
                audioThread.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Could not stop audio recording");
            }
        }
        if (targetLine == null) {
            return;
        }
        targetLine.stop();
        targetLine.close();
        System.out.println("Finished Recording");
    }

    /*
     * Safe threading that does not rely on thread.sleep to close
     */
    private class SafeAudioThreading implements Runnable {
        private volatile boolean running = true;
        public void terminate() {
            running = false;
        }
        /*
         * Thread run method when thread.start() is called
         */
        @Override
        public void run() {
            while (running) {
                try {
                    //Defines new audio input stream, takes in audio
                    AudioInputStream audioStream = new AudioInputStream(targetLine);

                    System.out.println("Starting Recording...");

                    //Writes audio to file
                    AudioSystem.write(audioStream,fileType, audioFile);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    running = false;
                }
            }
        }
    }
}

