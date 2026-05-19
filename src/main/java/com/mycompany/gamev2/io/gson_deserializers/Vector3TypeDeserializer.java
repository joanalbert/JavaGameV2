/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.io.gson_deserializers;

import com.google.gson.*;
import com.mycompany.gamev2.gamemath.Vector3;
import java.lang.reflect.Type;

/**
 *
 * @author J.A
 */

public class Vector3TypeDeserializer implements JsonDeserializer<Vector3> {
    @Override
    public Vector3 deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
         
        JsonObject obj = json.getAsJsonObject();
        double x = obj.get("x").getAsDouble();
        double y = obj.get("y").getAsDouble();
        //double z = obj.get("z").getAsDouble();
        
        return new Vector3(x,y,0);
    }
}
