package Java.repositories;
/**
 * класс для получения списка пользователей
 * при вызове данного класса он формирует список уникальных значений имен пользователей
 * доступ к которому можно получить через ConnectionDB.getUserSet()
 * проверить наличие в списке данного имени пользователя можно через ConnectionDB.checkUser(String user)
* */
import Java.Connection.ConnectionDB;
import Java.Settings;

import java.util.HashSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class UserRepository {
    private static Set<String> userSet = new HashSet<String>();
    static{
        Connection connection = ConnectionDB.getConnection();
        try (Statement statement = connection.createStatement()) {
            String selectUser = "SELECT User FROM cratu.user";
            ResultSet resultSetUser = statement.executeQuery(selectUser);
            while ( resultSetUser.next()) {
                //создаем обьект -клиент
                userSet.add(resultSetUser.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Settings.writeError(e);
        }
    }
    private UserRepository(){}

    public static Set<String> getUserSet(){
        return userSet;
    }
    public static boolean checkUser(String user){
        return userSet.contains(user);
    }

}
