package com.symonjin;


public class StaticShader extends ShaderProgram{
    private static final String VERTEX_FILE = "src/com/symonjin/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/com/symonjin/shaders/fragmentShader.txt";

    public StaticShader(){
        super(VERTEX_FILE, FRAGMENT_FILE);

    }

    @Override
    protected void bindAttr() {
        super.bindAttr(0, "position");
    }
}
