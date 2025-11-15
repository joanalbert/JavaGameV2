/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.event_system.game_events;

import com.mycompany.gamev2.providers.base.ProviderBundle;

/**
 *
 * @author J.A
 */
public abstract class BaseEvent {
    protected ProviderBundle providers_bundle = new ProviderBundle();
    protected ProviderBundle get_providers(){return this.providers_bundle;}
}
