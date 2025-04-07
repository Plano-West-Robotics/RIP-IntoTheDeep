//package org.firstinspires.ftc.teamcode.tune;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.res.AssetManager;
//import android.util.Log;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.qualcomm.robotcore.util.WebHandlerManager;
//import com.qualcomm.robotcore.util.WebServer;
//
//import org.firstinspires.ftc.ftccommon.external.OnCreate;
//import org.firstinspires.ftc.ftccommon.external.WebHandlerRegistrar;
//import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
//import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;
//import org.firstinspires.ftc.robotserver.internal.webserver.MimeTypesUtil;
//
//import java.io.IOException;
//
//import fi.iki.elonen.NanoHTTPD;
//
//public class Foo {
//    private static Foo instance;
//
//    @OnCreate
//    public static void start(Context context) {
//        if (instance == null) {
//            instance = new Foo();
//        }
//    }
//
//    @WebHandlerRegistrar
//    public static void attachWebServer(Context context, WebHandlerManager manager) {
//        if (instance != null) {
//            instance.internalAttachWebServer(manager.getWebServer());
//        }
//    }
//
//    private void internalAttachWebServer(WebServer webServer) {
//        if (webServer != null) {
//            Activity activity = AppUtil.getInstance().getActivity();
//            if (activity != null) {
//                WebHandlerManager webHandlerManager = webServer.getWebHandlerManager();
//                AssetManager assetManager = activity.getAssets();
//                webHandlerManager.register("/dash", this.newStaticAssetHandler(assetManager, "dash/index.html"));
//                webHandlerManager.register("/dash/", this.newStaticAssetHandler(assetManager, "dash/index.html"));
//                this.addAssetWebHandlers(webHandlerManager, assetManager, "dash");
//                this.addAssetWebHandlers(webHandlerManager, assetManager, "images");
//            }
//        }
//    }
//
//    private WebHandler newStaticAssetHandler(final AssetManager assetManager, final String file) {
//        return session -> {
//            if (session.getMethod() == NanoHTTPD.Method.GET) {
//                String mimeType = MimeTypesUtil.determineMimeType(file);
//                return NanoHTTPD.newChunkedResponse(NanoHTTPD.Response.Status.OK, mimeType, assetManager.open(file));
//            } else {
//                return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "");
//            }
//        };
//    }
//
//    private void addAssetWebHandlers(WebHandlerManager webHandlerManager, AssetManager assetManager, String path) {
//        try {
//            String[] list = assetManager.list(path);
//            if (list == null) return;
//
//            if (list.length > 0) {
//                for (String file : list) {
//                    this.addAssetWebHandlers(webHandlerManager, assetManager, path + "/" + file);
//                }
//            } else {
//                webHandlerManager.register("/" + path, this.newStaticAssetHandler(assetManager, path));
//            }
//        } catch (IOException e) {
//            Log.w("FtcDashboard", e);
//        }
//    }
//}
