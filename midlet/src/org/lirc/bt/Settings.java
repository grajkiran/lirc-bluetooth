package org.lirc.bt;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;

public class Settings {
    public static String getLastKnownURL() {
        RecordStore store;
        byte[] data = null;
        try {
            store = RecordStore.openRecordStore(Main.NAME, true);

            for (int i = 1, limit = store.getNextRecordID(); i < limit; i++) {
                try {
                    // Get the next record from the record store
                    data = store.getRecord(i);
                    // Get name from record (not shown)
                    // If the name matches the required player name,
                    // break (not shown)
                } catch (InvalidRecordIDException ex) {
                    // Skip records that have been deleted
                }
            }
        } catch (Exception e) {
            return null;
        }
        
        if (data != null) {
            return new String(data);
        } else {
            return null;
        }
    }

    public static void saveURL(String url) throws Exception {
        RecordStore store = RecordStore.openRecordStore(Main.NAME, true);
        byte[] data = url.getBytes();

        for (int i = 1, limit = store.getNextRecordID(); i < limit; i++) {
            try {
                // Get the next record from the record store
                store.setRecord(i, data, 0, data.length);
                return;
                // Get name from record (not shown)
                // If the name matches the required player name,
                // break (not shown)
            } catch (InvalidRecordIDException ex) {
                // Skip records that have been deleted
            }
        }
        
        store.addRecord(data, 0, data.length);
        
    }
}
