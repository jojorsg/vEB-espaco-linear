/**
 * Arvore van Emde Boas com espaço linear
 * 
 * Disciplina: Estrutura de Dados
 * 
 * Aluno:
 * @author Josué Roberto 
 * @version 1.0
 * @since 09/06/2026
 * 
 */

import java.util.*;

class VEB {
    final long u;
    long min;
    long max;
    VEB resumo;
    Map<Integer, VEB> clusters;
    int lowBits;
    long lowTamanho;
    int highBits;
    long highTamanho;

    public static final long NONE = -1L;
    public static final long INF_POS = Long.MAX_VALUE;
    public static final long INF_NEG = Long.MIN_VALUE;


    // Constrói um nó para o universo de tamanho u
    public VEB(long universo) {
        this.u = universo;
        min = NONE;
        max = NONE;
        if (u > 2) {
            int k = 63 - Long.numberOfLeadingZeros(u); //calcula a divisão de bits
            lowBits = k / 2;
            highBits = k - lowBits;
            lowTamanho  = 1L << lowBits;
            highTamanho = 1L << highBits;
            resumo = new VEB(highTamanho);
            clusters = new HashMap<>();
        } else {                      // sendo u == 2 ele é caso base sem resumo e clusters
            lowBits = highBits = 0;
            lowTamanho = highTamanho = 0;
        }
    }

    private int high(long x) {     // parte alta
        return (int)(x >> lowBits);
    }

    private int low(long x) {          // parte baixa
        return (int)(x & (lowTamanho - 1));
    }

    public void inserir(long x) {
        if (min == NONE) {
            min = max = x;
            return;
        }
        if (x == min || x == max) return;   // evita inserção de valores repetidos
        if (x < min) {               // novo mínimo
            long tmp = min;
            min = x;
            x = tmp;
        }
        if (u > 2) {
            int h = high(x);
            int l = low(x);
            VEB cluster = clusters.get(h);
            if (cluster == null) {
                cluster = new VEB(lowTamanho);
                clusters.put(h, cluster);
                resumo.inserir(h);
            }
            cluster.inserir(l);
        }
        if (x > max) {
            max = x;
        }
    }

    public void remover(long x) {
        if (min == NONE) return;

        if (min == max) {
            if (x == min) min = max = NONE;
            return;
        }

        if (u == 2) {
            if (x == min) min = max;
            else if (x == max) max = min;
            return;
        }

        if (x == min) {
            long primeiroHigh = resumo.min;
            VEB primeiroCluster = clusters.get((int)primeiroHigh);
            long novoMinLow = primeiroCluster.min;
            min = (primeiroHigh << lowBits) | novoMinLow;
            x = min;
        }

        int h = high(x);
        int l = low(x);
        VEB cluster = clusters.get(h);
        if (cluster == null) return;

        cluster.remover(l);
        if (cluster.min == NONE) {
            clusters.remove(h);
            resumo.remover(h);
        }

        if (x == max) {
            if (resumo.min == NONE) {
                max = min;
            } else {
                long ultimoHigh = resumo.max;
                VEB ultimoCluster = clusters.get((int)ultimoHigh);
                long novoMaxLow = ultimoCluster.max;
                max = (ultimoHigh << lowBits) | novoMaxLow;
            }
        }
    }

    // Retorna o menor valor que é estritamente maior que x ou INF_POS caso não exista
    public long sucessor(long x) {
        if (min == NONE) return INF_POS;
        if (x < min) return min;

        if (u == 2) {
            if (x < max) return max;
            return INF_POS;
        }

        int h = high(x);
        int l = low(x);
        VEB cluster = clusters.get(h);
        if (cluster != null && l < cluster.max) {
            long sucLow = cluster.sucessor(l);
            // Converte h para long antes do shift para evitar overflow de sinal
            return ((long)h << lowBits) | sucLow;
        }

        long proxHigh = resumo.sucessor(h);
        if (proxHigh == INF_POS) {
            if (x < max) return max;
            return INF_POS;
        }

        VEB proxCluster = clusters.get((int)proxHigh);
        long sucLow = proxCluster.min;
        return (proxHigh << lowBits) | sucLow;
    }

    // Retorna o maior valor que é estritamente menor que x ou INF_NEG caso não exista
    public long predecessor(long x) {
        if (min == NONE) return INF_NEG;
        if (x > max) return max;
        if (x <= min) return INF_NEG;

        if (u == 2) {
            if (x > min) return min;
            return INF_NEG;
        }

        int h = high(x);
        int l = low(x);
        VEB cluster = clusters.get(h);
        if (cluster != null && l > cluster.min) {
            long predLow = cluster.predecessor(l);
            // Converte h para long antes do shift para evitar overflow de sinal
            return ((long)h << lowBits) | predLow;
        }

        long prevHigh = resumo.predecessor(h);
        if (prevHigh == INF_NEG) {
            if (x > min) return min;
            return INF_NEG;
        }

        VEB prevCluster = clusters.get((int)prevHigh);
        long predLow = prevCluster.max;
        return (prevHigh << lowBits) | predLow;
    }

    public List<Long> coletarElementos() {    // coleta todos os elementos da subárvore em ordem crescente
        List<Long> resultado = new ArrayList<>();
        if (min == NONE) return resultado;
        resultado.add(min);
        if (u == 2) {
            if (max != min && max != NONE) resultado.add(max);
            return resultado;
        }
        if (resumo.min != NONE) {
            for (long h : resumo.coletarElementos()) {
                VEB cluster = clusters.get((int)h);
                if (cluster != null) {
                    for (long low : cluster.coletarElementos()) {
                        resultado.add((h << lowBits) | low);
                    }
                }
            }
        }
        return resultado;
    }

    public String imprimir() {
        if (min == NONE) return "Min: -INF";
        StringBuilder sb = new StringBuilder();
        sb.append("Min: ").append(min);
        if (u > 2 && resumo.min != NONE) {
            for (long h : resumo.coletarElementos()) {
                sb.append(", C[").append(h).append("]: ");
                VEB cl = clusters.get((int)h);
                List<Long> lows = cl.coletarElementos();
                for (int i = 0; i < lows.size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(lows.get(i));
                }
            }
        }
        return sb.toString();
    }
}
