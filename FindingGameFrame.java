
import javax.swing.JFrame;


public class FindingGameFrame extends JFrame{


    FindingGameFrame(){
        this.add(new FindingGamePanel2());
        this.setTitle("finding game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);     
        this.pack();
        this.setVisible(true);     
        this.setLocationRelativeTo(null);
    }

}