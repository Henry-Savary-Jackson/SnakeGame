
package snakegame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SnakeGame extends JFrame{
    public JLabel lblScore = new JLabel("Score:");
    public JLabel lblTime = new JLabel("Time:");
    public JPanel pnlInfo = new JPanel();
    public Game game ;
    
    public SnakeGame(int w, int h){
        super("Snake game");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(dim.width * 0.2f), (int)(dim.height * 0.2f));
        
        setLayout(new BorderLayout());
        
        //adds the statistics panel at the bottom
        pnlInfo.add(lblScore);
        pnlInfo.add(lblTime);
        pnlInfo.setOpaque(true);
        pnlInfo.setBackground(Color.LIGHT_GRAY);
        add(pnlInfo, BorderLayout.SOUTH);
        
        game = new Game(w,h, this);
        
        add(game);
        
        pack();
        
        setVisible(true);
        
        game.requestFocus();
        
    }
    
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        if (game != null){
            lblTime.setText("Time:" + String.valueOf(game.time) + "s");
            lblScore.setText("Score:" + String.valueOf(game.score));
        }
    }

    public static void main(String[] args) throws NumberFormatException , IndexOutOfBoundsException {
        
                final int columns;
                final int rows ;
                try {
                    columns = Integer.parseInt(args[0]);
                    rows  = Integer.parseInt(args[1]);
                } catch(NumberFormatException ex) {
                    System.out.print("Invalid columns and/or rows entered.\n\n");
                    throw ex;
                } catch ( IndexOutOfBoundsException indexErr){
                    System.out.print("Please Enter columns AND rows of the game.\n\n");
                    throw indexErr;
                }
                
                boolean validColumns = columns>= 15 && columns<=35;
                boolean validRows = rows>= 15 && rows <=35;
                if (validColumns && validRows){
                    SwingUtilities.invokeLater(()-> {
                        new SnakeGame(columns,rows);
                    });
                } else {
                    System.out.println("Columns and rows should be in between 15 and 35.");
                }
            
        }
}