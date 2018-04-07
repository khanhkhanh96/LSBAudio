
 package newpackage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
 
public class AudioSecrets {
 
        public static void main (String [] args) throws FileNotFoundException {
                Scanner scan = new Scanner(System.in);
                int maxChar = 40000;
 
                System.out.println("What would you like to do? (enter the number)");
                System.out.println("1 - Encode a text file into an audio file");
                System.out.print("2 - Decode an audio file(you will need the key) ");
                int command = scan.nextInt();
                System.out.println();
 
                if (command == 1) {
 
                        char [] contents = new char[maxChar]; // Initialize an array of characters to read the text file into          
                        System.out.print("Enter text file name that you want to encode (must be a plain text file) : ");
                        String textToEncode = scan.next(); // The text file to read in
                        if (!textToEncode.endsWith(".txt")) // Check if the filename ends with .txt and add the extension if it doesn't
                                textToEncode += ".txt";        
 
                        File file = new File(textToEncode);
                        Scanner s = new Scanner(file);
s.useDelimiter("\\Z");
if(s.hasNext()) {
   contents = s.next().toCharArray();
}
 
                        System.out.println("Enter the audio file name that you want the text encoded into (must be a .wav file)");
                        System.out.print("or if you want a generated clip of static, type \"1\" : ");
                        double[] audioFile = getAudioFile(scan.next());

                       
                        int audioCounter = 0; 
                        for (int i = 0; i < contents.length; i++) {
                                while ((audioFile[i+audioCounter] + (double)(contents[i])/10000.0) >= 1.0) {audioCounter++;}
                                audioFile[i+audioCounter] += ((double)(contents[i]))/10000.0;                          
                        }
 


                                               
                        System.out.print("Enter a name for the output audio file with the text encoded in it : ");
                        String outAudio = scan.next();
                        if (!outAudio.endsWith(".wav")) 
                                outAudio += ".wav";
                       
                        StdAudio.save(outAudio, audioFile);
                        scan.close();
                }
               
               
                if (command == 2) {
                        System.out.print("Enter audio file name that you want to decode (must be a wav file) : ");
                        String audioToDecode = scan.next();
                        
                        if (!audioToDecode.endsWith(".wav")) 
                                audioToDecode += ".wav";
 
                        System.out.print("Enter audio file that is the decoder key (must be a wav file) : ");
                        String audioKey = scan.next();
                        //System.out.println();
                        if (!audioKey.endsWith(".wav"))
                                audioKey += ".wav";
 
                        double[] audioEncoded = StdAudio.read(audioToDecode);
                        double[] key = StdAudio.read(audioKey);
                        char [] contents = new char[maxChar];
                       
 
                        int contentCounter = 0; 
                        for (int i = 0; i < key.length; i++) {
                                if (!((audioEncoded[i] - key[i] == 0))) { 
                                        contents[contentCounter] = (char) (Math.round((float) (10000 * ( audioEncoded[i] - key[i] )))); 
                                        contentCounter++;
                                }
                        }
                       
                        System.out.print("Enter the name for a text file to write the data to : ");
                        String outputName = scan.next();
                        if (!outputName.endsWith(".txt"))
                                outputName += ".txt";
                               
                        
                        BufferedWriter writer = null;
                        try {
                                writer = new BufferedWriter(new FileWriter(outputName));
                        } catch (IOException e1) {
                                e1.printStackTrace();
                        }
                        try {
                                for (int i = 0; i < contentCounter+1; i++) {
                                         writer.write(contents[i]);
                                }
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        try {
                                writer.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
               
                StdAudio.close();
                System.out.println("The End.");
 
 
        }
 
 
 
        // Makes a 5 second audio clip of static. 44100 values per second.
        public static double[] makeAudio () {
                double[] audio = new double[5*44100];
                int i;
                for (i=0; i < audio.length; i++)
                        audio[i] = 2*Math.random() - 1; // Generates a random double between -1 and 0.999999
 
                return audio;
        }
       
        // Returns the audio file given the name or uses another method to generate a clip of static if the argument is 1
        public static double[] getAudioFile(String input) {
                double[] audioFile;
               
                if (input.equals("1")) {
                        audioFile = makeAudio(); // Generates a clip of static.
                        System.out.print("Please enter a name for the generated audio clip : ");
                        Scanner scan = new Scanner(System.in);
                        String name = scan.next();
                        if (!name.endsWith(".wav")) // Check if the filename ends with .wav and add the extension if it doesn't
                                name += ".wav";
                        StdAudio.save(name, audioFile);
                }
                else {
                        if (!input.endsWith(".wav")) // Check if the filename ends with .wav and add the extension if it doesn't
                                input += ".wav";
 
                        audioFile = StdAudio.read(input); // Reads in the audio as an array of doubles
                }
               
                return audioFile;
        }
       
}