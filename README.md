```mermaid
classDiagram
   
    class Projetil {
        -String tipo
        -int posicaoX
        -int posicaoY
        -int largura, altura
        -int velocidade
        -Image imagem
       
        +void desenhar(Graphics g)
        +void mover(int posicaoX)
        +boolean foraDaTela()  
        +Rectangle getLimite()
    }
 
    class PainelDoJogo {
        -Jogador jogador
        -Projetil projetil
        -List [Lixos] lixos
        -boolean rodando
        -Thread threadDoJogo
        -Image bg
 
        +synchronized void iniciar()
        +void run()
        -void atualizar()
        +void paintComponent(Graphics g)
        +void keyTyped(KeyEvent e)
        +void keyPressed(KeyEvent e)
        +void keyReleased(KeyEvent e)
    }
 
    class Lixos{
        -String tipo
        -int posicaoX
        -int posicaoY
        -int velocidade
        -int largura, altura
        +void desenhar(Graphics g)
        +void mover()
        +Rectangle getLimite()
    }
 
    class Jogador{
        -int posicaoX
        +void mover(int posicaoX)
    }
