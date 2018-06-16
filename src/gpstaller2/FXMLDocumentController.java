/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gpstaller2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 *
 * @author obaquerog
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    TextField txtPhrase;
    @FXML
    Button btnRun;
    @FXML
    Button btnRunCsv;
    String phrase;

    String[] sujeto = {"PRP", "NNP", "NNPS"};
    String[] acciones = {"VB", "VBG", "VBN", "VBP", "VBZ", "VBD"};
    int intIndex = 0;
    int posicionSujeto = -1;
    int posicionAcciones = -1;

    InputStream tokenModelIn = null;
    InputStream posModelIn = null;
    @FXML
    public void actionRunCsv(ActionEvent event){
        String csvFile = "archivo.csv";
        BufferedReader br = null;
        String line = "";
        //Se define separador ","
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(cvsSplitBy);
                //Imprime datos.
                validarPhrase(datos[0]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @FXML
    public void actionRun(ActionEvent event) {
        validarPhrase(txtPhrase.getText());
    }
    public void validarPhrase(String sentence){
        posicionSujeto=-1;
        posicionAcciones=-1;
        try {
            // tokenize the sentence
            tokenModelIn = new FileInputStream("en-token.bin");
            TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
            Tokenizer tokenizer = new TokenizerME(tokenModel);
            String tokens[] = tokenizer.tokenize(sentence);

            // Parts-Of-Speech Tagging
            // reading parts-of-speech model to a stream 
            posModelIn = new FileInputStream("en-pos-maxent.bin");
            // loading the parts-of-speech model from stream
            POSModel posModel = new POSModel(posModelIn);
            // initializing the parts-of-speech tagger with model 
            POSTaggerME posTagger = new POSTaggerME(posModel);
            // Tagger tagging the tokens
            String tags[] = posTagger.tag(tokens);
            // Getting the probabilities of the tags given to the tokens
            double probs[] = posTagger.probs();
            for (int i = 0; i < tokens.length; i++) {
                boolean a = false;
                for (int j = 0; j < sujeto.length; j++) {

                    if (tags[i].equals(sujeto[j])) {
                        a = true;
                        if (posicionSujeto == -1) {
                            // System.out.println(tags[i]+" - "+sujeto[j]);
                            posicionSujeto = i;
                        } else if (i < posicionSujeto) {
                            //  System.out.println(tags[i]+" - "+sujeto[j]);
                            posicionSujeto = i;
                        }
                    }
                }
                if (!a) {
                    for (int j = 0; j < acciones.length; j++) {
                        //   System.out.println(tags[i]+" - "+acciones[j]);
                        if (tags[i].equals(acciones[j])) {
                            if (posicionAcciones == -1) {
                                posicionAcciones = i;
                            } else if (i < posicionAcciones) {
                                posicionAcciones = i;
                            }
                        }
                    }
                }

            }
            if (posicionSujeto == -1) {
                System.out.println("The Phrase : "+sentence+" is Passive");
            } else if (posicionSujeto < posicionAcciones) {
                System.out.println("The Phrase : "+sentence+" is Active");
            } else {
                System.out.println("The Phrase : "+sentence+" is Passive");
            }

        } catch (IOException e) {
            // Model loading failed, handle the error
            e.printStackTrace();
        } finally {
            if (tokenModelIn != null) {
                try {
                    tokenModelIn.close();
                } catch (IOException e) {
                }
            }
            if (posModelIn != null) {
                try {
                    posModelIn.close();
                } catch (IOException e) {
                }
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
