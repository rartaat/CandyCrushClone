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
        else if ((x1 - 1 == x2 && y1 == y2) || (x1 + 1 == x2 && y1 == y2))
            return true;
        //Else move = illegal
        //Check for Matches
        return false;
    }

    public static int pType(String tag) {
        int num = Integer.parseInt(tag.substring(5,6));
        return num;
    }

}
