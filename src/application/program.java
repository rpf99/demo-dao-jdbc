package application;

import java.text.ParseException;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.*;

public class program {

	public static void main(String[] args) throws ParseException {

		SellerDao sellerdao = DaoFactory.createSellerDao();
		
		System.out.println("Teste 1 - Seller By Id ");
		Seller se = sellerdao.findById(3);
		System.out.println(se);
	}

}