package com.qainfotech.tap.training.snl.api;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;



public class BoardTest {
	
	//UUID uuid;
	
	
	@Test
	public void MAXNumberOfExistingPlayers() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, IOException{
		JSONArray players;
		 Board testBoard=new Board();
		for(int playerNO=0;playerNO<4;playerNO++){
			
			try{
			players=testBoard.registerPlayer("paras"+playerNO);
			}
			catch(MaxPlayersReachedExeption exp){
				if(playerNO>4){
					assertEquals(exp, new MaxPlayersReachedExeption(playerNO));
				}
				exp.printStackTrace();
			}
			
			
		}
		
	}
	@Test
	public void IsAnyPlayersHasSameName() throws FileNotFoundException, UnsupportedEncodingException, IOException, GameInProgressException, MaxPlayersReachedExeption{
		JSONArray players;
		Board testBoard=new Board();
		try{
          testBoard.registerPlayer("player1");
          testBoard.registerPlayer("player1");
		}
		catch(PlayerExistsException exp){
			assertEquals(exp, new PlayerExistsException("player1"));
		}
				
	}
	@Test
	public void isPlayerRemoved() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, NoUserWithSuchUUIDException{
		JSONArray players;
		Board testBoard=new Board();
		for(int playerNO=0;playerNO<4;playerNO++){
			players=testBoard.registerPlayer("player"+playerNO);
		}
		
	
		players=testBoard.getPlayersList();
		int originalLength=players.length();
		System.out.println(" : "+players);
		System.out.println(players.length());
		/*for(Object object : players){
			JSONObject player=(JSONObject)object;
			String uuid=player.get("uuid").toString();
			testBoard.deletePlayer(UUID.fromString(uuid));
			
		}*/
		System.out.println("players len : "+players.length());
		for(int index=0;index<players.length();index++){
			JSONObject player=(JSONObject)players.get(index);
			String uuid=player.get("uuid").toString();
			testBoard.deletePlayer(UUID.fromString(uuid));
		}
		System.out.println(" : "+players.length());
		System.out.println(players);
		assertNotEquals(originalLength, players.length());
	
		//System.out.println(testBoard.getPlayersList().length());
		//System.out.println(players);
		
	}
	

}
