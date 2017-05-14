package application;

import java.io.*;
import java.nio.file.*;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;

public class Menu extends Application implements Battleship {
	public static String playername;
	private GameMenu gameMenu;
	MediaPlayer mediaPlayer;
	public static boolean isMute = false;
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		Pane root = new Pane();
		primaryStage.setHeight(600);
		primaryStage.setWidth(800);
		
		Text version = new Text("Version: " + VERSION);
		HBox bottom = new HBox();
		bottom.getChildren().addAll(version);
		bottom.setTranslateX(720);
		bottom.setTranslateY(550);

		InputStream is = Files.newInputStream(Paths.get("img/bg.jpg"));
		Image img = new Image(is);
		is.close();
		
		String bip = "sound/menu.mp3";
		Media hit = new Media(new File(bip).toURI().toString());
		mediaPlayer = new MediaPlayer(hit);
		if (!isMute) mediaPlayer.play();
		mediaPlayer.setVolume(0.5);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		
		ImageView imgView = new ImageView(img);
		imgView.setFitWidth(800);
		imgView.setFitHeight(600);

		gameMenu = new GameMenu();
		gameMenu.setVisible(true);

		root.getChildren().addAll(imgView, gameMenu, bottom);

		Scene scene = new Scene(root);
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				if (!gameMenu.isVisible()) {
					FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);
					ft.setFromValue(0);
					ft.setToValue(1);

					gameMenu.setVisible(true);
					ft.play();
				} else {
					FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);
					ft.setFromValue(1);
					ft.setToValue(0);
					ft.setOnFinished(evt -> gameMenu.setVisible(false));
					ft.play();
				}
			}
		});

		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Battleship");
		primaryStage.setAlwaysOnTop(true);
		
		// Icon
		InputStream icon = Files.newInputStream(Paths.get("img/icon.jpg"));
		Image iconimg = new Image(icon);
		icon.close();
		primaryStage.getIcons().add(iconimg);
		
		
		primaryStage.show();
	}

	private class GameMenu extends Parent {
		public GameMenu() {
			VBox menu0 = new VBox(10);
			VBox menu1 = new VBox(10);
			VBox menu2 = new VBox(10);
			VBox menu3 = new VBox(10);

			menu0.setTranslateX(100);
			menu0.setTranslateY(230);

			menu1.setTranslateX(100);
			menu1.setTranslateY(230);
			
			menu2.setTranslateX(100);
			menu2.setTranslateY(230);
			
			menu3.setTranslateX(100);
			menu3.setTranslateY(230);

			final int offset = 400;

			menu1.setTranslateX(offset);
			menu2.setTranslateX(offset*2);
			menu3.setTranslateX(offset*3);

			MenuButton btnStart = new MenuButton("START");
			btnStart.setOnMouseClicked(event -> {
				FadeTransition ft = new FadeTransition(Duration.seconds(0.5), this);
				ft.setFromValue(1);
				ft.setToValue(0);
				ft.setOnFinished(sukien -> {
					StartGame(btnStart);
				});
				ft.play();
			});

			MenuButton btnOptions = new MenuButton("OPTIONS");
			btnOptions.setOnMouseClicked(event -> {
				getChildren().add(menu1);

				TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu0);
				tt.setToX(menu0.getTranslateX() - offset);

				TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu1);
				tt1.setToX(menu0.getTranslateX());

				tt.play();
				tt1.play();

				tt.setOnFinished(evt -> {
					getChildren().remove(menu0);
				});
			});

			MenuButton btnExit = new MenuButton("EXIT");
			btnExit.setOnMouseClicked(event -> {
				System.exit(0);
			});

			MenuButton btnBack = new MenuButton("BACK");
			btnBack.setOnMouseClicked(event -> {
				getChildren().add(menu0);

				TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu1);
				tt.setToX(menu1.getTranslateX() + offset);

				TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu0);
				tt1.setToX(menu1.getTranslateX());

				tt.play();
				tt1.play();

				tt.setOnFinished(evt -> {
					getChildren().remove(menu1);
				});
			});
			
			MenuButton btnBack2 = new MenuButton("BACK");
			btnBack2.setOnMouseClicked(event -> {
				getChildren().add(menu1);

				TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu2);
				tt.setToX(menu2.getTranslateX() + offset);

				TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu1);
				tt1.setToX(menu2.getTranslateX());

				tt.play();
				tt1.play();

				tt.setOnFinished(evt -> {
					getChildren().remove(menu2);
				});
			});
			
			MenuButton btnBack3 = new MenuButton("BACK");
			btnBack3.setOnMouseClicked(event -> {
				getChildren().add(menu1);

				TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu3);
				tt.setToX(menu3.getTranslateX() + offset);

				TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu1);
				tt1.setToX(menu3.getTranslateX());

				tt.play();
				tt1.play();

				tt.setOnFinished(evt -> {
					getChildren().remove(menu3);
				});
			});
			
			MenuButton btnSound = new MenuButton("SOUND");
			btnSound.setOnMouseClicked(event -> {
				getChildren().add(menu3);
				
				TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu1);
				tt.setToX(menu1.getTranslateX() - offset);
				
				TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu3);
				tt1.setToX(menu1.getTranslateX());
				
				tt.play();
				tt1.play();
				
				tt.setOnFinished(evt -> getChildren().remove(menu1));
			});
			
			MenuButton btnVideo = new MenuButton("VIDEO");
			btnVideo.setOnMouseClicked(event -> {
				getChildren().add(menu2);

				TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu1);
				tt.setToX(menu1.getTranslateX() - offset);

				TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu2);
				tt1.setToX(menu1.getTranslateX());

				tt.play();
				tt1.play();

				tt.setOnFinished(evt -> getChildren().remove(menu1));
			});
			
			MenuButton btnSmall = new MenuButton("640 x 480");
			btnSmall.setOnMouseClicked(event -> {
				Stage stage = (Stage) btnSmall.getScene().getWindow();
				stage.setWidth(640);
				stage.setHeight(480);
				
			});
			MenuButton btnMedium = new MenuButton("800 x 600");
			btnMedium.setOnMouseClicked(event -> {
				Stage stage = (Stage) btnMedium.getScene().getWindow();
				stage.setWidth(800);
				stage.setHeight(600);
				
			});
			MenuButton btnLarge = new MenuButton("1024 x 768");
			btnLarge.setOnMouseClicked(event -> {
				Stage stage = (Stage) btnLarge.getScene().getWindow();
				stage.setWidth(1024);
				stage.setHeight(768);
				
			});

			MenuButton btnOn = new MenuButton("ON");
			btnOn.setOnMouseClicked(event -> {
				mediaPlayer.setMute(false);
				if (isMute) mediaPlayer.play();
				isMute = false;
			});
			
			MenuButton btnOff = new MenuButton("OFF");
			btnOff.setOnMouseClicked(event -> {
				mediaPlayer.setMute(true);
				isMute = true;
			});
			
			menu0.getChildren().addAll(PlayerName(), btnStart, btnOptions, btnExit);
			menu1.getChildren().addAll(btnSound, btnVideo, btnBack);
			menu2.getChildren().addAll(btnSmall, btnMedium, btnLarge, btnBack2);
			menu3.getChildren().addAll(btnOn, btnOff, btnBack3);

			Rectangle bg = new Rectangle(800, 600);
			bg.setFill(Color.GREY);
			bg.setOpacity(0.4);

			getChildren().addAll(bg, menu0);
		}
	}

	public static class MenuButton extends StackPane {
		private Text text;

		public MenuButton(String name) {
			text = new Text(name);
			text.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
			text.setFill(Color.WHITE);

			Rectangle bg = new Rectangle(250, 30);
			bg.setOpacity(0.6);
			bg.setFill(Color.BLACK);
			bg.setEffect(new GaussianBlur(3.5));

			setAlignment(Pos.CENTER_LEFT);
			setRotate(-0.5);
			getChildren().addAll(bg, text);

			setOnMouseEntered(event -> {
				bg.setTranslateX(10);
				text.setTranslateX(10);
				bg.setFill(Color.WHITE);
				text.setFill(Color.BLACK);
			});

			setOnMouseExited(event -> {
				bg.setTranslateX(0);
				text.setTranslateX(0);
				bg.setFill(Color.BLACK);
				text.setFill(Color.WHITE);
			});

			DropShadow drop = new DropShadow(50, Color.WHITE);
			drop.setInput(new Glow());

			setOnMousePressed(event -> setEffect(drop));
			setOnMouseReleased(event -> setEffect(null));
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private String formatName(String a, int limit) {
		String dots = "...";
		if (a.length() > limit) {
			// you can also use substr instead of substring
			a = a.substring(0, limit) + dots;
		}

		return a;
	}

	private void StartGame(MenuButton a) {
		Stage stage = (Stage) a.getScene().getWindow();
		stage.close();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Your class that extends Application
				try {
					new Main().start(new Stage());
					mediaPlayer.dispose();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private HBox PlayerName() {
		Label player = new Label("Player Name:");
		player.setTextFill(Color.WHITE);
		player.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		TextField name = new TextField("Player");
		
		name.setOnKeyReleased(sukien -> {
			playername = name.getText();
			playername = formatName(playername, 5);
		});
		if (playername == null) {
			playername = name.getText();
			playername = formatName(playername, 5);
		}
		HBox box = new HBox(10, player, name);
		return box;
	}
}