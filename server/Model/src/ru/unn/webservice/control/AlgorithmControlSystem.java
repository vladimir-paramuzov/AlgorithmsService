package ru.unn.webservice.control;

import ru.unn.webservice.payment.IPaymentSystem;
import ru.unn.webservice.server.*;
import ru.unn.webservice.statistic.IStatisticCollector;
import ru.unn.webservice.storage.*;

import java.util.ArrayList;
import java.util.Calendar;

public class AlgorithmControlSystem implements IAlgorithmControlSystem {

    public AlgorithmControlSystem(IDataAccess dataAccess, IStatisticCollector statisticCollector, IPaymentSystem paymentSystem) {
        this.dataAccess = dataAccess;
        this.statisticCollector = statisticCollector;
        this.paymentSystem = paymentSystem;
    }

    @Override
    public IResponse process(IRequest request) {
        if (request instanceof DownloadRequest) {
            return (IResponse)download((DownloadRequest) request);
        } else if (request instanceof BuyRequest){
            return (IResponse)buy((BuyRequest)request);
        } else if (request instanceof AddRequest){
            return (IResponse)add((AddRequest)request);
        } else if (request instanceof SearchAlgorithmsRequest){
            return (IResponse)search((SearchAlgorithmsRequest)request);
        } else {
            return null;
        }
    }

    private AddResponse add(AddRequest request) {
        LoadAlgorithmDataResponse algorithmResponse = (LoadAlgorithmDataResponse)dataAccess.process(
                new LoadAlgorithmDataRequest(request.algorithm.name));

        if (algorithmResponse.status.equals("OK")) {
            return new AddResponse("ALGORITHM ALREADY EXISTS");
        }

        return new AddResponse(((StoreAlgorithmDataResponse)dataAccess.process(
                                 new StoreAlgorithmDataRequest(request.algorithm))).status);
    }

    private DownloadResponse download(DownloadRequest request) {
        LoadUserDataResponse userResponse = (LoadUserDataResponse)dataAccess.process(
                                             new LoadUserDataRequest(request.username));
        if (!userResponse.status.equals("OK")) {
            return new DownloadResponse(null, userResponse.status);
        }

        LoadAlgorithmDataResponse algorithmResponse = (LoadAlgorithmDataResponse)dataAccess.process(
                                                        new LoadAlgorithmDataRequest(request.algorithmName));

        if (!algorithmResponse.status.equals("OK")) {
            return new DownloadResponse(null, algorithmResponse.status);
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
                return new DownloadResponse(null, "ALGORITHM IS NOT BOUGHT");
            }
        }

        statisticCollector.addDownloadDate(Calendar.getInstance().getTime());

        return new DownloadResponse(algorithm, "OK");
    }

    private BuyResponse buy(BuyRequest request) {
        LoadUserDataResponse userResponse = (LoadUserDataResponse)dataAccess.process(
                new LoadUserDataRequest(request.username));
        if (!userResponse.status.equals("OK")) {
            return new BuyResponse(userResponse.status);
        }

        LoadAlgorithmDataResponse algorithmResponse = (LoadAlgorithmDataResponse)dataAccess.process(
                new LoadAlgorithmDataRequest(request.algorithmName));

        if (!algorithmResponse.status.equals("OK")) {
            return new BuyResponse(algorithmResponse.status);
        }

        Algorithm algorithm = algorithmResponse.algorithm;
        User user = userResponse.user;

        for (String algorithmName : user.purchasedAlgorithms) {
            if (algorithmName.equals(request.algorithmName)) {
                return new BuyResponse("ALGORITHM ALREADY BOUGHT");
            }
        }

        ChangeBalanceResponse balanceResponse = paymentSystem.changeBalance(
                                                    new ChangeBalanceRequest(-algorithm.cost, user.login));
        if (!balanceResponse.status.equals("OK")) {
            return new BuyResponse(balanceResponse.status);
        }

        userResponse = (LoadUserDataResponse)dataAccess.process(
                new LoadUserDataRequest(request.username));

        if (!userResponse.status.equals("OK")) {
            dataAccess.process(new StoreUserDataRequest(user));
            return new BuyResponse(userResponse.status);
        }

        user = userResponse.user;

        user.purchasedAlgorithms.add(algorithm.name);
        StoreUserDataResponse storeUserDataResponse = (StoreUserDataResponse)dataAccess.process(
                                                        new StoreUserDataRequest(user));

        if (!storeUserDataResponse.status.equals("OK")) {
            return new BuyResponse(storeUserDataResponse.status);
        }

        statisticCollector.addPurchaseDate(Calendar.getInstance().getTime());

        return new BuyResponse("OK");
    }

    public SearchAlgorithmResponse search(SearchAlgorithmsRequest request) {
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

    IStatisticCollector statisticCollector;
    IPaymentSystem paymentSystem;
    IDataAccess dataAccess;
}
