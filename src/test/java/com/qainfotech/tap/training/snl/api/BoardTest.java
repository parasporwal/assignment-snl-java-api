package com.qainfotech.tap.training.snl.api;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.NoSuchFileException;
import java.util.Random;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.testng.Assert;
import org.testng.annotations.Test;



public class BoardTest {
	
	
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
	
	
	/**
	 * test whether it is valid turn for dice rolling to a player
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws PlayerExistsException
	 * @throws GameInProgressException
	 * @throws MaxPlayersReachedExeption
	 * @throws InvalidTurnException
	 */
	@Test(expectedExceptions=InvalidTurnException.class)
	public void testValidTurn() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException{
		JSONArray players = null;
		Board testBoard=new Board();
		for(int playerNO=0;playerNO<4;playerNO++){
			players=testBoard.registerPlayer("player"+playerNO);
		}	
		
			JSONObject player1=(JSONObject) players.get(0);
			JSONObject player3=(JSONObject)players.get(2);
			
			UUID uuidOfPlayer1=UUID.fromString(player1.get("uuid").toString());
			UUID uuidOfPlayer3=UUID.fromString(player3.get("uuid").toString());
			
			player1=testBoard.rollDice(uuidOfPlayer1);
			player3=testBoard.rollDice(uuidOfPlayer3);
		/*}
		catch(InvalidTurnException exp){
			JSONObject player2=(JSONObject)players.get(1);
			UUID uuidOfPlayer2=UUID.fromString(player2.get("uuid").toString());
			Assert.assertEquals(exp, new InvalidTurnException(uuidOfPlayer2));
		}*/
		//System.out.println(players);
		
		
		
	}
	/**
	 * validating ladder must have target bigger than current position
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@Test
	public void testLadderOrientation() throws FileNotFoundException, UnsupportedEncodingException, IOException{
		//Board testBoard=new Board();
		UUID uuid=UUID.randomUUID();
		WrongBoardStructure.init(uuid);
		Board invalidBoard=new Board(uuid);
		JSONObject data=invalidBoard.getData();
		JSONArray steps=data.getJSONArray("steps");
	 	    for(Object object : steps){
	    	JSONObject step=(JSONObject)object;
	    	if(Integer.parseInt(step.get("type").toString())==2){
	    		System.out.println(step);
	    		int number=Integer.parseInt(step.get("number").toString());
	    		int target=Integer.parseInt(step.get("target").toString());
	    		
	    		int tenth_digit=number/10;
	    		System.out.println("number : "+number +" tenth_digit : "+tenth_digit);
	    		switch(tenth_digit){
	    		 
	    		case 0: 
	    			   Assert.assertTrue(target>10);
	    			   break;
	    		case 1: 
	    			   Assert.assertTrue(target>20);
	    			   break;
	    		case 2: 
	    			   Assert.assertTrue(target>30);
	    			   break;
	    		case 4: 
	    			   Assert.assertTrue(target>40);
	    			   break;
	    		case 5: 
	    			   Assert.assertTrue(target>50);
	    			   break;
	    		case 6: 
	    			   Assert.assertTrue(target>60);
	    			   break;
	    		case 7: 
	    			   Assert.assertTrue(target>70);
	    			   break;
	    		case 8: 
	    			   Assert.assertTrue(target>80);
	    			   break;
	    		case 9:
	    			   System.out.println("problem");
	    			   break;
	    		}
	    		
	    	}
	    }
	}
	
	/**
	 * test whether player moved to the target position if bitten by snake
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws PlayerExistsException
	 * @throws GameInProgressException
	 * @throws MaxPlayersReachedExeption
	 * @throws JSONException
	 * @throws InvalidTurnException
	 */
	@Test
	public void testSnakeBite() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, JSONException, InvalidTurnException{
		/*Board testBoard=Mockito.mock(Board.class);
		Mockito.when((new Random()).nextInt(6)+1).then();*/
		
		
		
		UUID uuid=UUID.randomUUID();
		BoardService2.init(uuid);
		Board customizeBoard=new Board(uuid);
		JSONArray players=customizeBoard.registerPlayer("player");
		JSONObject data=customizeBoard.getData();  
		JSONArray steps=(JSONArray) data.get("steps");
		players=customizeBoard.getPlayersList();
		JSONObject player=(JSONObject) players.get(0);
		player.put("position", 22);
		//System.out.println("players : "+players);
		JSONObject playerBitten=customizeBoard.rollDice(UUID.fromString(player.get("uuid").toString()));  
		 int dice=Integer.parseInt(playerBitten.get("dice").toString());

		     int playerPosition=Integer.parseInt(player.get("position").toString());
		    JSONObject step=(JSONObject) steps.get(playerPosition);
		    int expectedTarget=Integer.parseInt(step.get("target").toString());
		    int actualTarget=Integer.parseInt(playerBitten.get("message").toString().substring(playerBitten.get("message").toString().lastIndexOf(' ')).trim());
		    Assert.assertEquals(expectedTarget,actualTarget);
		
		/*int playerPosition=Integer.parseInt(player.get("position").toString());
	    JSONObject step=(JSONObject) steps.get(dice+playerPosition);
	    int expectedTarget=Integer.parseInt(step.get("target").toString());
	    int actualTarget=Integer.parseInt(playerBitten.get("message").toString().substring(playerBitten.get("message").toString().lastIndexOf(' ')).trim());
	    System.out.println("player : "+player +"dice : "+dice+ "STEP : "+step+ "bitten : "+playerBitten +" exptarget : "+expectedTarget +" actualTarget : "+actualTarget);*/
		   
		   // System.out.println("player : "+player +"dice : "+dice+ "STEP : "+step+ "bitten : "+playerBitten +" exptarget : "+expectedTarget +" actualTarget : "+actualTarget);
		
		
	}
	
	/**
	 * test board whether it is initialized or not
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@Test
	public void testBoardInitialized() throws FileNotFoundException, UnsupportedEncodingException, IOException{
		
		Board board=new Board();
		Assert.assertNotEquals(board.getData(), (null));
		
	}
	
	/**
	 * whether snake exists in the beginning.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@Test
	public void testSnakeAtBegining() throws FileNotFoundException, UnsupportedEncodingException, IOException{
		Board board=new Board();
		JSONArray steps=(JSONArray) board.getData().get("steps");
		JSONObject firstStep=(JSONObject) steps.get(0);
		
		Assert.assertNotEquals(Integer.parseInt(firstStep.get("type").toString()), 1);
	}
	/**
	 * validating last step should not have a snake or a ladder.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@Test
	public void testLadderOrSnakeAtFinish() throws FileNotFoundException, UnsupportedEncodingException, IOException{
		Board board=new Board();
		JSONArray steps=(JSONArray) board.getData().get("steps");
		JSONObject lastStep=(JSONObject) steps.get(100);
		int type=Integer.parseInt(lastStep.get("type").toString());
		
		Assert.assertTrue(type!=1 && type!=2);
		
	}
	
	
	public void testDiceRolling() throws FileNotFoundException, UnsupportedEncodingException, IOException{
	
		
		
	}
	
	/**
	 * validating FileNotFoundException
	 * @throws IOException
	 */
	@Test(expectedExceptions=NoSuchFileException.class)
	public void testFileNotFoundException() throws IOException{
		UUID uuid=UUID.randomUUID();
		Board testBoard=new Board(uuid);
		/*try {
			Board testBoard=new Board(uuid);
		} catch (IOException e) {
			Assert.assertEquals(e.getMessage(), (new NoSuchFileException(uuid.toString())).getMessage());
		}*/
	}

}
