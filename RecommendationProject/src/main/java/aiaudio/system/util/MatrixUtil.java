package aiaudio.system.util;

import org.apache.mahout.math.SparseColumnMatrix;
import org.apache.mahout.math.SparseRowMatrix;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Anastasiya
 */
public class MatrixUtil {
    public static SparseRowMatrix convert(SparseColumnMatrix matrix){
        SparseRowMatrix m = new SparseRowMatrix(matrix.rowSize(), matrix.columnSize());
        for (int columnNum = 0; columnNum < matrix.columnSize(); columnNum++) {
            Vector column = matrix.viewColumn(columnNum);
            for (int rowNum = 0; rowNum < matrix.rowSize(); rowNum++) {
                m.setQuick(rowNum, columnNum, column.getQuick(rowNum));
            }
        }
        
        return m;
    }
    public static SparseColumnMatrix convert(SparseRowMatrix matrix){
        SparseColumnMatrix m = new SparseColumnMatrix(matrix.rowSize(), matrix.columnSize());
        for (int rowNum = 0; rowNum < matrix.rowSize(); rowNum++) {
            Vector row = matrix.viewRow(rowNum);
            for (int colNum = 0; colNum < matrix.columnSize(); colNum++) {
                m.setQuick(rowNum, colNum, row.getQuick(colNum));
            }
        }
        
        return m;
    }
}
