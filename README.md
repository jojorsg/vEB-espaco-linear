---------------------------------------
ÁRVORE VAN EMDE BOAS COM ESPAÇO LINEAR
---------------------------------------
Implementação em Java (openjdk 21.0.10) de uma árvore van Emde Boas (vEB) para universo de 32 bits (0 a 2^32 - 1), com uso de tabela de dispersão (TabelaHash) e técnica de table doubling/halving para manter o consumo de memória proporcional ao número de elementos armazenados.

ESTRUTURA DO PROJETO
--------------------
- VEB.java          -> Implementação da árvore van Emde Boas
- TabelaHash.java   -> Implementação de uma tabela de dispersão com operações O(1), linear probing, lazy deletion e redimensionamento automático (doubling/halving)
- Main.java         -> Classe principal com leitura do arquivo e execução
- Makefile          -> Automação de compilação e execução
- README.md         -> Este arquivo

DESCRIÇÃO DAS CLASSES E MÉTODOS
-------------------------------
1. VEB
   - final long u                             : tamanho do universo (potência de 2, >= 2)
   - long min                                 : menor elemento
   - long max                                 : maior elemento
   - VEB resumo                               : subárvore vEB que armazena os índices dos clusters não vazios
   - TabelaHash<Integer, VEB> clusters        : tabela de dispersão com clusters indexados pela parte alta
   - int lowBits                              : número de bits da parte baixa
   - long lowTamanho                          : 2^lowBits
   - int highBits                             : número de bits da parte alta
   - long highTamanho                         : 2^highBits
   - NONE = -1L                               : valor sentinela para “vazio”
   - INF_POS = Long.MAX_VALUE                 : representa +∞
   - INF_NEG = Long.MIN_VALUE                 : representa -∞
   - VEB(long universo)	                      : construtor que cria nó vazio para o universo dado
   - int high(long x)                         : extrai a parte alta do valor (índice do cluster)
   - int low(long x)                          : extrai a parte baixa do valor (valor dentro do cluster)
   - inserir(long x)                          : insere x na estrutura
   - remover(long x)                          : remove x da estrutura (se existir)
   - long sucessor(long x)                    : retorna o menor valor > x ou INF_POS
   - long predecessor(long x)                 : retorna o maior valor < x ou INF_NEG
   - List<Long> coletarElementos()            : coleta todos os elementos da subárvore em ordem crescente
   - imprimir()	                            : retorna a árvore

2. TabelaHash
   - Entry<K,V>[] tabela	                   : vetor que armazena as entradas da tabela (slots)
   - int tamanho	                            : número atual de elementos ativos (não deletados)
   - int capacidade	                         : tamanho total da tabela (número de slots)
   - FATOR_CARGA_MAX = 0.75f                  : limite superior de ocupação
   - FATOR_CARGA_MIN = 0.25f	                : limite inferior de ocupação
   - CAPACIDADE_MINIMA = 16	                : tamanho mínimo da tabela (evita reduções abaixo desse valor)
   - Entry<K,V>                               : K key (chave), V value	(valor associado) e boolean removido (lazy deletion)
   - TabelaHash()	                            : construtor que inicializa a tabela com capacidade mínima
   - int hash(K key)	                         : função de dispersão (hash)
   - void put(K key, V value)	                : insere ou atualiza (caso exista) um par chave‑valor. Utiliza linear probing e aproveita slots marcados como removido. Se a carga ultrapassar 75%, dispara doubling (redimensionar(capacidade * 2)).
   - V get(K key)                             : busca uma chave e retorna o valor associado, ou null se não encontrar
   - V remove(K key)                          : remove a chave (marcação removido = true). Decrementa tamanho e, se a carga cair abaixo de 25% e a capacidade for maior que o mínimo, dispara halving (redimensionar(capacidade / 2)).
   - int tamanho()                            : retorna o número de elementos ativos na tabela
   - boolean vazio()	                         : retorna true se a tabela não contém elementos ativos
   - void redimensionar(int novaCapacidade)   : redimensiona a tabela para a nova capacidade, re‑insere todos os elementos ativos e atualiza os campos capacidade e tamanho.

3. Main
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
- SUC:  
       SUC X   
       resultado (valor estritamente maior que x ou +INF)
- PRE:  
       PRE X  
       resultado (valor estritamente menor que x ou -INF)
- IMP:  
       IMP  
       Estrutura <Min, C[ ]: x, y, z> (mínimo, clusters e valores contidos nos respectivos clusters)

OBSERVAÇÕES
-----------
- Valores fora do universo (x < 0 ou x >= 2^32) são ignorados pela inserção.
- Duplicatas não são inseridas (a estrutura é um set ordenado).

AUTOR/DESENVOLVEDOR
-----------------------
- Josué Roberto Santana Gomes - 603238

------------------------------------------------------------
