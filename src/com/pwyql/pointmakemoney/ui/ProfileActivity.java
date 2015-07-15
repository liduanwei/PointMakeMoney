package com.pwyql.pointmakemoney.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;

public class ProfileActivity extends BaseActivity {
    android.support.v4.widget.SwipeRefreshLayout mSwipeRefreshLayout;
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_profile);

	init();
    }

    private void init() {
	// TODO Auto-generated method stub
	TextView tvTopTitle = getViewById(R.id.tv_top_title);
	tvTopTitle.setText("个人资料");
	TextView tvLeftIcon = getViewById(R.id.tv_left_icon);
	tvLeftIcon.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	    }
	});
	findViewById(R.id.tv_top_action).setVisibility(View.GONE);

	mWebView = getViewById(R.id.webview);

	// 设置WebView对JavaScript的支持
	mWebView.getSettings().setJavaScriptEnabled(true);
	// 
	mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
	mSwipeRefreshLayout.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {

	    @Override
	    public void onRefresh() {
		// TODO Auto-generated method stub
		mWebView.loadUrl(AppConstants.URL_PROFILE);
	    }
	});
	// 设置进度条颜色
	mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

	mWebView.setWebViewClient(new WebViewClient() {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) { //在内部继续处理链接
		// TODO Auto-generated method stub
		view.loadUrl(url);
		return super.shouldOverrideUrlLoading(view, url);
	    }

	    @Override
	    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		// TODO Auto-generated method stub
		String data = "<div style='color:red'>页面加载时出错了,请确保网络连接畅通<br>错误码:" + errorCode + "<br>" + description + "</div>";
		view.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
		//	        super.onReceivedError(view, errorCode, description, failingUrl);
	    }
	});
	mWebView.setWebChromeClient(new WebChromeClient() {
	    @Override
	    public void onProgressChanged(WebView view, int newProgress) {
		// TODO Auto-generated method stub
		if (newProgress == 100) {
		    mSwipeRefreshLayout.setRefreshing(false);// 刷新完成
		} else {
		    if (!mSwipeRefreshLayout.isRefreshing()) {
			mSwipeRefreshLayout.setRefreshing(true);
		    }
		}
		super.onProgressChanged(view, newProgress);
	    }
	});

	// 添加cookie 确保session能用
	CookieSyncManager.createInstance(this);
	CookieManager cookieManager = CookieManager.getInstance();
	String cookies = "PHPSESSID=" + MyApplication.PHPSESSID + "";
	cookieManager.setCookie(AppConstants.HOST, cookies);//cookies是在HttpClient中获得的cookie  
	CookieSyncManager.getInstance().sync();

	//

	//自定义WebView的背景颜色
//	mWebView.setBackgroundColor(Color.TRANSPARENT);//先设置背景色为transparent
//	mWebView.setBackgroundResource(R.drawable.m);

	//添加JS调用Android(Java)的方法接口
	//	MJavascriptInterface mJavascriptInterface = new MJavascriptInterface(getApplicationContext());
	//	mWebView.addJavascriptInterface(mJavascriptInterface, "WebViewFunc");

	// 从assets目录下面的加载html
	//	mWebView.loadUrl("file:///android_asset/web.html");
	// 该方法需要设置cookie, 但是cookie由AsyncHttp存储着, 换成先由asyncHttp加载下所有的html内容再给webView
	mWebView.loadUrl(AppConstants.URL_PROFILE);

    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
    }

}
