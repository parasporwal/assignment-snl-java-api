package com.qainfotech.tap.training.snl.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

public class WrongBoardStructure {

	public static void init(UUID uuid) throws FileNotFoundException, UnsupportedEncodingException{
        JSONArray steps = new JSONArray();
        for(int position = 0; position <=100; position++){
            steps.put(position, getStep(position, 0, position));
        }
       // snakes
      /*  steps.put(28, getStep(28, 1, 15));
        steps.put(27, getStep(27, 1, 3));
        steps.put(26, getStep(26, 1, 6));
        steps.put(25, getStep(25, 1, 13));
        steps.put(24, getStep(24, 1, 12));
        steps.put(23, getStep(23, 1, 7));*/
     
        steps.put(51, getStep(51, 2, 13));
        steps.put(2, getStep(2, 2, 54));
        steps.put(3, getStep(3, 2, 85));
        steps.put(4, getStep(4, 2, 67));
        steps.put(5, getStep(5, 2, 90));
        steps.put(6, getStep(6, 2, 97));
        
        JSONObject data = new JSONObject();
        data.put("players", new JSONArray());
        data.put("turn", 0);
        data.put("steps", steps);
        
        PrintWriter writer = new PrintWriter(uuid.toString() + ".board", "UTF-8");
        writer.println(data.toString(2));
        writer.close();
    }
	
	 private static JSONObject getStep(Integer number, Integer type, Integer target){
	        return new JSONObject("{\"number\":"+number+",\"type\":"+type+", \"target\":"+target+"}");
	    }
	 
	 
	  public static JSONObject data(UUID uuid) throws IOException{
	        return new JSONObject(new String(
	                Files.readAllBytes(Paths.get(uuid.toString() + ".board"))));
	    }
}
