/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.providers.base;

import com.mycompany.gamev2.providers.base.BaseProvider;
import java.util.HashMap;
import java.util.Optional;

/**
 *
 * @author J.A
 */
public class ProviderBundle {
    
    private HashMap<Class<?>, BaseProvider> bundle;
    
    public ProviderBundle(){
        bundle = new HashMap<Class<?>, BaseProvider>();
    }
   
    public <T extends BaseProvider> Optional<T> tryGet(Class<T> type){
        BaseProvider provider = bundle.get(type);
        if(provider != null && type.isInstance(provider)) return Optional.of(type.cast(provider));
        else return Optional.empty();
    }
    
    
    public <T extends BaseProvider> void add(Class<T> provider_type, T provider){
        bundle.put(provider_type, provider);
    }
    
    public <T extends BaseProvider> void remove(Class<T> type){
        bundle.remove(type);
    }
    
    public void clear(){
        bundle.clear();;
    }
}
