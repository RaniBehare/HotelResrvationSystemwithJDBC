package com.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HotelReservation {

	public static void main(String[] args) throws ClassNotFoundException, SQLException 
	{
		System.out.println("Hotel Management System\n");
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_db","root","root");
			
			while(true)
			{
				System.out.println();
				Scanner sc = new Scanner(System.in);
				System.out.println("1.Reserve a room"
						+ "\n2.View Reservations"
						+ "\n3.Get Room Number"
						+ "\n4.Update Reservation"
						+ "\n5.Delete Reservation"
						+ "\nChoose an option");
				int choice = sc.nextInt();
				switch(choice) {
				case 1:
					reserveRoom(conn);
					break;
				case 2:
					viewReservation(conn);
					break;
				case 3:
					getRoomNumber(conn);
					break;
				case 4:
					updateReservation(conn);
					break;
				case 5:
					deleteReservation(conn);
					break;
				default:
					System.out.println("Invalid choice. Try again!");
				}
			}
		}catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	public static void reserveRoom(Connection con)  throws SQLException
	{
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter guest name: ");
		String guestName = sc.next()+sc.nextLine();
		System.out.println("Enter room number: ");
		int roomNumber = sc.nextInt();
		System.out.println("Enter contact number: ");
		String contactNumber = sc.next()+sc.nextLine();
		
		String sql = "INSERT INTO reservations(guest_name, room_number, contact_number)" + "VALUES('"+guestName+"', "+roomNumber+", '"+contactNumber+"')";
		Statement statement = con.createStatement();
		int affectedRows = statement.executeUpdate(sql);
		
		if(affectedRows > 0)
		{
			System.out.println("Reservation successful");
		}else 
		{
			System.out.println("Reservation Failed..");
		}
	}
	
	public static void viewReservation(Connection con) throws SQLException
	{
		String sql ="SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";
		Statement statement = con.createStatement();
		ResultSet resultset = statement.executeQuery(sql);
		
		System.out.println("Current Reservations:");
		System.out.println("+----------------+-----------------+---------------+----------------------+------------------------+");
        System.out.println("| Reservation ID | Guest Name      | Room Number   | Contact Number       | Reservation Date       |");
        System.out.println("+----------------+-----------------+---------------+----------------------+------------------------+");
      
		
		while(resultset.next())
		{
			int reservationId = resultset.getInt("reservation_id");
			String guestName = resultset.getString("guest_name");
			int roomNumber = resultset.getInt("room_number");
			String contactNumber = resultset.getString("contact_number");
			String reservationDate = resultset.getString("reservation_date").toString();
			
			System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n",reservationId, guestName, roomNumber, contactNumber, reservationDate);
		}
	}
	
	private static void getRoomNumber(Connection con) throws SQLException
	{
		
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter reservation ID: ");
        int reservationId = sc.nextInt();
        System.out.print("Enter guest name: ");
        String guestName = sc.next();

        String sql = "SELECT room_number FROM reservations WHERE reservation_id =" +reservationId+ "AND guest_name ='" +guestName+ "' ";

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
            
        if (resultSet.next()) 
        {
          	int roomNumber = resultSet.getInt("room_number");
            System.out.println("Room number for Reservation ID " + reservationId + " and Guest " + guestName + " is: " + roomNumber);
        }
        else 
        {
         	System.out.println("Reservation not found for the given ID and guest name..");
        }            
    }

	private static void updateReservation(Connection con) throws SQLException
	{
       
		Scanner sc = new Scanner(System.in);
        System.out.print("Enter reservation ID to update: ");
        int reservationId = sc.nextInt();
            
        if (!reservationExists(con, reservationId))
        {
        	System.out.println("Reservation not found for the given ID.");
               // return;
        }

        System.out.print("Enter new guest name: ");
        String newGuestName = sc.next()+sc.nextLine(); // Consume the newline character
        System.out.print("Enter new room number: ");
        int newRoomNumber = sc.nextInt();
        System.out.print("Enter new contact number: ");
        String newContactNumber = sc.next();

        String sql = "UPDATE reservations SET guest_name ='"+newGuestName+"',"+"room_number ="+newRoomNumber+","+"contact_number ='"+newContactNumber+"'"+"WHERE reservation_id ="+ reservationId;

        Statement statement = con.createStatement();
        int affectedRows = statement.executeUpdate(sql);

        if (affectedRows > 0) 
        {
           System.out.println("Reservation updated successfully!");
        }
        else
        {
           System.out.println("Reservation update failed.");
        }
     } 
        
	private static boolean reservationExists(Connection con, int reservationId) throws SQLException
    {
       String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = "+reservationId;

       Statement statement = con.createStatement();
       ResultSet resultSet = statement.executeQuery(sql);

       return resultSet.next(); // If the reservation exists 
     }
        
	private static void deleteReservation(Connection con) throws SQLException
	{
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter reservation ID to delete: ");
        int reservationId = sc.nextInt();

        if (!reservationExists(con, reservationId))
        {
          System.out.println("Reservation not found for the given ID.");
                //return;
        }

        String sql = "DELETE FROM reservations WHERE reservation_id =" +reservationId;

        Statement statement = con.createStatement();
        int affectedRows = statement.executeUpdate(sql);

        if (affectedRows > 0) 
        {
           System.out.println("Reservation deleted successfully!");
        }
        else 
        {
        	System.out.println("Reservation deletion failed.");
        }
      }
}