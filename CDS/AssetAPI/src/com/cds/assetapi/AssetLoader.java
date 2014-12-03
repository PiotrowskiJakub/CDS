/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.assetapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 *
 * @author Jakub Piotrowski
 */
public class AssetLoader 
{
    public static JsonArray mapParametersJsonMatrix;
    
    public static void load()
    {
        ClassLoader cl = AssetLoader.class.getClassLoader();
        Reader reader = new InputStreamReader(cl.getResourceAsStream("com/cds/files/tablica.json"));
        JsonParser parser = new JsonParser();
        mapParametersJsonMatrix = (JsonArray) parser.parse(reader);
    }
}
