/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aiaudio.system.util;

import aiaudio.system.algorithms.realizations.preprocessing.remap.ArtistUserDictionary;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.math.*;

/**
 *
 * @author Anastasiya
 */
public abstract class MatrixDataLoader<M extends AbstractMatrix> {

    protected MatrixDataLoader() {
    }
    public static final ColumnMatrixDataLoader columnMatrixDataLoader = new ColumnMatrixDataLoader();
    public static final RowMatrixDataLoader rowMatrixDataLoader = new RowMatrixDataLoader();
    public static final DenseMatrixDataLoader denseMatrixDataLoader = new DenseMatrixDataLoader();

    public M readFromFile(String filePath, ArtistUserDictionary dictionary) throws IOException, TasteException {
        File ratingsFile = new File(filePath);
        DataModel model = new FileDataModel(ratingsFile);
        M resultMatrix = createMatrix(model.getNumItems(), model.getNumUsers());
        LongPrimitiveIterator userIDs = model.getUserIDs();
        while (userIDs.hasNext()) {
            int userID = userIDs.next().intValue();
            FastIDSet itemIDsFromUser1 = model.getItemIDsFromUser(userID);
            LongPrimitiveIterator iterator = itemIDsFromUser1.iterator();
            while (iterator.hasNext()) {
                int itemID = (int) iterator.nextLong();
                double preferenceValue = model.getPreferenceValue(userID, itemID).doubleValue();
                resultMatrix.setQuick(dictionary.getAndAddIfNotPresentsArtist(itemID), dictionary.getAndAddIfNotPresentsUsers(userID), preferenceValue);
            }
        }

        return resultMatrix;

    }

    public M readFromFile(String filePath) throws IOException, TasteException {
        File ratingsFile = new File(filePath);
        DataModel model = new FileDataModel(ratingsFile);
        long maxUser = findMax(model.getUserIDs());
        long maxItem = findMax(model.getItemIDs());
        
        M resultMatrix = createMatrix((int)maxItem + 1, (int)maxUser + 1);
        LongPrimitiveIterator userIDs = model.getUserIDs();
        while (userIDs.hasNext()) {
            int userID = userIDs.next().intValue();
            FastIDSet itemIDsFromUser1 = model.getItemIDsFromUser(userID);
            LongPrimitiveIterator iterator = itemIDsFromUser1.iterator();
            while (iterator.hasNext()) {
                int itemID = (int) iterator.nextLong();
                double preferenceValue = model.getPreferenceValue(userID, itemID).doubleValue();
                resultMatrix.setQuick(itemID, userID, preferenceValue);
            }
        }

        return resultMatrix;

    }

    private long findMax(LongPrimitiveIterator userIDs) {
        long maxUser = 0;
        while(userIDs.hasNext()){
            long nextLong = userIDs.nextLong();
            if(maxUser < nextLong){
                maxUser = nextLong;
            }
        }
        return maxUser;
    }
    
    public M readFromFile(String filePath, int artistCount, int usersCount) throws IOException, TasteException {
        File ratingsFile = new File(filePath);
        DataModel model = new FileDataModel(ratingsFile);
        M resultMatrix = createMatrix(artistCount, usersCount);
        LongPrimitiveIterator userIDs = model.getUserIDs();
        while (userIDs.hasNext()) {
            int userID = userIDs.next().intValue();
            FastIDSet itemIDsFromUser1 = model.getItemIDsFromUser(userID);
            LongPrimitiveIterator iterator = itemIDsFromUser1.iterator();
            while (iterator.hasNext()) {
                int itemID = (int) iterator.nextLong();
                double preferenceValue = model.getPreferenceValue(userID, itemID).doubleValue();
                resultMatrix.setQuick(itemID, userID, preferenceValue);
            }
        }

        return resultMatrix;
    }

    public void writeToFile(M matrix, String filePath, boolean skipZeroRatings) {
        if (matrix == null) {
            return;
        }
        BufferedWriter w = null;
        try {
            w = write(matrix, filePath, skipZeroRatings);
        } catch (IOException ex) {
            Logger.getLogger(MatrixDataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void writeToFile(M matrix, String filePath) {
        writeToFile(matrix, filePath, true);
    }

    private BufferedWriter write(M matrix, String filePath, boolean skipZeroRatings) throws IOException {
        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter(filePath));
        int rowSize = freeMotionSize(matrix);
        for (int freeMotionDirection = 0; freeMotionDirection < rowSize; freeMotionDirection++) {
            Vector vector = getVectorAt(matrix, freeMotionDirection);
            saveRow(writer, vector, freeMotionDirection, skipZeroRatings);
        }
        writer.flush();
        return writer;

    }

    private void saveRow(BufferedWriter writer, Vector vector, int freeMotionDirection, boolean skipZeroRatings) throws IOException {
        for (int i = 0; i < vector.size(); i++) {
            double value = vector.getQuick(i);
            if (value == 0.0 && skipZeroRatings) {
                continue;
            }
            String line = combine(freeMotionDirection, i, value);
            writer.write(line);
            writer.newLine();
        }
    }

    protected abstract String combine(int freeMotionDirection, int i, double value);

    protected abstract M createMatrix(int numItems, int numUsers);

    protected abstract Vector getVectorAt(M matrix, int freeMotionDirection0);

    protected abstract int freeMotionSize(M matrix);

    public static class RowMatrixDataLoader extends MatrixDataLoader<SparseRowMatrix> {

        RowMatrixDataLoader() {
        }

        @Override
        protected String combine(int freeMotionDirection, int i, double value) {
            return i + "\t" + freeMotionDirection + "\t" + value;
        }

        @Override
        protected SparseRowMatrix createMatrix(int numItems, int numUsers) {
            return new SparseRowMatrix(numItems, numUsers, true);
        }

        @Override
        protected Vector getVectorAt(SparseRowMatrix matrix, int freeMotionDirection) {
            return matrix.viewRow(freeMotionDirection);
        }

        @Override
        protected int freeMotionSize(SparseRowMatrix matrix) {
            return matrix.rowSize();
        }
    }

    public static class ColumnMatrixDataLoader extends MatrixDataLoader<SparseColumnMatrix> {

        ColumnMatrixDataLoader() {
        }

        @Override
        protected String combine(int freeMotionDirection, int i, double value) {
            return freeMotionDirection + "\t" + i + "\t" + value;
        }

        @Override
        protected SparseColumnMatrix createMatrix(int numItems, int numUsers) {
            return new SparseColumnMatrix(numItems, numUsers);
        }

        @Override
        protected Vector getVectorAt(SparseColumnMatrix matrix, int freeMotionDirection) {
            return matrix.viewColumn(freeMotionDirection);

        }

        @Override
        protected int freeMotionSize(SparseColumnMatrix matrix) {
            return matrix.columnSize();
        }
    }
    public static class DenseMatrixDataLoader extends MatrixDataLoader<DenseMatrix> {

        DenseMatrixDataLoader() {
        }

        @Override
        protected String combine(int freeMotionDirection, int i, double value) {
            return i + "\t" + freeMotionDirection + "\t" + value;
        }

        @Override
        protected DenseMatrix createMatrix(int numItems, int numUsers) {
            return new DenseMatrix(numItems, numUsers);
        }

        @Override
        protected Vector getVectorAt(DenseMatrix matrix, int freeMotionDirection) {
            return matrix.viewRow(freeMotionDirection);

        }

        @Override
        protected int freeMotionSize(DenseMatrix matrix) {
            return matrix.rowSize();
        }
    }
}
