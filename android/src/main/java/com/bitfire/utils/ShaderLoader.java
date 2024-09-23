

package com.bitfire.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public final class ShaderLoader {
    public static String BasePath = "";
    public static boolean Pedantic = true;

    public static ShaderProgram fromFile(String vertexFileName, String fragmentFileName) {
        return ShaderLoader.fromFile(vertexFileName, fragmentFileName, "");
    }

    public static ShaderProgram fromFile(String vertexFileName, String fragmentFileName, String defines) {
        String log = "\"" + vertexFileName + "/" + fragmentFileName + "\"";
        if (defines.length() > 0) {
            log += " w/ (" + defines.replace("\n", ", ") + ")";
        }
        log += "...";
        Gdx.app.log("ShaderLoader", "Compiling " + log);

        String vpSrc = Gdx.files.internal(BasePath + vertexFileName + ".vertex").readString();
        String fpSrc = Gdx.files.internal(BasePath + fragmentFileName + ".fragment").readString();

        ShaderProgram program = ShaderLoader.fromString(vpSrc, fpSrc, vertexFileName, fragmentFileName, defines);
        return program;
    }

    public static ShaderProgram fromString(String vertex, String fragment, String vertexName, String fragmentName) {
        return ShaderLoader.fromString(vertex, fragment, vertexName, fragmentName, "");
    }

    public static ShaderProgram fromString(String vertex, String fragment, String vertexName, String fragmentName, String defines) {
        ShaderProgram.pedantic = ShaderLoader.Pedantic;
        ShaderProgram shader = new ShaderProgram(defines + "\n" + vertex, defines + "\n" + fragment);

        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("shader compilation failed: " + shader.getLog());
        }

        return shader;
    }

    private ShaderLoader() {
    }
}
