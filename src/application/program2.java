package application;

import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.*;

public class program2 {

	public static void main(String[] args) throws ParseException {

		DepartmentDao departmentdao = DaoFactory.createDepartmentDao();
		Scanner sc = new Scanner(System.in);
		
		
		System.out.println("--------------TEST 1: findById Department --------------");
		
		Department dep = departmentdao.findById(1);
		System.out.println(dep);
		
		
		System.out.println("\n\n\n--------------TEST 2: findAll Department--------------");
		
		List<Department> list = departmentdao.findAll();
		list.forEach(d -> System.out.println(d + "\n"));
		
		
		System.out.println("\n\n\n--------------TEST 3: Insert Department--------------");
		
		Department newDepartment = new Department(null, "Music");
		departmentdao.insert(newDepartment);
		System.out.println("Novo id: " + newDepartment.getId());
		
		
		System.out.println("\n\n\n--------------TEST 4: Update Department--------------");
		
		Department dep2 = departmentdao.findById(1);
		dep2.setName("Food");
		departmentdao.update(dep2);
		System.out.println("Update Concluido");
		
		
		System.out.println("\n\n\n--------------TEST 5: Delete Department--------------");
		System.out.print("Digite um id do Departamento a ser deletado: ");
		int id = sc.nextInt();
		
		departmentdao.deleteById(id);
		System.out.println("Item deletado com sucesso");

		sc.close();
	}

}