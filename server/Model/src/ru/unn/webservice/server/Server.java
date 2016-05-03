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

		Algorithm algorithm = new Algorithm("alg2", "superalg2", tags, sourceFile, testFile, "Me", 100500, Algorithm.Language.CPP);
		IDataRequest request2 = new AddAlgorithmRequest(algorithm);
		AddAlgorithmResponse response = (AddAlgorithmResponse)da.process(request2);
		System.out.println(response.status);

		IDataRequest request4 = new FindAlgorithmRequest(tags);
		FindAlgorithmResponse response4 = (FindAlgorithmResponse)da.process(request4);
		System.out.println(response4.status);
		response4.algorithms.forEach(Algorithm::print);

//
//		Algorithm algorithm1 = new Algorithm("alg1", null, null, null, null, null, 0, null);
//		IDataRequest request3 = new GetAlgorithmRequest(algorithm1);
//		GetAlgorithmResponse response1 = (GetAlgorithmResponse)da.process(request3);
//		System.out.println(response1.status);
//		response1.algorithm.print();

//		User user = new User("sadgdh", "dfhdh", 1, 0);
//		IDataRequest request = new AddUserRequest(user);
//		IDataRequest request1 = new GetUserRequest(user);
//		AddUserResponse response = (AddUserResponse)da.process(request);
//		System.out.println(response.status);
//		GetUserResponse response1 = (GetUserResponse)da.process(request1);
//		System.out.println(response1.status);
//		response1.user.print();
		//da.getUser((AddUserRequest)request);
	}
}