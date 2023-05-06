package com.sergeev.srp.common.math;

public class Matrix {


    double[][] val; // The local variable storing Matrix entries

    public Matrix(int m, int n) {
        // Constructor

        if ((m < 1) || (n < 1)) {
            System.out.println("Cannot create a Matrix smaller that 1x1");
            System.exit(0);
        }

        val = new double[m][n];
    }

    public int rows() {
        // Return the number of rows in the Matrix.

        return val.length;
    }

    public int cols() {
        // Return the number of columns in the Matrix.

        return val[0].length;
    }

    public void set(int m, int n, double v) {
        // Set value at row m and column n to be v.

        if ((m < 1) || (m > rows()) || (n < 1) || (n > cols())) {
            System.out.println("Elements outside the Matrix cannot be assigned values");

            return;
        }

        val[m - 1][n - 1] = v;
    }

    public double get(int m, int n) {
        // Return the Matrix value at row m and column n.

        if ((m < 1) || (m > rows()) || (n < 1) || (n > cols())) {
            System.out.println("Elements outside the Matrix cannot be accessed");
            return Double.NaN;
        }

        return val[m - 1][n - 1];
    }

    public Matrix transpose() {
        // Return a copy of the Matrix where row and column values
        // have been interchanged.

        Matrix M = new Matrix(cols(), rows());

        for (int i = 1; i <= cols(); i++) {
            for (int j = 1; j <= rows(); j++) {
                M.set(i, j, get(j, i));
            }
        }

        return M;
    }

    public void println() {
        // Pretty-print the Matrix to stdout.

        double v;

        for (int i = 1; i <= rows(); i++) {
            System.out.format("[");
            for (int j = 1; j <= cols(); j++) {
                if (j > 1)
                    System.out.format(" ");
                v = get(i, j);
                System.out.format("%f", v);
            }
            System.out.format("]\n");
        }
    }

    public Matrix add(Matrix B) {
        // Return a new Matrix which is the element-wise addition of
        // this with other Matrix B.  The two matrices must be of the
        // same size.

        if ((rows() != B.rows()) || (cols() != B.cols())) {
            System.out.println("Error, cannot add since the 2 matrices do not have the same dimensions.");
            System.exit(0);
        }

        Matrix M = new Matrix(rows(), cols());
        double v;

        for (int i = 1; i <= rows(); i++) {
            for (int j = 1; j <= cols(); j++) {
                v = get(i, j);
                M.set(i, j, v + B.get(i, j));
            }
        }

        return M;
    }

    public Matrix add(double v) {
        // Return a new Matrix which is the element-wise addition of
        // this with v.

        Matrix M = new Matrix(rows(), cols());

        for (int i = 1; i <= rows(); i++) {
            for (int j = 1; j <= cols(); j++) {
                M.set(i, j, v + get(i, j));
            }
        }

        return M;
    }

    public Matrix mul(double v) {
        // Return a new Matrix which is the element-wise
        // multiplication of this with v.

        Matrix M = new Matrix(rows(), cols());

        for (int i = 1; i <= rows(); i++) {
            for (int j = 1; j <= cols(); j++) {
                M.set(i, j, v * get(i, j));
            }
        }

        return M;
    }

    public void setIdentity() {
        // Change values to be the identity Matrix.

        for (int i = 1; i <= Math.min(rows(), cols()); i++) {
            for (int j = 1; j <= Math.min(rows(), cols()); j++) {
                if (i == j)
                    set(i, j, 1);
                else
                    set(i, j, 0);
            }
        }
    }

    public void setConstant(double v) {
        // Change values to be constant value v.

        for (int i = 1; i <= rows(); i++) {
            for (int j = 1; j <= cols(); j++) {
                set(i, j, v);
            }
        }
    }

    public Matrix concatenateRows(Matrix B) {
        // Return a new Matrix which is the concatenation of this with
        // B, i.e. [this | B].

        if (rows() != B.rows()) {
            System.out.println("Error, the matrices must have the same number of rows to be concatenated.");
            System.exit(0);
        }

        Matrix M = new Matrix(rows(), cols() + B.cols());

        for (int i = 1; i <= M.rows(); i++) {
            for (int j = 1; j <= cols(); j++)
                M.set(i, j, get(i, j));
            for (int j = 1; j <= B.cols(); j++)
                M.set(i, j + cols(), B.get(i, j));
        }

        return M;
    }

    public Matrix subMatrix(int from_row, int from_col, int to_row, int to_col) {
        // Return a new Matrix whos elements are copied from this
        // Matrix from rows from_row .. to_row and columns from_col
        // .. to_col.

        if ((from_row < 1) || (from_row > rows()) || (from_col < 1) || (from_col > cols()) || (to_row < from_row) || (to_row > rows()) || (to_col < from_col) || (to_col > cols())) {
            System.out.println("Error, cannot take a submatrix of elements outside the original Matrix.");
            System.exit(0);
        }

        int ROWS = to_row - from_row + 1;
        int COLS = to_col - from_col + 1;
        Matrix M = new Matrix(ROWS, COLS);

        for (int i = 1; i <= ROWS; i++)
            for (int j = 1; j <= COLS; j++) {
                M.set(i, j, get(i + from_row - 1, j + from_col - 1));
            }
        return M;
    }

    public Matrix swapRows(int a, int b) {
        // Return a new Matrix, where rows a and b are interchanged

        if ((a < 1) || (a > rows()) || (b < 1) || (b > rows())) {
            System.out.println("Error, cannot swap rows outside the original Matrix.");
            System.exit(0);
        }

        Matrix M = add(0);

        if (a != b) {
            for (int i = 1; i <= rows(); i++) {
                if (i == a) {
                    for (int j = 1; j <= cols(); j++) {
                        M.set(b, j, get(i, j));
                    }
                } else if (i == b) {
                    for (int j = 1; j <= cols(); j++) {
                        M.set(a, j, get(i, j));
                    }
                } else {
                    for (int j = 1; j <= cols(); j++) {
                        M.set(i, j, get(i, j));
                    }
                }
            }
        }

        return M;
    }

    public Matrix addMulRows(int a, int b, int c, double v) {
        // Return a new Matrix, where row a has been replaced by the
        // sum of row b plus v times row c.

        if ((a < 1) || (a > rows()) || (b < 1) || (b > rows()) || (c < 1) || (c > rows())) {
            System.out.println("Error, reference to non-existing rows.");
            System.exit(0);
        }

        Matrix M = add(0);

        for (int i = 1; i <= rows(); i++) {
            for (int j = 1; j <= cols(); j++) {
                if (i == a) {
                    M.set(a, j, get(b, j) + v * get(c, j));
                } else {
                    M.set(i, j, get(i, j));
                }
            }
        }

        return M;
    }

    public Matrix deleteCol(int a) {
        // Return a new Matrix as a copy of this not including the
        // i'th col.
        if ((a < 1) || (a > cols())) {
            System.out.println("Error, cannot remove a row outside the original Matrix.");
            System.exit(0);
        }

        Matrix M;

        if (a == 1) {
            M = subMatrix(1, 2, rows(), cols());
        } else if (a == cols()) {
            M = subMatrix(1, 1, rows(), cols() - 1);
        } else {
            M = subMatrix(1, 1, rows(), a - 1);
            M = M.concatenateRows(subMatrix(1, a + 1, rows(), cols()));
        }

        return M;
    }

    public Matrix replaceCol(int a, Matrix b) {
        // Return a new Matrix as a copy of this where column a is replaced with b.
        if ((a < 1) || (a > cols())) {
            System.out.println("Error, cannot replace a column outside the original Matrix.");
            System.exit(0);
        }
        if ((b.rows() != rows()) || (b.cols() != 1)) {
            System.out.println("Error, replacement column must be nx1, where n is the number of rows in this Matrix.");
            System.exit(0);
        }

        Matrix M = add(0);

        for (int i = 1; i <= rows(); i++) {
            M.set(i, a, b.get(i, 1));
        }
        return M;
    }


    //////////////////////////////////////////////////////////////////
    // Solution to project A
    //////////////////////////////////////////////////////////////////

    public Matrix mul(Matrix B) {
        // Return a new Matrix which is the Matrix product of this
        // with B.
        Matrix M = new Matrix(rows(), B.cols());
        double v, w;

        if (cols() != B.rows()) {
            System.out.println("Error, cannot multiply since left Matrix does not have same number of cols as right has rows.");
            System.exit(0);
        }

        for (int i = 1; i <= rows(); i++) {
            for (int j = 1; j <= B.cols(); j++) {
                double s = 0;
                for (int k = 1; k <= cols(); k++) {
                    v = get(i, k);
                    w = B.get(k, j);
                    s += v * w;
                }
                M.set(i, j, s);
            }
        }
        return M;
    }


    //////////////////////////////////////////////////////////////////
    // Solution to project B
    //////////////////////////////////////////////////////////////////

    public Matrix Gauss() {
        // Return a new Matrix on echelon form which is equivalent to
        // this.
        Matrix M = add(0);
        double v, w, u;
        int i, j;

        for (j = 1; j < Math.min(rows(), cols()); j++) {
            i = j;
            do {
                v = M.get(i, j);
                i = i + 1;
            } while ((i <= rows()) && (v == 0));

            if (i <= rows()) {
                // we found a nonzero element, so we swap and reduce
                M = M.swapRows(j, i);
                v = M.get(j, j);
                for (i = j + 1; i <= rows(); i++) {
                    u = M.get(i, j);
                    M = M.addMulRows(i, i, j, -u / v);
                }
            }
        }

        return M;
    }

    public Matrix GaussJordan() {
        // Return a new Matrix on reduced row echelon form which is
        // equivalent to this.
        Matrix M = Gauss();
        double v, w, u;
        int i, j;

        // Check for zero in the diagonal
        for (j = 1; j <= rows(); j++) {
            v = M.get(j, j);
            if (v == 0) {
                System.out.println("Error, the Matrix is singular.");
                System.exit(0);
            }
        }

        for (j = rows(); j > 0; j--) {
            // We normalize the jth row
            v = M.get(j, j);
            M = M.addMulRows(j, j, j, 1 / v - 1);
        }

        for (j = rows(); j > 1; j--) {
            // we eliminate the variable in the rows above j
            v = M.get(j, j);
            for (i = j - 1; i > 0; i--) {
                u = M.get(i, j);
                M = M.addMulRows(i, i, j, -u / v);
            }
        }

        return M;
    }


}