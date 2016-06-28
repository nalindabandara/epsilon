package com.epsilon.Leak.Hawk.weka;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class WekaTestManager {

	public static void main(String[] args) {
		
		WekaTestManager manager = new WekaTestManager();
		manager.generateArrfFile();
		//NaiveBayesUpdateable nbClassifire = manager.buildIncrementalClassifier();
		
		J48 tree = manager.buildBatchClassifier();
		//manager.classfyInstances();
	}

	
	private void classfyInstances( NaiveBayesUpdateable nbClassifire ){
		
		 try {
			// load unlabeled data
			 Instances unlabeled = new Instances(
			                         new BufferedReader(
			                           new FileReader("/some/where/unlabeled.arff")));
			 
			 // set class attribute
			 unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
			 
			 // create copy
			 Instances labeled = new Instances(unlabeled);
			 
			 // label instances
			 for (int i = 0; i < unlabeled.numInstances(); i++) {
			   double clsLabel = nbClassifire.classifyInstance(unlabeled.instance(i));
			   labeled.instance(i).setClassValue(clsLabel);
			 }
			 // save labeled data
			 BufferedWriter writer = new BufferedWriter(new FileWriter("labeled.arff"));
			 writer.write(labeled.toString());
			 writer.newLine();
			 writer.flush();
			 writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void generateArrfFile(){
		
		 FastVector      atts;
	     FastVector      attsRel;
	     FastVector      attVals;
	     FastVector      attValsRel;
	     Instances       data;
	     Instances       dataRel;
	     double[]        vals;
	     double[]        valsRel;
	     int             i;
	 
	     // 1. set up attributes
	     atts = new FastVector();
	     // - numeric
	     atts.addElement(new Attribute("att1"));
	     // - nominal
	     attVals = new FastVector();
	     for (i = 0; i < 5; i++)
	       attVals.addElement("val" + (i+1));
	     atts.addElement(new Attribute("att2", attVals));
	     // - string
	     atts.addElement(new Attribute("att3", (FastVector) null));
	     // - date
	     atts.addElement(new Attribute("att4", "yyyy-MM-dd"));
	     // - relational
	     attsRel = new FastVector();
	     // -- numeric
	     attsRel.addElement(new Attribute("att5.1"));
	     // -- nominal
	     attValsRel = new FastVector();
	     for (i = 0; i < 5; i++)
	       attValsRel.addElement("val5." + (i+1));
	     attsRel.addElement(new Attribute("att5.2", attValsRel));
	     dataRel = new Instances("att5", attsRel, 0);
	     atts.addElement(new Attribute("att5", dataRel, 0));
	 
	     // 2. create Instances object
	     data = new Instances("MyRelation", atts, 0);
	 
	     // 3. fill with data
	     // first instance
	     vals = new double[data.numAttributes()];
	     // - numeric
	     vals[0] = Math.PI;
	     // - nominal
	     vals[1] = attVals.indexOf("val3");
	     // - string
	     vals[2] = data.attribute(2).addStringValue("This is a string!");
	     // - date
	     try {
			vals[3] = data.attribute(3).parseDate("2001-11-09");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	     // - relational
	     dataRel = new Instances(data.attribute(4).relation(), 0);
	     // -- first instance
	     valsRel = new double[2];
	     valsRel[0] = Math.PI + 1;
	     valsRel[1] = attValsRel.indexOf("val5.3");
	     dataRel.add(new DenseInstance(1.0, valsRel));
	     // -- second instance
	     valsRel = new double[2];
	     valsRel[0] = Math.PI + 2;
	     valsRel[1] = attValsRel.indexOf("val5.2");
	     dataRel.add(new DenseInstance(1.0, valsRel));
	     vals[4] = data.attribute(4).addRelation(dataRel);
	     // add
	     data.add(new DenseInstance(1.0, vals));
	 
	     // second instance
	     vals = new double[data.numAttributes()];  // important: needs NEW array!
	     // - numeric
	     vals[0] = Math.E;
	     // - nominal
	     vals[1] = attVals.indexOf("val1");
	     // - string
	     vals[2] = data.attribute(2).addStringValue("And another one!");
	     // - date
	     try {
			vals[3] = data.attribute(3).parseDate("2000-12-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     // - relational
	     dataRel = new Instances(data.attribute(4).relation(), 0);
	     // -- first instance
	     valsRel = new double[2];
	     valsRel[0] = Math.E + 1;
	     valsRel[1] = attValsRel.indexOf("val5.4");
	     dataRel.add(new DenseInstance(1.0, valsRel));
	     // -- second instance
	     valsRel = new double[2];
	     valsRel[0] = Math.E + 2;
	     valsRel[1] = attValsRel.indexOf("val5.1");
	     dataRel.add(new DenseInstance(1.0, valsRel));
	     vals[4] = data.attribute(4).addRelation(dataRel);
	     // add
	     data.add(new DenseInstance(1.0, vals));
	 
	     // 4. output data
	     System.out.println(data);
	     
	     // save labeled data
	     BufferedWriter writer = null;
		 try {
			 writer = new BufferedWriter(new FileWriter("data.arff"));
			 writer.write(data.toString());
			 writer.newLine();
			 writer.flush();
			 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private NaiveBayesUpdateable buildIncrementalClassifier(){
		
		NaiveBayesUpdateable nb = null;
		try {
			// load data
			 ArffLoader loader = new ArffLoader();
			 loader.setFile(new File("data.arff"));
			 Instances structure = loader.getStructure();
			 structure.setClassIndex(structure.numAttributes() - 1);
			 
			 // train NaiveBayes
			 nb = new NaiveBayesUpdateable();
			 nb.buildClassifier(structure);
			 Instance current;
			 while ((current = loader.getNextInstance(structure)) != null)
			   nb.updateClassifier(current);
			 
			 // output generated model
			 System.out.println(nb);
			    
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return nb;
	}
	
	
	private J48 buildBatchClassifier(){
		
		J48 tree = null;
		try {
			// load data
			 ArffLoader loader = new ArffLoader();
			 loader.setFile(new File("data.arff"));
			 Instances structure = loader.getStructure();
			 structure.setClassIndex(structure.numAttributes() - 1);
			
			 String[] options = new String[1];
			 options[0] = "-U";            // unpruned tree
			 tree = new J48();         // new instance of tree
			 tree.setOptions(options);     // set the options
			 tree.buildClassifier(structure);   // build classifier
			 
			 System.out.println( tree );
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return tree;
	}
	
	
	
}
