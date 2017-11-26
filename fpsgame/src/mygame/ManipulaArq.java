package mygame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
 
public class ManipulaArq {
 
    public static ArrayList leitor(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));       
        ArrayList lines = new ArrayList();
        int coluna = 0;
        String line = "";
        
        while (true) {
            line = reader.readLine();
            if (line == null) {
                reader.close();
                break;
            } else if (!line.startsWith("!")) {
                lines.add(line);
                coluna = Math.max(coluna, line.length());
                Main.jMap = coluna;
            }
        }
        Main.iMap = lines.size();
        return lines;
    }
 
    public static void escritor(String path) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        String linha = "Teste";
            
        //buffWrite.append(linha + "\n");
        buffWrite.write(linha);
        buffWrite.newLine();
        buffWrite.close();
    }
 
}
