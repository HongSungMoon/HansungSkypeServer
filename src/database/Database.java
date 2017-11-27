package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {
    
    private static Connection con; //���α׷����� �������� ����Ҽ��ְ� static���� ����
    
    private Database(){ //�ܺο��� �������� ���ϰ� ������ private���� ����       
    }
    
    public static Connection getConnection(){
        
        
        if(con!=null){ //con�� null�� �ƴϸ� con�� �����ϰ��ִ� connection�ν��Ͻ��� ����
            return con;
        }
        
        try {
                   
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hansungskype", "root", "1234");
            
            System.out.println("���� ����");
            
        } catch (ClassNotFoundException e) {
            System.out.println("����̹��� ã���������ϴ� : "+e);
        }catch(SQLException e){
            System.out.println(("�Ϲ� ����:"+e));
        }
        
        return con;
    }//getConnection();
    
    
}

