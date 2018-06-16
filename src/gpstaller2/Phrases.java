/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gpstaller2;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author obaquerog
 */
public class Phrases {
    public SimpleStringProperty frase = new SimpleStringProperty();
    public SimpleStringProperty tipo = new SimpleStringProperty();

    public String getFrase() {
        return frase.get();
    }

    public String getTipo() {
        return tipo.get();
    }
    
}
