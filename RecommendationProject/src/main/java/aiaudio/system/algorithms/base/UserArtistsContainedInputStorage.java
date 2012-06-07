package aiaudio.system.algorithms.base;

import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public abstract class UserArtistsContainedInputStorage extends AbstractInputStorage {

    public abstract int getArtistsCount();

    public abstract int getUsersCount();

    public abstract Vector getVectorByUser(int userId);

    public abstract Vector getVectorByArtist(int artistId);

    public abstract double getValueByUserArtist(int userId, int artistId);
}
