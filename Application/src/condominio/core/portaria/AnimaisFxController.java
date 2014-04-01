/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.core.portaria;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TextField;
import condominio.server.modelo.ANIMAIS;

/**
 *
 * @author Marlon Harnisch
 */
public class AnimaisFxController implements Initializable{
    
    
    @FXML
    TextField nomeAnimal;
    @FXML
    TextField tipo;
    @FXML
    ComboBox<String> porte;
    @FXML
    TextField CorAnimal;
    @FXML
    Button concluirAnimal;
    @FXML
    Button cancelarAnimal;
    private PortariaFxController portaria;
	private Long editingId;
	private static Long id = 1L;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
    }  
    
    
     public void concluirAnimal(ActionEvent event){
    	 if(!verificaCampos()){
    		 Dialogs.showErrorDialog(portaria.getStage(), "Preencha todos os Campos!");    		 
    		 return;
    	 }
         ANIMAIS anm = new ANIMAIS();
         anm.setCor(CorAnimal.getText());
         anm.setNome(nomeAnimal.getText());
         anm.setPorte(porte.getSelectionModel().getSelectedItem().toString());
         anm.setTipo(tipo.getText());
         if(editingId==null){
        	 anm.setId(id++);        	 
         }else{
        	 anm.setId(editingId);
         }       
         portaria.concluirAnimal(anm);
         editingId = null;
         hide(event);
     }
     private boolean verificaCampos() {
		if(CorAnimal.getText().isEmpty()){
			return false;
		}
		if(porte.getSelectionModel().getSelectedIndex()<0){
			return false;
		}
		if(tipo.getText().isEmpty()){
			return false;
		}
		if(nomeAnimal.getText().isEmpty()){
			return false;
		}
		return true;
	}


	public void cancelarAnimal(ActionEvent event){
          hide(event);
     }

    private void hide(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    public void setApp(PortariaFxController portaria) {
        this.portaria = portaria;      
    }

	public void editar(PortariaFxController portaria, ANIMAIS anm) {
		this.portaria = portaria;
		CorAnimal.setText(anm.getCor());
		porte.getSelectionModel().select(getIndex(anm.getPorte()));
 		nomeAnimal.setText(anm.getNome());
 		tipo.setText(anm.getTipo());
 		editingId = anm.getId();
	}

	private int getIndex(String tipo) {
	    	if(tipo==null || tipo.isEmpty()){
	    		return 0;
	    	}
			if(tipo.equals("Pequeno")){
				return 0;
			}
			if(tipo.equals("Medio")){
				return 1;
			}
			return 2;
	}
    
}
