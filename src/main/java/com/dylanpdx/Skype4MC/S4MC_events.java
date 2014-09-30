package com.dylanpdx.Skype4MC;

import com.skype.Chat;
import com.skype.Chat.Status;
import com.skype.ChatMessage;
import com.skype.ChatMessage.Type;
import com.skype.Call;
import com.skype.CallListener;
import com.skype.ChatMessageListener;
import com.skype.GlobalChatListener;
import com.skype.Group;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;
import com.skype.connector.Connector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.ServerChatEvent;
import cpw.mods.fml.client.config.GuiConfigEntries.ChatColorEntry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class S4MC_events {
	String lastsent = "";
	Chat g1 = null;
	Chat lastchat = null;
	int conng1;

	public void SendHelp(EntityPlayerMP player,String command,String info){
		SendChat(player,EnumChatFormatting.AQUA+command+" - "+EnumChatFormatting.RESET+info);
	}
	
	public void SendChat(EntityPlayerMP player, String message) {
		String thissent = message;
		if (!thissent.equals(lastsent)) {
			player.addChatMessage(new ChatComponentText(message));
			lastsent = thissent;
		}

	}

	public void SendChatClient(EntityClientPlayerMP player, String message) {
		String thissent = message;
		if (!thissent.equals(lastsent)) {
			player.addChatMessage(new ChatComponentText(message));
			lastsent = thissent;
		}
	}

	@SubscribeEvent
	public void PlayerChat(ServerChatEvent event) throws SkypeException {
		// ~~~~~~~

		Skype.addCallListener(new CallListener() {

			@Override
			public void callReceived(Call arg0) throws SkypeException {
				// TODO Auto-generated method stub
				S4MC_events.this.SendChatClient(Minecraft.getMinecraft().thePlayer, EnumChatFormatting.YELLOW
						+ "Incoming call from " + EnumChatFormatting.AQUA + arg0.getPartner().getFullName());
			}

			@Override
			public void callMaked(Call arg0) throws SkypeException {
				// TODO Auto-generated method stub
				S4MC_events.this.SendChatClient(Minecraft.getMinecraft().thePlayer, EnumChatFormatting.YELLOW
						+ "Calling " + EnumChatFormatting.AQUA + arg0.getPartner().getFullName());
			}
		});

		Skype.addChatMessageListener(new ChatMessageListener() {

			@Override
			public void chatMessageSent(ChatMessage arg0) throws SkypeException {
				// TODO Auto-generated method stub
				String recepients = "";
				String thissent = EnumChatFormatting.BLUE + "Me >> " + arg0.getChat().getWindowTitle() + ": "
						+ EnumChatFormatting.RESET + arg0.getContent();
				S4MC_events.this.SendChatClient(Minecraft.getMinecraft().thePlayer, thissent);
				// lastchat = arg0.getChat();
			}

			@Override
			public void chatMessageReceived(ChatMessage arg0) throws SkypeException {
				// TODO Auto-generated method stub
				// if (arg0.getStatus() == ChatMessage.Status.RECEIVED){
				String chat = "";
				String thissent = "";
				chat = arg0.getContent();
				if (arg0.getType() == Type.EMOTED) {
					chat = arg0.getSender().getFullName() + " " + arg0.getContent();
					thissent = EnumChatFormatting.BLUE + "" + EnumChatFormatting.ITALIC + chat + "";
					S4MC_events.this.SendChatClient(Minecraft.getMinecraft().thePlayer, thissent);
					lastchat = arg0.getChat();
					return;
				}

				System.out.println(arg0.getChat().getAllMembers().length);

				if (arg0.getChat() == g1) {
					thissent = EnumChatFormatting.BLUE + arg0.getSender().getFullName() + EnumChatFormatting.GOLD
							+ " (Group 1)" + EnumChatFormatting.BLUE + " >> Me: " + EnumChatFormatting.RESET + chat;
				} else if (arg0.getAllUsers().length > 2) {
					thissent = EnumChatFormatting.BLUE + arg0.getSender().getFullName() + "(Group) >> Me: "
							+ EnumChatFormatting.RESET + chat;
				} else {
					thissent = EnumChatFormatting.BLUE + arg0.getSender().getFullName() + " >> Me: "
							+ EnumChatFormatting.RESET + chat;
				}
				S4MC_events.this.SendChatClient(Minecraft.getMinecraft().thePlayer, thissent);
				lastchat = arg0.getChat();

			}

			// }
		});

		// ~~~~~~~

		System.out.println(event.message);
		if (event.message.startsWith(".skype")) {
			String[] args = event.message.split(" ");
			if (args.length == 1) {
				SendChat(event.player, EnumChatFormatting.RED + "Not enough Arguments.");
				SendChat(event.player, EnumChatFormatting.RED + "Use '.skype help' to see commands.");
			} else {
				EntityPlayerMP ply = event.player;
				int multis = 0;
				if (args[1].equals("decline") || args[1].equals("hangup")) {
					for (int i = 0; i < Skype.getAllActiveCalls().length; i++) {
						Skype.getAllActiveCalls()[i].cancel();
					}

				}

				else if (args[1].equals("answer")) {
					for (int i = 0; i < Skype.getAllActiveCalls().length; i++) {
						if (Skype.getAllActiveCalls()[i].getStatus() == com.skype.Call.Status.INPROGRESS) {

						} else {
							Skype.getAllActiveCalls()[i].answer();
						}

					}

				}

				else if (args[1].equals("help")) {

					SendChat(ply, "~~ Help Menu " + EnumChatFormatting.GOLD + "(1/1)" + EnumChatFormatting.RESET
							+ " ~~");
					SendHelp(ply, "connect","Tells Skype to connect with Minecraft.");
					SendHelp(ply, "about","Displays mod info");
					SendHelp(ply, "send <name> <message>","Sends a message to a Skype User.");
					SendHelp(ply, "r <message>"," Replies to the last person you contacted.");
					SendHelp(ply, "answer","Answers a currently incoming call");
					SendHelp(ply, "decline","Declines a call or hangs up a current call.");
					SendHelp(ply, "call <name[s]>","Calls the person/people you specified. (Separated by commas)");
					SendHelp(ply, "hangup","Declines a call or hangs up a current call.");
					SendChat(ply, EnumChatFormatting.GOLD+"~ Prefixes ~"); // 
					SendHelp(ply, "n;","Name instead of Username");
					SendChat(ply, "Example: .skype call n;John"); //
				}
				else if (args[1].equals("send")) {
					String tosend = "";
					for (int i = 3; i < args.length; i++) {
						tosend = tosend + args[i] + " ";

					}
					Skype.chat(NametoUser(args[2])).send(tosend);
				}

				else if (args[1].equals("connect")) {
					try {
						System.out.println(Skype.getProfile().getStatus());
						SendChat(ply, EnumChatFormatting.GREEN + "Successfully Connected.");
					} catch (Exception ex) {
						SendChat(ply, EnumChatFormatting.RED + "Error Connecting.");
					}
				}

				else if (args[1].equals("r")) {
					String tosend = "";
					if (lastchat != null) {
						for (int i = 2; i < args.length; i++) {
							tosend = tosend + args[i] + " ";

						}
						lastchat.send(tosend);
					}
				}
				/*
				 * if (args[1].equals("connectgroup")) { for (int i = 0; i <
				 * Skype.getAllChats().length; i++) { if
				 * (Skype.getAllChats()[i].getStatus() ==
				 * Status.MULTI_SUBSCRIBED) { Chat allchats =
				 * Skype.getAllChats()[i]; SendChat( ply, i + ": " +
				 * allchats.getRecentChatMessages()[allchats
				 * .getRecentChatMessages().length - 1] .getContent()); if
				 * (args.length > 2) { if (args[2].equals(String.valueOf(i))) {
				 * g1 = allchats.getRecentChatMessages()[0] .getChat();
				 * SendChat( ply, EnumChatFormatting.GREEN +
				 * "Successfully connected to group id " + i + "."); conng1 = i;
				 * } } } } }
				 */
				else if (args[1].equals("call")) {
					if (args[2].contains(",")) {
						String orig;
						String[] split;
						String finals = "";
						orig = args[2];
						split = orig.split(",");
						for (int i = 0; i < split.length; i++) {
							finals = finals + NametoUser(split[i]);
							if (i != finals.length()) {
								finals = finals + ",";
							}
						}
						Skype.call(finals);

					} else {
						Skype.call(NametoUser(args[2]));
					}
				}
				else if (args[1].equals("list")) {
					Chat[] allchats = Skype.getAllChats();
					int groupchats = 0;
					int singlechats = 0;
					for (int i = 0; i < allchats.length; i++) {
						if (allchats[i].getAllMembers().length > 1) {
							groupchats++;
						} else {
							singlechats++;
						}
						// SendChat(ply,""+allchats[i].getAllMembers().length);
					}
					SendChat(ply, "group: " + groupchats + " single:" + singlechats);
				}
				else{
					SendChat(event.player, EnumChatFormatting.RED + "Bad command name.");
					SendChat(event.player, EnumChatFormatting.RED + "Use '.skype help' to see commands.");
				}
			}

			event.setCanceled(true);
		}

	}

	public String NametoUser(String name) throws SkypeException {
		String lastuser = "err12";
		int found = 0;
		if (name.startsWith("n;")) {
			name = name.replace("n;", "");
			for (int o = 0; o < Skype.getContactList().getAllFriends().length; o++) {
				if (Skype.getContactList().getAllFriends()[o].getFullName().toLowerCase().contains(name.toLowerCase())) {
					found++;
					lastuser = Skype.getContactList().getAllFriends()[o].getId();
					// Skype.getUser("").getDisplayName();
				}
			}
			if (found == 1) {
				return lastuser;
			} else {
				return name;
			}
		} else {
			return name;
		}

	}
}
