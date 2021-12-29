package application;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.*;

public class program {

	public static void main(String[] args) throws ParseException {

		SellerDao sellerdao = DaoFactory.createSellerDao();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("--------------Teste 1 : Find By Id Seller--------------");
		Seller se = sellerdao.findById(3);
		System.out.println(se);
		
		
		System.out.println("\n\n\n--------------Teste 2 : Seller Find By DepartmentId--------------");
		Department d = new Department(2,null);
		List<Seller> list = sellerdao.findByDepartment(d);
		list.forEach(s -> System.out.println(s) );
		
		
		System.out.println("\n\n\n--------------Teste 3 : find All Seller--------------");
		list = sellerdao.findAll();
		list.forEach(s -> System.out.println(s) );
		
		
		System.out.println("\n\n\n--------------Teste 4 : Insert Seller--------------");
		Date date = new Date();
		date.setYear(1985);

		Seller s = new Seller(null, "Greg", "greg@gmail.com", date , 4000.0, d);
		sellerdao.insert(s);
		System.out.println("Inserted: " + s.getId());
		
		
		System.out.println("\n\n\n--------------Teste 5 : Update Seller--------------");
		Seller seller = sellerdao.findById(1);
		seller.setName("Martha Wayne");
		sellerdao.update(seller);
		System.out.println("Update Completed");
		
		
		System.out.println("\n\n\n--------------Teste 6 : Delete Seller--------------");
		
		System.out.println("Digite um Id para deletar");
		int id = sc.nextInt();
		
		sellerdao.deleteById(id);
		System.out.println("Item deletado com sucesso");
		
		sc.close();
	}

}