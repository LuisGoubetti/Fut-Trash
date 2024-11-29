import javax.swing.JFrame;
public class JanelaDoJogo extends JFrame{
    public JanelaDoJogo(){
        this.setTitle("Fut-Trash");
        this.setSize(1080,720);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new PainelDoJogo());
        this.setVisible(true);
    }

    public static void main(String args[]){
        new JanelaDoJogo();
    }
}