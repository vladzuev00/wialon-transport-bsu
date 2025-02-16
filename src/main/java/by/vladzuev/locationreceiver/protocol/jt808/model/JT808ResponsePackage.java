package by.vladzuev.locationreceiver.protocol.jt808.model;

import lombok.Value;

@Value
public class JT808ResponsePackage {
    short replyFlowId;
    short replyId;
    byte result;
}
