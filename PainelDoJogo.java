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
        private Lixo lixo;
        private List<Projetil> projeteis = new ArrayList<>();
        private List<Lixeira> lixeiras = new ArrayList<>();
        private boolean rodando = false;
        private Thread threadDoJogo;
        private Image bg;

        public PainelDoJogo(){
            lixo = new Lixo(375, 400);
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
            lixo.mover(getWidth());
           // Movimentação dos projéteis
           Iterator<Projetil> iteradorProjeteis = projeteis.iterator();
           while(iteradorProjeteis.hasNext()) {
                Projetil proj = iteradorProjeteis.next();
                proj.mover();
                if (proj.foraDaTela()) {
                    iteradorProjeteis.remove(); //Remove Projéteis que sairam da tela
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
            g.drawImage(bg, 0, 0, null);
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

            if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                projeteis.add(new Projetil(lixo.getX(),lixo.getY(), lixo.getTipo()));
                lixo.setAtivo(false); 
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
}
