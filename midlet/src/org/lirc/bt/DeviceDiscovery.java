package org.lirc.bt;

import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;

public class DeviceDiscovery implements DiscoveryListener, CommandListener {
    private static final String BTLIRC_UUID = "084a08dae72211dd9bf8000fb0c7d780";

    private DiscoveryAgent agent = null;
    private LocalDevice local = null;

    private boolean discoveryRunning;

    private Vector remoteDevices = new Vector();
    private Vector servicesFound = new Vector();
    private int deviceIndex = 0;

    private final Form form = new Form("Select Device");
    private final Main main;

    private List list = new List("Select Device", List.IMPLICIT);
    private StringItem status = new StringItem(null, "");

    public DeviceDiscovery(Main main) {
        this.main = main;
        Command backCommand = new Command("Back", Command.EXIT, 1);
        form.addCommand(backCommand);
        form.setCommandListener(this);

        list.addCommand(backCommand);
    }

    private void print(String message) {
        form.append(message + "\n");
    }

    private void addDevice(RemoteDevice d) {
        remoteDevices.addElement(d);
        status.setText("Bluetooth devices in range (" + remoteDevices.size()
                + ")");
    }

    private void addService(ServiceRecord record) {
        servicesFound.addElement(record);
        RemoteDevice dev = record.getHostDevice();
        list.append(getDeviceName(dev), null);
    }

    public static String getDeviceName(RemoteDevice dev) {
        try {
            return dev.getFriendlyName(true);
        } catch (IOException e) {
            return dev.getBluetoothAddress();
        }
    }

    public void discover() {
        list.deleteAll();
        form.deleteAll();
        form.append(status);
        remoteDevices.removeAllElements();
        servicesFound.removeAllElements();

        Display.getDisplay(main).setCurrent(form);

        status.setText("Searching for Bluetooth devices in range...");
        print("\n");
        try {
            // Retrieve the local Bluetooth device object try { local =
            local = LocalDevice.getLocalDevice();
            agent = local.getDiscoveryAgent();
            discoveryRunning = true;
            agent.startInquiry(DiscoveryAgent.GIAC, this);
        } catch (BluetoothStateException e) {
            print("Failed to retrieve the local device (" + e.getMessage()
                    + ")");
            return;
        }

    }

    /**
     * Called each time a new device is discovered. This method prints the
     * device’s Bluetooth address to the screen.
     * 
     * @param device
     *            the device that was found
     * @param cod
     *            the class of device record
     */
    public void deviceDiscovered(RemoteDevice device, DeviceClass cod) {
        addDevice(device);
    }

    /**
     * Called when an inquiry ends. This method displays an Alert to notify the
     * user the inquiry ended. The reason the inquiry ended is displayed in the
     * Alert.
     * 
     * @param type
     *            the reason the inquiry completed
     */
    public void inquiryCompleted(int type) {
        // Determine if an error occurred.
        if (type != DiscoveryListener.INQUIRY_COMPLETED) {
            status.setText("Failed to retrieve list of devices");
        } else {
            startServiceSearch();
        }
    }

    public void servicesDiscovered(int transID, ServiceRecord[] record) {
        if (record != null) {
            for (int i = 0; i < record.length; i++) {
                addService(record[i]);
                break;
            }
        }
    }

    public void serviceSearchCompleted(int transID, int respCode) {
        deviceIndex++;
        startServiceSearch();
    }

    private void startServiceSearch() {

        if (deviceIndex < remoteDevices.size()) {

            try {

                RemoteDevice device = (RemoteDevice) remoteDevices
                        .elementAt(deviceIndex);

                status.setText("Checking for remote control capeabilities \""
                        + getDeviceName(device) + "\" (" + (deviceIndex + 1)
                        + " of " + remoteDevices.size() + ")");
                // Search for the Bluetooth Game service record and
                // retrieve
                // the name attribute in addition to the default
                // attributes.
                UUID[] uuidList = new UUID[1];
                uuidList[0] = new UUID(BTLIRC_UUID, false);
                int[] attrList = new int[1];
                attrList[0] = 0x0003;

                agent.searchServices(null, uuidList, device, this);
            } catch (BluetoothStateException e) {
                print("Unable to start the service search (" + e.getMessage()
                        + ")");
            }
        } else {
            discoveryRunning = false;
            if (servicesFound.isEmpty()) {
                status.setText("No remote controlled devices found");
            } else {
                Display.getDisplay(main).setCurrent(form);
                list.setTitle("Select device");
                list.setSelectCommand(List.SELECT_COMMAND);
                list.setCommandListener(this);
                form.deleteAll();
                Display.getDisplay(main).setCurrent(list);
            }
            remoteDevices.removeAllElements();
        }
    }

    public void commandAction(Command command, Displayable displayable) {
        // TODO Auto-generated method stub
        if (command.getCommandType() == Command.EXIT) {
            if (discoveryRunning) {
                discoveryRunning = false;
                agent.cancelInquiry(this);
            }
            main.restore();
        } else if (displayable == list) {
            ServiceRecord sr = (ServiceRecord) servicesFound.elementAt(list
                    .getSelectedIndex());
            String connUrl = sr.getConnectionURL(
                    ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            try {
                Settings.saveURL(connUrl);
            } catch (Exception e) {
            }
            main.changeServer(connUrl);
        }
    }
}
