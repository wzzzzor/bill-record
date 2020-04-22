package com.wzzzzor.billrecord.utils;

import java.util.Arrays;

/**
 * 自定义一个稀疏数组
 * @author hanzheng.wu
 *
 */
public class SparseArray {
	
	public static void pringList(int arr[][]) {
		for(int i =0; i < arr.length; i++) {
			System.out.println(arr[i][0]+ "    " + arr[i][1] + "    " + arr[i][2]);
		}
	}

	/**
	 * 原始数组转为稀疏数组
	 * @param chessArr
	 * @return
	 */
	public static int[][]  asSparseArr(int chessArr[][]){
		int row = chessArr.length;
		int col = chessArr[0].length;
		//有效数字
		int valid = 0;
		for(int i =0; i< row; i++) {
			for(int j = 0; j < col; j++) {
				if(chessArr[i][j] != 0) {
					valid++;
				}
			}
		}
		
		int sparseArr[][] = new int[valid+1][3];
		sparseArr[0][0] = row;
		sparseArr[0][1] = col;
		sparseArr[0][2] = valid;
		int count = 0;
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				if(chessArr[i][j] != 0) {
					count++;
					sparseArr[count][0] = i;
					sparseArr[count][1] = j;
					sparseArr[count][2] = chessArr[i][j];
				}
			}
		}
		
		return sparseArr;
	}
	
	public static void main(String[] args) {
		int arr[][] = new int[11][11];
		arr[0][1] = 1;
		arr[5][1] = 2;
		int sparseArr[][] = SparseArray.asSparseArr(arr);
		SparseArray.pringList(sparseArr);
		
		
		SparseArray.listChessArr(SparseArray.asChessArray(sparseArr));
	}
	
	/**
	 * 稀疏数组转换为原始数组
	 * @return
	 */
	public static int[][] asChessArray(int sparseArray[][]){
		int row = sparseArray[0][0];
		int col = sparseArray[0][1];
		int valid = sparseArray[0][2];
		
		int chessArr[][] = new int[row][col];
		for(int i = 1; i < sparseArray.length; i++) {
			chessArr[sparseArray[i][0]][sparseArray[i][1]] = sparseArray[i][2];
		}
		return chessArr;
	}
	
	public static void listChessArr(int chessArr[][]) {
		Arrays.stream(chessArr).forEach(x -> {
			Arrays.stream(x).forEach(System.out::print);
			System.out.println();
		});
	}
}
