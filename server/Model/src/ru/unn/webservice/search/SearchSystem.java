package ru.unn.webservice.search;

public class SearchSystem {
//    public ArrayList<String> tags;
//
//    public FindAlgorithmDataRequest(ArrayList<String> tags) {
//        this.tags = tags;
//    }

//    public String status;
//    public ArrayList<Algorithm> algorithms;
//
//    public FindAlgorithmDataResponse(ArrayList<Algorithm> algorithms, String status) {
//        this.status = status;
//        this.algorithms = algorithms;
//    }
//    private FindAlgorithmDataResponse process(FindAlgorithmDataRequest request) {
//        FileInputStream fstream = null;
//        Algorithm algorithm = null;
//        File file = new File(ALGORITHMS_PATH);
//        ArrayList<Algorithm> result = new ArrayList<>();
//        String[] directories = file.list(new FilenameFilter() {
//            @Override
//            public boolean accept(File current, String name) {
//                return new File(current, name).isDirectory();
//            }
//        });
//
//        for (String directory : directories) {
//            try {
//                fstream = new FileInputStream(ALGORITHMS_PATH + directory + "/data.bin");
//                ObjectInputStream ostream = new ObjectInputStream(fstream);
//                algorithm = (Algorithm) ostream.readObject();
//                if (algorithm.tags.containsAll(request.tags)) {
//                    result.add(algorithm);
//                }
//            } catch (Exception ex) {
//                return new FindAlgorithmDataResponse(result, "FAIL");
//            }
//        }
//
//        return new FindAlgorithmDataResponse(result, "OK");
//    }
}
