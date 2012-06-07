package aiaudio.system.algorithms.realizations.postprocessing.analyse;

import aiaudio.system.algorithms.base.AbstractOutputStorage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.math.DenseVector;

/**
 *
 * @author Anastasiya
 */
public class MetricsCalculationOutputStorage extends AbstractOutputStorage {

    private HashMap<Double, RecallPrecision> map = new HashMap<Double, RecallPrecision>();
    private HashMap<Double, DenseVector> halfLifeUtilities = new HashMap<Double, DenseVector>();

    @Override
    protected void saveToDisk() {
        BufferedWriter writer = null;
        try {
            String filename = directory + fileNameProperties.getSavedMetricsName();
            writer = new BufferedWriter(new FileWriter(filename));
            writePrecisionRecall(writer);
            writeHalfLifeUtilities(writer);
        } catch (IOException ex) {
            Logger.getLogger(MetricsCalculationOutputStorage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(MetricsCalculationOutputStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void writePrecisionRecall(BufferedWriter writer) throws IOException {
        for (Map.Entry<Double, RecallPrecision> entry : map.entrySet()) {
            writeRecallPrecisionRecord(writer, entry);
        }
    }

    private void writeHalfLifeUtilities(BufferedWriter writer) throws IOException {
        Set<Entry<Double, DenseVector>> entrySet = halfLifeUtilities.entrySet();
        List<Entry<Double, DenseVector>> e = new ArrayList<Entry<Double, DenseVector>>(entrySet);
        Collections.sort(e, new Comparator<Entry<Double, DenseVector>>(){

            @Override
            public int compare(Entry<Double, DenseVector> t, Entry<Double, DenseVector> t1) {
                return t.getKey().compareTo(t1.getKey());
            }
        });
        
        for (Map.Entry<Double, DenseVector> entry : e) {
            writeHalfLifeUtilityRecord(writer, entry);
            
        }
    }

    private void writeHalfLifeUtilityRecord(BufferedWriter writer, Entry<Double, DenseVector> entry) throws IOException {
        writer.write("#ideal rating = " + entry.getKey());
        writer.newLine();
        writer.write("#places count = " + entry.getValue().size());
        writer.newLine();
        String hlString = toMatlabVector(entry.getValue());
        writer.write(hlString);
        writer.newLine();
    }

    private void writeRecallPrecisionRecord(BufferedWriter writer, Entry<Double, RecallPrecision> entry) throws IOException {
        writer.write("# threshold = " + entry.getKey());
        writer.newLine();
        String recallString = toMatlabVector(entry.getValue().recall);
        String precisionString = toMatlabVector(entry.getValue().precision);
        writer.write("recall = " + recallString);
        writer.newLine();
        writer.write("precision = " + precisionString);
        writer.newLine();
        writer.newLine();
        writer.newLine();
    }

    void initialize(int placesCount, ArrayList<RelevanceData> thresholds) {
        for (RelevanceData threshold : thresholds) {
            map.put(threshold.getThreshold(), new RecallPrecision(placesCount));
        }
    }

    void initialize(int placesCount, double maxRating, double minRating, double step) {
        for (double rating = maxRating; rating >= minRating; rating = rating - step) {
            halfLifeUtilities.put(rating, new DenseVector(placesCount));
        }
    }

    void setPrecisionRecall(double precision, double recall, int placeNum, double threshold) {
        RecallPrecision rp = map.get(threshold);
        rp.set(placeNum, recall, precision);
    }

    private String toMatlabVector(DenseVector vector) {
        String val = "[";

        for (int i = 0; i < vector.size(); i++) {
            val += vector.getQuick(i) + " ";
        }

        val += "]";
        return val;
    }

    void setHalfLifeUtility(double d, int placeNum, double threshold) {
        DenseVector v = halfLifeUtilities.get(threshold);
        v.setQuick(placeNum, d);
    }

    private static class RecallPrecision {

        private DenseVector recall;
        private DenseVector precision;

        RecallPrecision(int placesCount) {
            recall = new DenseVector(placesCount);
            precision = new DenseVector(placesCount);
        }

        private void set(int placeNum, double recallValue, double precisionValue) {
            recall.setQuick(placeNum, recallValue);
            precision.setQuick(placeNum, precisionValue);
        }
    }
}
