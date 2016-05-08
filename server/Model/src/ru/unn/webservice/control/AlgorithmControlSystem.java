package ru.unn.webservice.control;

import ru.unn.webservice.payment.IPaymentSystem;
import ru.unn.webservice.server.*;
import ru.unn.webservice.statistic.IStatisticCollector;
import ru.unn.webservice.storage.*;

import java.util.ArrayList;
import java.util.Calendar;

public class AlgorithmControlSystem implements IAlgorithmControlSystem {
    public AlgorithmControlSystem(IDataAccess dataAccess,
                                  IStatisticCollector statisticCollector,
                                  IPaymentSystem paymentSystem) {
        this.dataAccess = dataAccess;
        this.statisticCollector = statisticCollector;
        this.paymentSystem = paymentSystem;
    }

    @Override
    public IResponse process(IRequest request) {
        if (request instanceof DownloadAlgorithmRequest) {
            return download((DownloadAlgorithmRequest) request);
        } else if (request instanceof BuyAlgorithmRequest){
            return buy((BuyAlgorithmRequest)request);
        } else if (request instanceof AddAlgorithmRequest){
            return add((AddAlgorithmRequest)request);
        } else if (request instanceof SearchAlgorithmRequest){
            return search((SearchAlgorithmRequest)request);
        } else {
            return null;
        }
    }

    private AddAlgorithmResponse add(AddAlgorithmRequest request) {
        LoadAlgorithmDataResponse algorithmResponse = (LoadAlgorithmDataResponse)dataAccess.process(
                new LoadAlgorithmDataRequest(request.algorithm.name));

        if (algorithmResponse.status.equals("OK")) {
            return new AddAlgorithmResponse("ALGORITHM ALREADY EXISTS");
        }

        return new AddAlgorithmResponse(((StoreAlgorithmDataResponse)dataAccess.process(
                                 new StoreAlgorithmDataRequest(request.algorithm))).status);
    }

    private DownloadAlgorithmResponse download(DownloadAlgorithmRequest request) {
        LoadUserDataResponse userResponse = (LoadUserDataResponse)dataAccess.process(
                                             new LoadUserDataRequest(request.username));
        if (!userResponse.status.equals("OK")) {
            return new DownloadAlgorithmResponse(null, userResponse.status);
        }

        LoadAlgorithmDataResponse algorithmResponse = (LoadAlgorithmDataResponse)dataAccess.process(
                                                        new LoadAlgorithmDataRequest(request.algorithmName));

        if (!algorithmResponse.status.equals("OK")) {
            return new DownloadAlgorithmResponse(null, algorithmResponse.status);
        }

        Algorithm algorithm = algorithmResponse.algorithm;
        User user = userResponse.user;

        if (algorithm.cost > 0) {
            boolean isAlgorithmBought = false;
            for (String algorithmName : user.purchasedAlgorithms) {
                if (algorithmName.equals(algorithm.name)) {
                    isAlgorithmBought = true;
                    break;
                }
            }

            if (!isAlgorithmBought) {
                return new DownloadAlgorithmResponse(null, "ALGORITHM IS NOT BOUGHT");
            }
        }

        statisticCollector.addDownloadDate(Calendar.getInstance().getTime());

        return new DownloadAlgorithmResponse(algorithm, "OK");
    }

    private BuyAlgorithmResponse buy(BuyAlgorithmRequest request) {
        LoadUserDataResponse userResponse = (LoadUserDataResponse)dataAccess.process(
                new LoadUserDataRequest(request.username));
        if (!userResponse.status.equals("OK")) {
            return new BuyAlgorithmResponse(userResponse.status);
        }

        LoadAlgorithmDataResponse algorithmResponse = (LoadAlgorithmDataResponse)dataAccess.process(
                new LoadAlgorithmDataRequest(request.algorithmName));

        if (!algorithmResponse.status.equals("OK")) {
            return new BuyAlgorithmResponse(algorithmResponse.status);
        }

        Algorithm algorithm = algorithmResponse.algorithm;
        User user = userResponse.user;

        for (String algorithmName : user.purchasedAlgorithms) {
            if (algorithmName.equals(request.algorithmName)) {
                return new BuyAlgorithmResponse("ALGORITHM ALREADY BOUGHT");
            }
        }

        ChangeBalanceResponse balanceResponse = (ChangeBalanceResponse)paymentSystem.process(
                                                 new ChangeBalanceRequest(-algorithm.cost, user.login));
        if (!balanceResponse.status.equals("OK")) {
            return new BuyAlgorithmResponse(balanceResponse.status);
        }

        userResponse = (LoadUserDataResponse)dataAccess.process(
                new LoadUserDataRequest(request.username));

        if (!userResponse.status.equals("OK")) {
            dataAccess.process(new StoreUserDataRequest(user));
            return new BuyAlgorithmResponse(userResponse.status);
        }

        user = userResponse.user;

        user.purchasedAlgorithms.add(algorithm.name);
        StoreUserDataResponse storeUserDataResponse = (StoreUserDataResponse)dataAccess.process(
                                                        new StoreUserDataRequest(user));

        if (!storeUserDataResponse.status.equals("OK")) {
            return new BuyAlgorithmResponse(storeUserDataResponse.status);
        }

        statisticCollector.addPurchaseDate(Calendar.getInstance().getTime());

        return new BuyAlgorithmResponse("OK");
    }

    private SearchAlgorithmResponse search(SearchAlgorithmRequest request) {
        LoadAlgorithmsListDataResponse response = (LoadAlgorithmsListDataResponse)dataAccess.process(
                new LoadAlgorithmsListDataRequest());
        if (!response.status.equals("OK")) {
            return new SearchAlgorithmResponse(null, "FAIL");
        }

        ArrayList<Algorithm> fullAlgorithmsList = response.algorithmsList;
        ArrayList<Algorithm> algorithmsList = new ArrayList<>();

        for (Algorithm algorithm : fullAlgorithmsList) {
            if (algorithm.tags.containsAll(request.tags)) {
                algorithmsList.add(algorithm);
            }
        }

        return new SearchAlgorithmResponse(algorithmsList, "OK");
    }

    private IStatisticCollector statisticCollector;
    private IPaymentSystem paymentSystem;
    private IDataAccess dataAccess;
}
