
package snakegame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
        
        pnlInfo.add(lblScore);
        pnlInfo.add(lblTime);
        pnlInfo.setOpaque(true);
        pnlInfo.setBackground(Color.LIGHT_GRAY);
        add(pnlInfo, BorderLayout.SOUTH);
        
        game = new Game(w,h);
        
        add(game);
        
        pack();
        
        getContentPane().setPreferredSize(getSize());
        
        setVisible(true);
        
        game.requestFocus();
        
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(()-> {
                new SnakeGame(500,500);
            });
        } catch(NumberFormatException ex) {
            System.out.print("Invalid width and/or height entered.");
        }
       
    }
    
}
