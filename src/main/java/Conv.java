import java.io.*;

//import com.sun.java.util.jar.pack.Instruction;
import com.sun.org.apache.bcel.internal.generic.SWITCH;
import com.sun.tools.javah.Util;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.*;
import java.util.Objects;
//import org.apache.commons.io.*;


public class Conv {
    public static void main(String[] args) throws IOException{
        String typeWork = null;
        String fullPath = null;
        Path path;
        //Document doc = null;
        String title;
        //Elements timg = null;
        String allHTML;
       // URL url = new URL("http://solabos.ru/faq/img/3_button.png");
        //BufferedImage img = ImageIO.read(new File("/Users/shaman/Downloads/t_logo.png"));
        //url. ="http://solabos.ru/faq/img/3_button.png";
        //BufferedImage img = ImageIO.read(url);
        //String imgstr;
        //imgstr = encodeToString(img, "png");
       // System.out.println(base64File("http://solabos.ru/faq/img/3_button.png")); //Print base64 image
        if (args.length==0)
        {
            System.out.println("key: F for file, W for website; path for file or for website; savefilename filename with full path for save");
            System.exit(-2);
        }
        for (int i=0;i<args.length;i++){
            System.out.println(args[i]);
        }
        if (args[0] == "f") {typeWork="FILE";}
        if (args[0] == "F") {typeWork="FILE";}
        if ((args[0] == "w") || (args[0] == "W")) {typeWork="WEB";}
        System.out.println("Work type: "+typeWork);
      //  path = Paths.get (args[1]);
   //     fullPath = path.toAbsolutePath().toString();
                //getFull.toString();

        allHTML= ConnectToWeb(args[1]);
        if (saveToFile(args[2], allHTML)==Boolean.TRUE) {System.out.println("File has been written");}
    }

    private static String ConnectToWeb (String url) throws MalformedURLException {
        Document doc = null;
        Elements timg = null;
        String title = null;
        try {
            doc = Jsoup.connect(url).get();
            timg = doc.getElementsByTag("img");
        } catch (IOException e){
            e.printStackTrace();
        }
        if (doc != null) {
            title = doc.title();
        }
        System.out.println(title);
        addAllTag(timg);
    return String.valueOf(doc);
    }

    private static Boolean saveToFile (String fullName, String textHTML){
        try (FileOutputStream fos=new FileOutputStream(fullName)) {
            byte[] buffer = textHTML.getBytes();
            fos.write(buffer, 0, buffer.length);
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }
    private static void addAllTag (Elements elm) throws MalformedURLException {
        Element txt;
        String txt2;
        for (int i=0; i < elm.size();i++) {
         txt = elm.get(i); //content of tag img
         txt2 = txt.attr("src"); // content of attr "src"
         Path path = Paths.get(txt2); //get path
         txt = txt.attr("alt", path.getFileName().toString()); // add attr "alt" with image file name
         txt = txt.attr ("src",base64File(txt2)); //"http://solabos.ru/faq/"+
        }

    }
    @org.jetbrains.annotations.NotNull
    private static String base64File (String urlPic) throws MalformedURLException {
        URL url2 = new URL(urlPic);
        String imgstr = null;
        Path path = Paths.get(urlPic); //get path to pic
        String ext = urlPic.replaceAll("^.*\\.(.*)$", "$1");
        System.out.println(path.getFileName().toString());
        try {
            BufferedImage img = ImageIO.read(url2);
            if (Objects.equals(ext, "svg")) {
                System.out.println("It's SVG file. Sorry");
                return urlPic; //return for SVG file
            } else {
                imgstr = encodeToString(img, ext);
            }
            }catch(IOException e){
                e.printStackTrace();
            }

        return "data:image/"+ext+";base64,"+imgstr;
    }
}
