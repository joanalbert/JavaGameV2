/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.io.gson_deserializers;


import com.google.gson.*;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridTile;
import java.lang.reflect.Type;

/**
 *
 * @author J.A
 */
public class ColisionTypeDeserializer implements JsonDeserializer<LevelGridTile.COLISION_TYPE> {
    @Override
    public LevelGridTile.COLISION_TYPE deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String label = json.getAsString();
        if(label == null || label.isEmpty() || label.equals("")) label = "B";
        return LevelGridTile.COLISION_TYPE.fromLabel(label);
    }
}
