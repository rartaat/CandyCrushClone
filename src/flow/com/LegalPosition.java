package flow.com;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class LegalPosition {

    public static int[] getXY(String candy) {
        int xy[] = new int[2];

        xy[0]= Integer.parseInt(candy.substring(1,2));
        xy[1]= Integer.parseInt(candy.substring(3,4));
        return xy;
    }

    public static boolean isLegal(int x1,int y1,int x2, int y2) {
        //Check Position
        // Left + Right
        if ((x1 == x2 && y1 + 1 == y2) || (x1 == x2 && y1 - 1 == y2))
            return true;
            //Down + Up
        else return (x1 - 1 == x2 && y1 == y2) || (x1 + 1 == x2 && y1 == y2);
        //Else move = illegal
        //Check for Matches
    }

    public static int pType(String tag) {
        return Integer.parseInt(tag.substring(5,6));
    }


}
