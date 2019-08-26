package net.wagic.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Node;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Enumeration;

import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionMethod;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Stream;

import android.graphics.*;
import android.app.ProgressDialog;

public class ImgDownloader {

    private static String convertStreamToString(java.io.InputStream inputStream) {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try {
            Reader in = new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1);
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
        } catch (Exception e) {
        }
        return out.toString();
    }

    private static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try {
            File file = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null)
                contentBuilder.append(st).append("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    public static String getSetInfo(String setName, boolean zipped, String path) {
        String cardsfilepath = "";
        boolean todelete = false;
        if (zipped) {
            File resFolder = new File(path + File.separator);
            File[] listOfFile = resFolder.listFiles();
            ZipFile zipFile = null;
            InputStream stream = null;
            java.nio.file.Path filePath = null;
            try {
                for (int i = 0; i < listOfFile.length; i++) {
                    if (listOfFile[i].getName().contains(".zip")) {
                        zipFile = new ZipFile(path + File.separator + listOfFile[i].getName());
                        break;
                    }
                }
                if (zipFile == null)
                    return "";
                Enumeration<? extends ZipEntry> e = zipFile.entries();
                while (e.hasMoreElements()) {
                    ZipEntry entry = e.nextElement();
                    String entryName = entry.getName();
                    if (entryName.contains("sets" + File.separator)) {
                        if (entryName.contains("_cards.dat")) {
                            String[] names = entryName.split(File.separator);
                            if (setName.equalsIgnoreCase(names[1])) {
                                stream = zipFile.getInputStream(entry);
                                byte[] buffer = new byte[1];
                                java.nio.file.Path outDir = Paths.get(path + File.separator);
                                filePath = outDir.resolve("_cards.dat");
                                try {
                                    FileOutputStream fos = new FileOutputStream(filePath.toFile());
                                    BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
                                    int len;
                                    while ((len = stream.read(buffer)) != -1) {
                                        bos.write(buffer, 0, len);
                                    }
                                    fos.close();
                                    bos.close();
                                    cardsfilepath = filePath.toString();
                                    todelete = true;
                                } catch (Exception ex) {
                                }
                                break;
                            }
                        }
                    }
                }
            } catch (IOException ioe) {
            } finally {
                try {
                    if (zipFile != null) {
                        zipFile.close();
                    }
                } catch (IOException ioe) {
                }
            }
        } else {
            File setFolder = new File(path + File.separator + "sets" + File.separator + setName + File.separator);
            cardsfilepath = setFolder.getAbsolutePath() + File.separator + "_cards.dat";
        }
        String lines = readLineByLineJava8(cardsfilepath);
        if (todelete) {
            File del = new File(cardsfilepath);
            del.delete();
        }
        int totalcards = 0;
        String findStr = "total=";
        int lastIndex = lines.indexOf(findStr);
        String totals = lines.substring(lastIndex, lines.indexOf("\n", lastIndex));
        totalcards = Integer.parseInt(totals.split("=")[1]);
        findStr = "name=";
        lastIndex = lines.indexOf(findStr);
        String name = lines.substring(lastIndex, lines.indexOf("\n", lastIndex)).split("=")[1];
        return name + " (" + totalcards + " cards)";
    }

    public static String getSpecialCardUrl(String id){
        String cardurl = "";

        if(id.equals("15208711"))
            cardurl = "https://img.scryfall.com/cards/large/front/9/c/9c138bf9-8be6-4f1a-a82c-a84938ab84f5.jpg?1562279137";
        else if(id.equals("15208712"))
            cardurl = "https://img.scryfall.com/cards/normal/front/d/4/d453ee89-6122-4d51-989c-e78b046a9de3.jpg?1561758141";
        else if(id.equals("2050321"))
            cardurl = "https://img.scryfall.com/cards/large/front/1/8/18b9c83d-4422-4b95-9fc2-070ed6b5bdf6.jpg?1562701921";
        else if(id.equals("2050322"))
            cardurl = "https://crystal-cdn4.crystalcommerce.com/photos/504053/ooze_token_b.jpg";
        else if(id.equals("22010012"))
            cardurl = "https://img.scryfall.com/cards/normal/front/8/4/84dc847c-7a37-4c7f-b02c-30b3e4c91fb6.jpg?1561757490";
        else if(id.equals("8759611"))
            cardurl = "https://img.scryfall.com/cards/large/front/4/1/41004bdf-8e09-4b2c-9e9c-26c25eac9854.jpg?1562493483";
        else if(id.equals("8759911"))
            cardurl = "https://img.scryfall.com/cards/large/front/0/b/0b61d772-2d8b-4acf-9dd2-b2e8b03538c8.jpg?1562492461";
        else if(id.equals("8759511"))
            cardurl = "https://img.scryfall.com/cards/large/front/d/2/d224c50f-8146-4c91-9401-04e5bd306d02.jpg?1562496100";
        else if(id.equals("8471611"))
            cardurl = "https://img.scryfall.com/cards/png/front/8/4/84920a21-ee2a-41ac-a369-347633d10371.png?1562494702";
        else if(id.equals("8760011"))
            cardurl = "https://img.scryfall.com/cards/large/front/4/2/42ba0e13-d20f-47f9-9c86-2b0b13c39ada.jpg?1562493487";
        else if(id.equals("401721"))
            cardurl = "https://deckmaster.info/images/cards/DDP/401721-hr.jpg";
        else if(id.equals("401722"))
            cardurl = "https://deckmaster.info/images/cards/DDP/401722-hr.jpg";
        else if(id.equals("19784311"))
            cardurl = "https://deckmaster.info/images/cards/AKH/-4173-hr.jpg";
        else if(id.equals("19784312"))
            cardurl = "https://deckmaster.info/images/cards/BNG/-10-hr.jpg";
        else if(id.equals("19784313"))
            cardurl = "https://deckmaster.info/images/cards/DDD/201843-hr.jpg";
        else if(id.equals("20787512"))
            cardurl = "https://deckmaster.info/images/cards/SOM/-227-hr.jpg";
        else if(id.equals("20787511"))
            cardurl = "https://deckmaster.info/images/cards/SOM/-226-hr.jpg";
        else if(id.equals("11492111"))
            cardurl = "https://deckmaster.info/images/cards/TSP/-2841-hr.jpg";
        else if(id.equals("11492112"))
            cardurl = "https://deckmaster.info/images/cards/TSP/-2840-hr.jpg";
        else if(id.equals("11492113"))
            cardurl = "https://img.scryfall.com/cards/large/front/5/b/5b9f471a-1822-4981-95a9-8923d83ddcbf.jpg?1562702075";
        else if(id.equals("11492114"))
            cardurl = "https://deckmaster.info/images/cards/DDN/386322-hr.jpg";
        else if(id.equals("11492115") || id.equals("209162"))
            cardurl = "https://deckmaster.info/images/cards/DDE/209162-hr.jpg";
        else if(id.equals("7448911"))
            cardurl = "https://img.scryfall.com/cards/large/front/c/a/ca03131a-9bd4-4fba-b95c-90f1831e86e7.jpg?1562879774";
        else if(id.equals("7453611"))
            cardurl = "https://img.scryfall.com/cards/large/front/7/3/73636ca0-2309-4bb3-9300-8bd0c0bb5b31.jpg?1562877808";
        else if(id.equals("7447611"))
            cardurl = "https://img.scryfall.com/cards/large/front/2/8/28f72260-c8f9-4c44-92b5-23cef6690fdd.jpg?1562876119";
        else if(id.equals("7467111"))
            cardurl = "https://img.scryfall.com/cards/large/front/1/f/1fe2b76f-ddb7-49d5-933b-ccb06be5d46f.jpg?1562875903";
        else if(id.equals("7409311"))
            cardurl = "https://img.scryfall.com/cards/large/front/7/5/758abd53-6ad2-406e-8615-8e48678405b4.jpg?1562877848";
        else if(id.equals("3896122"))
            cardurl = "https://img.scryfall.com/cards/large/front/5/9/59a00cac-53ae-46ad-8468-e6d1db40b266.jpg?1562542382";
        else if(id.equals("3896522"))
            cardurl = "https://www.cardkingdom.com/images/magic-the-gathering/masters-25/whale-token-42707-medium.jpg";
        else if(id.equals("3896521"))
            cardurl = "https://www.cardkingdom.com/images/magic-the-gathering/commander-2014/fish-token-zombie-token-blue-53462-medium.jpg";
        else if(id.equals("3896523"))
            cardurl = "https://img.scryfall.com/cards/large/front/d/0/d0cd85cc-ad22-446b-8378-5eb69fee1959.jpg?1562840712";
        else if(id.equals("687701"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2437-hr.jpg";
        else if(id.equals("687702"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3069-hr.jpg";
        else if(id.equals("687703"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2443-hr.jpg";
        else if(id.equals("687704"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2444-hr.jpg";
        else if(id.equals("687705"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2450-hr.jpg";
        else if(id.equals("687713"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3175-hr.jpg";
        else if(id.equals("687712"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2624-hr.jpg";
        else if(id.equals("687711"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3168-hr.jpg";
        else if(id.equals("687710"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3161-hr.jpg";
        else if(id.equals("687709"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2485-hr.jpg";
        else if(id.equals("687752"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3085-hr.jpg";
        else if(id.equals("687707"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2478-hr.jpg";
        else if(id.equals("687751"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3083-hr.jpg";
        else if(id.equals("687720"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2652-hr.jpg";
        else if(id.equals("687719"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2650-hr.jpg";
        else if(id.equals("687718"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3178-hr.jpg";
        else if(id.equals("687717"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2641-hr.jpg";
        else if(id.equals("687716"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2634-hr.jpg";
        else if(id.equals("687715"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2631-hr.jpg";
        else if(id.equals("687714"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2630-hr.jpg";
        else if(id.equals("687722"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2550-hr.jpg";
        else if(id.equals("687721"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3183-hr.jpg";
        else if(id.equals("687734"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2398-hr.jpg";
        else if(id.equals("687708"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3086-hr.jpg";
        else if(id.equals("687732"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3158-hr.jpg";
        else if(id.equals("687731"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3157-hr.jpg";
        else if(id.equals("687755"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3156-hr.jpg";
        else if(id.equals("687730"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2603-hr.jpg";
        else if(id.equals("687729"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2576-hr.jpg";
        else if(id.equals("687728"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2573-hr.jpg";
        else if(id.equals("687727"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2570-hr.jpg";
        else if(id.equals("687726"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2568-hr.jpg";
        else if(id.equals("687725"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2559-hr.jpg";
        else if(id.equals("687724"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3131-hr.jpg";
        else if(id.equals("687723"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3128-hr.jpg";
        else if(id.equals("687740"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2759-hr.jpg";
        else if(id.equals("687739"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2755-hr.jpg";
        else if(id.equals("687738"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2432-hr.jpg";
        else if(id.equals("687737"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3053-hr.jpg";
        else if(id.equals("687756"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3054-hr.jpg";
        else if(id.equals("687736"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2408-hr.jpg";
        else if(id.equals("687735"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2403-hr.jpg";
        else if(id.equals("687733"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2729-hr.jpg";
        else if(id.equals("687706"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3082-hr.jpg";
        else if(id.equals("687750"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2748-hr.jpg";
        else if(id.equals("687748"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2747-hr.jpg";
        else if(id.equals("687749"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2746-hr.jpg";
        else if(id.equals("687742"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2743-hr.jpg";
        else if(id.equals("687743"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2744-hr.jpg";
        else if(id.equals("687744"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2745-hr.jpg";
        else if(id.equals("687745"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2763-hr.jpg";
        else if(id.equals("687746"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2764-hr.jpg";
        else if(id.equals("687747"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2765-hr.jpg";
        else if(id.equals("687741"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-2761-hr.jpg";
        else if(id.equals("687753"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3176-hr.jpg";
        else if(id.equals("687754"))
            cardurl = "https://deckmaster.info/images/cards/DKM/-3184-hr.jpg";
        else if(id.equals("7897511"))
            cardurl = "https://img.scryfall.com/cards/large/front/a/4/a4f4aa3b-c64a-4430-b1a2-a7fca87d0a22.jpg?1562763433";
        else if(id.equals("7868811"))
            cardurl = "https://img.scryfall.com/cards/large/front/b/3/b3523b8e-065f-427c-8d5b-eb731ca91ede.jpg?1562763691";
        else if(id.equals("7868711"))
            cardurl = "https://img.scryfall.com/cards/large/front/5/8/58164521-aeec-43fc-9db9-d595432dea6f.jpg?1564694999";
        else if(id.equals("7868611"))
            cardurl = "https://img.scryfall.com/cards/large/front/3/3/33a8e5b9-6bfb-4ff2-a16d-3168a5412807.jpg?1562758927";
        else if(id.equals("7869111"))
            cardurl = "https://img.scryfall.com/cards/large/front/9/d/9de1eebf-5725-438c-bcf0-f3a4d8a89fb0.jpg?1562762993";
        else if(id.equals("7860011"))
            cardurl = "https://img.scryfall.com/cards/large/front/8/6/864ad989-19a6-4930-8efc-bbc077a18c32.jpg?1562762069";
        else if(id.equals("7867911"))
            cardurl = "https://img.scryfall.com/cards/large/front/c/8/c8265c39-d287-4c5a-baba-f2f09dd80a1c.jpg?1562764226";
        else if(id.equals("7867811"))
            cardurl = "https://img.scryfall.com/cards/large/front/a/0/a00a7180-49bd-4ead-852a-67b6b5e4b933.jpg?1564694995";
        else if(id.equals("7869511"))
            cardurl = "https://img.scryfall.com/cards/large/front/f/2/f2ddf1a3-e6fa-4dd0-b80d-1a585b51b934.jpg?1562765664";
        else if(id.equals("7869411"))
            cardurl = "https://img.scryfall.com/cards/large/front/6/e/6ee6cd34-c117-4d7e-97d1-8f8464bfaac8.jpg?1562761096";
        else if(id.equals("207998"))
            cardurl = "https://deckmaster.info/images/cards/DDE/207998-hr.jpg";
        else if (id.equals("19784555"))
            cardurl = "https://cdn.shopify.com/s/files/1/0790/8591/products/Grnelementalfinal_800x800.jpg?v=1476398274";
        else if (id.equals("19784612"))
            cardurl = "https://deckmaster.info/images/cards/RTR/-60-hr.jpg";
        else if (id.equals("19784613"))
            cardurl = "https://deckmaster.info/images/cards/RTR/-62-hr.jpg";
        else if (id.equals("19784611"))
            cardurl = "https://deckmaster.info/images/cards/RTR/-55-hr.jpg";
        else if (id.equals("4977511"))
            cardurl = "https://www.mtg.onl/static/acc7da698156ddfb2270f09ac7ae6f81/4d406/PROXY_Elemental_U_2_2.jpg";
        else if (id.equals("4977512"))
            cardurl = "https://deckmaster.info/images/cards/DST/-2818-hr.jpg";


        return cardurl;
    }

    public static String getSpecialTokenUrl(String id){
        String tokenurl = "";

        if(id.equals("75291t"))
            tokenurl = "http://4.bp.blogspot.com/-y5Fanm3qvrU/Vmd4gGnl2DI/AAAAAAAAAWY/FCrS9FTgOJk/s1600/Tatsumasa%2BToken.jpg";
        else if (id.equals("435411t") || id.equals("435410t"))
            tokenurl = "https://deckmaster.info/images/cards/XLN/-5173-hr.jpg";
        else if(id.equals("202474t") || id.equals("1098t") || id.equals("2024t") || id.equals("3766t") || id.equals("11183t") || id.equals("902t"))
            tokenurl = "https://deckmaster.info/images/cards/AST/-884-hr.jpg";
        else if(id.equals("202590t") || id.equals("2073t") || id.equals("1027t"))
            tokenurl = "https://deckmaster.info/images/cards/AST/-892-hr.jpg";
        else if(id.equals("201124t") || id.equals("3118t"))
            tokenurl = "http://i1013.photobucket.com/albums/af260/lovesoldier99/STARFISHTOKEN.jpg";
        else if(id.equals("184735") || id.equals("376488t") || id.equals("3066t"))
            tokenurl = "https://i.pinimg.com/originals/a9/fb/37/a9fb37bdfa8f8013b7eb854d155838e2.jpg";
        else if(id.equals("184598t"))
            tokenurl = "https://deckmaster.info/images/cards/HM/-2070-hr.jpg";
        else if(id.equals("184589t"))
            tokenurl = "http://d1f83aa4yffcdn.cloudfront.net/TOKEN/2%202%20Black%20Zombie.jpg";
        else if(id.equals("184730t"))
            tokenurl="https://www.mtg.onl/static/c88f42f8bd5a7c25aa36902546b690f5/4d406/PROXY_Knight_W_1_1.jpg";
        else if(id.equals("1649t") || id.equals("201182t"))
            tokenurl = "https://pbs.twimg.com/media/DH9n-2JVwAA0o8z.jpg";
        else if(id.equals("140233t") || id.equals("191239t") || id.equals("205957t"))
            tokenurl = "https://i860.photobucket.com/albums/ab170/mistergreen527/White%20Tokens/WAvatarX-X1.jpg";
        else if(id.equals("1686t") || id.equals("2881t") ||  id.equals("201231t"))
            tokenurl = "https://deckmaster.info/images/cards/A25/-5648-hr.jpg";
        else if(id.equals("121261t"))
            tokenurl = "https://i.pinimg.com/originals/a9/fb/37/a9fb37bdfa8f8013b7eb854d155838e2.jpg";
        else if(id.equals("368951t"))
            tokenurl = "https://d1rw89lz12ur5s.cloudfront.net/photo/facetofacegames/file/36262794e9f37368e7872326715ac806/eletok.jpg";
        else if(id.equals("46168t"))
            tokenurl = "https://deckmaster.info/images/cards/KLD/-3287-hr.jpg";
        else if(id.equals("49026t"))
            tokenurl = "https://www.mtg.onl/static/a9d81341e62e39e75075b573739f39d6/4d406/PROXY_Wirefly_2_2.jpg";
        else if(id.equals("414506t"))
            tokenurl = "https://poromagia.com/media/cache/25/f0/25f0cd307adc18d7655c465408267469.jpg";
        else if(id.equals("6142t"))
            tokenurl = "https://cdn.staticneo.com/w/mtg/c/cd/Beast5.jpg";
        else if(id.equals("126166t"))
            tokenurl = "https://deckmaster.info/images/cards/C14/-487-hr.jpg";
        else if(id.equals("136155t"))
            tokenurl = "http://static1.squarespace.com/static/583dca25ff7c5080991b2c87/583de52de6f2e18631eb2b32/58405d0dbe6594762f5bd8e6/1565969982322/wurm-white.jpg";
        else if(id.equals("107091t"))
            tokenurl = "https://media.mtgsalvation.com/attachments/13/534/635032476540667501.jpg";
        else if(id.equals("452760t"))
            tokenurl = "https://deckmaster.info/images/cards/M19/-6036.jpg";
        else if(id.equals("2959t"))
            tokenurl = "https://deckmaster.info/images/cards/HM/-2070-hr.jpg";
        else if(id.equals("380486t"))
            tokenurl = "https://deckmaster.info/images/cards/BNG/-5-hr.jpg";
        else if(id.equals("380487t"))
            tokenurl = "https://poromagia.com/media/cache/25/f0/25f0cd307adc18d7655c465408267469.jpg";
        else if(id.equals("234849t"))
            tokenurl = "https://deckmaster.info/images/cards/RTR/-61-hr.jpg";
        else if(id.equals("23319t"))
            tokenurl = "https://i860.photobucket.com/albums/ab170/mistergreen527/White%20Tokens/WReflectionX-X1.jpg";
        else if(id.equals("205297t") || id.equals("50104t"))
            tokenurl = "https://www.mtg.onl/static/df30395b530524a3988428d4c0b37161/4d406/PROXY_Pest_0_1.jpg";
        else if(id.equals("3449t"))
            tokenurl = "https://www.mtg.onl/static/8c7fed1a0b8edd97c0fb0ceab24a654f/4d406/PROXY_Goblin_Scout_R_1_1.jpg";
        else if(id.equals("3392t"))
            tokenurl = "https://deckmaster.info/images/cards/DDR/417498-hr.jpg";
        else if(id.equals("3280t"))
            tokenurl = "https://media.mtgsalvation.com/attachments/54/421/635032484680831888.jpg";
        else if(id.equals("3242t"))
            tokenurl = "https://deckmaster.info/images/cards/MI/-2828-hr.jpg";
        else if(id.equals("426025t"))
            tokenurl = "https://cdn.shopify.com/s/files/1/0790/8591/products/Grnelementalfinal_800x800.jpg?v=1476398274";
        else if(id.equals("19878t"))
            tokenurl = "https://www.cardkingdom.com/images/magic-the-gathering/commander-2014/ape-token-zombie-token-blue-65252-medium.jpg";
        else if(id.equals("21381t") || id.equals("40198t"))
            tokenurl = "https://img.scryfall.com/cards/large/back/8/c/8ce60642-e207-46e6-b198-d803ff3b47f4.jpg?1562921132";
        else if(id.equals("265141t"))
            tokenurl = "https://media.mtgsalvation.com/attachments/102/31/635032498723573408.jpg";
        else if(id.equals("24624t"))
            tokenurl = "https://www.mtg.onl/static/6d717cba653ea9e3f6bd1419741671cb/4d406/PROXY_Minion_B_1_1.jpg";
        else if(id.equals("409810t") || id.equals("409805t") || id.equals("409953t") || id.equals("409997t") || id.equals("410032t"))
            tokenurl = "https://deckmaster.info/images/cards/SOI/-2404-hr.jpg";
        else if(id.equals("74492t"))
            tokenurl = "https://media.mtgsalvation.com/attachments/94/295/635032496473215708.jpg";
        else if(id.equals("88973t"))
            tokenurl = "https://deckmaster.info/images/cards/DDQ/409655-hr.jpg";
        else if(id.equals("89051t"))
            tokenurl = "https://www.mtg.onl/static/b7625a256e10bcec251a1a0abbf17bd4/4d406/PROXY_Horror_B_4_4.jpg";
        else if(id.equals("5261t"))
            tokenurl = "https://static.cardmarket.com/img/5a0199344cad68eebeefca6fa24e52c3/items/1/MH1/376905.jpg";
        else if(id.equals("116384t") || id.equals("376564t"))
            tokenurl = "https://deckmaster.info/images/cards/TSP/-114916-hr.jpg";
        else if(id.equals("116383t"))
            tokenurl = "https://i.imgur.com/wRMebWg.jpg";
        else if(id.equals("114917t"))
            tokenurl = "https://deckmaster.info/images/cards/JOU/-43-hr.jpg";
        else if(id.equals("5610t"))
            tokenurl = "https://deckmaster.info/images/cards/DDE/207998-hr.jpg";
        else if(id.equals("185704t"))
            tokenurl = "https://deckmaster.info/images/cards/ZEN/-277-hr.jpg";
        else if(id.equals("461099t"))
            tokenurl = "https://img.scryfall.com/cards/large/front/d/e/de7ba875-f77b-404f-8b75-4ba6f81da410.jpg?1557575978";
        else if(id.equals("9667t"))
            tokenurl = "https://www.mtg.onl/static/abe5178af8ebbe84f5504493a1b5f154/4d406/PROXY_Giant_Chicken_R_4_4.jpg";
        else if (id.equals("368549t"))
            tokenurl = "https://deckmaster.info/images/cards/DDQ/409655-hr.jpg";
        else if (id.equals("73953t"))
            tokenurl = "https://www.mtg.onl/static/98da67c454fe6cde61ae45af4f9c3150/4d406/PROXY_Giant_Teddy_Bear_5_5.jpg";
        else if (id.equals("74265t"))
            tokenurl = "https://deckmaster.info/images/cards/UNH/-2064-hr.jpg";
        else if (id.equals("27634t"))
            tokenurl = "https://www.mtg.onl/static/8b684bdea239d594e296a134f5ec1783/4d406/PROXY_Hippo_G_1_1.jpg";
        else if (id.equals("111046t"))
            tokenurl = "https://media.mtgsalvation.com/attachments/32/354/635032480299772645.jpg";
        else if(id.equals("4771t"))
            tokenurl = "https://www.mtg.onl/static/b8060dffbaf67ef987c6324c1523d3e4/4d406/PROXY_Hound_G_1_1.jpg";
        else if(id.equals("3591t"))
            tokenurl = "https://i.pinimg.com/564x/6e/8d/fe/6e8dfeee2919a3efff210df56ab7b85d.jpg";
        else if(id.equals("72858t"))
            tokenurl = "https://www.mtg.onl/static/348314ede9097dd8f6dd018a6502d125/4d406/PROXY_Pincher_2_2.jpg";
        else if(id.equals("3832t"))
            tokenurl = "https://deckmaster.info/images/cards/GK1_DIMIR/-6541-hr.jpg";
        else if(id.equals("426909t") || id.equals("426705t"))
            tokenurl = "https://img.scryfall.com/cards/large/front/9/8/98956e73-04e4-4d7f-bda5-cfa78eb71350.jpg?1562844807";
        else if(id.equals("426897t"))
            tokenurl = "https://img.scryfall.com/cards/large/front/a/8/a8f339c6-2c0d-4631-849b-44d4360b5131.jpg?1562844814";
        else if(id.equals("175105t"))
            tokenurl = "https://crystal-cdn1.crystalcommerce.com/photos/371742/beast.jpg";
        else if(id.equals("470549t"))
            tokenurl = "https://img.scryfall.com/cards/large/front/7/7/7711a586-37f9-4560-b25d-4fb339d9cd55.jpg?1565299650";
        else if(id.equals("3227t"))
            tokenurl = "https://deckmaster.info/images/cards/PS/-2072-hr.jpg";
        else if(id.equals("3148t"))
            tokenurl = "https://deckmaster.info/images/cards/AL/-2156-hr.jpg";
        else if(id.equals("3113t"))
            tokenurl = "https://www.mtg.onl/static/fca7508d78c26e3daea78fd4640faf9a/4d406/PROXY_Orb_U_X_X.jpg";
        else if(id.equals("26815t"))
            tokenurl = "https://www.mtg.onl/static/8bb68cf125fdcc9d8a21b3dade2f11cb/4d406/PROXY_Cat_B_2_1.jpg";
        else if(id.equals("25956t"))
            tokenurl = "https://www.mtg.onl/static/740ce087c4aff57e881b01c28528c8f9/4d406/PROXY_Kavu_B_3_3.jpg";
        else if(id.equals("74027t"))
            tokenurl = "https://www.mtg.onl/static/48515f01d0fda15dd9308d3a528dae7b/4d406/PROXY_Spirit_W_3_3.jpg";

        return tokenurl;
    }

    public static boolean hasToken(String id){
        if(id.equals("456378")      || id.equals("2912")   || id.equals("1514")   || id.equals("364")    || id.equals("69")     || id.equals("369012") ||
                id.equals("417759") || id.equals("386476") || id.equals("456371") || id.equals("456360") || id.equals("391958") || id.equals("466959") ||
                id.equals("466813") || id.equals("201176") || id.equals("202483") || id.equals("3546")   || id.equals("425949") || id.equals("426027") ||
                id.equals("425853") || id.equals("425846") || id.equals("426036") || id.equals("370387") || id.equals("29955")  || id.equals("29989")  ||
                id.equals("19741")  || id.equals("19722")  || id.equals("19706")  || id.equals("24597")  || id.equals("24617")  || id.equals("24563")  ||
                id.equals("253539") || id.equals("277995") || id.equals("265415") || id.equals("289225") || id.equals("289215") || id.equals("253529") ||
                id.equals("253641") || id.equals("270957") || id.equals("401685") || id.equals("89116")  || id.equals("5183")   || id.equals("5177")   ||
                id.equals("209289") || id.equals("198171") || id.equals("10419")  || id.equals("470542") || id.equals("29992")  || id.equals("666")    ||
                id.equals("2026")   || id.equals("45395")  || id.equals("442021") || id.equals("423758") || id.equals("426930") || id.equals("998")    ||
                id.equals("446163") || id.equals("378411") || id.equals("376457") || id.equals("470749") || id.equals("450641") || id.equals("470623") ||
                id.equals("470620") || id.equals("470754") || id.equals("470750") || id.equals("470739") || id.equals("470708") || id.equals("470581") ||
                id.equals("470578") || id.equals("470571") || id.equals("470552"))
            return false;
        return true;
    }

    public static Document findTokenPage(String imageurl, String name, String set, String[] availableSets, String tokenstats) throws Exception {
        Document doc = null;
        Elements outlinks = null;
        try {
            doc = Jsoup.connect(imageurl + "t" + set.toLowerCase()).get();
            if (doc != null) {
                outlinks = doc.select("body a");
                if (outlinks != null) {
                    for (int k = 0; k < outlinks.size(); k++) {
                        String linktoken = outlinks.get(k).attributes().get("href");
                        if (linktoken != null && !linktoken.isEmpty()) {
                            try {
                                Document tokendoc = Jsoup.connect(linktoken).get();
                                if (tokendoc == null)
                                    continue;
                                Elements stats = tokendoc.select("head meta");
                                if (stats != null) {
                                    for (int j = 0; j < stats.size(); j++) {
                                        String a = stats.get(j).attributes().get("content");
                                        if (stats.get(j).attributes().get("content").contains(tokenstats) &&
                                                stats.get(j).attributes().get("content").toLowerCase().contains(name.toLowerCase())) {
                                            return tokendoc;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        System.out.println("Warning: Token " + name + " has not been found between " + set + " tokens, i will search for it in https://deckmaster.info");
        String json = "";
        try {
            URL url = new URL("https://deckmaster.info/includes/ajax.php?action=cardSearch&searchString=" + name);
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            if (httpcon != null) {
                httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
                InputStream stream = httpcon.getInputStream();
                if (stream != null) {
                    int i;
                    while ((i = stream.read()) != -1) {
                        json = json + ((char) i);
                    }
                }
            }
        } catch (Exception e) {
        }
        List<String> urls = new ArrayList<String>();
        String[] tok = json.split(",");
        for (int i = 0; i < tok.length; i++) {
            if (tok[i].contains("multiverseid")) {
                String id = tok[i].split(":")[1].replace("\"", "");
                urls.add(id);
            }
        }
        for (int i = 0; i < urls.size(); i++) {
            try {
                Document tokendoc = Jsoup.connect("https://deckmaster.info/card.php?multiverseid=" + urls.get(i)).get();
                if (tokendoc == null)
                    continue;
                Elements stats = tokendoc.select("head meta");
                if (stats != null) {
                    for (int j = 0; j < stats.size(); j++) {
                        if (stats.get(j).attributes().get("content").contains("Token Creature") && stats.get(j).attributes().get("content").toLowerCase().contains(name.toLowerCase())) {
                            if (stats.get(j).attributes().get("content").contains(tokenstats.replace("X/X", "★/★")))
                                return tokendoc;
                            stats = tokendoc.select("body textarea");
                            if (stats != null) {
                                for (int y = 0; y < stats.size(); y++) {
                                    List<Node> nodes = stats.get(y).childNodes();
                                    if (nodes != null) {
                                        for (int p = 0; p < nodes.size(); p++) {
                                            if (stats.get(y).childNode(p).attributes().get("#text").contains(tokenstats))
                                                return tokendoc;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        System.out.println("Warning: Token " + name + " has not been found in https://deckmaster.info so i will search for it between any other set in " + imageurl + " (it can take long time)");
        for (int i = 0; i < availableSets.length; i++) {
            String currentSet = availableSets[i].toLowerCase().split(" - ")[0];
            if (!currentSet.equalsIgnoreCase(set)) {
                try {
                    doc = Jsoup.connect(imageurl + "t" + currentSet).get();
                    if (doc == null)
                        continue;
                    outlinks = doc.select("body a");
                    if (outlinks != null) {
                        for (int k = 0; k < outlinks.size(); k++) {
                            String linktoken = outlinks.get(k).attributes().get("href");
                            try {
                                Document tokendoc = Jsoup.connect(linktoken).get();
                                if (tokendoc == null)
                                    continue;
                                Elements stats = tokendoc.select("head meta");
                                if (stats != null) {
                                    for (int j = 0; j < stats.size(); j++) {
                                        String a = stats.get(j).attributes().get("content");
                                        if (stats.get(j).attributes().get("content").contains(tokenstats) && stats.get(j).attributes().get("content").toLowerCase().contains(name.toLowerCase())) {
                                            System.out.println("Token " + name + " has been found between " + currentSet.toUpperCase() + " tokens, i will use this one");
                                            return tokendoc;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        System.err.println("Error: Token " + name + " has not been found between any set of " + imageurl);
        throw new Exception();
    }

    public static String DownloadCardImages(String set, String[] availableSets, String targetres, String basePath, String destinationPath, ProgressDialog progressBarDialog) throws IOException {
        String res = "";

        String baseurl = "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=";
        String imageurl = "https://scryfall.com/sets/";

        Integer ImgX = 0;
        Integer ImgY = 0;
        Integer ThumbX = 0;
        Integer ThumbY = 0;

        if (targetres.equals("High")) {
            ImgX = 672;
            ImgY = 936;
            ThumbX = 124;
            ThumbY = 176;
        } else if (targetres.equals("Medium")) {
            ImgX = 488;
            ImgY = 680;
            ThumbX = 90;
            ThumbY = 128;
        } else if (targetres.equals("Low")) {
            ImgX = 244;
            ImgY = 340;
            ThumbX = 45;
            ThumbY = 64;
        } else if (targetres.equals("Tiny")) {
            ImgX = 180;
            ImgY = 255;
            ThumbX = 45;
            ThumbY = 64;
        }

        File baseFolder = new File(basePath);
        File[] listOfFiles = baseFolder.listFiles();
        Map<String, String> mappa = new HashMap<String, String>();
        ZipFile zipFile = null;
        InputStream stream = null;
        java.nio.file.Path filePath = null;
        try {
            zipFile = new ZipFile(basePath + File.separator + listOfFiles[0].getName());
            Enumeration<? extends ZipEntry> e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                String entryName = entry.getName();
                if (entryName != null && entryName.contains("sets" + File.separator)) {
                    if (entryName.contains("_cards.dat")) {
                        String[] names = entryName.split(File.separator);
                        if (set.equalsIgnoreCase(names[1])) {
                            stream = zipFile.getInputStream(entry);
                            byte[] buffer = new byte[1];
                            java.nio.file.Path outDir = Paths.get(basePath);
                            filePath = outDir.resolve("_cards.dat");
                            try {
                                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                                BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
                                int len;
                                while ((len = stream.read(buffer)) != -1) {
                                    bos.write(buffer, 0, len);
                                }
                                fos.close();
                                bos.close();
                            } catch (Exception ex) {
                                System.out.println("Error extracting zip file" + ex);
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("Error opening zip file" + ioe);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error while closing zip file" + ioe);
            }
        }

        String lines = readLineByLineJava8(filePath.toString());
        File del = new File(filePath.toString());
        del.delete();
        int totalcards = 0;
        String findStr = "total=";
        int lastIndex = lines.indexOf(findStr);
        String totals = lines.substring(lastIndex, lines.indexOf("\n", lastIndex));
        totalcards = Integer.parseInt(totals.split("=")[1]);
        while (lines.contains("[card]")) {
            findStr = "[card]";
            lastIndex = lines.indexOf(findStr);
            String id = null;
            String primitive = null;
            int a = lines.indexOf("primitive=", lastIndex);
            if (a > 0)
                primitive = lines.substring(a, lines.indexOf("\n", a)).replace("//", "-").split("=")[1];
            int b = lines.indexOf("id=", lastIndex);
            if (b > 0)
                id = lines.substring(b, lines.indexOf("\n", b)).replace("-", "").split("=")[1];
            int c = lines.indexOf("[/card]", lastIndex);
            if (c > 0)
                lines = lines.substring(c + 8);
            if (primitive != null && id != null && !id.equalsIgnoreCase("null"))
                mappa.put(id, primitive);
            if (id.equals("114921")) {
                mappa.put("11492111", "Citizen");
                mappa.put("11492112", "Camarid");
                mappa.put("11492113", "Thrull");
                mappa.put("11492114", "Goblin");
                mappa.put("11492115", "Saproling");
            }
        }

        progressBarDialog.setProgress(0);
        progressBarDialog.setMax(totalcards);

        File imgPath = new File(destinationPath + set + File.separator);
        if (!imgPath.exists()) {
            System.out.println("creating directory: " + imgPath.getName());
            boolean result = false;
            try {
                imgPath.mkdir();
                result = true;
            } catch (SecurityException se) {
                System.err.println(imgPath + " not created");
                System.exit(1);
            }
            if (result) {
                System.out.println(imgPath + " created");
            }
        }

        File thumbPath = new File(destinationPath + set + File.separator + "thumbnails" + File.separator);
        if (!thumbPath.exists()) {
            System.out.println("creating directory: " + thumbPath.getName());
            boolean result = false;
            try {
                thumbPath.mkdir();
                result = true;
            } catch (SecurityException se) {
                System.err.println(thumbPath + " not created");
                System.exit(1);
            }
            if (result) {
                System.out.println(thumbPath + " created");
            }
        }

        String scryset = set;
        if (scryset.equalsIgnoreCase("MRQ"))
            scryset = "MMQ";
        else if (scryset.equalsIgnoreCase("AVN"))
            scryset = "DDH";
        else if (scryset.equalsIgnoreCase("BVC"))
            scryset = "DDQ";
        else if (scryset.equalsIgnoreCase("CFX"))
            scryset = "CON";
        else if (scryset.equalsIgnoreCase("DM"))
            scryset = "DKM";
        else if (scryset.equalsIgnoreCase("EVK"))
            scryset = "DDO";
        else if (scryset.equalsIgnoreCase("EVT"))
            scryset = "DDF";
        else if (scryset.equalsIgnoreCase("FVD"))
            scryset = "DRB";
        else if (scryset.equalsIgnoreCase("FVE"))
            scryset = "V09";
        else if (scryset.equalsIgnoreCase("FVL"))
            scryset = "V11";
        else if (scryset.equalsIgnoreCase("FVR"))
            scryset = "V10";
        else if (scryset.equalsIgnoreCase("HVM"))
            scryset = "DDL";
        else if (scryset.equalsIgnoreCase("IVG"))
            scryset = "DDJ";
        else if (scryset.equalsIgnoreCase("JVV"))
            scryset = "DDM";
        else if (scryset.equalsIgnoreCase("KVD"))
            scryset = "DDG";
        else if (scryset.equalsIgnoreCase("PDS"))
            scryset = "H09";
        else if (scryset.equalsIgnoreCase("PVC"))
            scryset = "DDE";
        else if (scryset.equalsIgnoreCase("RV"))
            scryset = "3ED";
        else if (scryset.equalsIgnoreCase("SVT"))
            scryset = "DDK";
        else if (scryset.equalsIgnoreCase("VVK"))
            scryset = "DDI";
        else if (scryset.equalsIgnoreCase("ZVE"))
            scryset = "DDP";

        for (int y = 0; y < mappa.size(); y++) {
            String id = mappa.keySet().toArray()[y].toString();
            progressBarDialog.incrementProgressBy((int) (1));
            String specialcardurl = getSpecialCardUrl(id);
            if (!specialcardurl.isEmpty()) {
                URL url = new URL(specialcardurl);
                HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                if(httpcon == null) {
                    System.err.println("Error: Problem fetching card: " + mappa.get(id) + "-" + id + ", i will not download it...");
                    break;
                }
                httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
                InputStream in = null;
                try{
                    in = new BufferedInputStream(httpcon.getInputStream());
                } catch (Exception ex) {
                    System.out.println("Warning: Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will retry 2 times more...");
                    try {
                        in = new BufferedInputStream(url.openStream());
                    } catch (Exception ex2) {
                        System.out.println("Warning: Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will retry 1 time more...");
                        try {
                            in = new BufferedInputStream(url.openStream());
                        } catch (Exception ex3) {
                            System.err.println("Error: Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will not retry anymore...");
                            break;
                        }
                    }
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }
                out.close();
                in.close();
                byte[] response = out.toByteArray();
                String cardimage = imgPath + File.separator + id + ".jpg";
                String thumbcardimage = thumbPath + File.separator + id + ".jpg";
                if (id.equals("11492111") || id.equals("11492112") || id.equals("11492113") ||
                        id.equals("11492114") || id.equals("11492115")) {
                    cardimage = imgPath + File.separator + id + "t.jpg";
                    thumbcardimage = thumbPath + File.separator + id + "t.jpg";
                }
                FileOutputStream fos = new FileOutputStream(cardimage);
                fos.write(response);
                fos.close();

                Bitmap yourBitmap = BitmapFactory.decodeFile(cardimage);
                Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, ImgX, ImgY, true);
                try {
                    FileOutputStream fout = new FileOutputStream(cardimage);
                    resized.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap resizedThumb = Bitmap.createScaledBitmap(yourBitmap, ThumbX, ThumbY, true);
                try {
                    FileOutputStream fout = new FileOutputStream(thumbcardimage);
                    resizedThumb.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                continue;
            }
            Document doc = null;
            try {
                doc = Jsoup.connect(baseurl + id).get();
            } catch (Exception e) {
                System.out.println("Warning: Problem reading card (" + mappa.get(id) + ") infos from: " + baseurl + id + ", i will retry 2 times more...");
                try {
                    doc = Jsoup.connect(baseurl + id).get();
                } catch (Exception e2) {
                    System.out.println("Warning: Problem reading card (" + mappa.get(id) + ") infos from: " + baseurl + id + ", i will retry 1 time more...");
                    try {
                        doc = Jsoup.connect(baseurl + id).get();
                    } catch (Exception e3) {
                        System.err.println("Error: Problem reading card (" + mappa.get(id) + ") infos from: " + baseurl + id + ", i will not retry anymore...");
                        continue;
                    }
                }
            }
            if (doc == null) {
                System.err.println("Error: Problem reading card (" + mappa.get(id) + ") infos from: " + baseurl + id + ", i can't download it...");
                continue;
            }
            Elements divs = doc.select("body div");
            if (divs == null) {
                System.err.println("Error: Problem reading card (" + mappa.get(id) + ") infos from: " + baseurl + id + ", i can't download it...");
                continue;
            }

            int k;
            for (k = 0; k < divs.size(); k++)
                if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("card name"))
                    break;
            if (k >= divs.size()) {
                System.err.println("Error: Problem reading card (" + mappa.get(id) + ") infos from: " + baseurl + id + ", i can't download it...");
                continue;
            }
            String cardname = divs.get(k + 1).childNode(0).attributes().get("#text").replace("\r\n", "").trim();

            if(scryset.equals("S00")){
                String deckutrl = "https://deckmaster.info/card.php?multiverseid=";
                try{
                    doc = Jsoup.connect(deckutrl + id).get();
                } catch(Exception e) {
                    System.out.println("Warning: Problem reading card (" + mappa.get(id) + ") infos from: " + deckutrl  + id + ", i will retry 2 times more...");
                    try{
                        doc = Jsoup.connect(deckutrl + id).get();
                    } catch(Exception e2) {
                        System.out.println("Warning: Problem reading card (" + mappa.get(id) + ") infos from: " + deckutrl  + id + ", i will retry 1 time more...");
                        try{
                            doc = Jsoup.connect(deckutrl + id).get();
                        } catch(Exception e3) {
                            System.err.println("Error: Problem reading card (" + mappa.get(id) + ") infos from: " + deckutrl  + id + ", i will not retry anymore...");
                            continue;
                        }
                    }
                }
            } else if(targetres.equals("High")) {
                try {
                    doc = Jsoup.connect(imageurl + scryset.toLowerCase()).get();
                    Elements outlinks = doc.select("body a");
                    if (outlinks != null) {
                        for (int h = 0; h < outlinks.size(); h++) {
                            String linkcard = outlinks.get(h).attributes().get("href");
                            if (linkcard != null && linkcard.contains(cardname.toLowerCase().replace(" ", "-"))) {
                                try {
                                    doc = Jsoup.connect(linkcard).get();
                                    if (doc == null)
                                        continue;
                                    Elements metadata = doc.select("head meta");
                                    if (metadata != null) {
                                        for (int j = 0; j < metadata.size(); j++) {
                                            if (metadata.get(j).attributes().get("content").toLowerCase().contains(cardname.toLowerCase())) {
                                                h = outlinks.size();
                                                break;
                                            }
                                        }
                                    }
                                } catch (Exception ex) {
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Warning: Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will retry 2 times more...");
                    try {
                        doc = Jsoup.connect(imageurl + scryset.toLowerCase()).get();
                        Elements outlinks = doc.select("body a");
                        if (outlinks != null) {
                            for (int h = 0; h < outlinks.size(); h++) {
                                String linkcard = outlinks.get(h).attributes().get("href");
                                if (linkcard != null && linkcard.contains(cardname.toLowerCase().replace(" ", "-"))) {
                                    try {
                                        doc = Jsoup.connect(linkcard).get();
                                        if (doc == null)
                                            continue;
                                        Elements metadata = doc.select("head meta");
                                        if (metadata != null) {
                                            for (int j = 0; j < metadata.size(); j++) {
                                                if (metadata.get(j).attributes().get("content").toLowerCase().contains(cardname.toLowerCase())) {
                                                    h = outlinks.size();
                                                    break;
                                                }
                                            }
                                        }
                                    } catch (Exception ex) {
                                    }
                                }
                            }
                        }
                    } catch (Exception e2) {
                        System.out.println("Warning: Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will retry 1 time more...");
                        try {
                            doc = Jsoup.connect(imageurl + scryset.toLowerCase()).get();
                            Elements outlinks = doc.select("body a");
                            if (outlinks != null) {
                                for (int h = 0; h < outlinks.size(); h++) {
                                    String linkcard = outlinks.get(h).attributes().get("href");
                                    if (linkcard != null && linkcard.contains(cardname.toLowerCase().replace(" ", "-"))) {
                                        try {
                                            doc = Jsoup.connect(linkcard).get();
                                            if (doc == null)
                                                continue;
                                            Elements metadata = doc.select("head meta");
                                            if (metadata != null) {
                                                for (int j = 0; j < metadata.size(); j++) {
                                                    if (metadata.get(j).attributes().get("content").toLowerCase().contains(cardname.toLowerCase())) {
                                                        h = outlinks.size();
                                                        break;
                                                    }
                                                }
                                            }
                                        } catch (Exception ex) {
                                        }
                                    }
                                }
                            }
                        } catch (Exception e3) {
                            System.err.println("Error: Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will not retry anymore...");
                            continue;
                        }
                    }
                }
            } else {
                try {
                    doc = Jsoup.connect(imageurl + scryset.toLowerCase()).get();
                } catch (Exception e) {
                    System.out.println("Warning: Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will retry 2 times more...");
                    try {
                        doc = Jsoup.connect(imageurl + scryset.toLowerCase()).get();
                    } catch (Exception e2) {
                        System.out.println("Warning: Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will retry 1 time more...");
                        try {
                            doc = Jsoup.connect(imageurl + scryset.toLowerCase()).get();
                        } catch (Exception e3) {
                            System.err.println("Error: Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will not retry anymore...");
                            res = mappa.get(id) + " - " + set + File.separator + id + ".jpg\n" + res;
                            continue;
                        }
                    }
                }
            }

            if(doc == null){
                System.err.println("Error: Problem fetching card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will not download it...");
                continue;
            }

            Elements imgs = doc.select("body img");
            if (imgs == null) {
                System.err.println("Error: Problem fetching card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will not download it...");
                continue;
            }

            for (int i = 0; i < imgs.size(); i++) {
                String title = imgs.get(i).attributes().get("alt");
                if(title.isEmpty())
                    title = imgs.get(i).attributes().get("title");
                if (title.toLowerCase().contains(cardname.toLowerCase())) {
                    String CardImage = imgs.get(i).attributes().get("src");
                    if (CardImage.isEmpty())
                        CardImage = imgs.get(i).attributes().get("data-src");
                    URL url = new URL(CardImage);
                    HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                    if(httpcon == null) {
                        System.err.println("Error: Problem fetching card: " + mappa.get(id) + "-" + id + ", i will not download it...");
                        break;
                    }
                    httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
                    InputStream in = null;
                    try {
                        in = new BufferedInputStream(httpcon.getInputStream());
                    } catch (IOException ex) {
                        System.out.println("Warning: Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will retry 2 times more...");
                        try {
                            in = new BufferedInputStream(httpcon.getInputStream());
                        } catch (IOException ex2) {
                            System.out.println("Warning: Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will retry 1 time more...");
                            try {
                                in = new BufferedInputStream(httpcon.getInputStream());
                            } catch (IOException ex3) {
                                System.err.println("Error: Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall, i will not retry anymore...");
                                break;
                            }
                        }
                    }
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int n = 0;
                    while (-1 != (n = in.read(buf))) {
                        out.write(buf, 0, n);
                    }
                    out.close();
                    in.close();
                    byte[] response = out.toByteArray();
                    String cardimage = imgPath + File.separator + id + ".jpg";
                    String thumbcardimage = thumbPath + File.separator + id + ".jpg";
                    FileOutputStream fos = new FileOutputStream(cardimage);
                    fos.write(response);
                    fos.close();

                    Bitmap yourBitmap = BitmapFactory.decodeFile(cardimage);
                    Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, ImgX, ImgY, true);
                    try {
                        FileOutputStream fout = new FileOutputStream(cardimage);
                        resized.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap resizedThumb = Bitmap.createScaledBitmap(yourBitmap, ThumbX, ThumbY, true);
                    try {
                        FileOutputStream fout = new FileOutputStream(thumbcardimage);
                        resizedThumb.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String text = "";
                    for (k = 0; k < divs.size(); k++)
                        if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("card text"))
                            break;
                    if (k < divs.size()) {
                        Element tex = divs.get(k + 1);
                        for (int z = 0; z < divs.get(k + 1).childNodes().size(); z++) {
                            for (int u = 0; u < divs.get(k + 1).childNode(z).childNodes().size(); u++) {
                                if (divs.get(k + 1).childNode(z).childNode(u).childNodes().size() > 1) {
                                    for (int w = 0; w < divs.get(k + 1).childNode(z).childNode(u).childNodes().size(); w++) {
                                        if (divs.get(k + 1).childNode(z).childNode(u).childNode(w).hasAttr("alt")) {
                                            String newtext = divs.get(k + 1).childNode(z).childNode(u).childNode(w).attributes().get("alt").trim();
                                            newtext = newtext.replace("Green", "{G}");
                                            newtext = newtext.replace("White", "{W}");
                                            newtext = newtext.replace("Black", "{B}");
                                            newtext = newtext.replace("Blue", "{U}");
                                            newtext = newtext.replace("Red", "{R}");
                                            newtext = newtext.replace("Tap", "{T}");
                                            text = text + newtext;
                                        } else
                                            text = text + " " + divs.get(k + 1).childNode(z).childNode(u).childNode(w).toString().replace("\r\n", "").trim() + " ";
                                        text = text.replace("} .", "}.");
                                        text = text.replace("} :", "}:");
                                        text = text.replace("} ,", "},");
                                    }
                                } else {
                                    if (divs.get(k + 1).childNode(z).childNode(u).hasAttr("alt")) {
                                        String newtext = divs.get(k + 1).childNode(z).childNode(u).attributes().get("alt").trim();
                                        newtext = newtext.replace("Green", "{G}");
                                        newtext = newtext.replace("White", "{W}");
                                        newtext = newtext.replace("Black", "{B}");
                                        newtext = newtext.replace("Blue", "{U}");
                                        newtext = newtext.replace("Red", "{R}");
                                        newtext = newtext.replace("Tap", "{T}");
                                        text = text + newtext;
                                    } else
                                        text = text + " " + divs.get(k + 1).childNode(z).childNode(u).toString().replace("\r\n", "").trim() + " ";
                                    text = text.replace("} .", "}.");
                                    text = text.replace("} :", "}:");
                                    text = text.replace("} ,", "},");
                                }
                                if (z > 0 && z < divs.get(k + 1).childNodes().size() - 1)
                                    text = text + " -- ";
                                text = text.replace("<i>", "");
                                text = text.replace("</i>", "");
                                text = text.replace("<b>", "");
                                text = text.replace("</b>", "");
                                text = text.replace(" -- (", " (");
                                text = text.replace("  ", " ");
                            }
                        }
                    }
                    if (hasToken(id) && ((text.trim().toLowerCase().contains("create") && text.trim().toLowerCase().contains("creature token")) || (text.trim().toLowerCase().contains("put") && text.trim().toLowerCase().contains("token")))) {
                        boolean tokenfound = false;
                        String arrays[] = text.trim().split(" ");
                        String nametoken = "";
                        String nametocheck = "";
                        String tokenstats = "";
                        for (int l = 1; l < arrays.length - 1; l++) {
                            if (arrays[l].equalsIgnoreCase("creature") && arrays[l + 1].toLowerCase().contains("token")) {
                                nametoken = arrays[l - 1];
                                if (l - 3 > 0)
                                    tokenstats = arrays[l - 3];
                                if (!tokenstats.contains("/")) {
                                    if (l - 4 > 0)
                                        tokenstats = arrays[l - 4];
                                }
                                if (!tokenstats.contains("/")) {
                                    if (l - 5 > 0)
                                        tokenstats = arrays[l - 5];
                                }
                                if (!tokenstats.contains("/")) {
                                    if (l - 6 > 0)
                                        tokenstats = arrays[l - 6];
                                }
                                if (!tokenstats.contains("/")) {
                                    if (l - 7 > 0)
                                        tokenstats = arrays[l - 7];
                                }
                                if (nametoken.equalsIgnoreCase("artifact")) {
                                    if (l - 2 > 0)
                                        nametoken = arrays[l - 2];
                                    if (l - 4 > 0)
                                        tokenstats = arrays[l - 4];
                                    if (!tokenstats.contains("/")) {
                                        if (l - 5 > 0)
                                            tokenstats = arrays[l - 5];
                                    }
                                    if (!tokenstats.contains("/")) {
                                        if (l - 6 > 0)
                                            tokenstats = arrays[l - 6];
                                    }
                                    if (!tokenstats.contains("/")) {
                                        if (l - 7 > 0)
                                            tokenstats = arrays[l - 7];
                                    }
                                    if (!tokenstats.contains("/")) {
                                        if (l - 8 > 0)
                                            tokenstats = arrays[l - 8];
                                    }
                                }
                                if (!tokenstats.contains("/"))
                                    tokenstats = "";
                                break;
                            } else if (arrays[l].equalsIgnoreCase("put") && arrays[l + 3].toLowerCase().contains("token")) {
                                nametoken = arrays[l + 2];
                                for (int j = 1; j < arrays.length - 1; j++) {
                                    if (arrays[j].contains("/"))
                                        tokenstats = arrays[j];
                                }
                                break;
                            }
                        }
                        String specialtokenurl = getSpecialTokenUrl(id + "t");
                        Elements imgstoken;
                        if (!specialtokenurl.isEmpty()) {
                            try {
                                doc = Jsoup.connect(imageurl + scryset.toLowerCase()).get();
                            } catch (Exception ex) {
                                System.err.println("Error: Problem occurring while searching for token: " + nametoken + "-" + id + "t, i will not download it...");
                                break;
                            }
                            if (doc == null)
                                break;
                            imgstoken = doc.select("body img");
                            if (imgstoken == null)
                                break;
                            tokenfound = true;
                        } else {
                            if (nametoken.isEmpty() || tokenstats.isEmpty()) {
                                tokenfound = false;
                                if (nametoken.isEmpty())
                                    nametoken = "Unknown";
                                nametocheck = mappa.get(id);
                                doc = Jsoup.connect(imageurl + scryset.toLowerCase()).get();
                            } else {
                                try {
                                    doc = findTokenPage(imageurl, nametoken, scryset, availableSets, tokenstats);
                                    tokenfound = true;
                                    nametocheck = nametoken;
                                } catch (Exception e) {
                                    tokenfound = false;
                                    nametocheck = mappa.get(id);
                                    doc = Jsoup.connect(imageurl + scryset.toLowerCase()).get();
                                }
                            }
                            if (doc == null)
                                break;
                            imgstoken = doc.select("body img");
                            if (imgstoken == null)
                                break;
                        }
                        for (int p = 0; p < imgstoken.size(); p++) {
                            String titletoken = imgstoken.get(p).attributes().get("alt");
                            if (titletoken.isEmpty())
                                titletoken = imgstoken.get(p).attributes().get("title");
                            if (titletoken.toLowerCase().contains(nametocheck.toLowerCase())) {
                                String CardImageToken = imgstoken.get(p).attributes().get("src");
                                if (CardImageToken.isEmpty())
                                    CardImageToken = imgstoken.get(p).attributes().get("data-src");
                                URL urltoken = new URL(CardImageToken);
                                if (!specialtokenurl.isEmpty())
                                    urltoken = new URL(specialtokenurl);
                                HttpURLConnection httpcontoken = (HttpURLConnection) urltoken.openConnection();
                                if (httpcontoken == null) {
                                    System.err.println("Error: Problem downloading token: " + nametoken + "-" + id + "t, i will not download it...");
                                    break;
                                }
                                httpcontoken.addRequestProperty("User-Agent", "Mozilla/4.76");
                                InputStream intoken = null;
                                try {
                                    intoken = new BufferedInputStream(httpcontoken.getInputStream());
                                } catch (IOException ex) {
                                    System.out.println("Warning: Problem downloading token: " + nametoken + "-" + id + "t, i will retry 2 times more...");
                                    try {
                                        intoken = new BufferedInputStream(httpcontoken.getInputStream());
                                    } catch (IOException ex2) {
                                        System.out.println("Warning: Problem downloading token: " + nametoken + "-" + id + "t, i will retry 1 time more...");
                                        try {
                                            intoken = new BufferedInputStream(httpcontoken.getInputStream());
                                        } catch (IOException ex3) {
                                            System.err.println("Error: Problem downloading token: " + nametoken + "-" + id + "t, i will not retry anymore...");
                                            break;
                                        }
                                    }
                                }
                                ByteArrayOutputStream outtoken = new ByteArrayOutputStream();
                                byte[] buftoken = new byte[1024];
                                int ntoken = 0;
                                while (-1 != (ntoken = intoken.read(buftoken))) {
                                    outtoken.write(buftoken, 0, ntoken);
                                }
                                outtoken.close();
                                intoken.close();
                                byte[] responsetoken = outtoken.toByteArray();
                                String tokenimage = imgPath + File.separator + id + "t.jpg";
                                String tokenthumbimage = thumbPath + File.separator + id + "t.jpg";
                                if (!tokenfound && !id.equals("464007t")) {
                                    System.err.println("Error: Problem downloading token: " + nametoken + " (" + id + "t) i will use the same image of its source card");
                                }
                                FileOutputStream fos2 = new FileOutputStream(tokenimage);
                                fos2.write(responsetoken);
                                fos2.close();

                                Bitmap yourBitmapToken = BitmapFactory.decodeFile(tokenimage);
                                Bitmap resizedToken = Bitmap.createScaledBitmap(yourBitmapToken, ImgX, ImgY, true);
                                try {
                                    FileOutputStream fout = new FileOutputStream(tokenimage);
                                    resizedToken.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Bitmap resizedThumbToken = Bitmap.createScaledBitmap(yourBitmapToken, ThumbX, ThumbY, true);
                                try {
                                    FileOutputStream fout = new FileOutputStream(tokenthumbimage);
                                    resizedThumbToken.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        try {
            try {
                File oldzip = new File(destinationPath + File.separator + set + File.separator + set + ".zip");
                oldzip.delete();
            } catch (Exception e) {
            }
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(CompressionMethod.STORE);
            File folder = new File(destinationPath + set + File.separator);
            File[] listOfFile = folder.listFiles();
            net.lingala.zip4j.ZipFile zipped = new net.lingala.zip4j.ZipFile(destinationPath + File.separator + set + File.separator + set + ".zip");
            for (int i = 0; i < listOfFile.length; i++) {
                if (listOfFile[i].isDirectory()) {
                    zipped.addFolder(listOfFile[i], zipParameters);
                } else {
                    zipped.addFile(listOfFile[i], zipParameters);
                }
            }
            File destFolder = new File(destinationPath + set + File.separator);
            listOfFiles = destFolder.listFiles();
            for (int u = 0; u < listOfFiles.length; u++) {
                if (!listOfFiles[u].getName().contains(".zip")) {
                    if (listOfFiles[u].isDirectory()) {
                        File[] listOfSubFiles = listOfFiles[u].listFiles();
                        for (int j = 0; j < listOfSubFiles.length; j++)
                            listOfSubFiles[j].delete();
                    }
                    listOfFiles[u].delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }
}
