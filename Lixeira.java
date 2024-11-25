import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Lixeira {
    private int x,y;
    private int largura = 40, altura = 20;
    private int velocidade = 2;
    private Image imagem;

    public Lixeira(int inicioX, int inicioY){
        this.x = inicioX;
        this.y = inicioY;
        try{
            imagem = ImageIO.read(getClass().getResource("/alien.png"));
            largura = imagem.getWidth(null); 
            altura = imagem.getHeight(null); 
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void mover(){
        x += velocidade;
        if (x<=0 || x+largura >= 800){ // checa as bordas
            velocidade = -velocidade; //inverte dieção
            y +=altura; //desce
        }
    }

    public void desenhar(Graphics g){
        g.drawImage(imagem, x, y, null); 
    }

    public Rectangle getLimites(){
        return new Rectangle(x,y,largura, altura);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }

    public Image getImagem() {
        return imagem;
    }

    public void setImagem(Image imagem) {
        this.imagem = imagem;
    }

    
}