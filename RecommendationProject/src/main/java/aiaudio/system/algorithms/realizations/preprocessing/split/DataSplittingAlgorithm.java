package aiaudio.system.algorithms.realizations.preprocessing.split;

import aiaudio.system.algorithms.base.UsersArtistsBasedAlgorithm;
import java.util.List;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class DataSplittingAlgorithm extends UsersArtistsBasedAlgorithm<DataSplittingInputStorage, DataSplittingOutputStorage> {

    private double learningSetPercent;

    public void setLearningSetPercent(double learningSetPercent) {
        this.learningSetPercent = learningSetPercent;
    }

    @Override
    public void start() {
        super.start();
        prepare();
        collectByUsers();
        collectByArtists();
    }

    private void collectByArtists() {
        List<Integer> checker = outputStorage.getArtistsWithoutUsers();
        log("artists without users: " + checker.size());
        for (int i = 0; i < checker.size(); i++) {
            double artistId = checker.get(i);
            processArtist((int) artistId);
        }
    }

    private void collectByUsers() {
        for (int userId = 0; userId < usersCount; userId++) {
            processUser(userId);
        }
    }

    private void prepare() {
        outputStorage.initialize(artistsCount, usersCount);
    }

    private void processUser(int userId) {
        log(userId + "");
        Vector viewColumn = getVectorByUser(userId);

        log("viewColumn.getNumNondefaultElements() = " + viewColumn.getNumNondefaultElements());
        int artistCountToFill = (int) (viewColumn.getNumNondefaultElements() * learningSetPercent);
        log("artistCountToFill = " + artistCountToFill);
        artistCountToFill = artistCountToFill < 1 ? 1 : artistCountToFill;
        int addedArtistsCount = 0;
        for (int artistId = 0; artistId < artistsCount; artistId++) {
            if (addedArtistsCount < artistCountToFill) {
                if (viewColumn.getQuick(artistId) != 0) {
                    outputStorage.setLearningRating(artistId, userId);
                    addedArtistsCount++;
                }
            }
        }
        log("addedArtistsCount = " + addedArtistsCount);
    }

    private void processArtist(int artistId) {
        Vector viewColumn = getVectorByArtist(artistId);
        int userCountToFill = (int) (viewColumn.getNumNondefaultElements() * learningSetPercent);
        userCountToFill = userCountToFill < 1 ? 1 : userCountToFill;
        int addedUsersCount = 0;

        for (int userId = 0; userId < usersCount; userId++) {
            if (addedUsersCount < userCountToFill) {
                if (viewColumn.getQuick(userId) != 0) {
                    outputStorage.setLearningRating(artistId, userId);
                    addedUsersCount++;
                }
            }
        }
    }

    @Override
    protected DataSplittingInputStorage createInputStorage() {
        return new DataSplittingInputStorage();
    }

    @Override
    protected DataSplittingOutputStorage createOutputStorage() {
        return new DataSplittingOutputStorage();
    }
}
