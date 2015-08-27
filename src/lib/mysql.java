package lib;

import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import com.google.common.collect.Lists;
import lib.FaceDb;
import java.util.List;

public class mysql {
    private static final String dbClassName = "com.mysql.jdbc.Driver";

    private static final String CONNECTION =
            "jdbc:mysql://127.0.0.1/zomato";


    public static void connect(String name, BufferedImage image) throws
            ClassNotFoundException, SQLException, java.io.IOException {
        // creates a driverManager class factory
        Class.forName(dbClassName);

        // Properties for user and password.
        Properties p = new Properties();
        p.put("user", "prasad");
        p.put("password", "marne");

        // Now try to connect
        Connection c = DriverManager.getConnection(CONNECTION, p);

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArray);
        InputStream is = new ByteArrayInputStream(byteArray.toByteArray());

        String sql = "insert into images values('" + name + "',?)";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setBinaryStream(1, is, is.available());
        int n = ps.executeUpdate();

        c.close();
    }

    public static class savedData {

        public String retName;
        public BufferedImage retImage;

      public  savedData(String s, BufferedImage i) {
            this.retImage = i;
            this.retName = s;
        }

       public static List<savedData> retrieve() throws
                ClassNotFoundException, SQLException, java.io.IOException {

            Class.forName(dbClassName);

            // Properties for user and password.
            Properties p = new Properties();
            p.put("user", "prasad");
            p.put("password", "marne");

            // Now try to connect
            Connection c = DriverManager.getConnection(CONNECTION, p);

            String sql = "select name,face from images";
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            List<savedData> ls= new ArrayList<savedData>();
            while (rs.next()) {
                String name = rs.getString("name");
                Blob photoBlob = rs.getBlob("face");
                BufferedImage photo = null;
                photo = ImageIO.read(photoBlob.getBinaryStream());
                savedData x=new savedData(name,photo);
                ls.add(x);
            }

            c.close();
            return ls;
        }
    }
}
