package me.dablakbandit.grandtheftminecart.player.listener;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;

import me.dablakbandit.grandtheftminecart.player.PlayerManager;

import org.bukkit.entity.Damageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CrackshotListener implements Listener{

    public CrackshotListener(){}

    @EventHandler
    public void hurtEntity(WeaponDamageEntityEvent e){
        if(e.getVictim() instanceof Damageable){
            PlayerManager.getInstance().hurtEntity(e.getPlayer(), (Damageable)e.getVictim(), e.getDamage());
        }
    }
}
