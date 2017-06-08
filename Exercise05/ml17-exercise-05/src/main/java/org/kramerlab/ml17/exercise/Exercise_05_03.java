package org.kramerlab.ml17.exercise;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Exercise_05_03 {
    //Matrix class to implement matrix calculation
    private static class Matrix {
        private final int rows;
        private final int cols;
        private final double[][] values;

        //boa constructor
        public Matrix(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            values = new double[rows][cols];
        }
        //setter
        public void set(int r, int c, double d) {
            values[r][c] = d;
        }
        //transposition function
        public Matrix transpose() {
            Matrix A = new Matrix(cols, rows);
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    A.values[j][i] = this.values[i][j];
            return A;
        }
        //matrix multiplication
        public Matrix multiply(Matrix B) {
            Matrix A = this;
            Matrix res = new Matrix(A.rows, B.cols);
            for (int i = 0; i < res.rows; i++)
                for (int j = 0; j < res.cols; j++)
                    for (int k = 0; k < A.cols; k++)
                        res.values[i][j] += (A.values[i][k] * B.values[k][j]);
            return res;
        }
        //getter
        public int getRows() {
            return rows;
        }
        public int getCols() {
            return cols;
        }
        public double get(int r, int c) {
            return values[r][c];
        }

        //inverses matrix by A^-1 = 1/det(A) * adj(A)
        public Matrix invert() {
            Matrix res = this.adj();
            double det = 1/this.det();
            for (int i=0;i<rows;i++) {
                for (int j=0;j<cols;j++) {
                    res.set(i,j,res.get(i,j)*det);
                }
            }
            return res;
        }
        //calculates determinante for a matrix via laplace
        public double det() {
            if (this.rows == 2 && this.cols ==2) {
                return values[0][0]*values[1][1] - values[0][1]*values[1][0];
            }
            Matrix tmp = new Matrix(rows-1, cols-1);
            double det=0;
            double factor=0;
            for (int a=0; a < cols; a++) {
                for (int i = 1; i < rows;i++) {
                    for (int j=0;j<cols;j++){
                        if (a > j) {
                            tmp.set(i-1,j,this.get(i,j));
                        } else if (a == j) {

                        } else {
                            tmp.set(i-1,j-1,this.get(i,j));
                        }
                    }
                }
                factor = Math.pow(-1,a)*this.get(0,a);
                det += tmp.det()*factor;
            }
            return det;
        }
        //builds the adj by cof(A)^T P.S.: TYUUUUUUKIIIIIIN!1!!111!111!!
        public Matrix adj() {
            Matrix cof = new Matrix(rows,cols);
            for (int i=0;i<rows;i++) {
                for (int j=0;j<cols;j++) {
                    Matrix tmp = new Matrix(rows-1,cols-1);
                    for (int k=0; k < rows; k++) {
                        for (int l=0;l < cols;l++) {
                            // i = 2, j = 1
                            if (k > i && l > j) {
                                tmp.set(k-1,l-1,this.get(k,l));
                            } else if (k > i && l < j) {
                                tmp.set(k-1,l,this.get(k,l));
                            } else if (k < i && l > j) {
                                tmp.set(k,l-1,this.get(k,l));
                            } else if (k < i && l < j) {
                                tmp.set(k,l,this.get(k,l));
                            } else {

                            }
                        }
                    }
                    cof.set(i,j,Math.pow(-1,(i+1+j+1))*tmp.det());
                }
            }
            return cof.transpose();
        }

    }
    //calculates w based on the formula from the lecture (with constant term & glorious hardcoded path)
    //no we don't have exception for invalid formats etc. but the csv was well enough made for it not to be necessary to make 45 mins of exceptions
    public static void main(String[] args) throws IOException {
        Matrix neo = new Matrix(19,5);
        Matrix mm = new Matrix(19,1);
        File file = new File("D:\\Kesveros\\Desktop\\#moodlekurse\\ML\\ml17-exercise-05\\ml17-exercise-05\\datasets\\cattle.csv");
        FileReader read = new FileReader(file);
        BufferedReader bff = new BufferedReader(read);
        bff.readLine();
        //reads data and creates the matrixikizies X and Y (neo and mm)
        for (int i=0;i<19;i++) {
            String tmp = bff.readLine();
            String[] data = tmp.split(",");
            for (int j=0;j<4;j++) {
                neo.set(i,j+1,Double.parseDouble(data[j]));
            }
            neo.set(i,0,1);
            mm.set(i,0,Double.parseDouble(data[4]));
        }
        //uses secret krabby patty formula from lecture
        Matrix neoT = neo.transpose();
        Matrix w = neoT.multiply(neo);
        w = w.invert();
        w = w.multiply(neoT.multiply(mm));
        for (int i = 0;i<5;i++) {
            System.out.print(w.get(i,0)+",");
        }
    }
}
