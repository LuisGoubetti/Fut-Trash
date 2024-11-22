import javax.swing.JFrame;

public class JanelaDoJogo extends JFrame{
    public JanelaDoJogo(){
        this.setTitle("Invasão Alienígena");
        this.setSize(800,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new PainelDoJogo());
        this.setVisible(true);
    }

    public static void main(String args[]){
        new JanelaDoJogo();
    }
}