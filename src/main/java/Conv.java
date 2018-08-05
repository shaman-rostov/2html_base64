import java.io.*;

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
        org.jsoup.nodes.Document doc = null;
        String title = null;
        Elements timg = null;
        String allHTML;
       // URL url = new URL("http://solabos.ru/faq/img/3_button.png");
        //BufferedImage img = ImageIO.read(new File("/Users/shaman/Downloads/t_logo.png"));
        //url. ="http://solabos.ru/faq/img/3_button.png";
        //BufferedImage img = ImageIO.read(url);
        //String imgstr;
        //imgstr = encodeToString(img, "png");
       // System.out.println(base64File("http://solabos.ru/faq/img/3_button.png")); //Print base64 image
        try {
            doc = Jsoup.connect("http://solabos.ru/faq/index.html").get();

            title = doc.title();
            timg = doc.getElementsByTag("img");
        } catch (IOException e){
            e.printStackTrace();
        }
        //System.out.println("title: "+title);
        //System.out.println(timg);
        timg = addAllTag (timg);
        allHTML = doc.html().toString();
        try (FileOutputStream fos=new FileOutputStream("/Users/shaman/Downloads/allHTML.html")) {
            byte[] buffer = allHTML.getBytes();
            fos.write(buffer, 0, buffer.length);
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        System.out.println("file has been written");
    }
    public static String encodeToString(BufferedImage image, String type) {
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
    public static Elements addAllTag (Elements elm) throws MalformedURLException {
        Element txt; String txt2;
        for (int i=0; i < elm.size();i++) {
         txt = elm.get(i); //content of tag img
         txt2 = txt.attr("src"); // content of attr "src"
         Path path = Paths.get(txt2); //get path
           // System.out.println(txt2);

            txt = txt.attr("alt", path.getFileName().toString()); // add attr "alt" with image file name
            txt = txt.attr ("src",base64File("http://solabos.ru/faq/"+txt2));
                  //getElementsByAttribute("src");
            //System.out.println(txt);
            //System.out.println(i+" : "+path.getFileName());
        }
        return elm;
    }
    @org.jetbrains.annotations.NotNull
    public static String base64File (String urlPic) throws MalformedURLException {
        URL url2 = new URL(urlPic);
        String imgstr = null;
        Path path = Paths.get(urlPic); //get path to pic
        String ext = urlPic.replaceAll("^.*\\.(.*)$", "$1");
        System.out.println(path.getFileName().toString());
        try {
            BufferedImage img = ImageIO.read(url2);
            if (Objects.equals(ext, "svg")) {
                System.out.println("It's SVG file. Sorry");
                return "SVG";
            } else {
                imgstr = encodeToString(img, ext);
            }
            }catch(IOException e){
                e.printStackTrace();
            }

        return "data:image/"+ext+";base64,"+imgstr;
    }
}
