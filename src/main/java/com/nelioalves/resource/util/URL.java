package com.nelioalves.resource.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URL {

	public static List<Integer> decodificarIntList(String s) {
/*		String[] vetor = s.split(",");
		List<Integer> lista = new ArrayList<>();
		
		for (int i = 0; i < vetor.length; i++) {
			lista.add(Integer.parseInt(vetor[i]));
		}
		
		return lista;
*/		
		//Java 8
		return Arrays.asList(s.split(",")).stream().map(l -> Integer.parseInt(l)).collect(Collectors.toList());
	}
}
