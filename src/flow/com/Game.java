package flow.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Game implements ActionListener {

    private int coord1[];
    private int coord2[];

    private int turn = 0;
    private int pic = 0;
    private int points;

    JLabel pt = new JLabel("" + points);
    JLabel lbpts = new JLabel("Points:");

    private final int ROW = 8;
    private final int COL = 8;

    public JButton candies[][] = new JButton[ROW][COL];

    //Set up window
    public Game() {

        JFrame frame = new JFrame("Candy Crush Clone");
        frame.setLayout(new GridLayout(9,COL));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        //Create Buttons
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                pic = getPic(i, j);
                candies[i][j].setActionCommand("("+ i + "," + j + "," + pic +")");
                candies[i][j].addActionListener(this);
                frame.getContentPane().add(candies[i][j]);
            }
        }

        //Check for 3 or more in a row
        boolean changeVertical = false;
        boolean changeHorizontal = false;

        do {
            changeHorizontal = compHorizontal(0);
            changeVertical = compVertical(0);

            if (changeVertical || changeHorizontal)
                fillHoles();
        } while (changeVertical || changeHorizontal);

        JPanel status = new JPanel();

        status.add(lbpts);
        status.add(pt);

        //Add Status Bar
        frame.add(status);

        //Display Window
        frame.pack();
        frame.setSize(480,500);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        points = 0;
        pt.setText("" + points);
    }

    private void horizontalEliminater (int x, int y, int num, ImageIcon blank) {
        for (int i = y; num >= 0; i--, num--) {
            candies[x][i].setActionCommand("(" + x + "," + i + "," + 0 + ")");
            candies[x][i].setIcon(blank);
        }
    }

    private void verticalEliminater (int x, int y, int num, ImageIcon blank) {
        for (int i = x; num >= 0; i--, num--) {
            candies[i][y].setActionCommand("(" + i + "," + y + "," + 0 + ")");
            candies[i][y].setIcon(blank);
        }
    }

    public void eliminater(int x, int y, int num, int mode ) {
        ImageIcon blank = new ImageIcon("icons/0.gif");

        points += ((num + 1) * 10);
        pt.setText(String.valueOf(points));

        boolean isHorizontal = mode == 1;

        if (isHorizontal) {
           horizontalEliminater(x, y, num, blank);
        } else {
            verticalEliminater(x, y, num, blank);
        }
    }

    public int getPic(int x, int y) {
        Random generator = new Random();
        int i =0;

        i = generator.nextInt(6)+1;
        ImageIcon pic = new ImageIcon("icons/" + i + ".png");
        candies[x][y] = new JButton(pic);

        return i;
    }

    public void changeImage(int x1,int y1,int x2, int y2) {
        int pic1,pic2;

            if (LegalPosition.isLegal(x1, y1, x2, y2)) {
                pic1 = LegalPosition.pType(candies[x1][y1].getActionCommand());
                pic2 = LegalPosition.pType(candies[x2][y2].getActionCommand());

            candies[x1][y1].setActionCommand("("+ x1 + "," + y1 + "," + pic2 +")");
            candies[x2][y2].setActionCommand("("+ x2 + "," + y2 + "," + pic1 +")");

            Icon temp = candies[x1][y1].getIcon() ;
            candies[x1][y1].setIcon(candies[x2][y2].getIcon());
            candies[x2][y2].setIcon(temp);
        }
    }

    public boolean isItGameOver() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (checkMove(i,j)) {
                    //System.out.println("Check:("+i+","+j+")");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean youWin() {
        return points >= 600;
    }

    public boolean checkMove(int x, int y) {

        boolean moveXh = false;
        boolean moveXv = false;

        //move up
        if (x != 0) {
            changeImage(x, y,x-1, y);

            moveXh = compHorizontal(1);
            moveXv = compVertical(1);

            changeImage(x, y,x-1, y);
        }

        //move down
        if (x != 7) {
            //Switch Images
            changeImage(x, y,x+1, y);
            moveXh = compHorizontal(1);
            moveXv = compVertical(1);

            //Switch back
            changeImage(x, y,x+1, y);
        }

        //move left
        if (y != 0) {
            //Switch Images
            changeImage(x, y, x,y-1);

            moveXh = compHorizontal(1);
            moveXv = compVertical(1);

            //Switch back
            changeImage(x, y, x,y-1);
        }

        //move right
        if (y != 7) {
            //Switch Images
            changeImage(x, y, x,y+1);

            moveXh = compHorizontal(1);
            moveXv = compVertical(1);

            //Switch back
            changeImage(x, y, x,y+1);
        }
        //isItGameOver turn to false;
        return moveXh || moveXv;
    }

	/*Test isItGameOver function
	public void endGame()
	{
		for(int j=0; j < 8;j++)
		{
			for(int i=0; i < 8;i++)
			{
				switch((i+j)%3)
				{
					case 0:
					setPic(j,i,1);
					break;
					case 1:
					setPic(j,i,2);
					break;
					case 2:
					setPic(j,i,3);
					break;
				}
			}
		}
	}*/

    public boolean samePiece(int x1, int y1, int x2, int y2) {
        if (x2 > 7 || y2 > 7 ){
            return false;
        }
        return LegalPosition.pType(candies[x1][y1].getActionCommand()) == LegalPosition.pType(candies[x2][y2].getActionCommand());
    }

    public boolean compVertical(int mode) {
        int counter = 0;
        boolean change = false;

        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if (samePiece(i, j,i + 1, j)) {
                    counter++;
                } else {
                    if (counter > 1) {
                        if (mode == 0) {
                            eliminater(i, j, counter, 0);
                        }
                        change = true;
                    }
                    counter = 0;
                }
            }
        }
        return change;
    }

    public boolean compHorizontal(int mode) {
        int counter = 0;
        boolean change = false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (samePiece(i,j,i,j+1)) {
                    counter++;
                } else {
                    if (counter > 1) {
                        if (mode == 0) {
                            eliminater(i, j, counter, 1);
                        }
                        change=true;
                    }
                    counter = 0;
                }
            }
        }
        return change;
    }

    //Fill Holes with Random Pieces
    public void fillHoles() {
        int change = 0;

        do {
            change = 0;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (LegalPosition.pType(candies[i][j].getActionCommand()) == 0) {
                        change++;
                        if (i == 0) {
                            setPic(i, j, 0);
                        } else {
                            changeImage(i, j, i - 1, j);
                        }
                    }
                }
            }
        } while (change!= 0);

    }

    public void setPic(int x,int y,int spic) {
        Random generator = new Random();
        int i = 0;

        if(spic == 0)
            i = generator.nextInt(6)+1;
        else
            i = spic;

        ImageIcon pic = new ImageIcon("icons/" + i + ".png");
        candies[x][y].setIcon(pic);
        candies[x][y].setActionCommand("("+ x + "," + y + "," + i +")");
    }

    public void actionPerformed(ActionEvent e) {
        if (turn == 0) {
            coord1 = LegalPosition.getXY(e.getActionCommand());
           turn++;
        } else {
            coord2 = LegalPosition.getXY(e.getActionCommand());
            changeImage(coord1[0],coord1[1],coord2[0],coord2[1]);

            boolean changeVertical = false;
            boolean changeHorizontal = false;

            int counter = 0;

            do {
                changeHorizontal = compHorizontal(0);
                changeVertical = compVertical(0);

                if (changeVertical || changeHorizontal) {
                    fillHoles();
                } else { //Move made no matches
                    if (counter == 0) {
                        JOptionPane.showMessageDialog(null,"Bad Move.", "Wrong Step Alert!", JOptionPane.INFORMATION_MESSAGE);
                        //Switch back Pictures, if move is bad
                        changeImage(coord1[0], coord1[1], coord2[0], coord2[1]);
                    }
                }
                counter++;
            } while (changeVertical || changeHorizontal);

            //endGame(); Test isItGameOver function

            //Check for moves. if moves = 0 flow.com.Game Over
            if (isItGameOver()) {
                JOptionPane.showMessageDialog(null, "You Lose!!!", "Failed", JOptionPane.INFORMATION_MESSAGE);
            }

            if (youWin()){
                JOptionPane.showMessageDialog(null, "You win", "Cheer!", JOptionPane.INFORMATION_MESSAGE);
            }
            turn = 0; // reset clickMethod
        }
    }
}