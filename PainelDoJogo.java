import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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

public class PainelDoJogo extends JPanel implements Runnable, KeyListener {
        private Lixo lixo;
        private List<Projetil> projeteis = new ArrayList<>();
        private List<Lixeira> lixeiras = new ArrayList<>();
        private int pontuacao;
        private int vidas;
        private String msg = "";
        private boolean rodando = false;
        private Thread threadDoJogo;
        private Image bg;
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
            } catch (IOException e){
                e.printStackTrace();
            }
            iniciar();
        }

        public synchronized void iniciar() {
            rodando = true;
            threadDoJogo = new Thread(this);
            threadDoJogo.start();
        }

        @Override
        public void run() {
            //som de fundo
            try{
                URL soundUReL = getClass().getResource("/sons/kokiri.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundUReL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY); //loopei o audio de fundo para tocar para sempre enquanto rodar
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); //configuração para controlar altura (para não ficar muitop alto e abafar os outros sons)
                gainControl.setValue(-10.0f);

                clip.start();
            } catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
                e.printStackTrace();
                System.out.println("Erro ao reproduzir som: " + e.getMessage());
            }          

            while(rodando){
                atualizar();
                repaint(); // Chama o método paintComponent para redesenhar os gráficos
                try {
                    Thread.sleep(16); // aproximadamente 60 fps
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void atualizar() {
            lixo.mover(getWidth(), pontuacao);
           // Movimentação dos projéteis
           Iterator<Projetil> iteradorProjeteis = projeteis.iterator();
           while(iteradorProjeteis.hasNext()) {
                Projetil proj = iteradorProjeteis.next();
                proj.mover();
                if (proj.foraDaTela()) {
                    iteradorProjeteis.remove(); //Remove Projéteis que sairam da tela
                    vidas-=1;

                    //Adicionando som quando projetil sai da tela
                    try{
                        URL soundUReL = getClass().getResource("/sons/errou.wav");
                        AudioInputStream audioStriam = AudioSystem.getAudioInputStream(soundUReL);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStriam);
                        clip.start();
                    } catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
                        e.printStackTrace();
                        System.out.println("Erro ao reproduzir som: " + e.getMessage());
                    }                
                    try {
                        lixo.randomizarLixo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lixo.setAtivo(true);
                    pontuacao-=100;
                    System.out.println("Pontuacao" + pontuacao);
                }
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
                        pontuacao-=100;
                        //Adicionando sonzinho quando erra
                        try{
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

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
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
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
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
                progresso.addLixeiras(lixeira);
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
                lixeiras.clear();

                for(Lixeira lixeira : progresso.getLixeiras()){
                    lixeiras.add(lixeira);
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
    
}
