package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.*;

public class program {

	public static void main(String[] args) throws ParseException {

		
		SellerDao sellerdao = DaoFactory.createSellerDao();
		Seller se = sellerdao.findById(3);
		System.out.println(se);
	}

}