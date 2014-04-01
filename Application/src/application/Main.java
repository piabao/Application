package application;
	
import java.util.HashMap;
import java.util.Map;

import org.omg.PortableServer.ForwardRequestHelper;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main {
	
	public static void main(String[] args) {
		Map<String, String> teste = new HashMap<String, String>();
		Map<String, Boolean> map = new HashMap<>();
		
		map.put("a", true);
		map.put("b", false);
		
		for (String b : map.keySet()) {
			System.out.println(b + map.get(b));
		}
	}
}
