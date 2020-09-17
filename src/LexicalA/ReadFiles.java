/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LexicalA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Jimena Valderrama
 */
public class ReadFiles {

    private BufferedReader reader;
    private FileReader file;
    private String[] words;

    // Permite validar las words contenidas en un file
    public void read(String path) {
        try {
            // Contar las currentLines y crear un arreglo
            file = new FileReader(path);
            reader = new BufferedReader(file);
            words = new String[(int) reader.lines().count()];
            file.close();
            reader.close();

            // Leer currentLine por currentLine su contenido
            file = new FileReader(path);
            reader = new BufferedReader(file);

            String currentLine;
            int ct = 0;

            while ((currentLine = reader.readLine()) != null) {
                words[ct] = currentLine;
                ct++;
            }
            file.close();
        } catch (IOException ioe) {
            System.out.println("Error while reading file "
                    + path + ": " + ioe);
        }
    }

    // Permite regresar el contenido por currentLine de un file
    public String[] getContent() {
        return words;
    }
}
