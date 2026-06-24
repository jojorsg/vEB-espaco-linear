/**
 * Arvore van Emde Boas com espaço linear
 * 
 * Disciplina: Estrutura de Dados
 * 
 * Aluno:
 * @author Josué Roberto 
 * @version 2.0
 * @since 09/06/2026
 * 
 */

import java.util.*;


public class TabelaHash<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;
        boolean removido;    // lazy deletion para linear probing

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.removido = false;
        }
    }

    private Entry<K, V>[] tabela;
    private int tamanho;            // elementos ativos (não removidos)
    private int capacidade;
    private static final float FATOR_CARGA_MAX = 0.75f;
    private static final float FATOR_CARGA_MIN = 0.25f;
    private static final int CAPACIDADE_MINIMA = 16;

    @SuppressWarnings("unchecked")
    public TabelaHash() {
        this.capacidade = CAPACIDADE_MINIMA;
        this.tamanho = 0;
        this.tabela = (Entry<K, V>[]) new Entry[capacidade];
    }

    //Função de hash
    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % capacidade;
    }

    // Inserção ou atualização de chave
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Chave nula não permitida");
        int indice = hash(key);
        int primeiroDeletado = -1;

        // Linear probing: percorre até encontrar um slot null
        while (tabela[indice] != null) {
            if (tabela[indice].removido) {
                // Guarda o primeiro deletado para possível reinserção
                if (primeiroDeletado == -1) {
                    primeiroDeletado = indice;
                }
            } else if (tabela[indice].key.equals(key)) {
                // Chave já existe -> atualiza valor
                tabela[indice].value = value;
                return;
            }
            indice = (indice + 1) % capacidade;
        }

        //Decide onde inserir, reaproveitando um deletado ou usando o slot null atual
        int indiceInsercao = (primeiroDeletado != -1) ? primeiroDeletado : indice;
        if (primeiroDeletado != -1) {
            tabela[primeiroDeletado].removido = false;
            tabela[primeiroDeletado].key = key;
            tabela[primeiroDeletado].value = value;
        } else {
            tabela[indice] = new Entry<>(key, value);
        }
        tamanho++;

        // Doubling: se a carga > 75%, dobra o tamanho
        if (tamanho > capacidade * FATOR_CARGA_MAX) {
            redimensionar(capacidade * 2);
        }
    }

    // Busca por chave (retorna null se não encontrada)
    public V get(K key) {
        int indice = hash(key);
        while (tabela[indice] != null) {
            if (!tabela[indice].removido && tabela[indice].key.equals(key)) {
                return tabela[indice].value;
            }
            indice = (indice + 1) % capacidade;
        }
        return null;
    }

    public V remove(K key) {
        int indice = hash(key);
        while (tabela[indice] != null) {
            if (!tabela[indice].removido && tabela[indice].key.equals(key)) {
                tabela[indice].removido = true;
                V valor = tabela[indice].value;
                tamanho--;

                // Halving: se carga < 25% e capacidade acima do mínimo, reduz pela metade
                if (capacidade > CAPACIDADE_MINIMA && tamanho < capacidade * FATOR_CARGA_MIN) {
                    redimensionar(capacidade / 2);
                }
                return valor;
            }
            indice = (indice + 1) % capacidade;
        }
        return null;
    }

    public int tamanho() { return tamanho; }

    public boolean vazio() { return tamanho == 0; }


    @SuppressWarnings("unchecked")
    private void redimensionar(int novaCapacidade) {
        Entry<K, V>[] tabelaAntiga = tabela;
        int capacidadeAntiga = capacidade;
        capacidade = novaCapacidade;
        tabela = (Entry<K, V>[]) new Entry[novaCapacidade];
        tamanho = 0;   // será reconstruído pelo put

        for (int i = 0; i < capacidadeAntiga; i++) {
            if (tabelaAntiga[i] != null && !tabelaAntiga[i].removido) {
                put(tabelaAntiga[i].key, tabelaAntiga[i].value);
            }
        }
    }
}