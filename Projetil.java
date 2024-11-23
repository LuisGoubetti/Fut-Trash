import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Projetil {
    private int x,y;
    private int largura = 3, altura = 3;
    private int velocidade = 5;
    private Image imagem;
    private static String tipo;

    private static final String IMG_PLASTICO = "/imgs/plastico.png";
    private static final String IMG_PAPEL = "/imgs/papel.png";
    private static final String IMG_METAL = "/imgs/metal.png";
    private static final String IMG_VIDRO = "/imgs/vidro.png";
    private static final String IMG_ORGANICO = "/imgs/organico.png";

    public Projetil(int inicioX, int inicioY, String tipoJogador){
        this.x = inicioX;
        this.y = inicioY;
        this.tipo = tipoJogador;
        try {           
            definirImagemDoTipo(tipoJogador);
            largura = imagem.getWidth(null); 
            altura = imagem.getHeight(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

       private void definirImagemDoTipo(String tipoDoJogador) throws IOException {
        switch (tipoDoJogador) {
            case "plastico":
                imagem = ImageIO.read(getClass().getResource(IMG_PLASTICO));
                break;
            case "papel":
                imagem = ImageIO.read(getClass().getResource(IMG_PAPEL));
                break;
            case "metal":
                imagem = ImageIO.read(getClass().getResource(IMG_METAL));
                break;
            case "vidro":
                imagem = ImageIO.read(getClass().getResource(IMG_VIDRO));
                break;
            case "organico":
            default:
                imagem = ImageIO.read(getClass().getResource(IMG_ORGANICO));
                break;
        }
    }

    public static String getTipo(){
        return tipo;
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
