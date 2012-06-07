package aiaudio.system.algorithms.base;

import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public abstract class UsersArtistsBasedAlgorithm<I extends UserArtistsContainedInputStorage, O extends AbstractOutputStorage> 
            extends Algorithm<I, O> {

    protected int usersCount;
    protected int artistsCount;

    @Override
    public void start() {
        usersCount = inputStorage.getUsersCount();
        artistsCount = inputStorage.getArtistsCount();
    }
    
    protected Vector getVectorByUser(int userId) {
        return inputStorage.getVectorByUser(userId);
    }

    protected Vector getVectorByArtist(int artistId) {
        return inputStorage.getVectorByArtist(artistId);
    }

    protected double getValueByUserArtist(int userId, int artistId) {
        return inputStorage.getValueByUserArtist(userId, artistId);
    }

    
}
