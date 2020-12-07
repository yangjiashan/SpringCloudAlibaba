import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FastDfsTest {

    private String locl_filename = "C://Users/18396/Desktop/springcloud.txt";
    private String conf_filename;

    {
        try {
            conf_filename = ResourceUtils.getFile("classpath:fdfs.properties").getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpload() {

        try {
            ClientGlobal.init(conf_filename);
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getTrackerServer();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            NameValuePair nvp[] = new NameValuePair[]{
                    new NameValuePair("age", "18"),
                    new NameValuePair("sex", "male")
            };
            //String fileIds[] = storageClient.upload_file(locl_filename, "jpg", nvp);
            String fileIds[] = storageClient.upload_file(locl_filename, "txt", nvp);
            System.out.println(storageClient.getErrorCode());
            System.out.println(fileIds.length);
            System.out.println("组名：" + fileIds[0]);
            System.out.println("路径: " + fileIds[1]);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDelete() {
        try {
            ClientGlobal.init(conf_filename);
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getTrackerServer();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            NameValuePair nvp[] = new NameValuePair[]{
                    new NameValuePair("age", "18"),
                    new NameValuePair("sex", "male")
            };
            //String fileIds[] = storageClient.upload_file(locl_filename, "jpg", nvp);
            int code = storageClient.delete_file("group1", "M00/00/00/wKgCbV_OQeOAXWmhAAADzfeKIXs186.txt");
            System.out.println(code);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGetFile() {
        try {
            ClientGlobal.init(conf_filename);
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getTrackerServer();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            NameValuePair nvp[] = new NameValuePair[]{
                    new NameValuePair("age", "18"),
                    new NameValuePair("sex", "male")
            };
            //String fileIds[] = storageClient.upload_file(locl_filename, "jpg", nvp);
            FileInfo fileInfo = storageClient.get_file_info("group1", "M00/00/00/wKgCbV_OQ0CAfctmAAADzfeKIXs498.txt");
            System.out.println(fileInfo);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDown() {
        try {
            ClientGlobal.init(conf_filename);
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getTrackerServer();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            NameValuePair nvp[] = new NameValuePair[]{
                    new NameValuePair("age", "18"),
                    new NameValuePair("sex", "male")
            };
            //String fileIds[] = storageClient.upload_file(locl_filename, "jpg", nvp);
            byte[] res = storageClient.download_file("group1", "M00/00/00/wKgCbV_OQ0CAfctmAAADzfeKIXs498.txt");
            System.out.println(res);

            IOUtils.write(res, new FileOutputStream("D:/" +
                    UUID.randomUUID().toString() + ".txt"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }


}
