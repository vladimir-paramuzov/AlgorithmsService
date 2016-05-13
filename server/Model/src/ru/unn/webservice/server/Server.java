package ru.unn.webservice.server;

import ru.unn.webservice.authorization.AuthorizationSystem;
import ru.unn.webservice.authorization.IAuthorizationSystem;
import ru.unn.webservice.control.AlgorithmControlSystem;
import ru.unn.webservice.control.IAlgorithmControlSystem;
import ru.unn.webservice.payment.IPaymentSystem;
import ru.unn.webservice.payment.PaymentSystemEmulator;
import ru.unn.webservice.statistic.IStatisticCollector;
import ru.unn.webservice.statistic.StatisticCollector;
import ru.unn.webservice.storage.DataAccess;
import ru.unn.webservice.storage.IDataAccess;
import ru.unn.webservice.test_automation.BuildBot;
import ru.unn.webservice.test_automation.IBuildBot;

import java.util.Scanner;

public class Server {
    Server() {
        dataAccess = new DataAccess();
        authorizationSystem = new AuthorizationSystem(dataAccess);
        paymentSystem = new PaymentSystemEmulator(dataAccess);
        statisticCollector = new StatisticCollector(dataAccess);
        buildbot = new BuildBot(dataAccess);
        algorithmControlSystem = new AlgorithmControlSystem(dataAccess, statisticCollector, paymentSystem);
        connector = new Connector(this);
        isTestMode = false;
    }

    Server(boolean isTestMode) {
        this();
        this.isTestMode = isTestMode;
    }

	public static void main(final String[] args) {
		Server server = new Server();
		server.run();
    }

	private void waitForEnter() {
		System.out.println("To kill server type 'kill'");
		Scanner scanner = new Scanner(System.in);
		String line;
		while (!(line = scanner.nextLine()).equals("kill")) {
			System.out.println(line + ": command not found");
		}
	}

	public void run() {
		connector.start();
		while (!connector.isAlive());
        if (!isTestMode) {
            waitForEnter();
            close();
        }

		System.out.println("Server killed :(");
	}

    public void close() {
        connector.close();
    }

    boolean isConnectorAlive() {
        return connector.isAlive();
    }

	IAuthorizationSystem getAuthorizationSystem() {
		return authorizationSystem;
	}

	IAlgorithmControlSystem getAlgorithmControlSystem() {
		return algorithmControlSystem;
	}

	IPaymentSystem getPaymentSystem() {
		return paymentSystem;
	}

	IStatisticCollector getStatisticCollector() {
		return statisticCollector;
	}

	public IDataAccess getDataAccess() {
		return dataAccess;
	}

	IBuildBot getBuildbot() {
		return buildbot;
	}

    private boolean isTestMode;

	private IAuthorizationSystem authorizationSystem;
	private IAlgorithmControlSystem algorithmControlSystem;
	private IPaymentSystem paymentSystem;
	private IStatisticCollector statisticCollector;
	private IDataAccess dataAccess;
	private IBuildBot buildbot;
	private Connector connector;
}