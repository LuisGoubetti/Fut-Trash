import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Jogador {
    private int x,y;
    private int largura = 10, altura = 10;
    private int velocidade= 5;
    private Image imagem;

    public Jogador(int inicioX, int inicioY){
        this.x = inicioX;
        this.y = inicioY;

        try{
            imagem = ImageIO.read(getClass().getResource("/nave.png"));
            largura = imagem.getWidth(null); 
            altura = imagem.getHeight(null); 
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void mover(int larguraDaTela){
        x -= velocidade;
        if (x<0) {
            x = 0;
            velocidade *= -1;
        } 
        if (x+largura > larguraDaTela) {
            x = larguraDaTela-largura;
            velocidade *= -1;
        }
    }

    public void desenhar(Graphics g){
        g.drawImage(imagem, x, y, null); 
    }

    public Rectangle getLimites(){
        return new Rectangle(x,y,largura, altura); //colisão
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    
}