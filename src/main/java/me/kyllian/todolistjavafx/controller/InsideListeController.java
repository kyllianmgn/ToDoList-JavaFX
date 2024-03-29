package me.kyllian.todolistjavafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import me.kyllian.todolistjavafx.StartApplication;
import me.kyllian.todolistjavafx.modele.BDD;
import me.kyllian.todolistjavafx.modele.List;
import me.kyllian.todolistjavafx.modele.Task;
import me.kyllian.todolistjavafx.modele.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InsideListeController implements Initializable {
    private final User currentUser;
    private final List checkedList;
    private Task selectedTask;

    @FXML
    private Label connected;

    @FXML
    private Button createList;

    @FXML
    private Button deleteTask;

    @FXML
    private Button modifyTask;

    @FXML
    private Button inviteUser;

    @FXML
    private Button retour;

    public InsideListeController(User currentUser, List checkedList){
        this.currentUser = currentUser;
        this.checkedList = checkedList;
    }

    @FXML
    private TableView<Task> tableTask;

    @FXML
    private TableColumn<Task, String> taskDesciption;

    @FXML
    private TableColumn<Task, String> taskDifficulty;

    @FXML
    private TableColumn<Task, String> taskEtat;

    @FXML
    private TableColumn<Task, String> taskName;

    @FXML
    private TableColumn<Task, String> taskType;

    @FXML
    private TableColumn<Task, String> dateButoir;

    @FXML
    private TableColumn<Task, String> dateDebut;

    @FXML
    private TableColumn<Task, String> dateFin;

    @FXML
    private Label titreListe;

    @FXML
    void onCreate(ActionEvent event) {
        StartApplication.changeScene("createTask",new CreateTaskController(checkedList,currentUser));
    }

    @FXML
    void onDelete(ActionEvent event) throws SQLException {
        selectedTask.delete(new BDD());
        tableTask.getItems().clear();
        System.out.println(selectedTask.getRef_liste());
        System.out.println(selectedTask.specificRead(new BDD(),currentUser).size());
        if (selectedTask.specificRead(new BDD(),currentUser).size()<=0){
            StartApplication.changeScene("liste", new ListeController(currentUser));
        }else{
            tableTask.getItems().addAll(selectedTask.specificRead(new BDD(),currentUser));
            modifyTask.setDisable(true);
            deleteTask.setDisable(true);
            inviteUser.setDisable(true);
        }
    }

    @FXML
    void onModify(ActionEvent event) {

    }

    @FXML
    void onReturn(ActionEvent event) {
        StartApplication.changeScene("liste", new ListeController(currentUser));
    }

    @FXML
    void onInvite(ActionEvent event){
        StartApplication.changeScene("userList", new UserListController(currentUser, selectedTask));
    }

    @FXML
    void onRowClick(MouseEvent event){
        selectedTask =tableTask.getSelectionModel().getSelectedItem();
        System.out.println(selectedTask.getIdTache());
        System.out.println(selectedTask.getRef_liste());
        System.out.println(selectedTask.toString());
        if (selectedTask != null){
            modifyTask.setDisable(false);
            deleteTask.setDisable(false);
            inviteUser.setDisable(false);
        }else{
            modifyTask.setDisable(true);
            deleteTask.setDisable(true);
            inviteUser.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task task = new Task(checkedList.getIdListe());
        titreListe.setText(checkedList.getTitre());
        connected.setText("Connecté en tant que "+currentUser.toString());
        taskDesciption.setCellValueFactory(new PropertyValueFactory<Task, String>("description"));
        taskDifficulty.setCellValueFactory(new PropertyValueFactory<Task, String>("difficulte"));
        taskEtat.setCellValueFactory(new PropertyValueFactory<Task, String>("etat"));
        taskName.setCellValueFactory(new PropertyValueFactory<Task, String>("libelle"));
        taskType.setCellValueFactory(new PropertyValueFactory<Task, String>("type"));
        dateButoir.setCellValueFactory(new PropertyValueFactory<Task, String>("dateButoir"));
        dateDebut.setCellValueFactory(new PropertyValueFactory<Task, String>("dateDebut"));
        dateFin.setCellValueFactory(new PropertyValueFactory<Task, String>("dateFin"));
        try {
            tableTask.getItems().addAll(task.specificRead(new BDD(),currentUser));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        modifyTask.setDisable(true);
        deleteTask.setDisable(true);
        inviteUser.setDisable(true);
    }
}
