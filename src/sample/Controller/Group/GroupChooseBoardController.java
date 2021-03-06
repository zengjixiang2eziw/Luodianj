package sample.Controller.Group;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Controller.Login.LoginController;
import sample.Controller.Task.TaskController;
import sample.Entity.ComputeTask;
import sample.Entity.GroupNode;
import sample.SocketConnect.SocketHandler;
import sample.StartProcess;
import sample.Utils.HintFrame;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GroupChooseBoardController implements Initializable {

    @FXML
    private JFXButton choose;

    @FXML
    private JFXButton back;

    @FXML
    private JFXMasonryPane masonry_pane;

    private ArrayList<GroupNode> myGroups;

    private Pane current_pane;

    private GroupNode current_group;

    private int data_index;

    private ComputeTask aimed_task;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data_index = TaskController.my_task_list_copy.getSelectionModel().getSelectedIndex();
        aimed_task = TaskController.myTasks.get(data_index);
        myGroups = SocketHandler.queryGroupsByUserID(LoginController.current_user_id);
        for (int i = myGroups.size() - 1; i >= 0; i--) {
            if (myGroups.get(i).getGroup_id().equals(aimed_task.getGroup_id())) {
                myGroups.remove(i);
                break;
            }
        }
        if (myGroups != null) {
            for (int i = 0; i < myGroups.size(); i++) {
                Pane temp = createNewGroupInfoPane(myGroups.get(i));
                masonry_pane.getChildren().add(temp);
            }
        }
    }

    @FXML
    void backToPreview(MouseEvent event) {
        Stage dataChooseBoardStage = StartProcess.hashMap.remove("group_choose_board");
        Stage taskStage = StartProcess.hashMap.get("tasks");
        dataChooseBoardStage.close();
        taskStage.show();
    }

    @FXML
    void chooseGroup(MouseEvent event) {
        if (current_group == null) HintFrame.showFailFrame("Please choose one group set!");
        else {
            /**??????????????????**/
            if (data_index >= 0) {
                if (SocketHandler.alterTaskBindGroup(aimed_task.getTask_id(), current_group.getGroup_id())) {
                    /**????????????**/
                    Stage groups = StartProcess.hashMap.get("tasks");
                    Stage data_choose_board = StartProcess.hashMap.remove("group_choose_board");
                    groups.show();
                    aimed_task.setGroup_id(current_group.getGroup_id());
                    data_choose_board.close();
                } else {
                    /**???????????????,?????????????????????**/
                    HintFrame.showFailFrame("Wrong type match or register duplicately!");
                }
            } else HintFrame.showFailFrame("Unknown Error!");
        }
    }

    private Pane createNewGroupInfoPane(GroupNode temp) {
        Pane temp_info = new Pane();
        temp_info.setPrefSize(150, 150);
        temp_info.setStyle("-fx-background-color: #104E8B;-fx-background-radius: 1em");
        temp_info.setEffect(new DropShadow(2, 2, 2, Color.BLACK));

        JFXButton temp_btn = new JFXButton("");
        temp_btn.setPrefSize(150, 150);
        temp_btn.setStyle("-fx-background-radius: 1em;");
        /**
         * ??????ripper????????????
         **/
        List<Stop> stop_list = new ArrayList<Stop>();
        stop_list.add(new Stop(1.0, Color.valueOf("#0ea5d6")));
        stop_list.add(new Stop(0, Color.BLACK));
        temp_btn.setRipplerFill(new RadialGradient(0, 0, 0.5112044817927172,
                0.4568345323741007, 0.5, true, CycleMethod.NO_CYCLE, stop_list));
        temp_btn.onMouseClickedProperty().setValue((e)->{
            if (current_pane != null)
                current_pane.setStyle("-fx-background-color: #104E8B;-fx-background-radius: 1em;");
            current_pane = (Pane) temp_btn.getParent();
            current_pane.setStyle("-fx-background-color: #0ea5d6;-fx-background-radius: 1em");
            current_group = temp;
        });

        temp_btn.setLayoutX(0);
        temp_btn.setLayoutY(0);

        Label data_name = new Label("group name: " + temp.getGroup_name());
        data_name.setFont(new Font("Chalkboard SE Light", 13.0));
        data_name.setTextFill(Paint.valueOf("#ffffff"));
        data_name.setPrefSize(150, 20);
        data_name.setLayoutX(30);
        data_name.setLayoutY(30);
        data_name.setAlignment(Pos.CENTER_LEFT);

        ImageView data_name_image = new ImageView("/sample/resources/MDicon/score.png");
        data_name_image.setFitHeight(25);
        data_name_image.setFitWidth(25);
        data_name_image.setLayoutX(5);
        data_name_image.setLayoutY(27);

        Label data_type = new Label("group type: " + temp.getType());
        data_type.setFont(new Font("Chalkboard SE Light", 13.0));
        data_type.setTextFill(Paint.valueOf("#ffffff"));
        data_type.setPrefSize(150, 20);
        data_type.setLayoutX(30);
        data_type.setLayoutY(60);
        data_type.setAlignment(Pos.CENTER_LEFT);

        ImageView data_type_image = new ImageView("/sample/resources/MDicon/types.png");
        data_type_image.setFitHeight(25);
        data_type_image.setFitWidth(25);
        data_type_image.setLayoutX(5);
        data_type_image.setLayoutY(57);

        Label row_num = new Label("member num: " + temp.getMember_num());
        row_num.setFont(new Font("Chalkboard SE Light", 13.0));
        row_num.setTextFill(Paint.valueOf("#ffffff"));
        row_num.setPrefSize(150, 20);
        row_num.setLayoutX(30);
        row_num.setLayoutY(90);
        row_num.setAlignment(Pos.CENTER_LEFT);

        ImageView row_num_image = new ImageView("/sample/resources/MDicon/icon_1.png");
        row_num_image.setFitHeight(25);
        row_num_image.setFitWidth(25);
        row_num_image.setLayoutX(5);
        row_num_image.setLayoutY(87);

        Label attr_num = new Label("creator: " + temp.getOwner().getUser_name());
        attr_num.setFont(new Font("Chalkboard SE Light", 13.0));
        attr_num.setTextFill(Paint.valueOf("#ffffff"));
        attr_num.setPrefSize(150, 20);
        attr_num.setLayoutX(30);
        attr_num.setLayoutY(120);
        attr_num.setAlignment(Pos.CENTER_LEFT);

        ImageView attr_num_image = new ImageView("/sample/resources/MDicon/attr.png");
        attr_num_image.setFitHeight(25);
        attr_num_image.setFitWidth(25);
        attr_num_image.setLayoutX(5);
        attr_num_image.setLayoutY(117);

        ImageView mark = new ImageView("/sample/resources/MDicon/bookmark.png");
        mark.setFitHeight(25);
        mark.setFitWidth(25);
        mark.setLayoutX(120);
        mark.setLayoutY(5);

        temp_info.getChildren().add(data_name);
        temp_info.getChildren().add(data_name_image);
        temp_info.getChildren().add(data_type);
        temp_info.getChildren().add(data_type_image);
        temp_info.getChildren().add(row_num);
        temp_info.getChildren().add(row_num_image);
        temp_info.getChildren().add(attr_num);
        temp_info.getChildren().add(attr_num_image);
        temp_info.getChildren().add(mark);
        temp_info.getChildren().add(temp_btn);
        return temp_info;
    }
}
