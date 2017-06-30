package com.qainfotech.tap.training.snl.api;

import static org.testng.Assert.assertEquals;

import static org.testng.Assert.assertNotEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.mockito.*;



public class BoardTest {
	
	//UUID uuid;
	
	/**
	 * validating maximun number of players
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws PlayerExistsException
	 * @throws GameInProgressException
	 * @throws IOException
	 */
	
	
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
	/**
	 * validating that players has unique names
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws GameInProgressException
	 * @throws MaxPlayersReachedExeption
	 */
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
	
	/**
	 * validating given uuid of player is removed.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws PlayerExistsException
	 * @throws GameInProgressException
	 * @throws MaxPlayersReachedExeption
	 * @throws NoUserWithSuchUUIDException
	 */
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
	
	@Test
	public void IsPlayerClimbLadder() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException{
				
		//Board mockBoard=Mockito.mock(Board.class);
      /*  
		BoardModelService.init(board.getUUID());
		Board mockBoard=new Board()
		Mockito.when(BoardModel.data(mockBoard.getUUID())).then((Answer<?>) BoardModelService.data(mockBoard.getUUID()));
		JSONArray players=null;
		for(int playerNO=0;playerNO<4;playerNO++){
			players=mockBoard.registerPlayer("player"+playerNO);
		}
		System.out.println(mockBoard.getData());
		*/
		int target=0;
		UUID uuid=UUID.randomUUID();
		BoardModelService.init(uuid);
		Board customizeBoard=new Board(uuid);
		JSONArray players=null;
		for(int playerNO=0;playerNO<4;playerNO++){
			players=customizeBoard.registerPlayer("player"+playerNO);
		}
		players=customizeBoard.getPlayersList();
	    JSONObject player1=(JSONObject)players.get(0);
	    int playerPosition=Integer.parseInt(player1.get("position").toString());
	    JSONObject data=customizeBoard.getData();
	    
	    
	    /*for(int itr=0;itr<steps.length();itr++){
	    	JSONObject step=(JSONObject) steps.get(itr);
	    	System.out.println(step);
	    	int number=Integer.parseInt(step.get("number").toString());
	    	if(number==playerPosition){
	    		System.out.println("step found");
	          target=Integer.parseInt(step.get("target").toString());
	    	}
	    	
	    }*/
	    String uuidString=player1.get("uuid").toString();
	    UUID uuidOfPlayer=UUID.fromString(uuidString);
	    //System.out.println("target  : "+target);
		/*System.out.println(customizeBoard.getData());
		System.out.println(uuidString);*/
	    JSONObject jsonObj=customizeBoard.rollDice(uuidOfPlayer);
	    int dice=Integer.parseInt(jsonObj.get("dice").toString());
	    JSONArray steps=(JSONArray) data.get("steps");
	    JSONObject step=(JSONObject) steps.get(dice+playerPosition);
	    int expectedTarget=Integer.parseInt(step.get("target").toString());
	    int actualTarget=Integer.parseInt(jsonObj.get("message").toString().substring(jsonObj.get("message").toString().lastIndexOf(' ')).trim());
	 
	    Assert.assertEquals(expectedTarget, actualTarget);
	    /*System.out.println("player pos : "+playerPosition);
	    System.out.println("dice : "+dice);
	    System.out.println(dice+playerPosition);
	   System.out.println(expectedTarget + " : "+ actualTarget);
		System.out.println(jsonObj);
		System.out.println("actualtarget : "+jsonObj.get("message").toString().substring(jsonObj.get("message").toString().lastIndexOf(' ')).trim());
		System.out.println(step);*/
		
	}
	
	
	@Test
	public void test1(){
				
		
	}
	
	

}
