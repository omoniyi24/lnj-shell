package com.omoniyi24.lnjshell.bitcoin.impl;//package com.omoniyi.lnj.service.bitcoin.impl;
//
//import com.omoniyi.lnj.model.*;
//import com.omoniyi.lnj.service.bitcoin.BitcoinClient;
//import org.springframework.stereotype.Service;
//
///**
// * @author OMONIYI ILESANMI
// */
////@Service
//public class BitcoinClientImpl implements BitcoinClient {
//    @Override
//    public boolean isSynced() {
//        return false;
//    }
//
//    @Override
//    public int blockHeight() {
//        return 0;
//    }
//
//    @Override
//    public int blockHeight(byte[] blockHash) {
//        return 0;
//    }
//
//    @Override
//    public byte[] blockHash(int height) {
//        return new byte[0];
//    }
//
//    @Override
//    public void publish(byte[] transaction) {
////        final var jtx = new Transaction(networkParameters, transaction);
////        final var tx = Hex.toHexString(jtx.bitcoinSerialize());
////        System.out.printf("Broadcasting tx: %s%n", tx);
////        final var body = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"sendrawtransaction\", \"params\": [\"0%s\"]}";
////        final var request = new Request.Builder()
////                .url(URL)
////                .post(RequestBody.create(JSON, String.format(body, tx)))
////                .addHeader("Authorization", String.format("Basic %s", AUTH))
////                .build();
////        try {
////            final var response = client.newCall(request).execute()
////                    .body().string();
////            final var json = new JSONObject(response);
////            System.out.println(json);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
//    }
//
//    @Override
//    public boolean isConfirmed(byte[] txid) {
//        return false;
//    }
//
//    @Override
//    public BlockchainInfo blockchainInfo(BitcoinRequest req) {
//        return null;
//    }
//
//
//    @Override
//    public long blockIndex(byte[] blockHash, byte[] txid) {
//        return 0;
//    }
//
//    @Override
//    public SendRawTransaction sendRawTransaction(BitcoinRequest req) {
//        return null;
//    }
//
//    @Override
//    public GetRawTransaction getRawTransaction(BitcoinRequest req) {
//        return null;
//    }
//}
