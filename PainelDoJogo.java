import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PainelDoJogo extends JPanel implements Runnable, KeyListener {
        private Jogador jogador;
        private List<Projetil> projeteis = new ArrayList<>();
        private List<Alienigena> alienigenas = new ArrayList<>();
        private boolean rodando = false;
        private Thread threadDoJogo;
        private Image bg;

        public PainelDoJogo(){
            jogador = new Jogador(375, 400);
            for(int i = 0; i < 15; i++) {
                alienigenas.add(new Alienigena(i * 70, 40));
            }
            addKeyListener(this);
            setFocusable(true);
            try{
                bg = ImageIO.read(getClass().getResource("/galaxia.jpg"));
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
           // Movimentação dos projéteis
           Iterator<Projetil> iteradorProjeteis = projeteis.iterator();
           while(iteradorProjeteis.hasNext()) {
                Projetil proj = iteradorProjeteis.next();
                proj.mover();
                if (proj.foraDaTela()) {
                    iteradorProjeteis.remove(); //Remove Projéteis que sairam da tela
                }
           }    
           for(Alienigena alien : alienigenas) {
            alien.mover();
           }    
            // verificar colisoes
            List<Alienigena> alienigenasARemover = new ArrayList<>();
            List<Projetil> projeteisARemover = new ArrayList<>();

            for (Alienigena alien : alienigenas) {
                for (Projetil proj : projeteis) {
                    if (alien.getLimites().intersects(proj.getLimite())) {
                        // Colisão detectada
                        alienigenasARemover.add(alien); 
                        projeteisARemover.add(proj); 
                    }
                }
            }

            alienigenas.removeAll(alienigenasARemover);
            projeteis.removeAll(projeteisARemover);

        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(bg, 0, 0, null);
            // desenhar objs aqui
            jogador.desenhar(g);
        
            for (Projetil proj : projeteis) {
                proj.desenhar(g);
            }
            for(Alienigena alien : alienigenas ) {
                alien.desenhar(g);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                jogador.moverEsquerda();
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                jogador.moverDireita(getWidth());
            }
            if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                projeteis.add(new Projetil(jogador.getX(),jogador.getY()));
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
}
