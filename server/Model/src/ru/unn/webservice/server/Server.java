package ru.unn.webservice.server;

import ru.unn.webservice.storage.*;

public class Server {
	public static void main(final String[] args) {
		System.out.println("aaaaaaa");
		IDataAccess da = new DataAccess();
		User user = new User("sadgdh", "dfhdh", 1, 0);
		IDataRequest request = new AddUserRequest(user);
		IDataRequest request1 = new GetUserRequest(user);
		AddUserResponse response = (AddUserResponse)da.process(request);
		System.out.println(response.status);
		GetUserResponse response1 = (GetUserResponse)da.process(request1);
		System.out.println(response1.status);
		response1.user.print();
		//da.getUser((AddUserRequest)request);
	}
}