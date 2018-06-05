/**
 * Sample Skeleton for 'SerieA.fxml' Controller Class
 */

package it.polito.tdp.seriea;

import java.lang.reflect.AnnotatedArrayType;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.spi.DirStateFactory.Result;

import it.polito.tdp.seriea.model.AnnataOro;
import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Punteggio;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class SerieAController {
	
	private Model model;
	
	private Team t;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxSquadra"
    private ChoiceBox<Team> boxSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="btnSelezionaSquadra"
    private Button btnSelezionaSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="btnTrovaAnnataOro"
    private Button btnTrovaAnnataOro; // Value injected by FXMLLoader

    @FXML // fx:id="btnTrovaCamminoVirtuoso"
    private Button btnTrovaCamminoVirtuoso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader



    @FXML
    void doSelezionaSquadra(ActionEvent event) {
    	txtResult.clear();
    	t = boxSquadra.getValue();
    	try {
    		if(t!=null) {
    			List<Punteggio> list = model.getPunteggio(t);
    			for(Punteggio p : list) {
    				txtResult.appendText(String.format("%-10s  %10d\n", p.getSeason().getDescription(), p.getPunteggio()));
    			}
    			
    		}else {
    			txtResult.appendText("Errore: nessuna squadra selezionata!\n");
    			return;
    		}
    		
    		
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}

    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {
    	txtResult.clear();
    	model.creaGrafo();
    	AnnataOro annataOro;
    	try {
    		if(t!=null) {
    	annataOro = model.getAnnataOro(t);
    	txtResult.setText("Annata d'oro è : "+annataOro.getSeason().toString()+"  differenza : "+annataOro.getDifferenza()+"\n");
    			
    		}else {
    			txtResult.appendText("Errore, seleziona squadra\n");
    		}
   			
    	}catch(Exception e) {
    		e.printStackTrace();
    		return;
    	}
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {
     	txtResult.clear();
    	model.creaGrafo();
    	List<Punteggio> p = new ArrayList<>();
    	Team team = boxSquadra.getValue();
    	try {
    	if(t!=null) {
    	model.camminoVirtuoso();
    	List<Punteggio> result =new ArrayList<>(model.getPunteggioOttimo(t));
    	for(Punteggio punteggio : result) {
    		txtResult.appendText(punteggio.toString()+"\n");
    	}
    	
    			
    		}else {
    			txtResult.appendText("Errore, seleziona squadra\n");
    		}
   			
    	}catch(Exception e) {
    		e.printStackTrace();
    		return;
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model=model;
		boxSquadra.getItems().addAll(model.getAllTeams());
		
	}
}
