package aiaudio.system.algorithms.realizations.social.remap;

import aiaudio.system.algorithms.base.Algorithm;

/**
 *
 * @author Dmitry
 */
public class RemapFriendsAlgorithm extends Algorithm<RemapFriendsInputStorage, RemapFriendsOutputStorage> {

    @Override
    public void start() {
        int usersCount = inputStorage.getFriendsMatrixUsersCount();
        int mappedUsersCount = inputStorage.getMappedUsersCount();
        outputStorage.initialize(mappedUsersCount);
        for (int i = 0; i < usersCount; i++) {
            
            if(remapForUser(i, usersCount))continue;
        }
    }

    private boolean remapForUser(int i, int usersCount) {
        int id1 = inputStorage.getUserId(i);
        if (id1 < 0) {
            return true;
        }
        for (int j = 0; j < usersCount; j++) {
            if (inputStorage.isFriends(i, j)) {
                int id2 = inputStorage.getUserId(j);
                if (id2 >= 0) {
                    outputStorage.setFriends(id1, id2);
                }
            }
        }
        if(i % 1000 == 0){
            log("Processed " + i + " users");
        }
        return false;
    }

    @Override
    protected RemapFriendsInputStorage createInputStorage() {
        return new RemapFriendsInputStorage();
    }

    @Override
    protected RemapFriendsOutputStorage createOutputStorage() {
        return new RemapFriendsOutputStorage();
    }
}
