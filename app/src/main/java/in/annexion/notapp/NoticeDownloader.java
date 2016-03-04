package in.annexion.notapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


/**
 * Created by fanatic on 7/2/16.
 */
public class NoticeDownloader {
    String title, uploadDate, uploadedBy, exp , noticeBoard , link, n_id, md5;

    Context context;

    public NoticeDownloader()
    {
    }


    public void downloadFile(String link)
    {
        this.link=link;
        new DownloadFile().execute();
    }

    public void insertIntoDB(Context context, String title, String uploadDate, String uploadedBy, String n_id, String exp, String noticeBoard, String link, String md5)
    {
        this.title=title;
        this.uploadDate=uploadDate;
        this.uploadedBy=uploadedBy;
        this.n_id=n_id;
        this.exp=exp;
        this.noticeBoard=noticeBoard;
        this.link = link;
        this.md5=md5;
        this.context=context;

        File file = new File(Environment.getExternalStorageDirectory().toString()+ "/Notapp/DB");
        if (!file.exists()) {
            file.mkdirs();
        }

        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(""+Environment.getExternalStorageDirectory()+"/Notapp/DB/notapp.db",null,null);
        db.enableWriteAheadLogging();

        db.execSQL("create table if not exists notices" +
                    "(n_id int(11), " +
                    "title varchar(50), " +
                    "uploadedBy varchar(30) , " +
                    "uploadDate varchar(15) , " +
                    "exp varchar(15) , " +
                    "noticeBoard varchar(15) , " +
                    "link varchar(15) , " +
                    "isFav integer default 0 , " +
                    "md5 varchar(50) , " +
                    "isRead integer default 0)");

        Log.e("n_downloader", "" + n_id + "  " + title + "  " + uploadDate + "  " + exp + "  " + link + "  ");
        //noticeList.add(noticeInfo);
        String nb = context.getResources().getStringArray(R.array.intent)[Integer.parseInt(noticeBoard)];
        int id = Integer.parseInt(n_id);
        String sql = "insert into notices values("
                    + id + ",'"
                    + title + "','"
                    + uploadedBy+"','"
                    + uploadDate + "','"
                    + exp + "','"
                    + nb + "','"
                    + link + "','"
                    + "0"   + "','"
                    + md5 + "','"
                    + "0" +"')";
        Log.e("sql", sql);
        db.execSQL(sql);
    }

    class DownloadFile extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {

            int count;
            int file_size = 0;
            byte [] data;

            try {
                link= URLEncoder.encode(link, "utf-8");

                String nb = context.getResources().getStringArray(R.array.intent)[Integer.parseInt(noticeBoard)];

                URL url = new URL("http://notapp.in/notices/" +nb+ "/" + link + ".pdf");

                Log.e("n_downloader url ", url.toString());
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(2000);
                connection.connect();
                file_size = connection.getContentLength();

                // download the file
                File file = new File(Environment.getExternalStorageDirectory().toString()+ "/Notapp");
                if (!file.exists()) {
                    file.mkdirs();
                }
                file = new File(Environment.getExternalStorageDirectory().toString() + "/Notapp/" + link + ".pdf");

                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                FileOutputStream fos = new FileOutputStream(file);

                data = new byte[file_size];

                while ((count = input.read(data)) != -1) {
                    fos.write(data, 0, count);
                }
                fos.flush();
                input.close();
                fos.close();
            } catch (ConnectTimeoutException e ) {
                Log.e("n_downloader Error: ", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {

            }
            return null;
        }
    }

}
