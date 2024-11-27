import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Lixo {
    private int x,y;
    private int largura = 10, altura = 10;
    private int velocidade= 5;
    private Image imagem;
    private String tipo;
    private boolean ativo = true;

    private static final String IMG_PLASTICO = "/imgs/plastico.png";
    private static final String IMG_PAPEL = "/imgs/papel.png";
    private static final String IMG_METAL = "/imgs/metal.png";
    private static final String IMG_VIDRO = "/imgs/vidro.png";
    private static final String IMG_ORGANICO = "/imgs/organico.png";

    public Lixo(int inicioX, int inicioY){
        this.x = inicioX;
        this.y = inicioY;

        try{
            randomizarLixo();
            largura = imagem.getWidth(null); 
            altura = imagem.getHeight(null); 
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void randomizarLixo() throws IOException{
        Random random = new Random();

        int numRandom = random.nextInt(5); 

        try{
            switch (numRandom) {
                case 0:
                    tipo = "plastico";
                    imagem = ImageIO.read(getClass().getResource(IMG_PLASTICO));
                    break;
                case 1:
                    tipo = "papel";
                    imagem = ImageIO.read(getClass().getResource(IMG_PAPEL));
                    break;
                case 2:
                    tipo = "metal";
                    imagem = ImageIO.read(getClass().getResource(IMG_METAL));
                    break;
                case 3:
                    tipo = "vidro";
                    imagem = ImageIO.read(getClass().getResource(IMG_VIDRO));
                    break;
                case 4:
                default:
                    tipo = "organico";
                    imagem = ImageIO.read(getClass().getResource(IMG_ORGANICO));
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Image carregarImagemTipo(String tipoString) throws IOException{
        Image i1;

        switch (tipoString) {
            case "plastico":
                return i1 = ImageIO.read(getClass().getResource(IMG_PLASTICO));
            case "papel":
                return i1 = ImageIO.read(getClass().getResource(IMG_PAPEL));
            case "metal":
                return i1 =  ImageIO.read(getClass().getResource(IMG_METAL));
            case "vidro":
                return i1 =  ImageIO.read(getClass().getResource(IMG_VIDRO));
            case "organico":
            default:
                return i1 =  ImageIO.read(getClass().getResource(IMG_ORGANICO));
        }
    }
    

    public void setImagem(Image imagem) {
        this.imagem = imagem;
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

    public void desenhar(Graphics g) throws IOException {
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

    public String getTipo() {
        return tipo;
    }

    public void setAtivo(boolean b) {
        this.ativo = b;
    }

    public boolean isAtivo() {
        return ativo;
    }
    
}