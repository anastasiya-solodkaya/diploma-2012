package aiaudio.system.iface;

import aiaudio.system.iface.prop.FileNameProperty;
import aiaudio.system.util.ReadingUtils;
import java.util.Properties;

/**
 *
 * @author Anastasiya
 */
public class FileNameProperties {

    private FileNameProperty normilizedRatingsName = new FileNameProperty("normilized_ratings_name", "normilized.csv");
    private FileNameProperty learningSetMaskName = new FileNameProperty("learning_set_mask_name", "learningTestMask.csv");
    private FileNameProperty reducingCentroidsName = new FileNameProperty("reducing_centroids_name", "centroidsList.csv");
    private FileNameProperty trainedNetworkName = new FileNameProperty("trained_network_name", "network.csv");
    private FileNameProperty networkGroupsName = new FileNameProperty("network_groups_name", "userGroups.csv");
    private FileNameProperty networkGroupsRatingsName = new FileNameProperty("network_groups_ratings_name", "userGroupRatings.csv");
    private FileNameProperty recalculatedRatingsName = new FileNameProperty("recalculated_ratings_name", "recalculatedRatings.csv");
    private FileNameProperty topListRatingsName = new FileNameProperty("top_list_ratings_name", "topListArtists.csv");
    private FileNameProperty topListArtistsName = new FileNameProperty("top_list_artists_name", "topListRatings.csv");
    private FileNameProperty savedMetricsName = new FileNameProperty("saved_metrics_name", "metrics.txt");
    private FileNameProperty userMapName = new FileNameProperty("user_map_name", "users_map.csv");
    private FileNameProperty artistMapName = new FileNameProperty("artist_map_name", "artists_map.csv");
    private FileNameProperty inputFileName = new FileNameProperty("input_name", "input.csv");
    private FileNameProperty remappedFileName = new FileNameProperty("remapped_name", "remapped.csv");
    private FileNameProperty friendsFileName = new FileNameProperty("friends_list", "friends.csv");
    private FileNameProperty socialRatingsFileName = new FileNameProperty("social_ratings", "social_ratings.csv");
    private FileNameProperty socialTopListRatingsFileName = new FileNameProperty("social_top_list_ratings", "social_top_list_ratings.csv");
    private FileNameProperty socialTopListArtistFileName = new FileNameProperty("social_top_list_artist", "social_top_list_artists.csv");
    private FileNameProperty socialMetricsName = new FileNameProperty("social_metrics", "social_metrics.txt");

    public String getArtistMapName() {
        return artistMapName.getValue();
    }

    public String getLearningSetMaskName() {
        return learningSetMaskName.getValue();
    }

    public String getNetworkGroupsName() {
        return networkGroupsName.getValue();
    }

    public String getNetworkGroupsRatingsName() {
        return networkGroupsRatingsName.getValue();
    }

    public String getNormilizedRatingsName() {
        return normilizedRatingsName.getValue();
    }

    public String getRecalculatedRatingsName() {
        return recalculatedRatingsName.getValue();
    }

    public String getReducingCentroidsName() {
        return reducingCentroidsName.getValue();
    }

    public String getSavedMetricsName() {
        return savedMetricsName.getValue();
    }

    public String getTopListArtistsName() {
        return topListArtistsName.getValue();
    }

    public String getTopListRatingsName() {
        return topListRatingsName.getValue();
    }

    public String getTrainedNetworkName() {
        return trainedNetworkName.getValue();
    }

    public String getUserMapName() {
        return userMapName.getValue();
    }

    public String getInputFileName() {
        return inputFileName.getValue();
    }

    public String getRemappedFileName() {
        return remappedFileName.getValue();
    }

    public String getFriendsFileName() {
        return friendsFileName.getValue();
    }

    public String getSocialMetricsName() {
        return socialMetricsName.getValue();
    }

    public String getSocialRatingsFileName() {
        return socialRatingsFileName.getValue();
    }

    public String getSocialTopListArtistFileName() {
        return socialTopListArtistFileName.getValue();
    }

    public String getSocialTopListRatingsFileName() {
        return socialTopListRatingsFileName.getValue();
    }

    public FileNameProperties() {
    }

    private void fill(Properties prop) {
        normilizedRatingsName.read(prop);
        learningSetMaskName.read(prop);
        reducingCentroidsName.read(prop);
        trainedNetworkName.read(prop);
        networkGroupsName.read(prop);
        networkGroupsRatingsName.read(prop);
        recalculatedRatingsName.read(prop);
        topListRatingsName.read(prop);
        topListArtistsName.read(prop);
        savedMetricsName.read(prop);
        artistMapName.read(prop);
        userMapName.read(prop);
        inputFileName.read(prop);
        remappedFileName.read(prop);


        friendsFileName.read(prop);
        socialMetricsName.read(prop);
        socialRatingsFileName.read(prop);
        socialTopListArtistFileName.read(prop);
        socialTopListRatingsFileName.read(prop);

    }

    public static FileNameProperties parse(String propertiesPath) {
        Properties properties = ReadingUtils.readPropertiesFromFile(propertiesPath);
        FileNameProperties fileNameProperties = new FileNameProperties();
        fileNameProperties.fill(properties);

        return fileNameProperties;
    }
}
