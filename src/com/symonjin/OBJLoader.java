package com.symonjin;

import com.symonjin.models.Model;
import com.symonjin.vector.Vector2f;
import com.symonjin.vector.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class OBJLoader {

    public static Model loadObjModel(String src, Loader loader) {
        //TODO: Optimize this code to use Java 8's stream
        FileReader fr = null;
        try {
            fr = new FileReader(new File("res/" + src + ".obj"));
        } catch (FileNotFoundException err) {
            System.out.println("Can't load file");
            err.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(fr);
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float[] verticesArray = null;
        float[] normalArray = null;
        float[] textureArray = null;
        int[] indicesArray = null;

        try {
            Scanner sc = new Scanner(reader);
            while(true){
                String symbol = sc.next();
                if (symbol.equals("v")) {
                    vertices.add(new Vector3f(sc.nextFloat(), sc.nextFloat(), sc.nextFloat()));
                } else if (symbol.equals("vn")) {
                    normals.add(new Vector3f(sc.nextFloat(), sc.nextFloat(), sc.nextFloat()));
                } else if (symbol.equals("vt")) {
                    textures.add(new Vector2f(sc.nextFloat(), sc.nextFloat()));
                } else if (symbol.equals("f")) {
                    textureArray = new float[vertices.size() * 2];
                    normalArray = new float[vertices.size() * 3];
                    break;
                }
            }

            line = sc.nextLine();

            System.out.println("Finished parsing vertices, normals, and textures");
            while (sc.hasNext()) {
                if (!line.startsWith("f ")) {
                    line = sc.nextLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                processVertex(vertex1, indices, textures, normals, textureArray, normalArray);
                processVertex(vertex2, indices, textures, normals, textureArray, normalArray);
                processVertex(vertex3, indices, textures, normals, textureArray, normalArray);
                line = sc.nextLine();
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finished processing vertices");

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        System.out.println("Finished loading obj file");
        return loader.loadToVAO(verticesArray, indicesArray, textureArray, normalArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices,
                                      List<Vector2f> textures, List<Vector3f> normals,
                                      float[] textureArray, float[] normalArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexPointer * 2] = currentTex.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalArray[currentVertexPointer * 3] = currentNorm.x;
        normalArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalArray[currentVertexPointer * 3 + 2] = currentNorm.z;
    }

}
