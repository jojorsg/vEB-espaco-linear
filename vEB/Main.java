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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            return;
        }
        VEB veb = new VEB(1L << 32);
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                switch (parts[0].toUpperCase()) {
                    case "INC":
                        veb.inserir(Long.parseLong(parts[1]));
                        break;
                    case "REM":
                        veb.remover(Long.parseLong(parts[1]));
                        break;
                    case "SUC":
                        long val = Long.parseLong(parts[1]);
                        System.out.println("SUC " + val);
                        long suc = veb.sucessor(val);
                        System.out.println(suc == VEB.INF_POS ? "+INF" : suc);
                        break;
                    case "PRE":
                        val = Long.parseLong(parts[1]);
                        System.out.println("PRE " + val);
                        long pre = veb.predecessor(val);
                        System.out.println(pre == VEB.INF_NEG ? "-INF" : pre);
                        break;
                    case "IMP":
                        System.out.println("IMP");
                        System.out.println(veb.imprimir());
                        break;
                }
            }
        }
    }
}