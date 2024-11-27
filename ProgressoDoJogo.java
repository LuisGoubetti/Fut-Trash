import java.util.ArrayList;
import java.util.List;

public class ProgressoDoJogo {
    private int posicaoLixoX;
    private int posicaoLixoY;
    private int pontuacao;
    private int vidas;
    private int velocidade;
    private String tipo;
    private List<Lixeira> lixeiras;

    public ProgressoDoJogo(int posicaoLixoX, int posicaoLixoY, int pontuacao, int vidas, int velocidade, String tipo){
        this.posicaoLixoX = posicaoLixoX;
        this.posicaoLixoY = posicaoLixoY;
        this.pontuacao = pontuacao;
        this.vidas = vidas;
        this.velocidade = velocidade;  
        this.tipo = tipo;
        this.lixeiras = new ArrayList<>();
    }

    // Getters e Setters
    public int getPosicaoLixoX() {
        return posicaoLixoX;
    }

    public void setPosicaoLixoX(int posicaoLixoX) {
        this.posicaoLixoX = posicaoLixoX;
    }

    public int getPosicaoLixoY() {
        return posicaoLixoY;
    }

    public void setPosicaoLixoY(int posicaoLixoY) {
        this.posicaoLixoY = posicaoLixoY;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Lixeira> getLixeiras() {
        return lixeiras;
    }

    public void addLixeiras(Lixeira posicoesLixeiras) {
        this.lixeiras.add(posicoesLixeiras);
    }
}
