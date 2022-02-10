package db;

import java.sql.*;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.Member;

/**
 * @deprecated
 */
public class DataBase {
    private static Map<String, Member> members = Maps.newHashMap();

    public static void addMember(Member user) {
        members.put(user.getUserId(), user);
    }

    public static Member findMemberById(String userId) {
        return members.get(userId);
    }

    public static Collection<Member> findAll() {
        return members.values();
    }

    /**
     * init database
     */
    static {
        addMember(new Member.Builder().userId("jm.hong").password("jm.hong").name("Hongjeongmin").email("jm.hong@kakaocorp.com").build());
        addMember(new Member.Builder().userId("sangin").password("mark.lim").name("limSangZin").email("mark.lim@kakaocorp.com").build());
    }

    public static void main(String[] args) {
        try{
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://10.202.179.65:3306",
                    "root",
                    "1234"
            );

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "show databases"
            );

            while(rs.next()) {
                System.out.println(rs.getString(1));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
