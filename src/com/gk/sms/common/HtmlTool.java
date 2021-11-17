package com.gk.sms.common;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

public class HtmlTool {

    public static String getRanUserAgent() {
        String userAgent = "Windows NT 5.1) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.77 Safari/534.24";
        try {
            userAgent = USER_AGENT[buildRandomUserAgent()];
        } catch (Exception e) {
        }

        return userAgent;
    }

    private static int buildRandomUserAgent() {
        int ran = 0;
        while (true) {
            ran = (int) (Math.random() * 10);
            if (ran < USER_AGENT.length) {
                break;
            }
        }
        return ran;
    }
    private static final String[] USER_AGENT = {
        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 1.1.4322; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET4.0C; .NET4.0E; InfoPath.3)",
        "Opera/9.80 (Windows NT 5.1; U; Edition Campaign 04; en) Presto/2.7.62 Version/11.01",
        "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.77 Safari/534.24",
        "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13",
        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)",
        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)",
        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)",
        "msnbot/1.1 (+http://search.msn.com/msnbot.htm)",
        "Mozilla/5.0 (compatible; Yahoo! Slurp; http://help.yahoo.com/help/us/ysearch/slurp)",
        "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"
    };

    public static final String html2text2(String html) {
        EditorKit kit = new HTMLEditorKit();
        Document doc = kit.createDefaultDocument();
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        try {
            Reader reader = new StringReader(html);
            kit.read(reader, doc, 0);
            return doc.getText(0, doc.getLength());
        } catch (IOException | BadLocationException e) {
            return "";
        }
    }

    public static String html2text(String contentsHtml) throws Exception {
        StringBuilder buff = new StringBuilder(contentsHtml.length());
        char c;
        boolean intag = false;
        for (int i = 0; i < contentsHtml.length(); i++) {
            c = contentsHtml.charAt(i);
            if ((c == '<') && ((contentsHtml.charAt(i + 1) == 'a') || (contentsHtml.charAt(i + 1) == 'A'))) {
                intag = false;
            } else if ((c == '<') && (contentsHtml.charAt(i + 1) == '/') && ((contentsHtml.charAt(i + 2) == 'a') || (contentsHtml.charAt(i + 2) == 'A'))) {
                intag = false;
            } else if (c == '<') {
                intag = true;
            }
            if (!intag) {
                buff.append(c);
            }
            if (c == '>') {
                intag = false;
            }
        }
        return buff.toString();
    }

    /**
     * parse String ve theo dinh dang plain text(chi la cac the don thuan ko co
     * css) param return
     *
     * @param contentsHtml
     * @return String
     * @throws java.lang.Exception
     */
    public static String htmlPlainTextAll(String contentsHtml) throws Exception {
        String[][] complie = {
            {"<!--([^>])*-->", ""}, //<comment>
            {"<( )*table([^>])*>", ""}, //<table>
            {"<( )*/table([^>])*>", ""}, //</table>
            {"font-face([^>])*; }", ""}, //</table>

            {"<([^>])*style([^>])*>", "<style>"}, //  <style  >
            {"<style>([^>])*></style>", ""}, //  <style  >

            {"<( )*tbody([^>])*>", ""}, //<tbody>
            {"<( )*/tbody([^>])*>", ""}, //</tbody>

            {"<( )*h\\d([^>])*>", ""}, //<h?>
            {"<( )*/h\\d([^>])*>", "<br/>"}, //</h?>

            // {"<( )*tr([^>])*>","<p>"},      //<tr>   Thay the bang <p>
            {"<( )*tr([^>])*>", ""}, //<tr>
            // {"<( )*/tr([^>])*>","</p>"},    //</tr>  Thay the bang </p>
            {"<( )*/tr([^>])*>", "<br/>"}, //</tr>

            {"<( )*td([^>])*>", ""}, //<td>
            {"<( )*/td([^>])*>", " "}, //</td>

            // {"<( )*div([^>])*>","<div>"},   //<div>
            {"<( )*div([^>])*>", ""}, //<div>
            {"<( )*/div([^>])*>", "<br/>"}, //<div>

            {"<( )*embed([^>])*>", ""}, //<embed>
            {"<( )*/embed([^>])*>", " "}, //</embed>

            {"<( )*center([^>])*>", ""}, //<center>
            {"<( )*/center([^>])*>", " "}, //</center>
            //  {"<( )*p([^>])*>","<p>"},       //<p>
            {"<( )*p([^>])*>", ""}, //<p>
            {"<( )*/p([^>])*>", "<br/>"}, //<p>

            {"<o:p([^>])*>", ""}, //
            {"</o:p>", "<br/>"}, //
            {"<[a-z]:([^>])*>", ""}, //         The La tu Define Vidu dantri se co cac loai the kieu nay
            {"</[a-z]:([^>])*>", ""}, //    Dong The La tu Define Vidu dantri se co cac loai the kieu nay

            {"<( )*blockquote([^>])*>", ""}, //<blockquote>
            {"<( )*/blockquote([^>])*>", " "}, //</blockquote>

            {"<( )*span([^>])*>", ""}, //<span>
            {"<( )*/span([^>])*>", " "}, //</span>

            {"<( )*font([^>])*>", ""}, //<font>
            {"<( )*/font([^>])*>", " "}, //</font>

            {"<( )*em([^>])*>", ""}, //<font>
            {"<( )*/em([^>])*>", " "}, //</font>
            {"<( )*li([^>])*>", ""}, //<li>
            {"<( )*/li([^>])*>", ""}, //</li>

            {"<( )*st([^>])*>", " "}, //</st>
            {"<( )*/st([^>])*>", " "}, //</st>

            //  {"<( )*(.?)*( )*class=([^>])*>", ""}, //<p  class=  > </>
            //{"<( )*br([^>])*>",""},               //<br>
            {"<( )*br([^>])*>", "<br/>"}, //<br>
            {"<( )*br/([^>])*>(\\s)*<( )*br/([^>])*>", "<br/>"},
            //{"<( )*br/([^>])*>( )*<( )*br/([^>])*>","<br/>"},

            {"<( )*a([^>])*>", ""}, //<a>
            {"<( )*/a([^>])*>", " "}, //</a>
            {"<( )*input([^>])*>", " "}, //<input>

            {"<( )*strong([^>])*><br/>", "<strong>"}, //
            {"<strong>( )*</strong>", ""}, //</strong>
            {"<br/>( )*<br/>", "<br/>"},
            {"<( )*b ([^>])*>", "<b>"},
            {"<b>", ""},
            {"</b>", ""},
            //  {"<( )*b([^>])*><br/>", "<b>"}, //
            {"<( )*i ([^>])*>", "<i>"}, //
            {"<( )*em([^>])*><br/>", "<em>"}, //
            {"<( )*img.+src=\"(.?)*\"([^>])*>", ""} //<img>
        };
        for (int i = 0; i < complie.length; i++) {
            contentsHtml = contentsHtml.replaceAll(complie[i][0], complie[i][1]);
        }

//        String sImgcompile = "<( )*img.+src=\"(.?)*\"([^>])*>";
//        if (!complie.equals("")) {
//            Pattern pattern = Pattern.compile(sImgcompile);
//            Matcher matcher = pattern.matcher(contentsHtml);
//            int idx = 0;
//            String link = "";
//            while (matcher.find()) {
//                String s4 = matcher.group(0);
////                System.out.println("-->"+s4);
//                idx = s4.indexOf("src=\"");
////                System.out.println("idx: "+idx);
//                if (idx > 0) {
//                    link = s4.substring(idx + 5); //+5 vi idx la vi tri cua ky tu s, nen pai tien 5 phan tu nua theo do dai cua thang src="
//                    link = link.substring(0, link.indexOf("\""));
//                    link = "<div align='center'><img class=\"pnews\" src=\"" + link + "\" alt =\"Bạn đợi chút...\"/></div>";
////                    System.out.println("link replace-->:"+link);
//                    contentsHtml = contentsHtml.replace(s4, link);
//                }
//            }
//        }
        return contentsHtml;
    }

    public String htmlCut(String contentsHtml) {
        int idx_cut = 200;
        HtmlTool web = new HtmlTool();
//        System.out.println("length(): " + contentsHtml.length());
        if (contentsHtml.length() > idx_cut) {
            String fstText = contentsHtml.substring(0, idx_cut);
            //cat doan dang sau ra
            String lstText = contentsHtml.substring(idx_cut);
            //lay 2 vi tri cua < va > ra de so sanh voi nhau
            int idx_lon = lstText.indexOf(">");
            int idx_be = lstText.indexOf("<");
            int idx_cheo = lstText.indexOf("/");
//            System.out.println("--idx_lon--" + idx_lon + "--idx_be:--" + idx_be + "--idx_cheo--:" + idx_cheo);
            if (idx_lon < idx_be) {
                // co dang: x__>____<__ =>vi tri cua x: dang o trong 1 tag
                //kiem tra xem dang trong tag dong' hay tag mo </x> or <x />
                if (idx_cheo != -1 && idx_cheo < idx_lon) {
                    // <x />
                    return contentsHtml.substring(0, idx_cut + idx_lon + 1) + "*****chen vao giua*1*****" + web.htmlCut(contentsHtml.substring(idx_cut + idx_lon + 1));
                } else {
                    // __x>
                    idx_cheo = fstText.lastIndexOf("/");
                    idx_be = fstText.lastIndexOf("<");
                    idx_lon = fstText.lastIndexOf(">");
                    if (idx_lon < idx_be && idx_be < idx_cheo) {
                        // _>__<_/__x => trong the dong
                        return contentsHtml.substring(0, lstText.indexOf(">")) + "*****chen vao giua*2*****" + web.htmlCut(contentsHtml.substring(lstText.indexOf(">")));
                    } else if (idx_cheo < idx_be && idx_lon < idx_be) {
                        // ___<__x => trong the mo
                        return contentsHtml.substring(0, idx_be) + "*****chen vao giua*3*****" + web.htmlCut(contentsHtml.substring(idx_be));
                    } else {
                        System.out.println("dinh dang khi j day chua parse dc");
                    }
                }
            } else {
                // co dang: x__<____>__ =>vi tri cua x: dang o ngoai tag
                if (idx_be < idx_cheo && idx_cheo < idx_lon) {
                    // x__<_/_>_
                    return contentsHtml.substring(0, idx_cut + idx_lon + 1) + "*****chen vao giua*4*****" + web.htmlCut(contentsHtml.substring(idx_cut + idx_lon + 1));
                } else {
                    // x__<__>_
                    return contentsHtml.substring(0, idx_cut + idx_be) + "*****chen vao giua*5*****" + web.htmlCut(contentsHtml.substring(idx_cut + idx_be));
                }
            }
        } else {
            return contentsHtml;
        }
        return "";
    }

    /**
     * parse html de chen cat ra cac trang param contentsHtml param index_cut
     * return
     *
     * @param contentsHtml
     * @param index_cut
     * @return
     */
    public String htmlParse(String contentsHtml, int index_cut) {
        String sReturn = "";
        try {
            int idx_cut = index_cut;
            int iCuted = 0, iLengCutLast = 100;
            HtmlTool web = new HtmlTool();
//            System.out.println("length(): " + contentsHtml.length()+"--content:--"+contentsHtml.substring(0,19));
            if (contentsHtml.length() > idx_cut) {
                //bat dau parse neu length cua content > so luong cat
                String fstText = contentsHtml.substring(0, idx_cut);    //cat doan dang truoc ra
                String lstText = contentsHtml.substring(idx_cut);       //cat doan dang sau ra
                int idx_br_lst = lstText.indexOf("<br/>");     //vi tri <br/> dau tien cua dau doan sau
                int idx_br_fst = fstText.lastIndexOf("<br/>"); //vi tri <br/> cuoi cung cua doan truoc
                int idx_lst_a = lstText.indexOf("</a>");     //vi tri </a> dau tien cua dau doan sau

                if (idx_br_fst == -1 || idx_br_lst <= idx_br_fst) {
                    //cat ve dang sau
                    int idx_img_lst = lstText.indexOf("<img"); //vi tri <img /> dau tien cua  doan sau
                    int idx_img_fst = fstText.lastIndexOf("<img"); //vi tri <img /> cuoi cung cua doan dau
                    if ((idx_img_lst > -1 && idx_img_lst < idx_br_lst) || idx_img_fst > idx_br_fst) {
                        //neu doan sau co <img va idx <img  <  idx <br dau tien cua doan sau--> next them 2 <br/> nua roi moi cat
                        //hoac idx <img cuoi cung doan dau  >  <br/> cuoi cung cua doan dau --> next them 2 <br/> nua roi moi cat
                        int idx = idx_cut + idx_br_lst;
                        fstText = contentsHtml.substring(0, idx + 5);
                        lstText = contentsHtml.substring(idx + 5);
                        idx = idx + lstText.indexOf("<br/>");
                        //cat lai tu dau toi <br/> (trong doan nay da chua <img/>)
                        iCuted = (idx + 10);
                        System.out.println("gia tri cut 11:" + iCuted);
                        if (iCuted > contentsHtml.length()) {
//                            System.out.println("lon hon:");
                            iCuted = contentsHtml.length();
                        }
                        String lastSubString = contentsHtml.substring(iCuted);
                        if (lastSubString.length() < iLengCutLast && lastSubString.indexOf("<img") == -1) {
                            //neu doan dang sau chi la text co length < 20 va ko chua <img> thi se ko cat
                            sReturn = contentsHtml;
                        } else {
                            //van cat binh thuong
                            sReturn = contentsHtml.substring(0, iCuted) + "--chen vao giua--" + web.htmlParse(lastSubString, idx_cut);
                        }
                    } else {
                        //<img/> ko nam trong doan
                        iCuted = (idx_cut + idx_br_lst + 5);
                        System.out.println("gia tri cut 22:" + iCuted);
                        if (iCuted > contentsHtml.length()) {
//                            System.out.println("lon hon:");
                            iCuted = contentsHtml.length();
                        }
                        String lastSubString = contentsHtml.substring(iCuted);
                        if (lastSubString.length() < iLengCutLast && lastSubString.indexOf("<img") == -1) {
                            //neu doan dang sau chi la text co length < 20 va ko chua <img thi se ko cat
                            sReturn = contentsHtml;
                        } else {
                            //van cat binh thuong
                            sReturn = contentsHtml.substring(0, iCuted) + "--chen vao giua--" + web.htmlParse(lastSubString, idx_cut);
//                        sReturn = contentsHtml.substring(0, iCuted) + "--chen vao giua--" + web.htmlParse(contentsHtml.substring(iCuted),idx_cut);
                        }
                    }
                } else {
                    //cat ve dang truoc
//                    System.out.println("cat dang truoc");
                    int idx_img = fstText.lastIndexOf("<img");
                    if (idx_img > idx_br_fst) {
                        //<img/> nam trong doan
//                        System.out.println("22--<img/> nam trong doan");
                        fstText = contentsHtml.substring(0, idx_cut + idx_br_lst + 5);  //cat lai tu dau toi <br/> (trong doan nay da chua <img/>)
                        lstText = contentsHtml.substring(idx_cut + idx_br_lst + 5);
//                        System.out.println("33--fstText:" + (idx_cut + idx_lst + 5) + "--lstText:" + (idx_cut + idx_lst + 5));
                        System.out.println("gia tri cut 33:" + (lstText.indexOf("<br/>") + 5));
                        iCuted = (lstText.indexOf("<br/>") + 5);
//                        System.out.println("gia tri cut 22:" + iCuted);
                        if (iCuted > contentsHtml.length()) {
//                            System.out.println("lon hon:");
                            iCuted = contentsHtml.length();
                        }
                        String lastSubString = contentsHtml.substring(iCuted);
                        if (lastSubString.length() < iLengCutLast && lastSubString.indexOf("<img") == -1) {
                            //neu doan dang sau chi la text co length < 20 va ko chua <img thi se ko cat
                            sReturn = contentsHtml;
                        } else {
                            //van cat binh thuong
                            sReturn = contentsHtml.substring(0, iCuted) + "--chen vao giua--" + web.htmlParse(lastSubString, idx_cut);
                        }
//                        sReturn = contentsHtml.substring(0, iCuted) + "--chen vao giua--" + web.htmlParse(contentsHtml.substring(iCuted),idx_cut);
                    } else {
                        //<img/> ko nam trong doan
//                        System.out.println("22--<img/> ko nam trong doan");
                        System.out.println("gia tri cut 44:" + (idx_cut + idx_br_lst + 5));
                        iCuted = (idx_cut + idx_br_lst + 5);
//                        System.out.println("gia tri cut 22:" + iCuted);
                        if (iCuted > contentsHtml.length()) {
//                            System.out.println("lon hon:");
                            iCuted = contentsHtml.length();
                        }
                        String lastSubString = contentsHtml.substring(iCuted);
                        if (lastSubString.length() < iLengCutLast && lastSubString.indexOf("<img") == -1) {
                            //neu doan dang sau chi la text co length < 20 va ko chua <img thi se ko cat
                            sReturn = contentsHtml;
                        } else {
                            //van cat binh thuong
                            sReturn = contentsHtml.substring(0, iCuted) + "--chen vao giua--" + web.htmlParse(lastSubString, idx_cut);
                        }
//                        sReturn = contentsHtml.substring(0, iCuted) + "--chen vao giua--" + web.htmlParse(contentsHtml.substring(iCuted),idx_cut);
                    }
                }
            } else {
                sReturn = contentsHtml;
            }
        } catch (Exception e) {
            System.out.println("loi htmlParse(): " + e.getMessage());
        }
        return sReturn;
    }

    /**
     * sua lai loi thieu the &lt;b&gt; va &lt;i&gt; param sHtml return
     *
     * @param sHtml
     * @return
     */
    public String invailHtml(String sHtml) {
        String sReturn = "";
        int iBOpen = 0, iBEnd = 0, iIOpen = 0, iIEnd = 0;
        try {
            iBOpen = countTagInHTML(sHtml, "<b>");
            iBEnd = countTagInHTML(sHtml, "</b>");

            iIOpen = countTagInHTML(sHtml, "<i>");
            iIEnd = countTagInHTML(sHtml, "</i>");
            System.out.println("iBOpen-->" + iBOpen + "--iBEnd-->" + iBEnd + "--iIOpen-->" + iIOpen + "--iIEnd-->" + iIEnd);
            if (iBOpen != iBEnd && iIOpen == iIEnd) {           //thieu the <b> con the <i> du
                System.out.println("11111111---->");
                //kt so the mo va the dong cua the <b> chenh lech
                if (iBOpen > iBEnd) {           //mo nhieu hon dong --> them the dong o cuoi
                    sReturn = sHtml;
                    for (int i = 0; i < (iBOpen - iBEnd); i++) {
                        sReturn += "</b>";
                    }
                } else if (iBOpen < iBEnd) {        //mo it hon dong --> them the mo o dau
                    for (int i = 0; i < (iBEnd - iBOpen); i++) {
                        sReturn += "<b>";
                    }
                    sReturn += sHtml;
                }
            } else if (iBOpen == iBEnd && iIOpen != iIEnd) {        //thieu the <i> con the <b> du
                System.out.println("2222222222---->");
                //kt so the mo va the dong cua the <i> chenh lech
                if (iIOpen > iIEnd) {           //mo nhieu hon dong --> them the dong o cuoi
//                    System.out.println("mo nhieu hon dong--->aaaaaa");
                    sReturn = sHtml;
                    for (int i = 0; i < (iIOpen - iIEnd); i++) {
                        sReturn += "</i>";
                    }
                } else if (iIOpen < iIEnd) {     //mo it hon dong --> them the mo o dau
//                    System.out.println("mo it hon dong--->bbbb");
                    for (int i = 0; i < (iIEnd - iIOpen); i++) {
                        sReturn += "<i>";
                    }
                    sReturn += sHtml;
                }
            } else if (iBOpen != iBEnd && iIOpen != iIEnd) {    //thieu ca <i> lan <b>
                System.out.println("3333333---->");
                if (iBOpen > iBEnd && iIOpen > iIEnd) {       // ca 2 deu the mo nhieu hon dong
                    int iBEndLast = sHtml.lastIndexOf("</b>");
                    int iIEndLast = sHtml.lastIndexOf("</i>");
                    int iBOpenLast = sHtml.lastIndexOf("<b>");
                    int iIOpenLast = sHtml.lastIndexOf("<i>");
                    if (iBOpenLast > iBEndLast && iIOpenLast > iIEndLast) { //neu la </i> <i> && </b><b> 
                        sReturn = sHtml;
                        if (iBOpenLast > iIOpenLast) {                                    //&& <i><b>
                            for (int i = 0; i < (iBOpen - iBEnd); i++) {
                                sReturn += "</b>";
                            }
                            for (int i = 0; i < (iIOpen - iIEnd); i++) {
                                sReturn += "</i>";
                            }
                        } else {                                                          //&& <b><i>
                            for (int i = 0; i < (iIOpen - iIEnd); i++) {
                                sReturn += "</i>";
                            }
                            for (int i = 0; i < (iBOpen - iBEnd); i++) {
                                sReturn += "</b>";
                            }
                        }
                    } else if (iBOpenLast > iBEndLast && iIOpenLast < iIEndLast) {  //  </b><b> && <i></i>
                        sReturn = sHtml;
                        for (int i = 0; i < (iBOpen - iBEnd); i++) {
                            sReturn += "</b>";
                        }
                        for (int i = 0; i < (iIOpen - iIEnd); i++) {
                            sReturn += "</i>";
                        }
                    } else if (iBOpenLast < iBEndLast && iIOpenLast > iIEndLast) {  //  <b></b> && </i><i>
                        sReturn = sHtml;
                        for (int i = 0; i < (iIOpen - iIEnd); i++) {
                            sReturn += "</i>";
                        }
                        for (int i = 0; i < (iBOpen - iBEnd); i++) {
                            sReturn += "</b>";
                        }
                    } else if (iBOpenLast < iBEndLast && iIOpenLast < iIEndLast) {  //  <b></b> && <i></i>
                        //ko hieu no bi cut nham o dau :|
                        sReturn = sHtml;
                        for (int i = 0; i < (iIOpen - iIEnd); i++) {
                            sReturn += "</i>";
                        }
                        for (int i = 0; i < (iBOpen - iBEnd); i++) {
                            sReturn += "</b>";
                        }
                    }
                } else if (iBOpen < iBEnd && iIOpen < iIEnd) {    // ca 2 deu the mo it hon dong
                    int iBOpenFisrt = sHtml.indexOf("<b>");
                    int iBEndFirst = sHtml.indexOf("</b>");
                    int iIOpenFirst = sHtml.indexOf("<i>");
                    int iIEndFirst = sHtml.indexOf("</i>");
                    if (iIEndFirst < iIOpenFirst && iBEndFirst < iBOpenFisrt) {     // </i> <i> && </b><b>
                        if (iBEndFirst < iIEndFirst) {             // </b></i>
                            for (int i = 0; i < (iIEnd - iIOpen); i++) {
                                sReturn += "<i>";
                            }
                            for (int i = 0; i < (iBEnd - iBOpen); i++) {
                                sReturn += "<b>";
                            }
                            sReturn += sHtml;
                        } else {                                    // </i></b>
                            for (int i = 0; i < (iBEnd - iBOpen); i++) {
                                sReturn += "<b>";
                            }
                            for (int i = 0; i < (iIEnd - iIOpen); i++) {
                                sReturn += "<i>";
                            }
                            sReturn += sHtml;
                        }
                    } else if (iIEndFirst < iIOpenFirst && iBEndFirst > iBOpenFisrt) {    // </i> <i> && <b></b>
                        for (int i = 0; i < (iBEnd - iBOpen); i++) {
                            sReturn += "<b>";
                        }
                        for (int i = 0; i < (iIEnd - iIOpen); i++) {
                            sReturn += "<i>";
                        }
                        sReturn += sHtml;
                    } else if (iIEndFirst > iIOpenFirst && iBEndFirst < iBOpenFisrt) {    // <i> </i>  && </b><b>
                        for (int i = 0; i < (iIEnd - iIOpen); i++) {
                            sReturn += "<i>";
                        }
                        for (int i = 0; i < (iBEnd - iBOpen); i++) {
                            sReturn += "<b>";
                        }
                        sReturn += sHtml;
                    } else if (iIEndFirst > iIOpenFirst && iBEndFirst > iBOpenFisrt) {    // <i> </i>  && <b></b>
                        for (int i = 0; i < (iIEnd - iIOpen); i++) {
                            sReturn += "<i>";
                        }
                        for (int i = 0; i < (iBEnd - iBOpen); i++) {
                            sReturn += "<b>";
                        }
                        sReturn += sHtml;
                    } else {
                        sReturn += sHtml;
                    }
                } else if (iBOpen > iBEnd && iIOpen < iIEnd) {    // <b> mo nhieu hon dong, <i> mo it hon dong
                    for (int i = 0; i < (iIEnd - iIOpen); i++) {
                        sReturn += "<i>";
                    }
                    sReturn += sHtml;
                    for (int i = 0; i < (iBOpen - iBEnd); i++) {
                        sReturn += "</b>";
                    }
                } else if (iBOpen < iBEnd && iIOpen > iIEnd) {    // <b> mo it hon dong, <i> mo nhieu hon dong
                    for (int i = 0; i < (iBEnd - iBOpen); i++) {
                        sReturn += "<b>";
                    }
                    sReturn += sHtml;
                    for (int i = 0; i < (iIOpen - iIEnd); i++) {
                        sReturn += "</i>";
                    }
                } else {
                    sReturn = sHtml;
                }
            } else if (iBOpen == iBEnd && iIOpen == iIEnd && iBOpen == 0 && iIOpen != 0) {
                //<i></i> bang nhau nhung van ko dc vi: </i><i></i><i> --> the moi chuoi
                System.out.println("4444444---->");
                int iIEndFirst = sHtml.indexOf("</i>");
                int iIOpenFirst = sHtml.indexOf("<i>");
                if (iIEndFirst < iIOpenFirst) {
                    sReturn += "<i>";
                    sReturn += sHtml + "</i>";
                } else {
                    sReturn += sHtml;
                }
            } else {
                System.out.println("555555555---->");
                sReturn = sHtml;
            }
        } catch (Exception e) {
            System.out.println("loi invailHtml0---web.java--->" + e.getMessage());
        }
        return sReturn;
    }

    /**
     * dem so the tag co trong doan content param sContent param stag return
     *
     * @param sContent
     * @param stag
     * @return
     */
    public int countTagInHTML(String sContent, String stag) {
        int ireturn = 0;
//        String complie = "</b>";
        try {
            if (!stag.equals("")) {
                Pattern pattern = Pattern.compile(stag);
                Matcher matcher = pattern.matcher(sContent);
                while (matcher.find()) {
                    ireturn++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ireturn;
    }

    public String addLinkDownTest(String content, String folder, long id, int stt, int tong) {
        String sreturn = "";
        try {
            int idxImg = content.indexOf("<img");
            if (idxImg > -1 && stt <= tong) {
                int idxLon = 0;
                String sLast = content.substring(idxImg);
                idxLon = sLast.indexOf(">");
                String linkDown = "<br/><div><a href='\"+response.encodeURL(Constants.WEB_ROOT_HOT + \"tai-mien-phi/tai_hinh_news.jsp?f=" + folder + "&idn=" + id + "&s=" + stt + ")+\"'>Tải về</a></div>";
                sreturn = content.substring(0, idxLon) + linkDown + addLinkDownTest(content.substring(idxLon), folder, id, stt, tong);
            } else {
                sreturn = content;
            }
        } catch (Exception e) {
            System.out.println("loi Web.java--addLinkDown(): " + e.getMessage());
        }
        return sreturn;
    }

    public static String FindTag(String sStr) {
        if (sStr == null) {
            sStr = "";
        }
        String sTag = "";
        String[] Tag = sStr.split("[ ,\\?\\:\\!]");
        boolean flag = false;
        int j = 0;
        for (int i = 0; i < Tag.length; i++) {
            if (Tool.convert2NoSign(Tag[i]).matches("[A-Z\\u0110][a-zA-Z]*")) {
                System.out.println("Tag " + i + ": OK");
                if ((i == 0 || i == 1) && i - j <= 1) {
                    sTag = sTag + " " + Tag[i];
                } else if (i - j == 1) {
                    sTag = sTag + " " + Tag[i];
                } else {
                    if (sTag.length() > 0) {
                        sTag = sTag + "," + Tag[i];
                    } else {
                        sTag = Tag[i];
                    }
                }
                j = i;
                if (Tool.convert2NoSign(Tag[i]).matches("[A-Z\\u0110][A-Z]*")) {
                    sTag = sTag.trim();
                }
                flag = true;
            } else if (i == 1) {
                if (sTag.indexOf(" ") > -1) {
                    sTag = sTag.substring(0, sTag.indexOf(" "));
                }
                flag = false;
            } else if (flag && (sStr.indexOf(Tag[i - 1]) - sStr.indexOf(":") == 2 || sStr.indexOf(Tag[i]) - sStr.indexOf(":") == 1)) {
                sTag = sTag + " " + Tag[i];
                flag = false;
            } else {
                flag = false;
            }
        }
        String sTag2 = "";
        int iBegin = sStr.indexOf("\"");
        if (iBegin > -1) {
            sTag2 = sStr.substring(iBegin + 1, sStr.length());
            System.out.println(sTag2);
            int iEnd = sTag2.indexOf("\"");
            if (iEnd > -1) {
                sTag2 = sTag2.substring(0, iEnd);
                sTag2 = sTag2.trim();
            } else {
                sTag2 = "";
            }
        }
        String comma = " ";
        if (sTag.length() > 0) {
            comma = ",";
        }
        if (sTag2.length() > 3) {
            sTag = sTag + comma + sTag2;
        }
        String sTag3 = "";
        iBegin = -1;
        iBegin = sStr.indexOf("\'");
        if (iBegin > -1) {
            sTag3 = sStr.substring(iBegin + 1, sStr.length());
            System.out.println(sTag3);
            int iEnd = sTag3.indexOf("\'");
            if (iEnd > -1) {
                sTag3 = sTag3.substring(0, iEnd);
                sTag3 = sTag3.trim();
            } else {
                sTag3 = "";
            }
        }
        if (sTag3.length() > 3) {
            sTag = sTag + comma + sTag3;
        }

        String sTag4 = "";
        iBegin = -1;
        iBegin = sStr.indexOf("“");
        if (iBegin > -1) {
            sTag4 = sStr.substring(iBegin + 1, sStr.length());
            System.out.println(sTag4);
            int iEnd = sTag4.indexOf("”");
            if (iEnd > -1) {
                sTag4 = sTag4.substring(0, iEnd);
                sTag4 = sTag4.trim();
            } else {
                sTag4 = "";
            }
        }
        if (sTag4.length() > 3) {
            sTag = sTag + comma + sTag4;
        }

        String sTag5 = "";
        iBegin = -1;
        iBegin = sStr.indexOf("`");
        if (iBegin > -1) {
            sTag5 = sStr.substring(iBegin + 1, sStr.length());
            System.out.println(sTag5);
            int iEnd = sTag5.indexOf("`");
            if (iEnd > -1) {
                sTag5 = sTag5.substring(0, iEnd);
                sTag5 = sTag5.trim();
            } else {
                sTag5 = "";
            }
        }
        if (sTag5.length() > 3) {
            sTag = sTag + comma + sTag5;
        }

        String sTag6 = "";
        iBegin = -1;
        iBegin = sStr.indexOf("‘");
        if (iBegin > -1) {
            sTag6 = sStr.substring(iBegin + 1, sStr.length());
            System.out.println(sTag6);
            int iEnd = sTag6.indexOf("’");
            if (iEnd > -1) {
                sTag6 = sTag6.substring(0, iEnd);
                sTag6 = sTag6.trim();
            } else {
                sTag6 = "";
            }
        }
        if (sTag6.length() > 3) {
            sTag = sTag + comma + sTag6;
        }

        return sTag.trim().replace(", ", ",");
    }

    public String addLinkDown(String content, String img, String folder, long id, int stt) {
        String sreturn = "";
        try {
            int idxImg = content.indexOf(img);
            if (idxImg > -1) {
                int idxLon = 0;
                String sLast = content.substring(idxImg);
                idxLon = sLast.indexOf("/>");
                String linkDown = "<#xdtaianh#><a class='s' href='\"+response.encodeURL(Constants.WEB_ROOT_HOT + \"tai-mien-phi/tai_hinh_news.jsp?idn=" + id + "&amp;s=" + stt + "&amp;f=" + folder + "\")+\"'>Tải làm hình nền</a>";
                int icut = idxImg + idxLon + 2; // + 2 vi indexOf("/>")
                sreturn = content.substring(0, icut) + linkDown + content.substring(icut);
            } else {
                sreturn = content;
            }
//            System.out.println("sreturn: "+sreturn);
        } catch (Exception e) {
            System.out.println("loi Web.java--addLinkDown(): " + e.getMessage());
        }
        return sreturn;
    }

    /**
     * Convert 1 String thanh HTML
     *
     * @param s
     * @return
     */
    public static String text2HtmlnoLink(String s) {
        StringBuilder builder = new StringBuilder();
        boolean previousWasASpace = false;
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                if (previousWasASpace) {
                    builder.append("&nbsp;");
                    previousWasASpace = false;
                    continue;
                }
                previousWasASpace = true;
            } else {
                previousWasASpace = false;
            }
            switch (c) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;
                case '&':
                    builder.append("&amp;");
                    break;
                case '"':
                    builder.append("&quot;");
                    break;
                case '\n':
                    builder.append("<br>");
                    break;
                // We need Tab support here, because we print StackTraces as HTML
                case '\t':
                    builder.append("&nbsp; &nbsp; &nbsp;");
                    break;
                default:
                    if (c < 128) {
                        builder.append(c);
                    } else {
                        builder.append("&#").append((int) c).append(";");
                    }
            }
        }
        return builder.toString();
    }

    /**
     * *
     * Convert 1 String thanh HTML xu ly http://abc thanh the
     * <code><a ></a></code> param s return
     *
     * @param s
     * @return
     */
    public static String txtToHtml(String s) {
        StringBuilder builder = new StringBuilder();
        boolean previousWasASpace = false;
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                if (previousWasASpace) {
                    builder.append("&nbsp;");
                    previousWasASpace = false;
                    continue;
                }
                previousWasASpace = true;
            } else {
                previousWasASpace = false;
            }
            switch (c) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;
                case '&':
                    builder.append("&amp;");
                    break;
                case '"':
                    builder.append("&quot;");
                    break;
                case '\n':
                    builder.append("<br>");
                    break;
                // We need Tab support here, because we print StackTraces as HTML
                case '\t':
                    builder.append("&nbsp; &nbsp; &nbsp;");
                    break;
                default:
//                    builder.append(c);
                    if (c < 128) {
                        builder.append(c);
                    } else {
                        builder.append("&#").append((int) c).append(";");
                    }

            }
        }
        String converted = builder.toString();
        String str = "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\'\".,<>?«»“”‘’]))";
        Pattern patt = Pattern.compile(str);
        Matcher matcher = patt.matcher(converted);
        converted = matcher.replaceAll("<a href=\"$1\">$1</a>");
        return converted;
    }

    public static String htmlCode2Vn(String input) {
        if (input == null || input.equals("")) {
            return "";
        }
        input = input.replaceAll("&Agrave;", "À");
        input = input.replaceAll("&agrave;", "à");
        input = input.replaceAll("&#258;", "Ă");
        input = input.replaceAll("&#259;", "ă");
        input = input.replaceAll("&Aacute;", "Á");
        input = input.replaceAll("&aacute;", "á");
        input = input.replaceAll("&Acirc;", "Â");
        input = input.replaceAll("&acirc;", "â");
        input = input.replaceAll("&Atilde;", "Ã");
        input = input.replaceAll("&atilde;", "ã");
        //--
        input = input.replaceAll("&Egrave;", "È");
        input = input.replaceAll("&egrave;", "è");
        input = input.replaceAll("&Eacute;", "É");
        input = input.replaceAll("&eacute;", "é");
        input = input.replaceAll("&Ecirc;", "Ê");
        input = input.replaceAll("&ecirc;", "ê");
        //--
        input = input.replaceAll("&Ograve;", "Ò");
        input = input.replaceAll("&ograve;", "ò");
        input = input.replaceAll("&Oacute;", "Ó");
        input = input.replaceAll("&oacute;", "ó");
        input = input.replaceAll("&Otilde;", "Õ");
        input = input.replaceAll("&otilde;", "õ");
        input = input.replaceAll("&Ocirc;", "Ô");
        input = input.replaceAll("&ocirc;", "ô");
        //---
        input = input.replaceAll("&Ugrave;", "Ù");
        input = input.replaceAll("&ugrave;", "ù");
        input = input.replaceAll("&Uacute;", "Ú");
        input = input.replaceAll("&uacute;", "ú");
        input = input.replaceAll("&#360;", "Ũ");
        input = input.replaceAll("&#361;", "ũ");
        //--
        input = input.replaceAll("&Igrave;", "Ì");
        input = input.replaceAll("&igrave;", "ì");
        input = input.replaceAll("&Iacute;", "Í");
        input = input.replaceAll("&iacute;", "í");
        input = input.replaceAll("&#296;", "Ĩ");
        input = input.replaceAll("&#297;", "ĩ");
        //--
        input = input.replaceAll("&#272;", "Đ");
        input = input.replaceAll("&#273;", "đ");
        input = input.replaceAll("&nbsp;", " ");
        return input;
    }

    public static String removeElement(String input, String patten) {
        if (input == null) {
            return "";
        } else {
            Pattern pattern = Pattern.compile(patten);
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                String str = matcher.group(0);
                input = Tool.replace(input, str, "");
            }
            return input;
        }

    }

    /**
     * Remove Element and content inner html Tag by Class
     *
     * @param input
     * @param tag
     * @param clas
     * @return
     */
    public static String removeElement(String input, String tag, String clas) {
        if (input == null) {
            return "";
        } else {
            input = input.replaceAll("<( )*" + tag + ".+class=\"(" + clas + ")\"([^>])*>([\\s\\S.]*)</" + tag + ">", "");
            return input;
        }
    }

    /**
     * Remove Attribute HTML
     *
     * @param htmlFragment
     * @param attributesToRemove
     * @return
     */
    public static String cleanHtmlFragment(String htmlFragment, String attributesToRemove) {
        return htmlFragment.replaceAll("\\s+(?:" + attributesToRemove + ")\\s*=\\s*\"[^\"]*\"", "");
    }

//    public static void main(String[] args) {
//        String str1 = "Ban dang dung goi cuoc Tomato. TK goc 1 cua ban la 10000 dong."
//                + "asld asdkj alsdj";
//        String str2 = "Ban dang dung goi cuoc Tomato. TK goc 1 cua ban la 50000 dong."
//                + "asld asdkj alsdj";
//        String patten = ". TK goc 1 cua ban la (.*) dong.";
//        String result = "";
//        try {
//
//            Pattern pattern = Pattern.compile(patten);
//            Matcher matcher = pattern.matcher(str2);
//            while (matcher.find()) {
//                String str = matcher.group(0);
//                result = str;
//            }
//            result = result.replaceAll(". TK goc 1 cua ban la ", "");
//            result = result.replaceAll(". TK goc 1 cua ban la ", " dong.");
//            System.out.println("Result:" + result);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
