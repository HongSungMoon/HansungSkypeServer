package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {
    
    private static Connection con; //프로그램에서 공동으로 사용할수있게 static으로 선언
    
    private Database(){ //외부에서 생성하지 못하게 생성자 private으로 지정       
    }
    
    public static Connection getConnection(){
        
        
        if(con!=null){ //con이 null이 아니면 con이 참조하고있는 connection인스턴스를 리턴
            return con;
        }
        
        try {
                   
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hansungskype", "root", "1234");
            
            System.out.println("연결 성공");
            
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버를 찾을수없습니다 : "+e);
        }catch(SQLException e){
            System.out.println(("일반 예외:"+e));
        }
        
        return con;
    }//getConnection();
    
    
}

