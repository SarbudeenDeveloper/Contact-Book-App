import java.io.FileWriter;
import java.sql.*;
import java.util.Scanner;

class DatabaseInit {
    Connection con;
    Scanner obj;

    DatabaseInit() {
        obj = new Scanner(System.in);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/contactbook", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void displayContactBook() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from bookdetails");
            System.out.println();
            System.out.println("Contact Book");
            System.out.println("S.No          Name                 Phone            Email");
            System.out.println("----------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "            " + rs.getString(2) + "                 "
                        + rs.getString(3) + "    " + rs.getString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void insertContact() {
        System.out.println();
        System.out.println("Add New Contact Details");

        System.out.print("Enter contact name : ");
        String name = obj.nextLine();

        System.out.print("Enter phone number : ");
        String phone = obj.nextLine();

        System.out.print("Enter email address : ");
        String email = obj.nextLine();

        if (checkContactExist(phone)) {
            System.out.println("You have already this contact");
        } else {
            try {
                PreparedStatement stmt = con
                        .prepareStatement("insert into bookdetails (name, phone, email) values (?,?,?)");
                stmt.setString(1, name);
                stmt.setString(2, phone);
                stmt.setString(3, email);

                stmt.executeUpdate();
                System.out.println("New contact added successfully");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    boolean checkContactExist(String phone) {
        try {
            PreparedStatement stmt = con.prepareStatement("select * from bookdetails where phone = ?");
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    void saveContact() {
        try {
            FileWriter writer = new FileWriter("saveContact.txt");
            writer.flush();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from bookdetails");
            writer.append("S.No          Name                 Phone            Email\n");
            writer.append("----------------------------------------------------------------------------------\n");
            while (rs.next()) {
                writer.append(rs.getInt(1) + "            " + rs.getString(2) + "                 "
                        + rs.getString(3) + "    " + rs.getString(4) +"\n");
            }


            writer.close();
            System.out.println("Contact Saved Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void exit() {
        try {
            con.close();
            obj.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ContactBook {
    public static void main(String[] args) {
        int choice;
        Scanner obj = new Scanner(System.in);
        DatabaseInit db = new DatabaseInit();
        System.out.println("Hey friend, I am your contact book!!!!");
        while (true) {
            System.out.println();
            System.out.println("Enter any option to perform");
            System.out.println("1.Display contact");
            System.out.println("2.Add new contact");
            System.out.println("3.Save");
            System.out.println("4.Exit");
            choice = obj.nextInt();
            switch (choice) {
            case 1:
                db.displayContactBook();
                break;
            case 2:
                db.insertContact();
                break;
            case 3:
                db.saveContact();
                break;
            case 4:
                db.exit();
                break;
            default:
                System.out.println("Enter 1 to 4 choice only");
            }
            if (choice == 4)
                break;
        }
        System.out.println("Thank you.");
        obj.close();
    }
}