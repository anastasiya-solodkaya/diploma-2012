package aiaudio.system.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public abstract class VectorLoader<V extends Vector> {

    public static final DenseVectorLoader denseVectorLoader = new DenseVectorLoader();
    public static final RASVectorLoader rasVectorLoader = new RASVectorLoader();

    public V readFromFile(String filePath) throws IOException, TasteException {
        File ratingsFile = new File(filePath);
        DataModel model = new FileDataModel(ratingsFile);
        int vectorSize = (int) findMax(model.getItemIDs()) + 1;
        System.out.println("vectorSize = " + vectorSize);
        V resultVector = createVector(vectorSize);
        LongPrimitiveIterator userIDs = model.getUserIDs();
        while (userIDs.hasNext()) {
            int userID = userIDs.next().intValue();
            LongPrimitiveIterator iterator = model.getItemIDsFromUser(userID).iterator();
            while (iterator.hasNext()) {
                int itemID = (int) iterator.nextLong();
                resultVector.setQuick(itemID, userID);
            }
        }

        return resultVector;

    }

    private long findMax(LongPrimitiveIterator userIDs) {
        long maxUser = 0;
        while (userIDs.hasNext()) {
            long nextLong = userIDs.nextLong();
            if (maxUser < nextLong) {
                maxUser = nextLong;
            }
        }
        return maxUser;
    }

    public void writeToFile(V vector, String filePath, boolean skipZeroes) {
        if (vector == null) {
            return;
        }
        BufferedWriter w = null;
        try {
            w = write(vector, filePath, skipZeroes);
        } catch (IOException ex) {
            Logger.getLogger(MatrixDataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void writeToFile(V vector, String filePath) {
        writeToFile(vector, filePath, false);
    }

    private BufferedWriter write(V vector, String filePath, boolean skipZeroes) throws IOException {
        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter(filePath));
        int rowSize = vector.size();
//        String mathX = "[";
//        String mathY = "[";
        for (int row = 0; row < rowSize; row++) {
            double value = vector.get(row);
            if (value == 0.0 && skipZeroes) {
                continue;
            }
            String line = row + " " + value;
            writer.write(line);
            writer.newLine();
//            mathX += row + " ";
//            mathY += value + " ";
            writer.newLine();
        }
        writer.flush();
//        mathX += "]";
//        mathY += "]";
//        System.out.println(mathX);
//        System.out.println(mathY);
        return writer;

    }

    protected abstract V createVector(int numItems);

    public static class DenseVectorLoader extends VectorLoader<DenseVector> {

        @Override
        protected DenseVector createVector(int numItems) {
            return new DenseVector(numItems);
        }
    }

    public static class RASVectorLoader extends VectorLoader<RandomAccessSparseVector> {

        @Override
        protected RandomAccessSparseVector createVector(int numItems) {
            return new RandomAccessSparseVector(numItems);
        }
    }
}
