import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Projetil {
    private int x,y;
    private int largura = 3, altura = 3;
    private int velocidade= 5;
    private Image imagem;

    public Projetil(int inicioX, int inicioY){
        this.x = inicioX;
        this.y = inicioY;
        try {
            imagem = ImageIO.read(getClass().getResource("/projetil.png")); 
            largura = imagem.getWidth(null); 
            altura = imagem.getHeight(null); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mover(){
        y -= velocidade;
    }

    public void desenhar(Graphics g){
        g.drawImage(imagem, x, y, null); 
    }

    public boolean foraDaTela(){
        return y + altura < 0;
    }

    public Rectangle getLimite(){
        return new Rectangle(x,y,largura,altura);
    }
    
}
