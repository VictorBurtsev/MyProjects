package ru.niivk.burtsev;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class webImageParser implements Runnable {
    private int pageStart;
    private int pageEnd;

    private webImageParser(final int pageStart, final int pageEnd) {
        this.pageStart = pageStart;
        this.pageEnd = pageEnd;
        new Thread(this).start();
    }

    public void run() {
        System.out.printf("Поиск с %-4d по %-4d страницу...\n", pageStart, pageEnd);
        webImageParser.pageRange(pageStart, pageEnd);
    }

    public static void main(final String... args) {

        int pageStart = 1;
        int pageEnd = 2500;
        final int numberThreads = 250;
        final int numberEnd = pageEnd;
        final int numberCount = pageEnd / numberThreads;
        pageEnd /= numberThreads;

        do {
            new webImageParser(pageStart, pageEnd);
            pageStart += numberCount;
            pageEnd += numberCount;
        } while (pageEnd <= numberEnd);
    }

    private static void pageRange(final int startPage, final int endPage) {

        for (int i = startPage; i <= endPage; i++) {
            webImageParser.imageSave(webImageParser.getImageURL(i, "Девушка дня"), i, "urod.ru/", "girl");
            webImageParser.imageSave(webImageParser.getImageURL(i, "Девушки дня"), i, "urod.ru/", "girls");
        }
    }

    private static String getImageURL(final int page, final String patternContext) {
        StringBuilder HTMLText = new StringBuilder(100_000);

        try {
            URL hp = new URL("http://urod.ru/newsall/" + page + "/");
            URLConnection hpCon = hp.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(hpCon.getInputStream(), "utf-8"));
            int i;
            while (((i = reader.read())) != -1)
                HTMLText.append((char) i);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchContext(HTMLText, page, patternContext);
    }

    private static String searchContext(final StringBuilder HTMLText, final int page, final String patternContext) {
        Matcher mat2 = Pattern.compile(patternContext).matcher(HTMLText);

        while (mat2.find()) {

            StringBuilder urlString = new StringBuilder(500);

            for (int i = mat2.start(); i < mat2.start() + 500; i++)
                urlString.append(HTMLText.charAt(i));

            Matcher mat4 = Pattern.compile("\\s*(?i)src\\s*=\\s*(\\\"([^\"]*\\\")|'[^']*'|([^'\">\\s]+))").matcher(urlString);

            while (mat4.find()) {
                String str = mat4.group().replaceAll("&#58;", ":");
                str = str.replaceAll("&#46;", ".");
                str = str.replaceAll("src=", "");
                str = str.replaceAll("\"", "");
                str = str.replaceAll("'", "");

                System.out.printf("Найдена %s cо страницы %-4d %-100s\n", patternContext, page, str);

                return str;
            }
        }
        return null;
    }

    private static void imageSave(final String urlName, final int pageNumber, final String sourcePath, final String fileName) {
        if (urlName != null) {
            try {
                URL url = new URL(urlName);
                URLConnection conn = url.openConnection();
                int i;

                try (BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                     FileOutputStream fos = new FileOutputStream(sourcePath + fileName + pageNumber + ".jpg")) {

                    while ((i = bis.read()) != -1)
                        fos.write(i);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //else System.out.printf("На странице %s ничего не найдено\n", pageNumber);
    }
}