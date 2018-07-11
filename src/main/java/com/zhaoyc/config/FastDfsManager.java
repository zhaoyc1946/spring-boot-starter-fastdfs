package com.zhaoyc.config;

import com.zhaoyc.fastdfs.StorageClient1;
import com.zhaoyc.fastdfs.StorageServer;
import com.zhaoyc.fastdfs.TrackerClient;
import com.zhaoyc.fastdfs.TrackerServer;

import java.io.IOException;

/**
 * A manager(wrapper) of original fastdfs client.
 *
 * @author zhaoyc
 */
public class FastDfsManager {

    private TrackerClient trackerClient;

    /**
     * Constructor
     *
     * @param trackerClient Initialized tracker client instance
     */
    FastDfsManager(TrackerClient trackerClient) {
        this.trackerClient = trackerClient;
    }

    /**
     * get TrackerClient
     *
     * @return TrackerClient
     */
    public TrackerClient getTrackerClient() {
        return trackerClient;
    }

    /**
     * get TrackerServer
     *
     * @return TrackerServer
     * @throws IOException
     */
    public TrackerServer getTrackerServer() throws IOException {
        return trackerClient.getConnection();
    }

    /**
     * close trackerServer
     *
     * @param trackerServer
     */
    public void closeTrackerServer(TrackerServer trackerServer) {
        try {
            if (trackerServer != null) {
                trackerServer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            trackerServer = null;
        }
    }

    /**
     * get StorageServer
     *
     * @return StorageServer
     * @throws IOException
     */
    public StorageServer getStorageServer() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        try {
            return trackerClient.getStoreStorage(trackerServer);
        } finally {
            closeTrackerServer(trackerServer);
        }
    }

    /**
     * close storageServer
     *
     * @param storageServer
     */
    public void closeStorageServer(StorageServer storageServer) {
        try {
            if (storageServer != null) {
                storageServer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            storageServer = null;
        }
    }

    /**
     * get StorageClient1
     *
     * @return StorageClient1
     */
    public StorageClient1 getStorageClient1() {
        TrackerServer trackerServer = null;
        try {
            trackerServer = getTrackerServer();
            StorageServer storageServer = null;
            return new StorageClient1(trackerServer, storageServer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeTrackerServer(trackerServer);
        }
        return null;
    }
}
