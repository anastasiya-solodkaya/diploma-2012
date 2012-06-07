package aiaudio.system.algorithms.realizations.neuro.network;

import aiaudio.system.algorithms.base.Algorithm;
import aiaudio.system.algorithms.realizations.neuro.network.som.CardinalityNotMatchException;
import aiaudio.system.algorithms.realizations.neuro.network.som.SelfOrganizedMap;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class NeuroNetworkGrouppingAlgorithm extends Algorithm<NeuroNetworkGrouppingInputStorage, NeuroNetworkGrouppingOutputStorage> {

    private SelfOrganizedMap network;
    private int totalUsersCount;
    private int totalArtistsCount;
    private int maxLearningCicles;
    private int groupCount;
    private int trainingSetSize;

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public void setMaxLearningCicles(int maxLearningCicles) {
        this.maxLearningCicles = maxLearningCicles;
    }

    public void setTrainingSetSize(int trainingSetSize) {
        this.trainingSetSize = trainingSetSize;
    }

    @Override
    protected NeuroNetworkGrouppingInputStorage createInputStorage() {
        return new NeuroNetworkGrouppingInputStorage();
    }

    @Override
    protected NeuroNetworkGrouppingOutputStorage createOutputStorage() {
        return new NeuroNetworkGrouppingOutputStorage();
    }

    @Override
    public void start() {
        try {
            processWithNetwork();
        } catch (IOException ex) {
            Logger.getLogger(NeuroNetworkGrouppingAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processWithNetwork() throws CardinalityNotMatchException, IOException {
        prepare();
        trainNetwork();
        groupUsers();
    }

    private void groupUsers() {
        log("start groupping");
        for (int i = 0; i < totalUsersCount; i++) {
            Vector userColumn = inputStorage.getArtistsByUserRatings(i);
            int group = network.calculateGroup(userColumn);
            outputStorage.setGroupForUser(i, group);
            if (i % 1000 == 0) {
                log("processed " + i + " users");
            }
        }
        log("end groupping");
    }

    private void trainNetwork() throws IOException, CardinalityNotMatchException {
        network = new SelfOrganizedMap(totalArtistsCount, groupCount);

        log("start training");
        Vector[] trainingSet = collectTrainingSet();
        network.startLearning(trainingSet, maxLearningCicles);
        log("training eneded");
        outputStorage.setAndSaveTrainedSOM(network);
    }

    private void prepare() {
        totalUsersCount = inputStorage.usersCount();
        log("total users" + totalUsersCount);
        totalArtistsCount = inputStorage.artistsCount();
        log("total artists" + totalArtistsCount);
        outputStorage.initialize(totalUsersCount, groupCount);
    }

    private Vector[] collectTrainingSet() {
        int sz = Math.min(trainingSetSize, totalUsersCount);

        Vector[] vectors = new Vector[sz];
        for (int i = 0; i < sz; i++) {
            vectors[i] = inputStorage.getArtistsByUserRatings(i).clone();
        }
        return vectors;
    }
}
