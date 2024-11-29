import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PainelDoJogo extends JPanel implements Runnable, KeyListener {
        private enum GameState {TITLE, TUTORIAL, PLAYING, GAME_OVER}
        private GameState gameState = GameState.TITLE;
        private Lixo lixo;
        private List<Projetil> projeteis = new ArrayList<>();
        private List<Lixeira> lixeiras = new ArrayList<>();
        private int pontuacao;
        private int vidas;
        private String msg = "";
        private boolean rodando = false;
        private Thread threadDoJogo;
        private Image bg;
        private Image gifGameOver;
        private Image titleImage;
        private Image tutorialImage;
        private ArrayList<String> urlsAleatoria = new ArrayList<>();
        private ArrayList<String> tiposAleatorio = new ArrayList<>();
        private int alpha = 255; 

        //Funcao que fara com que os tipos de lixeiras fique aleatorios na hora de iniciar o jogo
        private ArrayList<String> embaralharTipos(ArrayList<String> tipos) {
            Collections.shuffle(tipos);
            return tipos;
        } 

        //Funcao que compara as urls das imagens com os tipos ja embaralhados, para que possa adicionar as imagens de acordo com o tipo
        private ArrayList<String> compararTipoComUrl(ArrayList<String> tipos) {
            ArrayList<String> urls = new ArrayList<>();
            
            for(String tipo : tipos) {
                for(String url : Lixeira.urlImages) {
                    if(url.toUpperCase().contains(tipo.toUpperCase())) {
                        urls.add(url);
                    }
                }
            }
            return urls;
        }

        public PainelDoJogo(){
            pontuacao = 0;
            vidas = 5;

            //Embaralhando os tipos que ja estao preenchidos na classe Lixeira
            for(String tipo : embaralharTipos(Lixeira.tipos)) {
                tiposAleatorio.add(tipo);
            }

            //Preenchendo a arrayList das urls com a ordem correta para o inicio do jogo
            urlsAleatoria = compararTipoComUrl(tiposAleatorio);

            //Instanciando lista para trabalhar com os valores de posicao armazenados na class Lixeira
            List<Integer> posicaoX = new ArrayList<>(Lixeira.valoresXY.keySet());

            lixo = new Lixo(375, 550);
            for(int i = 0; i < 5; i++) {
                Integer key = posicaoX.get(i); // Pega a chave na posição i
                Integer posicaoY = Lixeira.valoresXY.get(key); // Obtém o valor associado à chave
                lixeiras.add(new Lixeira(key, posicaoY, tiposAleatorio.get(i), urlsAleatoria.get(i))); //Criando as Lixeiras e adicionando à lista
            }
            addKeyListener(this);
            setFocusable(true);
            try{
                bg = ImageIO.read(getClass().getResource("/imgs/Gol.jpg"));
                gifGameOver = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imgs/gameover.gif"));
                titleImage = ImageIO.read(getClass().getResource("/imgs/title.png"));
                tutorialImage = ImageIO.read(getClass().getResource("/imgs/tutorial.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            iniciar();
        }
    
        public synchronized void iniciar() {
            rodando = true;
            threadDoJogo = new Thread(this);
            threadDoJogo.start();
        }

        private void tocarSom(String caminho, boolean loop) {
            try {
                URL soundURL = getClass().getResource(caminho);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-10.0f); // Ajusta o volume para não ficar alto demais
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
                System.out.println("Erro ao reproduzir som: " + e.getMessage());
            }
        }

        @Override
        public void run() {           
            if (gameState == GameState.TITLE) {
                tocarSom("/sons/startup.wav", false);
            }
            // Aguarda o término do jingle para passar para o tutorial
            try {
                Thread.sleep(5000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // Exibe a tela de tutorial
            gameState = GameState.TUTORIAL;
            repaint();

            tocarSom("/sons/kokiri.wav", true);
            while(rodando){
                if (gameState == GameState.PLAYING) {
                    atualizar();
                } else if (gameState == GameState.GAME_OVER) {
                    repaint();
                    break; 
                }
                repaint();
                try {
                    Thread.sleep(16); // aproximadamente 60 fps
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void atualizar() {
            if (gameState == GameState.PLAYING) {
                lixo.mover(getWidth(), pontuacao);
     
                // Movimentação dos projéteis
                Iterator<Projetil> iteradorProjeteis = projeteis.iterator();
                while(iteradorProjeteis.hasNext()) {
                     Projetil proj = iteradorProjeteis.next();
                     proj.mover();
                     if (proj.foraDaTela()) {
                         iteradorProjeteis.remove(); //Remove Projéteis que sairam da tela
                         vidas  = (vidas==0) ? vidas = 0 : vidas-1;
                         tocarSom("/sons/errou.wav", false);
                         verificarGameOver();
     
                         try {
                             lixo.randomizarLixo();
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
     
                         lixo.setAtivo(true);
                         if (vidas>1) pontuacao = (pontuacao==0) ? pontuacao = 0 : pontuacao-100;
                     }
                }  
     
                for(Lixeira lixeira : lixeiras) {
                     lixeira.mover();
                }  
     
                for(Lixeira lixeira : lixeiras) {
                     lixeira.mover();
                }    
     
                List<Projetil> projeteisARemover = new ArrayList<>();
                boolean lixoDeveReaparecer = false;
     
                for (Lixeira lixeira : lixeiras) {
                 for (Projetil proj : projeteis) {
                     if (lixeira.getLimites().intersects(proj.getLimite())) {
                         // Colisão detectada
                         if (lixeira.getTipo().equalsIgnoreCase(proj.getTipo())){
                             pontuacao+=100;
                             //Adicionando sonzinho quando acerta
                             try{
                                 URL soundURL = getClass().getResource("/sons/acertou.wav");
                                 AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
                                 Clip clip = AudioSystem.getClip();
                                 clip.open(audioStream);
                                 clip.start();
                             } catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
                                 e.printStackTrace();
                                 System.out.println("Erro ao reproduzir som: " + e.getMessage());
                             }                          
                         } else{  
                             pontuacao = (pontuacao==0) ? pontuacao = 0 : pontuacao-100;
                             //Adicionando sonzinho quando erra
                             try{
                                vidas  = (vidas==0) ? vidas = 0 : vidas-1;
                                verificarGameOver();
                                 URL soundURL = getClass().getResource("/sons/errou.wav");
                                 AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
                                 Clip clip = AudioSystem.getClip();
                                 clip.open(audioStream);
                                 clip.start();
                             } catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
                                 e.printStackTrace();
                                 System.out.println("Erro ao reproduzir som: " + e.getMessage());
                             }                
                         }
                             projeteisARemover.add(proj); 
                             lixoDeveReaparecer = true;                      
                             lixoDeveReaparecer = true;                      
                         }
                     }
                 }
     
                 projeteis.removeAll(projeteisARemover);
     
                 if (lixoDeveReaparecer) {
                     try {
                         lixo.randomizarLixo();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     lixo.setAtivo(true);
                 }
            }
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            switch (gameState) {
                case TITLE:
                    g.drawImage(titleImage, 0, 0, getWidth(), getHeight(), this);
                    break;
    
                case TUTORIAL:
                    g.drawImage(tutorialImage, 0, 0, getWidth(), getHeight(), this);
                    break;
    
                case PLAYING:
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                    g.setFont(new Font("Arial", Font.BOLD, 20));;
                    
                    Graphics2D g2d = (Graphics2D) g;
                    if (alpha > 0) {
                        g2d.setColor(Color.RED);
                        g2d.drawString(msg, 30, 50);
                    }
                    g.setColor(Color.BLACK);
                    g.drawString("Pontos: "+pontuacao, 30, 650);
                    g.drawString("Vidas: "+vidas, 30, 670);
                    
                    // desenhar objs aqui
                    if (lixo.isAtivo()) {
                        try {
                            lixo.desenhar(g);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                
                    for (Projetil proj : projeteis) {
                        proj.desenhar(g);
                    }
                    for(Lixeira lixeira : lixeiras ) {
                        lixeira.desenhar(g);
                    }
                    break;
    
                case GAME_OVER:
                    if (gifGameOver != null) {
                        g.drawImage(gifGameOver, 0, 0, getWidth(), getHeight(), this);
                    }
                    g.setFont(new Font("Arial", Font.BOLD, 40));
                    g.setColor(Color.WHITE);
                    String textoPontuacao = "Sua pontuação: " + pontuacao;
                    int textoLargura = g.getFontMetrics().stringWidth(textoPontuacao);
                    g.drawString(textoPontuacao, (getWidth() - textoLargura) / 2, getHeight() / 2);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (gameState == GameState.TUTORIAL && e.getKeyCode() == KeyEvent.VK_ENTER) {
                gameState = GameState.PLAYING;
            }
            if(e.getKeyCode() == KeyEvent.VK_SPACE && lixo.isAtivo()) {
                projeteis.add(new Projetil(lixo.getX(),lixo.getY(), lixo.getTipo()));
                lixo.setAtivo(false); 
            }
            if(e.getKeyCode() == KeyEvent.VK_S && lixo.isAtivo()) {
                salvarProgresso();
                desvanecer();
            }
            if(e.getKeyCode() == KeyEvent.VK_L && lixo.isAtivo()) {
                carregarProgresso();                
                desvanecer();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        public void salvarProgresso(){
            ProgressoDoJogo progresso = new ProgressoDoJogo(lixo.getX(), lixo.getY(), pontuacao, vidas, lixo.getVelocidade(), lixo.getTipo());
            for(Lixeira lixeira : lixeiras){
                progresso.addPosicoesLixeiras(new int[]{lixeira.getX(), lixeira.getY()});
            }
            JogoUtils.salvarProgresso(progresso, "save.json");
            msg = "Progresso salvo com sucesso!";
        }

        public void carregarProgresso(){
            ProgressoDoJogo progresso = JogoUtils.carregarProgresso("save.json");
            if (progresso != null){
                lixo.setX(progresso.getPosicaoLixoX());
                lixo.setY(progresso.getPosicaoLixoY());
                lixo.setVelocidade(progresso.getVelocidade());
                if (lixo.getVelocidade()>0) lixo.setDirecao(1); else lixo.setDirecao(-1);
                lixo.setTipo(progresso.getTipo());
                try {
                    lixo.setImagem(lixo.carregarImagemTipo(lixo.getTipo()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pontuacao = progresso.getPontuacao();
                vidas = progresso.getVidas();
               // lixeiras.clear();
                
                for (int i = 0; i < lixeiras.size(); i++){
                    int[] posicao = progresso.getPosicoesLixeiras().get(i);
                    lixeiras.get(i).setX(posicao[0]);
                    lixeiras.get(i).setY(posicao[1]);   
                }

                msg = "Progresso carregado com sucesso!";
            }
            else msg = "Erro ao carregar o progresso.";
        }

        public void desvanecer(){
            alpha = 255;
            // Timer para criar o efeito de fade-out (executa a cada 50ms)
            Timer timer = new Timer(50, e -> {
                alpha -= 5; // Reduz a opacidade gradualmente
                if (alpha <= 0) {
                    ((Timer) e.getSource()).stop(); // Para o timer quando a string desaparecer
                }
                repaint(); // Atualiza o painel para aplicar o efeito
            });
            timer.start();
        }
    
        private void verificarGameOver() {
            if (vidas <= 0) {
                rodando = false; 
                gameState = GameState.GAME_OVER;
                repaint(); 
            }
        }
}
