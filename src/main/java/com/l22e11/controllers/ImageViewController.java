package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageViewController implements Initializable {
	
	@FXML
    private ImageView image;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
	}

	public void setImage(Image img) {
		image.setFitWidth(img.getWidth());
		image.setFitHeight(img.getHeight());
		image.setImage(img);
	}
}
