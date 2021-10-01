package com.example.a10jobs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class TmpActivity extends AppCompatActivity {
    ImageView imageView;
    Bitmap bitmap;
    String url = "http://j5d201.p.ssafy.io:12001";
    Socket socket;
    {
        try {
            socket = IO.socket(url);
            Log.v("socket", String.valueOf(socket));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);

        imageView = (ImageView) findViewById(R.id.tmp_image);
        socket.on("sendStreaming", onStream);
        socket.connect();

//        imageView.setImageBitmap(bitmap);

//        Thread imageThread = new Thread() {
//            @Override
//            public void run(){
//                try {
//                    //서버에 올려둔 이미지 URL
//                    URL url = new URL("http://j5d201.p.ssafy.io:12001/images/client/cam.jpg");
//
//                    //Web에서 이미지 가져온 후 ImageView에 지정할 Bitmap 만들기
//                    /* URLConnection 생성자가 protected로 선언되어 있으므로
//                     개발자가 직접 HttpURLConnection 객체 생성 불가 */
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//                    /* openConnection()메서드가 리턴하는 urlConnection 객체는
//                    HttpURLConnection의 인스턴스가 될 수 있으므로 캐스팅해서 사용한다*/
//                    conn.setDoInput(true); //Server 통신에서 입력 가능한 상태로 만듦
//                    conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
//
//                    InputStream is = conn.getInputStream(); //inputStream 값 가져오기
//                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 반환
//                } catch (MalformedURLException e){
//                    e.printStackTrace();
//                }catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        imageThread.start(); // 작업 Thread 실행
//
//        try{
//            //메인 Thread는 별도의 작업을 완료할 때까지 대기한다!
//            //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다림
//            //join() 메서드는 InterruptedException을 발생시킨다.
//            imageThread.join();
//
//            //작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
//            //UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정
//            imageView.setImageBitmap(bitmap);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }

//        (new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                while (!Thread.interrupted())
//                    try
//                    {
//                        Thread.sleep(1000);
//                        runOnUiThread(new Runnable() // start actions in UI thread
//                        {
//                            @Override
//                            public void run()
//                            {
//                                try {
//                                    //서버에 올려둔 이미지 URL
//                                    URL url = new URL("http://j5d201.p.ssafy.io:12001/images/client/cam.jpg");
//
//                                    //Web에서 이미지 가져온 후 ImageView에 지정할 Bitmap 만들기
//                                    /* URLConnection 생성자가 protected로 선언되어 있으므로
//                                     개발자가 직접 HttpURLConnection 객체 생성 불가 */
//                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//                                    /* openConnection()메서드가 리턴하는 urlConnection 객체는
//                                    HttpURLConnection의 인스턴스가 될 수 있으므로 캐스팅해서 사용한다*/
//                                    conn.setDoInput(true); //Server 통신에서 입력 가능한 상태로 만듦
//                                    conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
//
//                                    InputStream is = conn.getInputStream(); //inputStream 값 가져오기
//                                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 반환
//                                } catch (MalformedURLException e){
//                                    e.printStackTrace();
//                                }catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                    }
//                    catch (InterruptedException e)
//                    {
//                        e.printStackTrace();
//                    }
//            }
//        })).start();
    }

    // 리스너 -> 이벤트를 보냈을 때 이 리스너가 실행됨
    private Emitter.Listener onStream = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String)args[0];
                    Log.v("data", data);
                    bitmap = StringToBitmap(data);
                    imageView.setImageBitmap(bitmap);
                }
            });
        }
    };

    public static Bitmap StringToBitmap(String encodedString){
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
