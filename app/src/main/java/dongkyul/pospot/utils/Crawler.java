package dongkyul.pospot.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.view.main.InstaImageCallBack;

public class Crawler {

    private static final String instagram = "https://www.instagram.com/explore/tags/";
    public List<String> imgLinks;
    public WebView webView;
    public InstaImageCallBack imageCallback;
    public Context mContext;

    public Crawler(WebView wv){
        webView = wv;
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.clearCache(true);
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML);");
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private String makeURL(String tag){
        return instagram+tag;
    }

    public void getImages(String tag,InstaImageCallBack callBack){
        imageCallback = callBack;
        imgLinks = new ArrayList<>();
        webView.addJavascriptInterface(new getImageInterface(), "Android");
        try {
            webView.loadUrl(makeURL(tag));
        }
        catch (Exception e){
            Log.e("crawling","loadURL error");
        }
    }


    public class getImageInterface {
        @JavascriptInterface
        public void getHtml(String html) {
            Document doc = Jsoup.parse(html);
            Elements imgs = doc.getElementsByTag("img");
            for(Element img:imgs) {
                imgLinks.add(img.attr("src"));
            }
            SetImageThread thread = new SetImageThread(imgLinks,imageCallback);
            thread.start();

        }
    }

    class SetImageThread extends Thread {
        List<String> mLinks;
        InstaImageCallBack mCallBack;
        List<Bitmap> images;
        public SetImageThread(List<String> links, InstaImageCallBack callBack) {
            mLinks=links;
            mCallBack=callBack;
        }
        public void run() {
           try {
               images = new ArrayList<>();
               for(String imageLink:mLinks) {
                   //Log.e("s",imageLink);
                   URL url = new URL(imageLink);
                   InputStream is = url.openStream();
                   images.add(BitmapFactory.decodeStream(is));
               }
               if(images.size()>0)
                   mCallBack.loadImages(images);
               else
                   Log.e("i","# of images 0");
           } catch (Exception e) {
               Log.e("s", e.toString());
           }
        }
    }

}
