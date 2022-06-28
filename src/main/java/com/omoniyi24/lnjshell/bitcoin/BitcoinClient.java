package com.omoniyi24.lnjshell.bitcoin;//package com.omoniyi.lnj.service.bitcoin;
//
//import com.omoniyi.lnj.model.*;
//
///**
// * @author OMONIYI ILESANMI
// */
//public interface BitcoinClient {
//
//    boolean isSynced();
//    int blockHeight();
//    int blockHeight(byte[] blockHash);
//    byte[] blockHash(final int height);
//    void publish(final byte[] transaction);
//    boolean isConfirmed(final byte[] txid);
//    BlockchainInfo blockchainInfo(BitcoinRequest req);
//    long blockIndex(final byte[] blockHash, final byte[] txid);
//
//    SendRawTransaction sendRawTransaction(BitcoinRequest req);
//
//    GetRawTransaction getRawTransaction(BitcoinRequest req);
//}
