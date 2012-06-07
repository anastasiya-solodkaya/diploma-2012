package aiaudio.system;

/**
 *
 * @author Anastasiya
 */
public enum Phase {
    Map, SplitToSets, ConvertToRatings, ReduceDimension, ProcessNetwork, RecalculateRatings, FillTopList, Analyze, MapFriends, Social;

    public static Phase fromString(String input) {
        if (input.equalsIgnoreCase("map")) {
            return Map;
        }
        if (input.equalsIgnoreCase("split")) {
            return SplitToSets;
        }
        if (input.equalsIgnoreCase("make_ratings")) {
            return ConvertToRatings;
        }
        if (input.equalsIgnoreCase("reduce")) {
            return ReduceDimension;
        }
        if (input.equalsIgnoreCase("neuro")) {
            return ProcessNetwork;
        }
        if (input.equalsIgnoreCase("recalculate_ratings")) {
            return RecalculateRatings;
        }
        if (input.equalsIgnoreCase("recommend")) {
            return FillTopList;
        }
        if (input.equalsIgnoreCase("analyze")) {
            return Analyze;
        }
        if (input.equalsIgnoreCase("social")) {
            return Social;
        }
        if (input.equalsIgnoreCase("map_friends")) {
            return MapFriends;
        }
        return null;
    }
    
    public static Phase[] getOrderedPhases(){
        return new Phase[]{Map, SplitToSets, ConvertToRatings, ReduceDimension, ProcessNetwork, RecalculateRatings, FillTopList, Analyze};
    }
}
