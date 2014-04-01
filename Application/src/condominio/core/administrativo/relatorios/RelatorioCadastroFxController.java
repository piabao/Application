/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package condominio.core.administrativo.relatorios;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.view.JRViewer;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import condominio.Condominio;
import condominio.core.portaria.PortariaCrud;
import condominio.server.modelo.TIPO_MORADOR;

/**
 *
 * @author Marlon Harnisch
 */
public class RelatorioCadastroFxController implements Initializable{
	
	@FXML
    Button teste;
	@FXML
	AnchorPane relat;
    
    private PortariaCrud pCrud = new PortariaCrud();
	private Condominio main;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
    	
    }
     
    public void abrirRelatorio(ActionEvent evt){
    	try {
    	String arquivo = "condominio/core/administrativo/relatorios/Blank_A4.jasper"; //arquivo de relatorio  
        java.io.InputStream file = getClass().getClassLoader().getResourceAsStream(arquivo); //carrego o arquivo  
        List<TIPO_MORADOR> allTipoMorador = pCrud.getAllTipoMorador();
        JRBeanArrayDataSource dataSource = new JRBeanArrayDataSource(allTipoMorador.toArray(new TIPO_MORADOR[allTipoMorador.size()]));//aqui eu crio um datasource usando a propria jtable  
        Map parametros = new HashMap(); //apenas crio um map, mas nem passo parametro nem nada, os parametros sao as colunas da jtable  
        JasperPrint printer = JasperFillManager.fillReport(file, parametros, dataSource);
        
        JRViewer view = new JRViewer(printer);
        
        view.setVisible(true);
        
        SwingNode panel = new SwingNode();
        panel.prefHeight(500);
        panel.prefWidth(500);
        panel.setContent(view);
        
        relat.getChildren().add(panel);
        
    	} catch (JRException e) {
    		e.printStackTrace();
    	} 
    }
    
	public void setApp(Condominio main) {
        this.main = main;       
    }    
}
