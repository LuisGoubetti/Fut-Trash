#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <conio.h>

#define TAM 50
#define FILENAME "pizzarias.fatec"

FILE *arq;

struct pizza
{
    int id;
    char descricao[60];
    char tamanho[30];
    float valorVenda;
    int e;
};

struct pizza lista[TAM];

int menu();
void cadastrar();
void consultarTodos();
void consultar();
void alterar();
void excluirLogica();
void listagemApagados();
void listarPeloId();
void ordenarPorpreco(int n);
void ordenarPorId(int n);
void listarPelopreco();
void subConsultas();
int  exclusaoFisica();
void cabecalho();
void relatorios ();
void utilitarios ();
void backup();
void iguala();
void menorque();
void maiorque();
int limpar();

int main(){
    return menu();
    return 0;
}

int limpar()
{
    int response;
    FILE *arq;
    struct pizza ficha;

    if ((arq = fopen(FILENAME, "rb")) == NULL)
    {
        printf("\n Erro ao abrir o arquivo");
        return;
    }

    if ((arq = fopen("pizzarias.fatec", "wb")) == NULL)
    {
        printf("\n Erro ao criar o arquivo de backup");
        fclose(arq);
    }


        response=remove(FILENAME);

}


void menorque()
{
    int i;
    int val;
    struct pizza ficha;
    system("cls");
    cabecalho();
    printf("MENOR QUE VALOR\n");

    printf("Digite valor:");
        scanf("%d",&val);
    printf("\n");
    if ((arq = fopen(FILENAME, "rb")) == NULL)
    {
        printf("\n Erro");
        return 0;
    }
    while (fread(&ficha, sizeof(ficha), 1, arq))
    {
        if (ficha.e == 0)
            if (ficha.valorVenda < val)
        {
            printf("\n %4d | %s | %s | %.2f ", ficha.id, ficha.descricao, ficha.tamanho, ficha.valorVenda);
        }
    }
    fclose(arq);
    printf("\n tecle qualquer tecla para finalizar ...");
    setbuf(stdin, NULL);
    getch();
}

void maiorque()
{
    int i;
    int valm;
    struct pizza ficha;
    system("cls");
    cabecalho();
    printf("MAIOR QUE VALOR\n");

    printf("Digite valor:");
        scanf("%d",&valm);
    printf("\n");
    if ((arq = fopen(FILENAME, "rb")) == NULL)
    {
        printf("\n Erro");
        return 0;
    }
    while (fread(&ficha, sizeof(ficha), 1, arq))
    {
        if (ficha.e == 0)
            if (ficha.valorVenda > valm)
        {
            printf("\n %4d | %s | %s | %.2f ", ficha.id, ficha.descricao, ficha.tamanho, ficha.valorVenda);
        }
    }
    fclose(arq);
    printf("\n tecle qualquer tecla para finalizar ...");
    setbuf(stdin, NULL);
    getch();
}

void iguala()
{
    int i;
    int ide;
    struct pizza ficha;
    system("cls");
    cabecalho();
    printf("IGUAL A ID\n");

    printf("Digite o id:");
        scanf("%d",&ide);
    printf("\n");
    if ((arq = fopen(FILENAME, "rb")) == NULL)
    {
        printf("\n Erro");
        return 0;
    }
    while (fread(&ficha, sizeof(ficha), 1, arq))
    {
        if (ficha.e == 0)
            if (ficha.id == ide)
        {
            printf("\n %4d | %s | %s | %.2f ", ficha.id, ficha.descricao, ficha.tamanho, ficha.valorVenda);
        }
    }
    fclose(arq);
    printf("\n tecle qualquer tecla para finalizar ...");
    setbuf(stdin, NULL);
    getch();
}

void relatorios(){

    int opc = 1;
    do
    {
        system("cls");
        cabecalho();
        printf("RELATORIOS DE PIZZA\n");
        printf("*******************\n");

        printf("\n 5-1. Listagem geral");
        printf("\n 5-2. Listagem geral ordenado por preco");
        printf("\n 5-3. Listagem geral ordenado por id");
        printf("\n 5-4. Listagem igual a id");
        printf("\n 5-5. Listagem por menor que");
        printf("\n 5-6. Listagem por maior que");
        printf("\n 9. Voltar");

        printf("\n Informe a opcao desejada: ");
        setbuf(stdin, NULL);
        scanf("%d", &opc);

        if (opc == 1)
            consultarTodos();
        if (opc == 2)
            listarPelopreco();
        if (opc == 3)
            listarPeloId();
        if (opc == 4)
            iguala();
        if (opc == 5)
            menorque();
        if (opc == 6)
            maiorque();

    } while (opc != 9);
}

void utilitarios(){
    int opc = 1;
    do
    {
        system("cls");
        cabecalho();
        printf("UTILITARIOS\n");
        printf("*******************\n");


        printf("\n 6-1. Relatorio excluidos logicamente");
        printf("\n 6-2. Limpar registros excluidos");
        printf("\n 6-3. Fazer backup de registros");
        printf("\n 7-3. Limpar arquivo dados");
        printf("\n 9. Voltar");

        printf("\n Informe a opcao desejada: ");
        setbuf(stdin, NULL);
        scanf("%d", &opc);

        if (opc == 1)
            listagemApagados();
        if (opc == 2)
            exclusaoFisica();
        if (opc == 3)
            backup();
        if (opc == 4)
            limpar();

    } while (opc != 9);
}
void cabecalho(){
    printf("DISCIPLINA - LINGUAGEM DE PROGRAMACAO - TURNO:NOITE\n");
    printf("NOME: LUIS FELIPE SIQUEIRA GOBETTI - RA:0220482313021\n");
    printf("30/11/2023  19:30\n");
    printf("-----------------------------------------------------\n");
}
int menu()
{
    int opc;
    do{
        system("cls");
        cabecalho();
        printf("MENU PIZZA\n");
        printf("*******************\n");
        printf("\n 1. Inserir");
        printf("\n 2. Consulta");
        printf("\n 3. Editar");
        printf("\n 4. Exclusao");
        printf("\n 5. Relatorios");
        printf("\n 6. Utilitarios ");
        printf("\n 9. Finalizar");
        printf("\n ");

        printf("Digite a opcao desejada:");
        setbuf(stdin, NULL); //limpeza de buffer do teclado para n�o dar nada errado
            scanf("%d",&opc);

        switch (opc){ //

            case 1: cadastrar();
            break;

            case 2: subConsultas();
            break;

            case 3: alterar();
            break;

            case 4: excluirLogica();
            break;

            case 5: relatorios();
            break;

            case 6: utilitarios();
            break;

            default:
            break;
        }

    }while(opc != 9);

}

void subConsultas()
{
    int opc = 1;
    do
    {
        system("cls");
        cabecalho();
        printf("SUBCONSULTA DE PIZZAS\n");
        printf("*******************\n");

        printf("\n 2-1. Todos");
        printf("\n 2-2. Individual");
        printf("\n 9. Voltar");

        printf("\n Informe a opcao desejada: ");
        setbuf(stdin, NULL);
        scanf("%d", &opc);

        if (opc == 1)
            consultarTodos();
        if (opc == 2)
            consultar();

    } while (opc != 9);

}

void cadastrar()
{
    int finalizar = 0;
    struct pizza ficha;
    system("cls");
    cabecalho();
    printf("INSERCAO DE PIZZAS\n");
    printf("*******************\n");

    if ((arq = fopen(FILENAME, "rb+")) == NULL)
    {
        if ((arq = fopen(FILENAME, "wb")) == NULL)
        {
            printf("\n Erro");
            return 1;
        }
    }

    do
    {
        //
        printf("\ninforme o id da pizza: ");
        setbuf(stdin, NULL);
        scanf("%d", &ficha.id);
        printf("\ninforme qual a pizza: ");
        setbuf(stdin, NULL);
        scanf("%[^\n]", ficha.descricao);
        printf("\ninforme o tamanho da pizza: ");
        setbuf(stdin, NULL);
        scanf("%[^\n]", ficha.tamanho);
        printf("\ninforme o valor de venda: ");
        setbuf(stdin, NULL);
        scanf("%f", &ficha.valorVenda);


        ficha.e = 0;
        fseek(arq, 0, 2);
        fwrite(&ficha, sizeof(ficha), 1, arq);

        printf("\n Deseja incluir outro registro? \n Pressione [9] para voltar, outra tecla para continuar..");
        scanf("%d", &finalizar);
    } while (finalizar != 9);
    fclose(arq);
}

void consultarTodos()
{
    int i;
    struct pizza ficha;
    system("cls");
    cabecalho();
    printf("CONSULTA DE PIZZAS\n");


    printf("\n Lista de Todos as pizzas:");
    printf("\n");
    if ((arq = fopen(FILENAME, "rb")) == NULL)
    {
        printf("\n Erro");
        return 0;
    }
    while (fread(&ficha, sizeof(ficha), 1, arq))
    {
        if (ficha.e == 0)
        {
            printf("\n %4d | %s | %s | %.2f ", ficha.id, ficha.descricao, ficha.tamanho, ficha.valorVenda);
        }
    }
    fclose(arq);
    printf("\n tecle qualquer tecla para finalizar ...");
    setbuf(stdin, NULL);
    getch();
}

void consultar()
{
    struct pizza ficha;
    char nome[50];
    system("cls");
    cabecalho();
    printf("CONSULTA DE pizza\n");
    printf("\n Consulta por nome da pizza:");
    printf("\n");
    if ((arq = fopen(FILENAME, "rb")) == NULL)
    {
        printf("\n Erro");
        return 0;
    }
    printf("\n informe o descricao da descricao :");
    setbuf(stdin, NULL);
    scanf("%[^\n]", nome); // � assim que l� string

    int i = 0;
    while (fread(&ficha, sizeof(ficha), 1, arq))
    {
        if (ficha.e == 0)
        {
            if (strcmp(nome, ficha.descricao) == 0) // strcmp compara duas strings, se as duas forem iguais ele retorna 0, dai entra no if.
            {
                printf("\n codigo da descricao.......: %d", ficha.id);
                printf("\n descricao da descricao.........: %s", ficha.descricao);
                printf("\n descricao do tamanho: %s", ficha.tamanho);
                printf("\n valor de venda: %.2f", ficha.valorVenda);
                i = 1;
                break;
            }
        }
    }
    fclose(arq);
    if (i == 0)
    {
        printf("\n Falha, busca nao localizou o descricao da descricao");
    }
    printf("\n");
    printf("\n tecle qualquer tecla para finalizar ...");
    getch();
}

void alterar()
{
    int achou, confirmacao;
    struct pizza ficha;
    char nome[50];
    system("cls");
    cabecalho();
    printf("ALTERAR REGISTRO POR NOME DE PIZZA\n");
    printf("*******************\n");
    printf("\n");
    if ((arq = fopen(FILENAME, "rb+")) == NULL)
    {
        printf("\n Erro");
        return 0;
    }
    printf("\n informe o nome da pizza :");
    setbuf(stdin, NULL);
    scanf("%[^\n]", nome);
    achou = 0;
    while (fread(&ficha, sizeof(ficha), 1, arq))
    {

        if ((ficha.e == 0) && (strcmp(nome, ficha.descricao) == 0))
        {

            fseek(arq, ftell(arq) - sizeof(ficha), 0);

            printf("\ninforme o id da pizza : [ %d ] ", ficha.id);
            scanf("%d", &ficha.id);
            printf("\ninforme a pizza [ %s ]: ", ficha.descricao);
            setbuf(stdin, NULL);
            scanf("%[^\n]", ficha.descricao);
            printf("\ninforme o tamanho [ %s ]: ", ficha.tamanho);
            setbuf(stdin, NULL);
            scanf("%[^\n]", ficha.tamanho);
            printf("\ninforme o valor de venda [ %.2f ]: ", ficha.valorVenda);
            setbuf(stdin, NULL);
            scanf("%f", &ficha.valorVenda);

            printf("\n\n Deseja alterar a ficha? \n Pressione [1] para SIM e outra tecla para cancelar ...");
            scanf("%d", &confirmacao);
            if (confirmacao == 1)
            {
                fwrite(&ficha, sizeof(ficha), 1, arq);
            }
            fseek(arq, 0, 2);
            achou = 1;
        }
    }
    fclose(arq);
    if (achou == 0)
    {
        printf("\n Falha, busca nao localizou o descricao ");
        printf("\n");
        printf("\n tecle qualquer tecla para finalizar ...");
        getch();
    }
}

void excluirLogica()
{
    int achou, confirmacao;
    struct pizza ficha;
    char nome[50];
    system("cls");
    cabecalho();
    printf("EXCLUSAO DE PIZZA POR NOME");
    printf("*******************\n");

    printf("\n");
    if ((arq = fopen(FILENAME, "rb+")) == NULL)
    {
        printf("\n Erro");
        return 0;
    }
    printf("\n informe o nome da pizza que deseja excluir logicamente:");
    setbuf(stdin, NULL);
    scanf("%[^\n]", nome);

    achou = 0;
    while (fread(&ficha, sizeof(ficha), 1, arq))
    {
        if (ficha.e == 0)
        {
            if (strcmp(nome, ficha.descricao) == 0)
            {
                fseek(arq, ftell(arq) - sizeof(ficha), 0);
                printf("\n codigo.........: %d", ficha.id);
                printf("\n descricao:..........: %s", ficha.descricao);
                printf("\n tamanho.........: %s", ficha.tamanho);
                printf("\n valor de venda.........: %.2f", ficha.valorVenda);
                printf("\n\n Deseja excluir a ficha? \n Pressione [1] para SIM e outra tecla para cancelar ...");
                scanf("%d", &confirmacao);
                if (confirmacao == 1) // se for confirmado o "e" da ficha fica 1, assim n�o aparecendo nas telas posteriores.
                {
                    ficha.e = 1;
                    fwrite(&ficha, sizeof(ficha), 1, arq);
                }
                fseek(arq, 0, 2);
                achou = 1;
            }
        }
    }
    fclose(arq);
    if (achou == 0)
    {
        printf("\n Falha, busca nao localizou o descricao ");
        printf("\n");
        printf("\n tecle qualquer tecla para finalizar ...");
        getch();
    }
}

void listagemApagados()
{
    int i, n;
    struct pizza ficha;
    system("cls");
    cabecalho();
    printf("LISTAGEM DE PIZZAS APAGADAS\n");
    printf("*******************\n");

    if ((arq = fopen(FILENAME, "rb")) == NULL)
    {
        printf("\n Erro");
        return 0;
    }
    i = 0;
    while (fread(&ficha, sizeof(ficha), 1, arq))
    {
        if (ficha.e == 1)
        {
            printf("\n %4d | %s | %s | %.2f", ficha.id, ficha.descricao, ficha.tamanho, ficha.valorVenda);
            i++;
        }
    }
    fclose(arq);
    printf("\n tecle qualquer tecla para finalizar ...");
    setbuf(stdin, NULL);
    getch();
}

void listarPeloId()
{
    int i, n;
    struct pizza ficha;
    system("cls");
    cabecalho();
    printf("LISTAGEM DE PIZZA POR ID\n");

    if ((arq = fopen(FILENAME, "rb")) == NULL)
    {
        printf("\n Erro");
        return 0;
    }

    i = 0;
    while (fread(&ficha, sizeof(ficha), 1, arq))
    {
        lista[i] = ficha;
        i++;
    }

    fclose(arq);

    ordenarPorId(i);

    printf("\n");
    for (n = 0; n < i; n++)
    {
        printf("\n codigo.........: %d", lista[n].id);
        printf("\n descricao:..........: %s", lista[n].descricao);
        printf("\n tamanho.........: %s", lista[n].tamanho);
        printf("\n valor de venda.........: %.2f", lista[n].valorVenda);

        printf("\n-----------------------------------------------------\n");
    }
    printf("\n tecle qualquer tecla para finalizar ...");
    setbuf(stdin, NULL);
    getch();
}

void ordenarPorId(int n)
{
    int i, j;
    struct pizza aux;
    for (i = 0; i < n - 1; i++)
    {
        for (j = i + 1; j < n; j++)
        {
            if (lista[i].id > lista[j].id)
            {
                aux = lista[i];
                lista[i] = lista[j];
                lista[j] = aux;
            }
        }
    }
}
void listarPelopreco()
{
    int i, n;
    struct pizza ficha;
    system("cls");
    cabecalho();
    printf("LISTAGEM DE pizza PELO PRECO\n");

    if ((arq = fopen(FILENAME, "rb")) == NULL)
    {
        printf("\n Erro");
        return 0;
    }

    i = 0;
    while (fread(&ficha, sizeof(ficha), 1, arq))
    {
        lista[i] = ficha;
        i++;
    }

    fclose(arq);

    ordenarPorpreco(i);

    printf("\n");
    for (n = 0; n < i; n++)
    {
        printf("\n codigo.........: %d", lista[n].id);
        printf("\n descricao:..........: %s", lista[n].descricao);
        printf("\n tamanho.........: %s", lista[n].tamanho);
        printf("\n valor de venda.........: %.2f", lista[n].valorVenda);

        printf("\n-----------------------------------------------------\n");
    }
    printf("\n tecle qualquer tecla para finalizar ...");
    setbuf(stdin, NULL);
    getch();
}

void ordenarPorpreco(int n)
{

    int i, j;
    struct pizza aux;
    for (i = 0; i < n - 1; i++)
    {
        for (j = i + 1; j < n; j++)
        {
            if (lista[i].valorVenda > lista[j].valorVenda)
            {
                aux = lista[i];
                lista[i] = lista[j];
                lista[j] = aux;
            }
        }
    }
}

int exclusaoFisica()
{
    FILE *arqaux, *arqback;
    struct pizza ficha;


    if ((arq = fopen(FILENAME, "rb")) == NULL)
    {
        printf("\n Erro ao abrir o arquivo");
        return 1;
    }


    if ((arqaux = fopen("auxiliar.fatec", "wb")) == NULL)
    {
        printf("\n Erro ao criar o arquivo auxiliar");
        fclose(arq);
        return 1;
    }


    if ((arqback = fopen("backup.bin", "rb+")) == NULL)
    {

        if ((arqback = fopen("backup.bin", "wb")) == NULL)
        {
            printf("\n Erro ao criar o arquivo de backup");
            fclose(arq);
            fclose(arqaux);
            return 1;
        }
    }

    fseek(arq, 0, SEEK_SET);

    while (fread(&ficha, sizeof(ficha), 1, arq))
    {
        if (ficha.e == 0)
        {
            fwrite(&ficha, sizeof(ficha), 1, arqaux);
        }
        else
        {
            fwrite(&ficha, sizeof(ficha), 1, arqback);
        }
    }

    fclose(arq);
    fclose(arqaux);
    fclose(arqback);


    remove(FILENAME);


    if (rename("auxiliar.fatec", FILENAME) != 0)
    {
        printf("\n Erro ao renomear o arquivo auxiliar");
        return 1;
    }

    return 0;
}


void backup()
{
    FILE *arqback;
    struct pizza ficha;

    if ((arq = fopen(FILENAME, "rb")) == NULL)
    {
        printf("\n Erro ao abrir o arquivo");
        return;
    }

    if ((arqback = fopen("backup.bin", "wb")) == NULL)
    {
        printf("\n Erro ao criar o arquivo de backup");
        fclose(arq);
        return;
    }

    while (fread(&ficha, sizeof(ficha), 1, arq))
    {
        fwrite(&ficha, sizeof(ficha), 1, arqback);
    }

    fclose(arq);
    fclose(arqback);

    printf("\n Backup realizado com sucesso.");
}

