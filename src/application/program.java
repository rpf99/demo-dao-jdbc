package application;

import java.text.ParseException;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.*;

public class program {

	public static void main(String[] args) throws ParseException {

		SellerDao sellerdao = DaoFactory.createSellerDao();
		
		System.out.println("--------------Teste 1 : Seller By Id--------------\n");
		Seller se = sellerdao.findById(3);
		System.out.println(se);
		
		
		System.out.println("\n\n--------------Teste 2 : Seller Find By DepartmentId--------------\n");
		Department d = new Department(2,null);
		List<Seller> list = sellerdao.findByDepartment(d);
		list.forEach(s -> System.out.println(s) );
		
		
		System.out.println("\n\n--------------Teste 3 : Seller Find All--------------\n");
		list = sellerdao.findAll();
		list.forEach(s -> System.out.println(s) );
	}

}