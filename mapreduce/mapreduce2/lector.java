

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import StringSimilitud.Jaccard;


public class lector {
	
	  public static void main(String[] args) {
	        String path = "C:/Users/Administrador/Desktop/lee";
	        path = "/home/cloudera/leer";
	        String compara = "La duda razonable es el nivel de certeza de que una persona acusada es inocente, hasta que se demuestre su culpabilidad con certeza. Toda persona es inocente hasta que se demuestre lo contrario";
	        compara = "El dispositivo emisor mantiene una copia de la última trama enviada hasta que reciba un reconocimiento para la trama."+
"Para identificar las tramas y los ACK se numeran alternativamente 0 y 1."+
"Si se descubre un error en una trama de datos durante la transmisión, se devuelve una trama NAK. Las tramas NAK no están numeradas indican al emisor que retransmita";
	        
	        archivo[] encont;

	        String files;
	        File folder = new File(path);
	        File[] listOfFiles = folder.listFiles();
	        File archivo = null;
	        FileReader fr = null;
	        BufferedReader br = null;
	        
	        String textXT = "";

	        encont = new archivo[listOfFiles.length];

	        Jaccard j2 = new Jaccard(4);
	        for (int i = 0; i < listOfFiles.length; i++) {
	            encont[i] = new archivo();
	            if (listOfFiles[i].isFile()) {
	                files = listOfFiles[i].getName();

	                textXT = "";
	                
	                try {
	                    archivo = new File("" + listOfFiles[i]);
	                    fr = new FileReader(archivo);
	                    br = new BufferedReader(fr);
	                    String linea = "";
	                    System.out.println("");
	                    System.out.println("");
	                    System.out.println("---------------------------------------------------------------------");
	                    System.out.println("document nombre: " + listOfFiles[i].getName());
	                    if (files.endsWith(".docx") || files.endsWith(".DOCX")) {
	                        FileInputStream fis = new FileInputStream(archivo);
	                        InputStream entradaArch = fis;
	                        XWPFDocument ardocx = new XWPFDocument(entradaArch);

	                        XWPFWordExtractor xwpf_we = new XWPFWordExtractor(ardocx);

	                        String texto = xwpf_we.getText();

	                        System.out.print(texto);

	                        encont[i].setValor(j2.similarity(texto, compara));
	                        encont[i].setNombre(listOfFiles[i].getName());
	                        encont[i].setRuta(listOfFiles[i].getPath());
	                    } else {
	                        while ((linea = br.readLine()) != null) {
	                            System.out.println(linea);
	                            textXT += linea;
	                        }

	                        encont[i].setValor(j2.similarity(textXT, compara));
	                        encont[i].setNombre(listOfFiles[i].getName());
	                        encont[i].setRuta(listOfFiles[i].getPath());

	                    }
	                    System.out.println("---------------------------------------------------------------------");
	                    System.out.println("");
	                    System.out.println("");

	                } catch (Exception e) {
	                    System.err.println("error al leer: " + e);
	                }

	            }
	        }

	         for (int i = 0; i < encont.length-1; i++) {
	            archivo encont1 = encont[i];

	             System.out.println(i+1+". nombre: "+encont1.getNombre());
	             System.out.println("   similitud "+encont1.getValor()*100+"%");	             
	             System.out.println("   rut: "+encont1.getRuta());
	             System.out.println("--------------------------------------------------------------");
	        }
	    }


}
