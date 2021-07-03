package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Datebase.SQLHandler;
import sample.Entity.UserNode;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

public class StartProcess extends Application {
    public static String url = "jdbc:mysql://localhost:3306/Patronus" ;
    public static String username = "root" ;
    public static String password = "wyz123348377" ;
    /**目前已有的页面的key：
     * main_page
     * login
     * data_load
     * data_setting
     * groups
     * group_setting
     * group_information
     * coding
     * tasks
     * confirm**/
    public static HashMap<String, Stage> hashMap = new HashMap<String, Stage>();
    public static final int SCENE_WIDTH =1006;
    public static final int SCENE_HEIGHT = 770;
    @Override
    public void start(Stage primaryStage) throws Exception
    {
	    FXMLLoader myEditPageLoader = new FXMLLoader (getClass ().getResource ("./FXML/login.fxml"));
	    Parent root = myEditPageLoader.load();
	    /**加载数据库**/

        try{
            //加载MySql的驱动类
            Class.forName("com.mysql.jdbc.Driver") ;
            SQLHandler.con = DriverManager.getConnection(url , username , password );
            SQLHandler.query = SQLHandler.con.createStatement();

        }catch(ClassNotFoundException e){
            System.out.println("can't find driver，load driver failed!");
            e.printStackTrace() ;
        }catch (SQLException e){
            System.out.println("can't connect to local database!");
            e.printStackTrace() ;
        }

        /**启动登录页面**/
	    primaryStage.setTitle ("Patronus");
	    Scene scene = new Scene(root);
	    primaryStage.initStyle(StageStyle.UNDECORATED);
	    //scene.getStylesheets().add(JavaKeywordsAsync.class.getResource("java-keywords.css").toExternalForm());
	    primaryStage.setScene (scene);
        hashMap.put("login", primaryStage);
	    primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}