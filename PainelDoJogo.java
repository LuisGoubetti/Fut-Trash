import javax.imageio.ImageIO;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        private int alpha = 255;  

        public PainelDoJogo(){
            lixo = new Lixo(375, 400);
            pontuacao = 0;
            vidas = 3;

            for(int i = 0; i < 15; i++) {
                lixeiras.add(new Lixeira(i * 70, 40));
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

                    try {
                        lixo.randomizarLixo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lixo.setAtivo(true);
                }
           }    
           for(Lixeira alien : lixeiras) {
            alien.mover();
           }    
            // verificar colisoes
            List<Lixeira> lixeirasARemover = new ArrayList<>();
            List<Projetil> projeteisARemover = new ArrayList<>();

            boolean lixoDeveReaparecer = false;

            for (Lixeira alien : lixeiras) {
                for (Projetil proj : projeteis) {
                    if (alien.getLimites().intersects(proj.getLimite())) {
                        // Colisão detectada
                        lixeirasARemover.add(alien); 
                        projeteisARemover.add(proj); 
                        pontuacao+=100;
                        lixoDeveReaparecer = true;
                    }
                }
            }

            lixeiras.removeAll(lixeirasARemover);
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
            for(Lixeira alien : lixeiras ) {
                alien.desenhar(g);
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
                progresso.addPosicoesAlienigenas(new int[]{lixeira.getX(), lixeira.getY()});
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

                for(int[] posicao : progresso.getPosicoesLixeiras()){
                    lixeiras.add(new Lixeira(posicao[0], posicao[1]));
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
