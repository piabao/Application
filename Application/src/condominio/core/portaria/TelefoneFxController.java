/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.core.portaria;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TextField;
import condominio.server.modelo.OPERADORA;
import condominio.server.modelo.TELEFONES;

/**
 *
 * @author Marlon Harnisch
 */
public class TelefoneFxController implements Initializable{
    
    @FXML
    TextField numeroTel;
    @FXML
    ComboBox<OPERADORA> operadora;
    @FXML
    Button concluirTelefone;
    @FXML
    Button cancelarTelefone;
    
    private PortariaFxController portaria;
    private PortariaCrud pCrud = new PortariaCrud();
    private static Long id = 1L;
	private Long editingId;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	buildOperadoraList();
    }  
    
    private void buildOperadoraList() {
		ObservableList<OPERADORA> telefones = FXCollections.observableList(
				pCrud.getOperadora());	
		operadora.setItems(telefones);
	}
    
     public void concluirTelefone(ActionEvent event){
    	 if(!verificaCampos()){
    		 Dialogs.showErrorDialog(portaria.getStage(), "Preencha todos os Campos!");  		 
    		 return;
    	 }
         TELEFONES tel = new TELEFONES();
         if(editingId==null){
        	 tel.setId(id++);        	 
         }else{
        	 tel.setId(editingId);
         }
         tel.setNumero(numeroTel.getText());
         tel.setOperadora(operadora.getSelectionModel().getSelectedItem().toString());
         portaria.concluirTelefone(tel);
         editingId = null;
         hide(event);
     }
     
     public void editar(PortariaFxController portaria, TELEFONES tel) {
    	 this.portaria = portaria;
 		numeroTel.setText(tel.getNumero());
 		operadora.getSelectionModel().select(getIndex(tel.getOperadora()));
 		editingId = tel.getId();
 	}
     
     private OPERADORA getIndex(String operadora2) {
		for(OPERADORA opp : operadora.getItems()){
			if(opp.getOperadora().toLowerCase().equals(operadora2.toLowerCase())){
				return opp;
			}
		}
		return null;
	}

	private boolean verificaCampos() {
		if(numeroTel.getText().isEmpty()){
			return false;
		}
		if(operadora.getSelectionModel().getSelectedIndex()<0){
			return  false;
		}
		return true;
	}

	public void cancelarTelefone(ActionEvent event){
          hide(event);
     }

    private void hide(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    public void setApp(PortariaFxController portaria) {
        this.portaria = portaria;      
    }
    
}
