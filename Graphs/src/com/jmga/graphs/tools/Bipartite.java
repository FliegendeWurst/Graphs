package com.jmga.graphs.tools;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.jmga.graphs.classes.Graph;
import com.jmga.graphs.classes.Link;
import com.jmga.graphs.classes.Node;

import android.util.Log;

public class Bipartite {

	private static final String TAG = "Bipartite";	
	private Graph g;
	private Hashtable<String,Integer> visited;
	private Hashtable<String,Integer> temp;
	private ArrayList<Node> A;
	private ArrayList<Node> B;
	
	public Bipartite(Graph g_){
		g = g_;
		visited = new Hashtable<String,Integer>();
		temp = new Hashtable<String,Integer>();
		A = new ArrayList<Node>();
		B = new ArrayList<Node>();
	}
	
	public Hashtable<String,Integer> getSubSet(){
		return visited;
	}
	
	public boolean execute() throws Exception{		
		// Se inicializan los conjuntos
		String start = "";
		start = g.getId(0);
		visited.put(start, 1);
		A.add(g.getNode(start));
		Node n = new Node();
		n = (Node)g.getNode(start);
		for(Link lk : n.getEnlaces()){
			temp.put(lk.getIdf(), 2);
			B.add(g.getNode(lk.getIdf()));
		}

		// Recorrido en amplitud del grafo: Breadth First Search (BFS)
		Enumeration<String> ks = temp.keys();
		int size = temp.size();
		while(ks.hasMoreElements()){
			String key = (String)ks.nextElement();
			visited.put(key, temp.get(key));
			Node n_ = (Node)g.getNode(key);
			for(Link lk : n_.getEnlaces()){
				if(!visited.containsKey(lk.getIdf())){
					temp.put(lk.getIdf(), (temp.get(key) == 1)?2:1);
					if(temp.get(lk.getIdf()) == 1)
						A.add(g.getNode(lk.getIdf()));
					else
						B.add(g.getNode(lk.getIdf()));
				}
			}
			temp.remove(key);
			size--;
			if(size == 0){
				ks = temp.keys();
				size = temp.size();
			}
		}
		
		Enumeration<String> k = visited.keys();
		while(k.hasMoreElements()){
			String key = k.nextElement();
			Log.d(TAG, key + ": " + visited.get(key));
		}
		
		// Comprobando que tanto los vertices del subconjunto A pertenecientes a V, 
		// como los vertices del subconjunto B pertenecientes a V, son disjuntos
		// en ambos casos (G=(V,A))
		Iterator<Node> na_ = A.iterator();
		while(na_.hasNext()){
			Node na = na_.next();
			Log.d(TAG, "(A) Actual: " + na.getId());
			Iterator<Node> na__ = A.iterator();
			while(na__.hasNext()){
				Node n_ = na__.next();
				Log.d(TAG, "(A) Estudiando: " + n_.getId());
				if(!na.getId().equals(n_.getId()))
					for(Link lk : na.getEnlaces()){
						Log.d(TAG, "(A) Comparando: " + n_.getId() + " - " + lk.getIdf());
						if(n_.getId().equals(lk.getIdf())){
							return false;
						}
					}
			}
		}
		Iterator<Node> nb_ = B.iterator();
		while(nb_.hasNext()){
			Node nb = nb_.next();
			Log.d(TAG, "(B) Actual: " + nb.getId());
			Iterator<Node> nb__ = B.iterator();
			while(nb__.hasNext()){
				Node n_ = nb__.next();
				Log.d(TAG, "(A) Estudiando: " + n_.getId());
				if(!nb.getId().equals(n_.getId()))
					for(Link lk : nb.getEnlaces()){
						Log.d(TAG, "(B) Comparando: " + n_.getId() + " - " + lk.getIdf());
						if(n_.getId().equals(lk.getIdf())){
							return false;
						}
					}
			}
		}
		
		return true;
	}
	
}
