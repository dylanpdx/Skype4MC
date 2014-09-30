package com.dylanpdx.Skype4MC;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

import com.skype.*;

@Mod(modid = "Skype4MC", name = "Skype4MC", version = "1.0")
public class Skype4MC_main {
	

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws SkypeException {
    	MinecraftForge.EVENT_BUS.register(new S4MC_events());
    	//Packet3Chat.maxChatLength = 32767;
    	//System.out.println(Skype.getProfile().getMoodMessage());
    	//FMLCommonHandler.instance().bus().register(this);

    }

}
