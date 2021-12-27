package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import model.dao.DaoFactory;
import model.entities.*;

public class program {

	public static void main(String[] args) throws ParseException {

		Department obj = new Department(1, "Books");
		System.out.println(obj);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Seller seller = new Seller(1, "Rodrigo", "rpf34@gmail.com", sdf.parse("19/05/2001"), 3000.00, obj);
		
		
		SellerDao sellerdao = DaoFactory.createSellerDao();
		
		System.out.println("\n" + seller);
	}

}