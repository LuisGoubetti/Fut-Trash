import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.imageio.ImageIO;

public class Lixeira {
    public static ArrayList<String> tipos = new ArrayList<>(Arrays.asList("metal", "organico", "papel", "plastico", "vidro"));;
    private String tipo;
    private int x,y;
    private int velocidade = 2;
    private int largura = 100, altura = 80;
    private Image imagem;
    public static ArrayList<String> urlImages = new ArrayList<>(Arrays.asList("imgs/LixoMetal.png", "imgs/LixoOrganico.png", "imgs/LixoPapel.png", "imgs/LixoPlastico.png", "imgs/LixoVidro.png"));
    public static Map<Integer, Integer> valoresXY = Map.of(120, 140, 400, 140, 110, 360, 280, 250, 500, 360);

    public Lixeira(int inicioX, int inicioY, String tipo, String urlImage){
        this.x = inicioX;
        this.y = inicioY;
        this.tipo = tipo;
        try{
            imagem = ImageIO.read(getClass().getResource(urlImage));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void mover(){
        x += velocidade;
        if (x<=110 || x+largura >= 950){ // checa as bordas
            velocidade = -velocidade; //inverte dieção
        }
    }

    public void desenhar(Graphics g){
        g.drawImage(imagem, x, y, largura, altura,null); 
    }

    public Rectangle getLimites(){
        return new Rectangle(x,y,largura, altura);
    }

    public String getTipo(){
        return tipo;
    }
}