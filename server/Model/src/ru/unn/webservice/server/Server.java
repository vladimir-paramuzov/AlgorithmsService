package ru.unn.webservice.server;

import ru.unn.webservice.storage.*;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class Server {
	public static void main(final String[] args) {
		System.out.println("aaaaaaa");
		IDataAccess da = new DataAccess();
		String code = "#include <cstdio>";
		String testdata = "Here will be test data";
		byte[] sourceFile = code.getBytes(Charset.forName("UTF-8"));
		byte[] testFile = testdata.getBytes(Charset.forName("UTF-8"));
		ArrayList<String> tags = new ArrayList<String>(){ { add("tag1"); add("tag2"); }};

		Algorithm algorithm = new Algorithm("alg1", "superalg2", tags, sourceFile, testFile, "Me", 100500, Algorithm.Language.CPP);
		IDataRequest request2 = new StoreAlgorithmDataRequest(algorithm);
		StoreAlgorithmDataResponse response = (StoreAlgorithmDataResponse)da.process(request2);
		System.out.println(response.status);
//
//		IDataRequest request4 = new FindAlgorithmDataRequest(tags);
//		FindAlgorithmDataResponse response4 = (FindAlgorithmDataResponse)da.changeBalance(request4);
//		System.out.println(response4.status);
//		response4.algorithms.forEach(Algorithm::print);

		IDataRequest request3 = new LoadAlgorithmDataRequest("alg1");
		LoadAlgorithmDataResponse response1 = (LoadAlgorithmDataResponse)da.process(request3);
		System.out.println(response1.status);
		response1.algorithm.print();

//		User user = new User("sadgdsdgh", "dfhd11h", User.TYPE.USER, 100500);
//		IDataRequest request = new StoreUserDataRequest(user);
//		IDataRequest request1 = new LoadUserDataRequest("sadgdsdgh");
//		StoreUserDataResponse response5 = (StoreUserDataResponse)da.changeBalance(request);
//		System.out.println(response5.status);
//		LoadUserDataResponse response1 = (LoadUserDataResponse)da.changeBalance(request1);
//		System.out.println(response1.status);
//		response1.user.print();
//		da.changeBalance(request);
	}
}