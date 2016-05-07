package ru.unn.webservice.server;

import ru.unn.webservice.authorization.AuthorizationSystem;
import ru.unn.webservice.authorization.IAuthorizationSystem;
import ru.unn.webservice.control.AlgorithmControlSystem;
import ru.unn.webservice.control.IAlgorithmControlSystem;
import ru.unn.webservice.payment.IPaymentSystem;
import ru.unn.webservice.payment.PaymentSystemEmulator;
import ru.unn.webservice.statistic.IStatisticCollector;
import ru.unn.webservice.statistic.StatisticCollector;
import ru.unn.webservice.storage.*;
import ru.unn.webservice.test_automation.BuildBot;
import ru.unn.webservice.test_automation.IBuildBot;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	public Server() {
		dataAccess = new DataAccess();
		authorizationSystem = new AuthorizationSystem(dataAccess);
		paymentSystem = new PaymentSystemEmulator(dataAccess);
		statisticCollector = new StatisticCollector(dataAccess);
		buildbot = new BuildBot(dataAccess);
		algorithmControlSystem = new AlgorithmControlSystem(dataAccess, statisticCollector, paymentSystem);
		connector = new Connector();
	}

	public static void main(final String[] args) {
		Server server = new Server();
		server.run();
	}

	public static void waitForEnter() {
		System.out.println("To kill server type 'kill'");
		Scanner scanner = new Scanner(System.in);
		String line;
		while (!(line = scanner.nextLine()).equals("kill")) {
			System.out.println(line + ": command not found");
		}

	}

	public void run() {
		connector.start();
		waitForEnter();
		connector.interrupt();
	}

	private static IAuthorizationSystem authorizationSystem;
	private static IAlgorithmControlSystem algorithmControlSystem;
	private static IPaymentSystem paymentSystem;
	private static IStatisticCollector statisticCollector;
	private static IDataAccess dataAccess;
	private static IBuildBot buildbot;
	private static Connector connector;
}