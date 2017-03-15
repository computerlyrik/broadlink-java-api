package com.github.mob41.blapi;

import java.io.IOException;
import java.net.DatagramPacket;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mob41.blapi.mac.Mac;
import com.github.mob41.blapi.pkt.CmdPayload;
import com.github.mob41.blapi.pkt.Payload;

public class A1Device extends BLDevice {
    private static final Logger log = LoggerFactory.getLogger(A1Device.class);
    public static final String DESC_A1 = "Environmental Sensor";

    public A1Device(String host, Mac mac) throws IOException {
        super(BLDevice.DEV_A1, host, mac);
        this.setDeviceDescription(DESC_A1);
    }

    public Object checkSensors() throws Exception {
        DatagramPacket packet = sendCmdPkt(new CmdPayload() {

            @Override
            public byte getCommand() {
                return 0x6a;
            }

            @Override
            public Payload getPayload() {
                return new Payload() {

                    @Override
                    public byte[] getData() {
                        byte[] b = new byte[16];
                        b[0] = 1;
                        return b;
                    }

                };
            }

        });
        byte[] data = packet.getData();

        log.debug("checkSensors Packet received bytes: " + DatatypeConverter.printHexBinary(data));

        int err = data[0x22] | (data[0x23] << 8);

        if (err == 0) {
            log.warn("Everything OK: " + Integer.toHexString(err) + " / " + err);

        } else {
            log.warn("Received an error: " + Integer.toHexString(err) + " / " + err);
        }
        return null;
    }
}
