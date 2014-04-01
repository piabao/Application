/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.core.portaria;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import condominio.core.utils.Formater;
import condominio.server.modelo.MODELO;
import condominio.server.modelo.VEICULOS;

/**
 *
 * @author Marlon Harnisch
 */
public class VeiculosFxController implements Initializable{
   
    private static final String CARRO = "Carro";
	private static final String MOTO = "Moto";
	
	@FXML
    TextField placa;
    @FXML
    ComboBox<String> TipoVeiculo;
    @FXML
    TextField modeloVeiculo;
    @FXML
	ListView<MODELO> resultado;
    @FXML
    TextField corVeiculo;
    @FXML
    Button concluirVeiculo;
    @FXML
    Button cancelarVeiculo;
    
    private PortariaFxController portaria;
    private PortariaCrud pCrud = new PortariaCrud();
    private static Long id = 1L;
    private boolean canChange = false;
    private boolean navegando = false;
	private Long editingId;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
    	Formater.addMask(placa, "   -    ");
    	ObservableList<String> obj = FXCollections.observableArrayList(CARRO, MOTO);
    	TipoVeiculo.setItems(obj);
    	resultado.setVisible(false);
    	resultado.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MODELO>() {

			@Override
    	    public void changed(ObservableValue<? extends MODELO> observable, MODELO oldValue, MODELO newValue) {
    	    	if(newValue == null){
    	    		return;
    	    	}
    	    	if(navegando){
    	    		return;
    	    	}
    	    	modeloVeiculo.setText(newValue.toString());
    	    	canChange = true;
    	    	resultado.setVisible(false);
    	    }
    	});
    	modeloVeiculo.textProperty().addListener(new ChangeListener<String>() {
    	    @Override
    	    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
    	    	if(newValue.isEmpty()){
    	    		resultado.setVisible(false);
    	    		return;
    	    	}
    	    	if(canChange){
    	    		canChange = false;
    	    		return;
    	    	}
    	    	ObservableList<MODELO> resultadoList = FXCollections.observableList(pCrud.searchModelo(newValue));
    	    	if(resultadoList.isEmpty()){
    	    		resultado.setVisible(false);
    	    		return;
    	    	}
    	    	resultado.setVisible(true);
    	        resultado.setItems(resultadoList);
    	    }
    	});
    	
    	modeloVeiculo.setOnKeyPressed(new EventHandler<KeyEvent>()
    		    {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.DOWN)){
                	resultado.requestFocus();
                }
            }
        });
    	
    	resultado.setOnKeyPressed(new EventHandler<KeyEvent>()
    		    {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.DOWN)){
                	navegando = true;
                }
                if (ke.getCode().equals(KeyCode.ENTER)){
                	navegando = false;
                	modeloVeiculo.setText(resultado.getSelectionModel().getSelectedItem().toString());
                	resultado.setVisible(false);
                }
            }
        });
    } 
    
    private int getIndex(String tipo){
    	if(tipo==null || tipo.isEmpty()){
    		return 0;
    	}
		if(tipo.equals(CARRO)){
			return 0;
		}
		return 1;
	}
        
     public void concluirVeiculo(ActionEvent event){
    	 if(!verificaCampos()){
    		 Dialogs.showErrorDialog(portaria.getStage(), "Preencha todos os Campos!");   		 
    		 return;
    	 }
         VEICULOS veic = new VEICULOS();
         veic.setCor(corVeiculo.getText());
         veic.setModelo(modeloVeiculo.getText());
         veic.setTipo(TipoVeiculo.getSelectionModel().getSelectedItem());
         veic.setPlaca(placa.getText());
         if(editingId==null){
        	 veic.setId(id++);        	 
         }else{
        	 veic.setId(editingId);
         }
         portaria.concluirVeiculo(veic);       
         editingId = null;
         hide(event);
     }
     private boolean verificaCampos() {
		if(corVeiculo.getText().isEmpty()){
			return false;
		}
		if(modeloVeiculo.getText().isEmpty()){
			return false;
		}
		if(TipoVeiculo.getSelectionModel().getSelectedIndex()<0){
			return false;
		}
		if(placa.getText().isEmpty()){
			return false;
		}
		return true;
	}

	public void cancelarVeiculo(ActionEvent event){
          hide(event);
     }

    private void hide(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    public void setApp(PortariaFxController portaria) {
        this.portaria = portaria;      
    }    

	public void editar(PortariaFxController portaria, VEICULOS veic) {
		this.portaria = portaria;
		editarVeiculo(veic);		
	}

	private void editarVeiculo(VEICULOS veic) {
		placa.setText(veic.getPlaca());
		corVeiculo.setText(veic.getCor());
		placa.setText(veic.getPlaca());
		modeloVeiculo.setText(veic.getModelo());
		TipoVeiculo.getSelectionModel().select(getIndex(veic.getTipo()));
		editingId = veic.getId();
	}
    
}
