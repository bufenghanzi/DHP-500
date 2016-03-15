package com.mingseal.utils;

import java.util.BitSet;

/**
 * <p>Title: BitSetConvert
 * <p>Description: BitSet与byte数组互转
 * <p>Company: MingSeal .Ltd
 * @author lyq
 * @date 2015年10月29日
 */
public class BitSetConvert{
	
	/**
	 * <p>Title: bitSet2ByteArray
	 * <p>Description: BitSet转Byte数组
	 * @param bitSet
	 * @return
	 */
	public static byte[] bitSet2ByteArray(BitSet bitSet){
		byte[] bytes = new byte[bitSet.size() / 8];
		for(int i = 0; i < bitSet.size(); i++){
			int index = i / 8;
			int offset = 7 - i % 8;
			bytes[index] |= (bitSet.get(i) ? 1 : 0) << offset;
		}
		return bytes;
	}
	
	/**
	 * <p>Title: byteArray2BitSet
	 * <p>Description: Byte数组转BitSet
	 * @param bytes
	 * @return
	 */
	public static BitSet byteArray2BitSet(byte[] bytes){
		BitSet bitSet = new BitSet(bytes.length * 8);
		int index = 0;
		for(int i = 0; i < bytes.length; i++){
			for(int j = 7; j >= 0; j--){
				bitSet.set(index++, (bytes[i] & (1 << j)) >> j == 1 ? true : false);
			}
		}
		return bitSet;
	}
}
