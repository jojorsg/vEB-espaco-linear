
======================================================
===     ÁRVORE VAN EMDE BOAS COM ESPAÇO LINEAR     ===
======================================================

Implementação em Java (openjdk 21.0.10) de uma árvore van Emde Boas (vEB) para universo de 32 bits (0 a 2^32 - 1), com uso de tabela de dispersão (HashMap) e técnica de table doubling/halving para manter o consumo de memória proporcional ao número de elementos armazenados.

ESTRUTURA DO PROJETO
--------------------
- VEB.java      -> Implementação da árvore van Emde Boas
- Main.java     -> Classe principal com leitura do arquivo e execução
- Makefile      -> Automação de compilação e execução
- README.md     -> Este arquivo

DESCRIÇÃO DAS CLASSES E MÉTODOS
-------------------------------
1. VEB
   - final long u                      : tamanho do universo (potência de 2, >= 2)
   - long min                          : menor elemento
   - long max                          : maior elemento
   - VEB resumo                        : subárvore vEB que armazena os índices dos clusters não vazios
   - Map<Integer, VEB> clusters        : mapa de clusters indexados pela parte alta (usando HashMap)
   - int lowBits                       : número de bits da parte baixa
   - long lowTamanho                   : 2^lowBits
   - int highBits                      : número de bits da parte alta
   - long highTamanho                  : 2^highBits
   - NONE = -1L                        : valor sentinela para “vazio”
   - INF_POS = Long.MAX_VALUE          : representa +∞
   - INF_NEG = Long.MIN_VALUE          : representa -∞
   - VEB(long universo)	               : construtor que cria nó vazio para o universo dado
   - int high(long x)                  : extrai a parte alta do valor (índice do cluster)
   - int low(long x)                   : extrai a parte baixa do valor (valor dentro do cluster)
   - inserir(long x)                   : insere x na estrutura
   - remover(long x)                   : remove x da estrutura (se existir)
   - long sucessor(long x)	            : retorna o menor valor > x ou INF_POS
   - long predecessor(long x)	         : retorna o maior valor < x ou INF_NEG
   - List<Long> coletarElementos()	   : coleta todos os elementos da subárvore em ordem crescente
   - imprimir()	                     : retorna a árvore

2. Main
   - Lê arquivo de entrada (passado como argumento)
   - Para cada linha identifica operação (INC, REM, SUC, PRE, IMP)
   - Chama método correspondente da árvore
   - Imprime resultados conforme especificado

COMO EXECUTAR
-------------
1. Compilar:
   - Linux: make ou make build
   - Windows: javac Main.java

2. Executar com arquivo de entrada:
   - Linux: java Main "NOME_DO_ARQUIVO".txt ou make run INPUT="NOME_DO_ARQUIVO".txt (por padrão, se não for passado um arquivo INPUT, o comando make run irá executar o arquivo de entrada "entrada.txt")
   - Windows: java Main "NOME_DO_ARQUIVO".txt

3. Limpar arquivos .class:
   make clean

FORMATO DA ENTRADA
------------------
Cada linha contém uma operação:
   INC <x> -> insere o valor de x na estrutura
   REM <x> -> remove o valor de x (se existir) na estrutura
   SUC <x> -> sucessor de x
   PRE <x> -> predecessor de x
   IMP     -> imprime o estado atual da estrutura

FORMATO DA SAÍDA
----------------
- SUC: SUC <x> 
       <resultado> (valor estritamente maior que x ou +INF)
- PRE: PRE <x> 
       <resultado> (valor estritamente menor que x ou -INF)
- IMP: IMP 
       Estrutura <Min, C[]: x, y, z> (mínimo, clusters e valores contidos nos respectivos clusters)

AUTOR/DESENVOLVEDOR
-----------------------
- Josué Roberto Santana Gomes - 603238

===========================================================